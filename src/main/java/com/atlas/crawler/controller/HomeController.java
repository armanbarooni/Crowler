/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.controller;


import com.atlas.crawler.alerter.email;
import com.atlas.crawler.entity.Log;
import com.atlas.crawler.entity.User;
import com.atlas.crawler.model.VisibleLog;
import com.atlas.crawler.service.LogService;
import com.atlas.crawler.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private LogService logService;

    @Autowired
    private UserService userService;

    @Autowired
    private email email;

    @GetMapping("/")
    public String home(Authentication auth, Model model, HttpServletRequest httpRequest) {
             email.sendEmail("armanbarooni@gmail.com","Alert","this is for test");
        try {
            String message = (String) httpRequest.getSession().getAttribute("passwordExpire");
            httpRequest.getSession().removeAttribute("passwordExpire");

            String userName = auth.getName();
            User user = userService.findByUserName(userName);

            if ((userService.isPasswordExpired(user) || message != null) &&
                    httpRequest.getSession().getAttribute("messageTitle") == null) {

                model.addAttribute("messageTitle", "هشدار");
                model.addAttribute("messageContent", "رمز عبور شما منقضی شده است! لطفا اقدام به تغییر رمز نمایید");
                model.addAttribute("messageIcon", "warning");

            } else if (httpRequest.getSession().getAttribute("messageTitle") != null) {
                model.addAttribute("messageTitle", httpRequest.getSession().getAttribute("messageTitle"));
                model.addAttribute("messageContent", httpRequest.getSession().getAttribute("messageContent"));
                model.addAttribute("messageIcon", httpRequest.getSession().getAttribute("messageIcon"));

                httpRequest.getSession().removeAttribute("messageTitle");
                httpRequest.getSession().removeAttribute("messageContent");
                httpRequest.getSession().removeAttribute("messageContent");
            }


            List<VisibleLog> visibleLogs = convertLogToVisibleLog(logService.getFiveLastLogin(userName));

            Integer numberOfFailLogin = logService.getNumberOfFailLogin(userName);

            model.addAttribute("logs", visibleLogs);
            model.addAttribute("numberOfFailLogin", numberOfFailLogin);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("messageTitle", "خطا");
            model.addAttribute("messageContent", "خطای سرور");
            model.addAttribute("messageIcon", "error");
        }

        return "home";
    }

    private List<VisibleLog> convertLogToVisibleLog(List<Log> logs){
        List<VisibleLog> visibleLogs = new ArrayList<>();
        for (Log lg:logs) {
            VisibleLog vlg = new VisibleLog();
            vlg.setBrowserName(lg.getBrowserName());
            vlg.setIp(lg.getIp());
            vlg.setDate(convertTimeStampToDate(String.valueOf(lg.getTimeStampFinishDate())));
            visibleLogs.add(vlg);
        }

        return visibleLogs;
    }

    private String convertTimeStampToDate(String stringTimeStamp) {
        Long timeStamp = Long.parseLong(stringTimeStamp);
        Date date = new Date(timeStamp);
        String pattern = "yyyy-MM-dd hh:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String myDate = formatter.format(date);

        return myDate;
    }


}
