package com.atlas.crawler.core;

import com.atlas.crawler.controller.ReportController;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jsoup.Jsoup;
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
import java.util.logging.Logger;
import java.util.zip.DataFormatException;

import static java.time.LocalDate.now;

public class Nvd  {
    private General general;
    private Connection connection;
    public Nvd(Connection connection){
        this.connection = connection;
        general = new General(connection);
    }
    static int nfflag = 0;
    static final boolean justBodhi = false;

    static final Logger LOGGER = Logger.getLogger(Bugzilla.class.getName());
    public  ArrayList<Elements> GetNVDallPage() throws SQLException, ParseException, IOException, InterruptedException {
        LocalDate Cut_date =general. Cut_Time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDate date = now();
        Document alternate = null;
        int flag =0;
        ArrayList<Elements> all = new ArrayList<>();
        int index = 0;
        for(int i=0;i<2;i++){
            index=0;
            while (true) {
                flag = 1;
                String Sm= String.valueOf(Cut_date.getMonthValue());
                if(Sm.length()==1)
                    Sm='0'+Sm;
                String Sd= String.valueOf(Cut_date.getDayOfMonth());
                if(Sd.length()==1)
                    Sd='0'+Sd;
                String Lm= String.valueOf(now().getMonthValue());
                if(Lm.length()==1)
                    Lm='0'+Lm;
                String Ld= String.valueOf(now().getDayOfMonth());
                if(Ld.length()==1)
                    Ld='0'+Ld;
                //    String alternateURL = "https://nvd.nist.gov/vuln/search/results?form_type=Advanced&results_type=overview&search_type=all&cve_id=CVE-2020-26972&mod_start_date=" + Sm + "/" + Sd + "/" + Cut_date.getYear() + "&mod_end_date=" + Lm + "/" + Ld + "/" + now().getYear() + "&orderBy=modifiedDate&orderDir=asc&startIndex=" + String.valueOf(index);
                String alternateURL="";
                if(i==0)
                {
                    alternateURL = "https://nvd.nist.gov/vuln/search/results?form_type=Advanced&results_type=overview&query=fedora&search_type=all&cvss_version=3&cvss_v3_severity=HIGH&mod_start_date=" + Sm + "/" + Sd + "/" + Cut_date.getYear() + "&mod_end_date=" + Lm + "/" + Ld + "/" + now().getYear() + "&orderBy=modifiedDate&orderDir=asc&startIndex=" + String.valueOf(index);

                }
                else {
                    alternateURL = "https://nvd.nist.gov/vuln/search/results?form_type=Advanced&results_type=overview&query=fedora&search_type=all&cvss_version=3&cvss_v3_severity=CRITICAL&mod_start_date=" + Sm + "/" + Sd + "/" + Cut_date.getYear() + "&mod_end_date=" + Lm + "/" + Ld + "/" + now().getYear() + "&orderBy=modifiedDate&orderDir=asc&startIndex=" + String.valueOf(index);

                }

                index = index + 20;
                alternate =general. getDocument2(alternateURL);
                Elements temp = alternate.getElementsByAttributeValue("data-testid", "vuln-results-table").first().getElementsByTag("tr");
                if (temp.size() == 1){
                    break;
                }
                all.add(temp);

            }
        }

        return all;


    }


