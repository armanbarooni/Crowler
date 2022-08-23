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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.DataFormatException;

import static java.time.LocalDate.now;

public class CveDetails    {
    private General general;
    private Connection connection;
    public CveDetails(Connection connection){
        this.connection = connection;
        general = new General(connection);
    }

    static final boolean justBodhi = false;
    static final String startfromhere = "";
    static String package_notfound = null;
    static int nfflag = 0;
    static HashMap<String,JBrowserDriver> driverHashMap=new HashMap<>();
    //  static final Logger LOGGER = Logger.getLogger(.Bugzilla.class.getName());

    public  ArrayList<Elements> GetCVEDetails() throws SQLException, ParseException, IOException {
        LocalDate Cut_date =general. Cut_Time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate date = now();
        Document alternate = null;
        int flag =0;
        ArrayList<Elements> all = new ArrayList<>();
        int index = 0;
        int month = Cut_date.getMonthValue();
        int year = Cut_date.getYear();
        int year2 = 0;
        while (true) {
            flag = 1;
            String alternateURL="";
            if (month == 12){
                year2 = year +1;
                alternateURL = "https://www.cvedetails.com/vulnerability-search.php?f=1&vendor=&product=fedora&cveid=&msid=&bidno=&cweid=&cvssscoremin=&cvssscoremax=&psy=&psm=&pey=&pem=&usy="+Integer.toString(year)+"&usm="+Integer.toString(month)+"&uey="+Integer.toString(year2)+"&uem="+Integer.toString(1);
                year = year +1;
                month = 1;
            }
            else {
                year2 = year ;
                alternateURL = "https://www.cvedetails.com/vulnerability-search.php?f=1&vendor=&product=fedora&cveid=&msid=&bidno=&cweid=&cvssscoremin=&cvssscoremax=&psy=&psm=&pey=&pem=&usy="+Integer.toString(year)+"&usm="+Integer.toString(month)+"&uey="+Integer.toString(year2)+"&uem="+Integer.toString(month +1);
                month++;
            }
            if (year2 == date.getYear() && month == date.getMonthValue()) {
                break;
            }
            try {
                alternate =general. getDocument2(alternateURL);
            }catch (Exception e){
            }
            try {
                Elements temp = alternate.getElementById("vulnslisttable").getElementsByClass("srrowns");
                all.add(temp);
            }catch (Exception e){

            }

        }
        return all;

    }
    public  CVE GetProduct(CVE CVEObject , Document CVEDetails, String CVEElement) {


        try {
            ArrayList<String> a= new ArrayList<>();
            ArrayList<String> b= new ArrayList<>();
            Elements s = CVEDetails.getElementById("vulnprodstable").getElementsByTag("tbody").first().getElementsByTag("tr");
            if(s.isEmpty() == false) {
                try {
                    int f = 0;
                    for (Element e1 : s) {
                        if (f != 0) {

                            String product_name = e1.getElementsByTag("td").get(3).getElementsByTag("a").text();
                            String product_version = e1.getElementsByTag("td").get(4).text();

                            if (product_name != null && product_version != null) {
                                a.add(product_name);
                                b.add(product_version);
                            }

                        }

                        f++;
                    }
                }catch (Exception e){

                }
            }
            if(CVEObject.Product_version==null)
                CVEObject.Product_version=new ArrayList<>();
            if(CVEObject.Product_name==null)
                CVEObject.Product_name=new ArrayList<>();
            CVEObject.Product_name.add(a);
            CVEObject.Product_version.add(b);
        }catch (Exception e){
        }





        return  CVEObject;
    }
    public  CVE GetDetailsByCVEDetails(CVE CVEObject, Document CVEDetailsDoc) {
        try {
            CVEObject.Authentication = CVEDetailsDoc.getElementById("cvssscorestable").getElementsByTag("tr").get(5).getElementsByTag("span").first().ownText();
        } catch (Exception e) {
            CVEObject.Authentication = "Not Clear";

        }

        try {
            Elements Vulnurability_Types = CVEDetailsDoc.getElementById("cvssscorestable").getElementsByTag("tr").get(7).getElementsByTag("span");

            for (Element Vulnurability_Type : Vulnurability_Types) {
                CVEObject.VulnurabiltyType += Vulnurability_Type.ownText() + ",";

            }
            CVEObject.VulnurabiltyType = CVEObject.VulnurabiltyType.replace("null", "");
        } catch (Exception e) {
            CVEObject.VulnurabiltyType = "Not Clear";

        }
        if (CVEObject.VulnurabiltyType == "null") {
            CVEObject.VulnurabiltyType = "Not Clear";
        }
        if (CVEObject.VulnurabiltyType.equals("")) {
            CVEObject.VulnurabiltyType = "Not Clear";
        }
        try {
            CVEObject.GainedAccess = CVEDetailsDoc.getElementById("cvssscorestable").getElementsByTag("tr").get(6).getElementsByTag("span").first().ownText();
        } catch (Exception e) {
            CVEObject.GainedAccess = "Not Clear";
            //      LOGGER.log(Level.FINE,"Getdetailsbycvedetails:" ,e.getMessage());

        }
        return CVEObject;
    }
    public    String Getdesc(Document CVEDetailsDoc) {
        String Desc="";
        try {
            Desc = CVEDetailsDoc.getElementsByClass("cvedetailssummary").first().ownText();
        } catch (Exception e) {

            //    LOGGER.log(Level.FINE,"",e);
        }
        return  Desc;
    }
    public  void LoopOverEachRow_CVE(ArrayList<Elements> RowsIncveallPage, Document Bugzilla, boolean flag,String reference) throws IOException, ParseException, InvalidFormatException, SQLException, DataFormatException {
        String S = null;
        String F = null;
        general.  FinishBodhi = false;
        S =general. iStart;
        F =general. iFinish;
        ExecutorService pool = Executors.newFixedThreadPool(5);


        if (!ReportController. UseDB) {
            if (!justBodhi) {
                for (int i=0;i<RowsIncveallPage.size();i++) {
                    for (Element EachRow :RowsIncveallPage.get(i)) {
                        Runnable r = new Runnable() {
                            @Override
                            public void run() {
                                AnalyzeEachRow_cve(EachRow, Bugzilla,reference);
                            }

                        };

                    }




                }
            }

            if (!general. FinishBodhi) {
//                    List<.CVE> CVEObjects = GetAllBodhi(S, F);
//                    for (.CVE CVEObject : CVEObjects) {
//                        AnalyzeEachCVEForBodhi(CVEObject.RefferencesLinks.get(0), CVEObject);
//                    }
            }
        } else {
            //GetFromDB(iStart,iFinish,60365);
        }
    }
    public  void AnalyzeEachRow_cve(Element EachRow, Document Bugzilla,String refrerence){
        String EachSummaryLink = EachRow.getElementsByTag("td").get(3).getElementsByTag("a").text();
        Document EachSummaryDoc = null;
        Elements CVEElements = null;
        try {
            EachSummaryDoc =general. getDocument3("https://bugzilla.redhat.com/show_bug.cgi?id=" + EachSummaryLink);
            CVEElements = EachSummaryDoc.select("#short_desc_nonedit_display a");

        } catch (Exception e) {
            if (e.getMessage().equals("HTTP error fetching URL")) {
            } else if (e.getMessage().equals("Connection timed out: connect")) {
                Nvd nvd=new Nvd(connection);
                nvd.AnalyzeEachRow_nvd(EachRow, Bugzilla,refrerence);
                return;
            } else if (e.getMessage().equals("Read timed out")) {
                //AnalyzeEachRow(EachRow, BugzillaFirstPageDoc);
            } else {
                if (general. ProblemCounter1 < 5) {
                    general.  ProblemCounter1++;
                    Nvd nvd=new Nvd(connection);

                    nvd.AnalyzeEachRow_nvd(EachRow, Bugzilla,refrerence);
                    general. ProblemCounter1 = 0;
                }


            }
        }
        if (CVEElements != null) {
            if (!CVEElements.isEmpty()) {
                for (Element CVEElement : CVEElements) {
                    if (EachRow != null  && EachSummaryDoc != null && CVEElement != null && EachSummaryLink != null) {
                        Fedora fedora = new Fedora(connection);
                        fedora.AnalyzeEachCVE( Bugzilla, EachSummaryDoc, CVEElement, EachSummaryLink, null,refrerence);
                    }
                }
            }
        }


    }


}
