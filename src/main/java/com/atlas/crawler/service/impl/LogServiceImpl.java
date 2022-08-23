/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.service.impl;

import com.atlas.crawler.dao.LogRepository;
import com.atlas.crawler.entity.Log;
import com.atlas.crawler.entity.User;
import com.atlas.crawler.service.LogService;
import com.atlas.crawler.service.UserService;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public void save(String userName, HttpServletRequest request, Log log) {

        User user = userService.findByUserName(userName);
        if (user != null) {
            String ip = this.clientIp(request);
            log.setIp(ip);
            String browserName = this.clientBrowserName(request);
            log.setBrowserName(browserName);
            log.setPort(80);

            user.addLog(log);

            logRepository.save(log);
        }

    }

    @Override
    public String clientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.trim().length() == 0) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.trim().length() == 0) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.trim().length() == 0) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ip == null || ip.trim().length() == 0) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.trim().length() == 0) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    @Override
    public String clientBrowserName(HttpServletRequest request) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        String browserName = userAgent.getBrowser().getName();
        return browserName;
    }

    @Override
    public Integer clientPortNumber(HttpServletRequest request) {
        return null;
    }

    @Override
    @Transactional
    public List<Log> getFiveLastLogin(String userName) {
        User user = userService.findByUserName(userName);
        if (user != null) {
            List<Log> logs = logRepository.findTop5LogsByUserAndEventResultAndEventTypeOrderByIdDesc(user, 1, "Login Successful");
            return logs;
        }
        return null;
    }

    @Override
    @Transactional
    public Integer getNumberOfFailLogin(String userName) {
        User user = userService.findByUserName(userName);
        if (user != null) {
            Integer number = logRepository.countLogsFailLogin(user);
            return number;
        }
        return null;
    }

    public Long convertDateToTimeStamp(String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = dateFormat.parse(date);
            Timestamp timestamp = new Timestamp(parsedDate.getTime());
            return timestamp.getTime();

        } catch (Exception e) {
            return null;
        }

    }


    @Transactional
    @Override
    public List<Log> findLogs(String log, String startDate, String endDate) {
        try {

            Collection<String> result = getRealTypeEvent(log);
            Long sDate = null;
            Long eDate = null;

            List<Log> logs = null;

            if (startDate.trim().length() == 0 && endDate.trim().length() == 0) {
                if (result != null) {
                    logs = logRepository.findLogsByEventTypeInOrFormNameInOrderByTimeStampStartDateDesc(result);
                } else {
                    logs = logRepository.findAllByOrderByTimeStampStartDateDesc();
                }

            } else if (startDate.trim().length() == 0) {

                eDate = convertDateToTimeStamp(endDate);

                if (result != null) {
                    logs = logRepository.findLogsByEventTypeInOrFormNameInAndTimeStampStartDateLessThanEqualOrderByTimeStampStartDateDesc(result, eDate);
                } else {
                    logs = logRepository.findLogsByTimeStampStartDateLessThanEqualOrderByTimeStampStartDateDesc(eDate);
                }

            } else if (endDate.trim().length() == 0) {

                sDate = convertDateToTimeStamp(startDate);


                if (result != null) {
                    logs = logRepository.findLogsByEventTypeInOrFormNameInAndTimeStampStartDateGreaterThanEqualOrderByTimeStampStartDateDesc(result, sDate);
                } else {
                    logs = logRepository.findLogsByTimeStampStartDateGreaterThanEqualOrderByTimeStampStartDateDesc(sDate);
                }

            } else {

                sDate = convertDateToTimeStamp(startDate);
                eDate = convertDateToTimeStamp(endDate);

                if (result != null) {
                    logs = logRepository.findLogsByEventTypeInOrFormNameInAndTimeStampStartDateBetweenOrderByTimeStampStartDateDesc(result, sDate, eDate);
                } else {
                    logs = logRepository.findLogsByTimeStampStartDateBetweenOrderByTimeStampStartDateDesc(sDate, eDate);
                }
            }

            return logs;


        } catch (Exception e) {
            return null;
        }
    }

    private Collection<String> getRealTypeEvent(String log) {
        Collection<String> lg = new ArrayList<>();

        if (log.equals("login")) {
            lg.add("Login Unsuccessful");
            lg.add("Login Successful");

        } else if (log.equals("logout")) {
            lg.add("Logout");

        } else if (log.equals("locked")) {
            lg.add("Login Locked");
            lg.add("Login UnLocked");

        } else if (log.equals("user")) {
            lg.add("Create User");
            lg.add("Update User");

        } else if (log.equals("enabled")) {
            lg.add("Disabled User");
            lg.add("Enabled User");

        }else if (log.equals("patch")) {
            lg.add("Create Patch");

        } else if (log.equals("NotFound")) {
            lg.add("Error NOT FOUND");

        } else if (log.equals("Forbidden")) {
            lg.add("Error FORBIDDEN");

        } else if (log.equals("BadRequest")) {
            lg.add("Error BAD REQUEST");

        } else if (log.equals("RequestTimeout")) {
            lg.add("Error REQUEST TIMEOUT");

        } else if (log.equals("ServiceUnavailable")) {
            lg.add("Error SERVICE UNAVAILABLE");

        } else if (log.equals("InternalError")) {
            lg.add("Error INTERNAL SERVER ERROR");

        } else if (log.equals("all")) {
            return null;
        }

        return lg;
    }


}