    public  ArrayList<Elements> GetNVDallPage1() throws SQLException, ParseException, IOException, InterruptedException {
        LocalDate Cut_date =general. Cut_Time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDate date = now();
        Document alternate = null;
        int flag =0;
        ArrayList<Elements> all = new ArrayList<>();
        int index = 0;
        for(int i=0;i<2;i++){
            index=0;
            while (true) {
                flag = 1;
                String Sm= String.valueOf(Cut_date.getMonthValue());
                if(Sm.length()==1)
                    Sm='0'+Sm;
                String Sd= String.valueOf(Cut_date.getDayOfMonth());
                if(Sd.length()==1)
                    Sd='0'+Sd;
                String Lm= String.valueOf(now().getMonthValue());
                if(Lm.length()==1)
                    Lm='0'+Lm;
                String Ld= String.valueOf(now().getDayOfMonth());
                if(Ld.length()==1)
                    Ld='0'+Ld;
                //    String alternateURL = "https://nvd.nist.gov/vuln/search/results?form_type=Advanced&results_type=overview&search_type=all&cve_id=CVE-2020-26972&mod_start_date=" + Sm + "/" + Sd + "/" + Cut_date.getYear() + "&mod_end_date=" + Lm + "/" + Ld + "/" + now().getYear() + "&orderBy=modifiedDate&orderDir=asc&startIndex=" + String.valueOf(index);
                String alternateURL="";
                if(i==0)
                {
                    alternateURL = "https://nvd.nist.gov/vuln/search/results?form_type=Advanced&results_type=overview&query=redhat&search_type=all&cvss_version=3&cvss_v3_severity=HIGH&mod_start_date=" + Sm + "/" + Sd + "/" + Cut_date.getYear() + "&mod_end_date=" + Lm + "/" + Ld + "/" + now().getYear() + "&orderBy=modifiedDate&orderDir=asc&startIndex=" + String.valueOf(index);

                }
                else {
                    alternateURL = "https://nvd.nist.gov/vuln/search/results?form_type=Advanced&results_type=overview&query=redhat&search_type=all&cvss_version=3&cvss_v3_severity=CRITICAL&mod_start_date=" + Sm + "/" + Sd + "/" + Cut_date.getYear() + "&mod_end_date=" + Lm + "/" + Ld + "/" + now().getYear() + "&orderBy=modifiedDate&orderDir=asc&startIndex=" + String.valueOf(index);

                }
                index = index + 20;
                alternate =general. getDocument2(alternateURL);
                Elements temp = alternate.getElementsByAttributeValue("data-testid", "vuln-results-table").first().getElementsByTag("tr");
                if (temp.size() == 1){
                    break;
                }
                all.add(temp);

            }
        }

        return all;


    }




