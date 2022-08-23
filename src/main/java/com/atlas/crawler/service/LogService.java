/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.service;

import com.atlas.crawler.entity.Log;
import com.atlas.crawler.model.VisibleLog;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface LogService {
    void save(String userName , HttpServletRequest request, Log log);

    String clientIp(HttpServletRequest request);

    String clientBrowserName(HttpServletRequest request);

    Integer clientPortNumber(HttpServletRequest request);

    List<Log> getFiveLastLogin(String userName);

    Integer getNumberOfFailLogin(String userName);

    List<Log> findLogs(String log,String startDate,String endDate);
}
