package com.atlas.crawler.core;

import com.atlas.crawler.controller.ReportController;
import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;

public class Bugzilla  {
    private General general;

    private Connection connection;
    public Bugzilla(Connection connection){
        this.connection = connection;
        general = new General(connection);
    }
    static final boolean justBodhi = false;
    static final String startfromhere = "";

    static HashMap<String,JBrowserDriver> driverHashMap=new HashMap<>();
    static final Logger LOGGER = Logger.getLogger(Bugzilla.class.getName());
    public  void LoopOverEachRow(ArrayList<String> bugzilla_fedora_list, Document BugzillaFirstPageDoc, boolean flag, String reference) throws IOException, ParseException, InvalidFormatException, SQLException, DataFormatException {
        String S = null;
        String F = null;
        general. FinishBodhi = false;
        S =general. iStart;
        F =general. iFinish;
        System.out.println("your system  has " +general. processors +" thread");
        if (!ReportController. UseDB) {
            if (!justBodhi) {
                boolean flag1 = false;
                int it=0;
                for (String EachRow : bugzilla_fedora_list) {
                    it=it+1;
                        flag1 = true;
                            Runnable r = new Runnable() {
                                public void run() {
                                    AnalyzeEachRow(EachRow, BugzillaFirstPageDoc,  reference);

                                }
                            };
                          general.  pool.execute(r);

                }
            }
        }
    }
    public  void LoopOverEachRow1(ArrayList<String> RowsInBugzillaFirstPage, Document BugzillaFirstPageDoc, boolean flag,String reference) throws IOException, ParseException, InvalidFormatException, SQLException, DataFormatException {



        String S = null;
        String F = null;
        general. FinishBodhi = false;
        S =general. iStart;
        F =general. iFinish;
        System.out.println("your system  has " +general. processors +" thread");
        if (!ReportController. UseDB) {
            if (!justBodhi) {
                boolean flag1 = false;
                int it=0;
                for (String EachRow : RowsInBugzillaFirstPage) {
                    it=it+1;
                    flag1 = true;
                    Runnable r = new Runnable() {
                        public void run() {
                            Centos centos=new Centos(connection);
                            try {
                                centos.Start(EachRow, BugzillaFirstPageDoc,reference);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    general.  pool.execute(r);
                }
            }
        }







    }

    public  void AnalyzeEachRow(String EachRow, Document BugzillaFirstPageDoc,String reference) {
        general.  fedora_count1_b=general. fedora_count1_b+1;
            System.out.println( "fedora->number of packet that will going for proccess: " +general. fedora_count1_b +" of  "+general. fedora_count_b);
            String EachSummaryLink = EachRow;
            Document EachSummaryDoc = null;
            Elements CVEElements = null;
            try {
                EachSummaryDoc =general. getDocument3("https://bugzilla.redhat.com/" + EachSummaryLink);
                CVEElements = EachSummaryDoc.select("#short_desc_nonedit_display a");
            } catch (Exception e) {
                if (e.getMessage().equals("HTTP error fetching URL")) {
                } else if (e.getMessage().equals("Connection timed out: connect")) {
                    AnalyzeEachRow(EachRow, BugzillaFirstPageDoc,reference);
                    return;
                } else if (e.getMessage().equals("Read timed out")) {
                } else {
                    if (general. ProblemCounter1 < 5) {
                        general.  ProblemCounter1++;
                        AnalyzeEachRow(EachRow, BugzillaFirstPageDoc,reference);
                        general.  ProblemCounter1 = 0;
                    }
                }
            }
            if (CVEElements != null) {
                if (!CVEElements.isEmpty()) {
                    for (Element CVEElement : CVEElements) {
                        if (EachRow != null && BugzillaFirstPageDoc != null && EachSummaryDoc != null && CVEElement != null && EachSummaryLink != null) {
                            Fedora fedora = new Fedora(connection);
                            fedora.AnalyzeEachCVE( BugzillaFirstPageDoc, EachSummaryDoc, CVEElement, CVEElement.ownText(), null,reference);
                        }
                    }
                }
            }
    }
    public  CVE GetDetailsByAccessRedHatv2(CVE CVEObject, Document AccessRedHatDocOfEachCVE, Document CVEDetailsDoc) throws ParseException {

        if (AccessRedHatDocOfEachCVE.body().text().contains("CVSS v2 metrics") && !AccessRedHatDocOfEachCVE.body().text().contains("CVSS v3 metrics")) {
            CVEObject.AttackVector = "Get From NVD";
            CVEObject.PrevilagesRequired = "Get From NVD";
            CVEObject.AccessComplexity = AccessRedHatDocOfEachCVE.getElementsByAttributeValue("headers", "th-access-complexity").get(0).ownText();
            //Obj.AttackVector = doc_AccessRedHat.getElementsByAttributeValue("headers", "th-attack-vector").get(0).ownText();
            CVEObject.IntegrityImpact = AccessRedHatDocOfEachCVE.getElementsByAttributeValue("headers", "th-integrity-impact").get(0).ownText();
            CVEObject.ConfidentialityImpact = AccessRedHatDocOfEachCVE.getElementsByAttributeValue("headers", "th-confidentiality-impact").get(0).ownText();
            CVEObject.AvailibilityImpact = AccessRedHatDocOfEachCVE.getElementsByAttributeValue("headers", "th-availability-impact").get(0).ownText();
            //Obj.PrevilagesRequired = doc_AccessRedHat.getElementsByAttributeValue("headers", "th-privileges-required").get(0).ownText();
            //System.out.println(Obj.CVEName);
            try {
                String tempcvss = AccessRedHatDocOfEachCVE.getElementsByAttributeValue("headers", "th-base-score").get(0).ownText();
                if (Float.valueOf(CVEObject.CVSS_details.split("-")[0]) < Float.valueOf(tempcvss) )
                    CVEObject.CVSS_details =  tempcvss + "-RedHatV2";

            } catch (Exception e) {

                //  LOGGER.log(Level.FINE,"",e);
                //System.out.println(Obj.CVEName);

            }
        }
        return CVEObject;
    }
    public  CVE GetDetailsByAccessRedHatv3(CVE CVEObject, Document AccessRedHatDocOfEachCVE, Document CVEDetailsDoc) throws ParseException {
        if(AccessRedHatDocOfEachCVE==null)
            return CVEObject;
        try {
            Element element = AccessRedHatDocOfEachCVE.getElementById("cve-cvss-v3");
            Elements info = AccessRedHatDocOfEachCVE.select("div#cve-cvss-v3");

            if (element != null) {
                CVEObject.AttackVector = AccessRedHatDocOfEachCVE.getElementsByAttributeValue("headers", "th-cve-category2 th-cve-rh").first().ownText();
                CVEObject.IntegrityImpact = AccessRedHatDocOfEachCVE.getElementsByAttributeValue("headers", "th-cve-category8 th-cve-rh").first().ownText();
                CVEObject.ConfidentialityImpact = AccessRedHatDocOfEachCVE.getElementsByAttributeValue("headers", "th-cve-category7 th-cve-rh").first().ownText();
                CVEObject.AvailibilityImpact = AccessRedHatDocOfEachCVE.getElementsByAttributeValue("headers", "th-cve-category9 th-cve-rh").first().ownText();
                CVEObject.PrevilagesRequired = AccessRedHatDocOfEachCVE.getElementsByAttributeValue("headers", "th-cve-category4 th-cve-rh").first().ownText();
                CVEObject.AccessComplexity = AccessRedHatDocOfEachCVE.getElementsByAttributeValue("headers", "th-cve-category3 th-cve-rh").first().ownText();
                try {
                    String tempcvss = (AccessRedHatDocOfEachCVE.getElementsByAttributeValue("headers", "th-cve-category1 th-cve-rh").first().ownText());
                    if (Float.valueOf(CVEObject.CVSS) < Float.valueOf(tempcvss.split(" ")[0]) )
                    {
                        CVEObject.CVSS_details = "RedHatV3";
                        CVEObject.CVSS=  tempcvss.split(" ")[0] ;
                    }

//
                } catch (Exception e) {
                }
            } else {

            }
        } catch (Exception e) {

        }
        return CVEObject;
    }
    public   String Getdesc (Document AccessRedHatDocOfEachCVE) {
        String Desc="";
        try {
            int size = AccessRedHatDocOfEachCVE.getElementById("cve-details-description").getElementsByTag("p").size();

            if (size > 1)
                Desc = AccessRedHatDocOfEachCVE.getElementById("cve-details-description").getElementsByTag("p").get(1).ownText();
            else if (size == 1)
                Desc = AccessRedHatDocOfEachCVE.getElementById("cve-details-description").getElementsByTag("p").first().ownText();

        } catch (Exception e) {

            //  LOGGER.log(Level.FINE,"",e);
        }
        return  Desc;
    }
    public  Date ToDate( Document Bugzilla_List, CVE Obj) throws ParseException {
        String Date = Obj.LastUpdate.toString();
        String CurrentDateString = Bugzilla_List.getElementsByClass("bz_query_timestamp").first().ownText();
        Date CurrentDate = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss", Locale.ENGLISH).parse(CurrentDateString);
        general.  Today = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss", Locale.ENGLISH).parse(CurrentDateString);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(CurrentDate);
        if (Date.toLowerCase().matches(".*[a-z].*"))//Fri 11:43
        {
            int CurrentWeekDay = calendar.get(Calendar.DAY_OF_WEEK);
            int PreviousWeekDay =general. WeekDayToNumber(Date.split("\\s+")[0]);
            int Difference = CurrentWeekDay - PreviousWeekDay;
            if (Difference > 0) {
                calendar.add(Calendar.DATE, -1 * Difference);
            } else if (Difference < 0) {
                Difference = Difference + 7;
                calendar.add(Calendar.DATE, -1 * Difference);
            }
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(Date.split("\\s+")[1].split(":")[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(Date.split("\\s+")[1].split(":")[1]));
            calendar.set(Calendar.SECOND, 0);
            CurrentDate = calendar.getTime();
        } else if (Date.contains("-"))//2017-03-17
        {
            CurrentDate = new SimpleDateFormat("yyyy-MM-dd").parse(Date);
        } else//11:45:34
        {
            CurrentDate = new SimpleDateFormat("HH:mm:ss,E MMM dd yyyy").parse(Date + "," + CurrentDateString);
        }
        Obj.Date_Changed = CurrentDate;

        return CurrentDate;
    }
    public  CVE GetPatch(CVE CVEObject, Document EachSummaryDoc) {
        try {
            Elements Comments = EachSummaryDoc.getElementsByTag("pre");
            for (Element Comment : Comments) {
                if (Comment.ownText().toLowerCase().contains("upstream patch") ) {
                    Elements CommentLinks = Comment.select("a");
                    for (Element CommentLink : CommentLinks) {
                        CVEObject.PatchLink.add(CommentLink.attr("href"));
                    }
                }
                if (Comment.ownText().toLowerCase().contains("upstream fix") && (Comment.ownText().toLowerCase().contains("fc26") || Comment.ownText().toLowerCase().contains("fedora 26"))) {
                    Elements CommentLinks = Comment.select("a");
                    for (Element CommentLink : CommentLinks) {
                        CVEObject.PatchLink.add(CommentLink.attr("href"));
                    }
                }
            }
        }catch (Exception e)
        {
        }
        return CVEObject;
    }

}