    public  CVE GetProduct(CVE CVEObject , Document CVENVD, String CVEElement) {

        //  List <String> pck= new ArrayList<>();
        Document Package_list = null;
        try {
            String url = "https://nvd.nist.gov/vuln/detail/"+CVEElement+"/cpes?expandCpeRanges=true";
            Package_list =general. getDocument(url);

        }
        catch (Exception e) {
            System.err.println("----------------------------------------------------------------------------------------------------------------- ");

            // LOGGER.log(Level.FINE, "", e.getMessage());
        }

        try{

            ArrayList<String> productname = new ArrayList<>();
            ArrayList<String> productversion = new ArrayList<>();
            String pac=null;
            int cc=0;
            String pck=null;
            while (true){

                Elements k =null;
                Elements kk=null;
                Elements T =null;
                cc=cc+1;
                Element config=null;
                config=Package_list.getElementById("config-div-"+String.valueOf(cc));
                Element m=config;
                T=m.getElementsByClass("vulnerable");
                Element pack=T.get(0);
                String [] sp = pack.text().split(":");
                if(pac==null)
                {
                    CVEObject.PackageName.add(sp[3]);
                    pac=sp[3];
                    productname=new ArrayList<>();
                    productversion=new ArrayList<>();
                }
                if(!pac.equals(sp[3]))
                {
                    CVEObject.PackageName.add(sp[3]);
                    productname=new ArrayList<>();
                    productversion=new ArrayList<>();
                }




                int ty;

                try{

                    for(Element t :T)// yekbar tekrar
                    {
                        try {

                            kk = t.getElementsByTag("td").first().getElementsByTag("b");
                            Element e2=kk.get(0);
                            String [] spl = e2.text().split(":");
                            String product_name = spl[4];
                            String product_version = "";
                            if (spl[6].equals( "*"))
                                product_version =  spl[5];
                            else
                                product_version = spl[5] +" " + spl[6];

                            productname.add(product_name);
                            productversion.add((product_version));


                            k = t.getElementsByTag("td").first().getElementsByTag("div").first().getElementsByTag("ul").first().getElementsByTag("li");

                            for (Element e1 :k) {

                                spl = e1.getElementsByTag("i").text().split(":");
                                product_name = spl[4];
                                product_version = "";
                                if (spl[6].equals( "*"))
                                    product_version =  spl[5];
                                else
                                    product_version = spl[5] +" " + spl[6];
                                productname.add(product_name);
                                productversion.add((product_version));

                            }

                        }catch (Exception  e){

                            if (nfflag == 1){
                                //     CVEObject.Product_name.add(package_notfound);
                                //     CVEObject.Product_version.add("*");
                            }

                        }
                    }
                    if(CVEObject.Product_version==null)
                        CVEObject.Product_version=new ArrayList<>();
                    if(CVEObject.Product_name==null)
                        CVEObject.Product_name=new ArrayList<>();
                    CVEObject.Product_name.add(productname);
                    CVEObject.Product_version.add(productversion);






                } catch (Exception e)
                {


                }













            }//while true
        }catch (Exception e){
        }





        return  CVEObject;
    }
    public  CVE GetDetailsByNVDV2(CVE CVEObject, Document CVENVD) {
        try {
            String tempcvss = CVENVD.getElementById("Cvss2CalculatorAnchor").ownText();
            if (Float.valueOf(CVEObject.CVSS) < Float.valueOf(tempcvss.split(" ")[0]) )
            {
                CVEObject.CVSS_details ="NVDV2";
                CVEObject.CVSS=  tempcvss.split(" ")[0] ;
            }
            //B:  add this section to get some features that changed in dom of nvd
            String  vector= CVENVD.getElementById("nistV2MetricHidden").getElementsByAttribute("value").toString();
            String vector3=vector.split("value="+"+")[1];
            vector3=vector3.substring(0,vector3.length()-1);
            Document vector2= Jsoup.parse(vector3);

            CVEObject.AttackVector = vector2.getElementsByAttributeValue("data-testid", "vuln-cvssv2-av").first().ownText();
            CVEObject.IntegrityImpact = vector2.getElementsByAttributeValue("data-testid", "vuln-cvssv2-i").first().ownText();
            CVEObject.AccessComplexity = vector2.getElementsByAttributeValue("data-testid", "vuln-cvssv2-ac").first().ownText();

        } catch (Exception e) {

        }
        return CVEObject;
    }
    public  CVE GetDetailsByNVDV3(CVE CVEObject, Document CVENVD) {
        try {
            String tempcvss = CVENVD.getElementsByAttributeValue("data-testid", "vuln-cvss3-panel-score").first().ownText();

            if (Float.valueOf(CVEObject.CVSS) < Float.valueOf(tempcvss.split(" ")[0]) )
            {
                CVEObject.CVSS_details ="NVDV3";
                CVEObject.CVSS=  tempcvss.split(" ")[0] ;
            }
            String  vector= CVENVD.getElementById("nistV3MetricHidden").getElementsByAttribute("value").toString();
            String vector3=vector.split("value="+"+")[1];
            vector3=vector3.substring(0,vector3.length()-1);
            Document vector2=Jsoup.parse(vector3);

            CVEObject.AttackVector = vector2.getElementsByAttributeValue("data-testid", "vuln-cvssv3-av").first().ownText();  //
            CVEObject.ConfidentialityImpact = vector2.getElementsByAttributeValue("data-testid", "vuln-cvssv3-c").first().ownText();//
            CVEObject.IntegrityImpact = vector2.getElementsByAttributeValue("data-testid", "vuln-cvssv3-i").first().ownText();//
            CVEObject.AvailibilityImpact = vector2.getElementsByAttributeValue("data-testid", "vuln-cvssv3-a").first().ownText();//
            CVEObject.AccessComplexity = vector2.getElementsByAttributeValue("data-testid", "vuln-cvssv3-ac").first().ownText();//
            CVEObject.PrevilagesRequired = vector2.getElementsByAttributeValue("data-testid", "vuln-cvssv3-pr").first().ownText();//

        } catch (Exception e) {

            //    general.logging( "Cannot get from NVD");
        }
        return CVEObject;
    }
    public    String Getdesc(Document CVENVD){
        String Desc="";
        try {
            if (Desc.isEmpty())
                Desc = CVENVD.getElementsByAttributeValue("data-testid", "vuln-description").first().ownText();
        } catch (NullPointerException e) {
        }
        return  Desc;
    }
    public  void LoopOverEachRow_NVD(ArrayList<Elements> RowsInNVDallPage, Document Bugzilla, boolean flag,String reference) throws IOException, ParseException, InvalidFormatException, SQLException, DataFormatException {

        String S = null;
        String F = null;
        general.   FinishBodhi = false;
        S =general. iStart;
        F =general. iFinish;

        System.out.println("your system  has " +general. processors +" thread");

        if (!ReportController. UseDB) {
            if (!justBodhi) {
                for (int i=0;i<RowsInNVDallPage.size();i++) {
                    int k=0;
                    for (Element EachRow :RowsInNVDallPage.get(i)) {
                        if(k==0)
                            k=k+1;
                        else {
                            k=k+1;
                            Runnable r = new Runnable() {
                                @Override
                                public void run() {
                                    AnalyzeEachRow_nvd(EachRow, Bugzilla,reference);

                                }
                            };
                         general.   pool.execute(r);
                        }

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
    public  void AnalyzeEachRow_nvd(Element EachRow, Document Bugzilla,String reference){
               String EachSummaryLink = null;

        if(reference=="other")
        {
            general. fedora_count1=general. fedora_count1+1;
            try {
                System.out.println( "package->number of packet that will going for proccess: " +general. fedora_count1 +" of  "+general. fedora_count);

                EachSummaryLink = EachRow.getElementsByTag("th").first().getElementsByTag("span").text();
            }
            catch (Exception e)
            {

            }
        }
        else
        {
            general. fedora_count1=general. fedora_count1+1;
            System.out.println( "fedora->number of packet that will going for proccess: " +general. fedora_count1 +" of  "+general. fedora_count);

            try {
                EachSummaryLink = EachRow.getElementsByTag("th").first().getElementsByTag("strong").text();

            }catch (Exception e){
                EachSummaryLink=null;
            }
        }


        Document EachSummaryDoc = null;
        Elements CVEElements = null;
        try {
            EachSummaryDoc =general. getDocument3("https://bugzilla.redhat.com/show_bug.cgi?id=" + EachSummaryLink);
            CVEElements = EachSummaryDoc.select("#short_desc_nonedit_display a");

        } catch (Exception e) {
        }
        if (EachSummaryLink != null) {
            if(CVEElements!=null)
            {
                for (Element CVEElement : CVEElements) {
                    if (EachRow != null  && EachSummaryLink != null) {
                        Fedora fedora =new Fedora(connection);
                        fedora.AnalyzeEachCVE( Bugzilla, EachSummaryDoc, CVEElement, EachSummaryLink, null,reference);
                    }
                }
            }
            else
            {
                Fedora fedora =new Fedora(connection);

                fedora.AnalyzeEachCVE( Bugzilla, EachSummaryDoc, null, EachSummaryLink, null,reference);

            }



        }


    }

    public  void LoopOverEachRow_NVD1(ArrayList<Elements> RowsInNVDallPage, Document Bugzilla, boolean flag,String reference) throws IOException, ParseException, InvalidFormatException, SQLException, DataFormatException {
        String S = null;
        String F = null;
        general. FinishBodhi = false;
        S =general. iStart;
        F =general. iFinish;

        System.out.println("your system  has " +general. processors +" thread");

        if (!ReportController. UseDB) {
            if (!justBodhi) {
                for (int i=0;i<RowsInNVDallPage.size();i++) {
                    int k=0;
                    for (Element EachRow :RowsInNVDallPage.get(i)) {
                        if(k==0)
                            k=k+1;
                        else {
                            k=k+1;
                            Runnable r = new Runnable() {
                                @Override
                                public void run() {
                                    AnalyzeEachRow_nvd1(EachRow, Bugzilla,reference);

                                }
                            };
                        general.    pool.execute(r);
                        }

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
    public  void AnalyzeEachRow_nvd1(Element EachRow, Document Bugzilla,String reference){
        general. centos_count1=general. centos_count1+1;
        System.out.println( "Centos->number of packet that will going for proccess: " +general. centos_count1 +" of  "+general. centos_count);

        String EachSummaryLink;
        try {
            EachSummaryLink = EachRow.getElementsByTag("th").first().getElementsByTag("strong").text();

        }catch (Exception e){
            EachSummaryLink=null;
        }
        Document EachSummaryDoc = null;
        Elements CVEElements = null;
        try {
            EachSummaryDoc =general. getDocument3("https://bugzilla.redhat.com/show_bug.cgi?id=" + EachSummaryLink);
            CVEElements = EachSummaryDoc.select("#short_desc_nonedit_display a");

        } catch (Exception e) {
        }

        if (EachSummaryLink != null) {
            if (CVEElements != null) {
                for (Element CVEElement : CVEElements) {
                    if (EachRow != null && EachSummaryLink != null) {
                        Centos centos =new Centos(connection);
                        centos.Track_Each_CVE( Bugzilla, EachSummaryDoc, CVEElement, EachSummaryLink,reference);
                    }
                }
            } else
            {
                Centos centos =new Centos(connection);
                centos.Track_Each_CVE( Bugzilla, EachSummaryDoc, null, EachSummaryLink,reference);

            }


        }


    }

}
