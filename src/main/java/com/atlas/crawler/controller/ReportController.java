/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.controller;

import com.atlas.crawler.adapter.Adapter;
import com.atlas.crawler.core.General;
import com.atlas.crawler.core.GetcveFromcore;
import com.atlas.crawler.model.CVE;
import com.atlas.crawler.service.CoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller

@RequestMapping("/ordinary/report")
public class ReportController {

    public  static  int threadcounter=0;
    public static int processors_count = Runtime.getRuntime().availableProcessors();
    ExecutorService pool_thread = Executors.newFixedThreadPool(processors_count);
    public  static  List<CVE> cves=null;
    public  static  int CountAll=0;

    public static String  startDatee, finishDatee , distributionTypee;
    public  static int usevv;
    public static List<String> packagess;
    public static  String general_method="";
    public  static  String adapter_method="";
    public static boolean searchByPackage_flag = false;
    public static boolean UseDB = false;
    public  static Collection<List<String>> resultrecods=null;
    public static List<Integer> wholeIds = new ArrayList<Integer>();
    public  static  boolean isrunning=false;
    @Autowired
    private CoreService coreService;


    @GetMapping("/reports")
    public String HomeReports(Model model, HttpServletRequest request) {

        if (request.getSession().getAttribute("messageTitle") != null) {

            model.addAttribute("messageTitle", request.getSession().getAttribute("messageTitle"));
            model.addAttribute("messageContent", request.getSession().getAttribute("messageContent"));
            model.addAttribute("messageIcon", request.getSession().getAttribute("messageIcon"));

            request.getSession().removeAttribute("messageTitle");
            request.getSession().removeAttribute("messageContent");
            request.getSession().removeAttribute("messageContent");
        }

        model.addAttribute("cve", new CVE());
        return "ordinary/index-report";
    }


    @ResponseBody
    @PostMapping("/reports-list")
    public Object findCVES(
            @RequestParam("file") String pkgs,
            @RequestParam("usev") int usev,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ) {
        try {
            if (startDate == null || startDate.isEmpty()) {
                startDate = "p";
            } else {
                startDate = startDate.replace("/", "-");
            }

            if (endDate == null || endDate.isEmpty()) {
                endDate = "p";
            } else {
                endDate = endDate.replace("/", "-");
            }

            Connection connection = coreService.getConnection();
            General general = new General(connection);

            if (!"p".equals(startDate) && !general.isValidFormat("yyyy-MM-dd", startDate, Locale.ENGLISH)) {
                return 10;
            }
            if (!"p".equals(endDate) && !general.isValidFormat("yyyy-MM-dd", endDate, Locale.ENGLISH)) {
                return 10;
            }

            List<String> packages = convertStringToList(pkgs);
            int checkForBlacklist = blaclist(packages);
            if (checkForBlacklist != 0) {
                return checkForBlacklist;
            }

            Integer errorLineNumber = checkIntegrityPackages(packages);
            if (errorLineNumber == 0 && usev != 3) {
                return 0;
            }

            int totalCount = getCVECountFromCore(packages, startDate, endDate, usev, "",connection, page, size);

            List<CVE> pagedCves = getCVEFromCore(packages, startDate, endDate, usev, "", connection, page, size);

            if (pagedCves == null || pagedCves.isEmpty()) {
                return 70007;
            }

            // ⬇️ ساختار خروجی شامل دیتا و اطلاعات صفحه‌بندی
            Map<String, Object> response = new HashMap<>();
            response.put("data", pagedCves);
            response.put("totalItems", totalCount);
            response.put("totalPages", (int) Math.ceil((double) totalCount / size));
            response.put("currentPage", page);

            return response;

        } catch (Exception e) {
            return -1;
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

    public List<CVE> getCVEFromCore(List<String> packages, String startDate, String finishDate, int usev, String distributionType,  Connection connection, int page , int size) throws InterruptedException, SQLException {
        Thread getpack=null;

        adapter_method="search";

                packagess=packages;
        GetcveFromcore test=new GetcveFromcore(connection);
        test.usevv=usev;
        test.distributionTypee=distributionType;
        test.finishDatee=finishDate;
        test.startDatee=startDate;
        test.packagess=packages;
        test.size=size;
        test.page=page;
        if(getpack==null)
         getpack=new Thread(test);

        getpack.setName("getpackone");
        getpack.start();

        cves= test.return_result();
        connection.close();

        return cves;
    }
    public int getCVECountFromCore(List<String> packages, String startDate, String finishDate, int usev, String distributionType,  Connection connection, int page , int size) throws InterruptedException, SQLException {
        Thread getpack=null;

        adapter_method="search";

        packagess=packages;
        GetcveFromcore test=new GetcveFromcore(connection);
        test.usevv=usev;
        test.distributionTypee=distributionType;
        test.finishDatee=finishDate;
        test.startDatee=startDate;
        test.packagess=packages;
        test.size=size;
        test.page=page;
        if(getpack==null)
            getpack=new Thread(test);

        getpack.setName("getpackone");
        getpack.start();

        CountAll= test.return_Count();
        connection.close();

        return CountAll;
    }

    @GetMapping("/charts")
    public String HomeCharts(Model model, HttpServletRequest request) {

        if (request.getSession().getAttribute("messageTitle") != null) {

            model.addAttribute("messageTitle", request.getSession().getAttribute("messageTitle"));
            model.addAttribute("messageContent", request.getSession().getAttribute("messageContent"));
            model.addAttribute("messageIcon", request.getSession().getAttribute("messageIcon"));

            request.getSession().removeAttribute("messageTitle");
            request.getSession().removeAttribute("messageContent");
            request.getSession().removeAttribute("messageContent");

        }else if(request.getSession().getAttribute("data") != null){
            model.addAttribute("data", request.getSession().getAttribute("data"));
            request.getSession().removeAttribute("data");
        }

        return "ordinary/index-chart";
    }

    @PostMapping("/show-chart")
    public String showChart(Model model,HttpServletRequest request ,
                            @RequestParam("startDate") String startDate,
                            @RequestParam("endDate") String endDate,
                            @RequestParam("package")String pack,
                            @RequestParam("distro")String dis){

        try {
            if (startDate == null && endDate == null) {

                startDate = "p";
                endDate = "p";

            } else if (startDate == null) {
                startDate = "p";
                endDate = endDate.replace("/", "-");


            } else if (endDate == null) {
                endDate = "p";
                startDate = startDate.replace("/", "-");


            } else {
                startDate = startDate.replace("/", "-");
                endDate = endDate.replace("/", "-");

            }
            String error="";
            Connection connection = coreService.getConnection();

            General general=new General(connection);
            if(startDate!="p")
            {
                if(!general.isValidFormat("yyyy-MM-dd", startDate, Locale.ENGLISH)){
                    error=  "10";
                }
            }
            if(endDate!="p")
            {
                if(!general.isValidFormat("yyyy-MM-dd", endDate, Locale.ENGLISH)){
                    error=  "10";
                }
            }

            Adapter adapter = new Adapter(connection);
            ArrayList<Integer> data =  adapter.buildChart(startDate,endDate,pack,dis, error);
            request.getSession().setAttribute("data",data);

        }catch (Exception e){
            request.getSession().setAttribute("messageTitle", "خطا");
            request.getSession().setAttribute("messageContent", "خطای سرور");
            request.getSession().setAttribute("messageIcon", "error");
        }

        return "redirect:/ordinary/report/charts";

    }

}
