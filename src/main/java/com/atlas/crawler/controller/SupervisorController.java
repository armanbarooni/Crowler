package com.atlas.crawler.controller;

import com.atlas.crawler.core.General;
import com.atlas.crawler.entity.Log;
import com.atlas.crawler.export.CveExcelExporter;
import com.atlas.crawler.model.CVE;
import com.atlas.crawler.model.VisibleLog;
import com.atlas.crawler.service.CoreService;
import com.atlas.crawler.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Reza Dehghani
 */
@Controller
@RequestMapping("/supervisor")
public class SupervisorController {

    @Autowired
    private CoreService coreService;

    @Autowired
    private LogService logService;


    @ResponseBody
    @PostMapping("/report/export-reports-list")
    public void exportCVES(@RequestParam("textField") String pkgs,
                          @RequestParam("distribution") String distribution,
                          @RequestParam("usev") int usev,
                          @RequestParam("startDate") String startDate,
                          @RequestParam("endDate") String endDate,
                          HttpServletResponse response) {

        try {
            if (startDate == null || startDate == "") {
                startDate = "p";
            }
            else{

                startDate = startDate.replace("/", "-");
            }  if (endDate == null || endDate =="") {
                endDate = "p";
            }
            else
            {
                endDate = endDate.replace("/", "-");


            }
            Connection connection = coreService.getConnection();
            General general=new General(connection);

            if(startDate!="p")
            {
                if(!general.isValidFormat("yyyy-MM-dd", startDate, Locale.ENGLISH)){
                    return;
                }
            }
            if(endDate!="p")
            {
                if(!general.isValidFormat("yyyy-MM-dd", endDate, Locale.ENGLISH)){
                    return  ;
                }
            }
            List<String> packages = convertStringToList(pkgs);
            int chekforblacklist=blaclist(packages);
            if(chekforblacklist!=0)
            {
                return ;
            }
            Integer errorLineNumber = checkIntegrityPackages(packages);
            if (errorLineNumber != -1 && ((usev == 0) || (usev==2))) {
                return ;
            }
            if(errorLineNumber==0){
                return  ;
            }
            List<CVE> cves =  getCVEFromCore(packages, startDate, endDate, usev, distribution);




            CveExcelExporter cveExcelExporter = new CveExcelExporter(cves);

            cveExcelExporter.exportCsv2(response);


        } catch (Exception e) {
            //return -1;
        }

    }

    private List<String> convertStringToList(String file) {
        String pkg[] = file.split("\\r?\\n");
        List<String> temp = Arrays.asList(pkg.clone());

        return temp;
    }
    private  int blaclist(List<String> packg)
    {
        String[] blaclist = {"--", ";--", ";", "/*", "*/", "@@",
                "@", "char", "nchar", "varchar", "nvarchar", "alter",
                "begin", "cast", "create", "cursor", "declare", "delete",
                "drop", "end", "exec", "execute", "fetch", "insert",
                "kill",  "select", "sys", "sysobjects", "syscolumns",
                "table", "update"};
        int i=1;
        for(String test : packg)
        {
            i=i+1;
            for(String test2:blaclist)
            {
                if(test.contains(test2))
                    return -i;
            }
        }
        return  0;
    }
    private Integer checkIntegrityPackages(List<String> packages) {

        int line = 0;
        for (String st : packages) {
            if(st!="")

            {
                line++;
                int index = st.indexOf(" ");
                int index2 = st.indexOf("*");
                if ((index == -1) && (index2 == -1)) {
                    return line;
                }

            }
        }
        if(line==0)
            return 0;
        return -1;
    }

    private List<CVE> getCVEFromCore(List<String> packages, String startDate, String finishDate, int usev, String distributionType) throws InterruptedException, SQLException {

        Connection connection = coreService.getConnection();
        ReportController report=new ReportController();


        List<CVE> cves = report.getCVEFromCore(packages, startDate, finishDate, usev, distributionType,connection);
        return cves;
    }

    @ResponseBody
    @PostMapping("/report/log-list")
    public List<VisibleLog> logReport(@RequestParam("log") String log,
                                      @RequestParam("startDate") String startDate,
                                      @RequestParam("endDate") String endDate,
                                      HttpServletResponse response){
        try {
            startDate = startDate.replace("/","-");
            endDate = endDate.replace("/","-");

            List<Log> logs = logService.findLogs(log,startDate,endDate);
            List<VisibleLog> visibleLogs = convertLogToVisibleLog(logs);

            return visibleLogs;
        }catch (Exception e){
            return null;
        }


    }

    private List<VisibleLog> convertLogToVisibleLog(List<Log> logs){
        List<VisibleLog> visibleLogs = new ArrayList<>();
        for (Log lg:logs) {
            VisibleLog vlg = new VisibleLog();
            vlg.setUserName(lg.getUser().getUserName());
            vlg.setBrowserName(lg.getBrowserName());
            vlg.setEventMessage(lg.getEventType());
            vlg.setIp(lg.getIp());
            vlg.setFormName(lg.getFormName());
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
