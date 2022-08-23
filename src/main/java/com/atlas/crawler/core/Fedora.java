package com.atlas.crawler.core;

import com.atlas.crawler.controller.ReportController;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.simple.parser.JSONParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
public class Fedora extends  Thread {
    static final Logger LOGGER = Logger.getLogger(Fedora.class.getName());
    public String rref="";

    public FileWriter myWriter=null;

    private General general;
    private Connection connection;
    public Fedora(Connection connection){
        this.connection = connection;
        general = new General(connection);
    }
    public  void  Tenable (int iii) throws InterruptedException
    {
        if(iii==162319)
        {

            iii=iii;
        }
        String uuu="";
        try {
            uuu="https://www.tenable.com/plugins/nessus/"+String.valueOf(iii)  ;
            Document   tenable = null;

            tenable =general. getDocument2(uuu);
            if(uuu.toString().contains("A 404 error occurred."))
            {
                System.out.println("was ->  "+String.valueOf(iii));

                uuu="https://www.tenable.com/plugins/was/"+String.valueOf(iii)  ;
            }

            ///////////////////////////////
            String ag="";
            String Agent="";
            String synp="";
            String  name="";
            String CVE="";
            String soloution="";
            int ID=-1;
            String family="";
            String Datee="";
            ArrayList<String >  products=new ArrayList<>();
            ArrayList<String >  vendors=new ArrayList<>();
            ArrayList<String >  versions=new ArrayList<>();
            Elements divs=tenable. getElementsByClass("tab-pane active").first().getElementsByClass("col-md-4").first().getElementsByTag("p");
            int ctn=0;
            for(Element div : divs)
            {
                try{
                    String object=div.getElementsByTag("strong").first().ownText();
                    if(object.contains("Agent"))
                    {
                      Agent=div.getElementsByTag("span").first().ownText();
                    }
                    if(object.contains("Updated"))
                    {
                        Datee= div.getElementsByTag("span").first().ownText();
                    }

                    if(object.contains("Family"))
                    {
                        family=div.getElementsByTag("span").first().getElementsByTag("a").first().text();
                    }
                    if(object.contains("CVSS Score Source"))
                    {
                        try {
                            CVE=div.getElementsByTag("span").first().getElementsByTag("a").first().text();

                        }catch (Exception rr)
                        {

                        }
                    }
                    if(object.contains("CPE"))
                    {
                        Elements cpes=div.getElementsByTag("span");
                        for(Element cpe :cpes)
                        {
                        try {
                            String CPE=cpe.ownText();
                            String[] parts=CPE.split(":");
                            if(parts[1].equals("2.3"))
                            {
                                vendors.add(parts[3]);
                                products.add(parts[4]);
                                try {
                                    versions.add(parts[5]);
                                }catch (Exception ee){
                                    versions.add("");
                                }
                            }
                            else
                            {
                                vendors.add(parts[2]);
                                products.add(parts[3]);
                                try {
                                    versions.add(parts[4]);
                                }catch (Exception ee){
                                    versions.add("");
                                }
                            }

                        }catch (Exception jj)
                        {
                            System.out.println(1+" "+jj.getMessage());

                        }


                        }
                    }

                }
                catch (Exception ee)
                {
                    System.out.println(2+" "+ee.getMessage());

                }
            }
            try {
                Element synpo=tenable.getElementsByClass("tab-pane active").first().    getElementsByClass("col-md-8").first();
                Element syn=synpo.getElementsByTag("span").first();
                synp=syn.text();
            }catch (Exception el)
            {
                System.out.println(3+" "+el.getMessage());

            }
            try {
                name=tenable.getElementsByTag("h1").first().text();

            }catch (Exception ec)
            {
                System.out.println(4+" "+ec.getMessage());

            }
                        ////////////////name
            /////////////////ID
            ID=iii;
            ////////////////////// soloution
            try {
                Element sol=tenable.getElementsByClass("tab-pane active").first().getElementsByClass("col-md-8").first().getElementsByClass("mb-3").get(2);;
                Element solo=sol.getElementsByTag("span").first();
                soloution=solo.text();
            }catch (Exception yyy)
            {
                System.out.println(5+" "+yyy.getMessage());

            }
            Date LastUpdate=null;
                try {
                    SimpleDateFormat Formatter1 = new SimpleDateFormat("dd/MM/yyyy");
                     LastUpdate=Formatter1.parse(Datee);
                }catch (Exception ll)
                {
                    System.out.println(6+" "+ll.getMessage());

                }

try {
    System.out.println("addding new tenable   "+iii);
    general.add_cve_to_database2(ID,Agent,synp,name,CVE,soloution,family,LastUpdate,vendors,products,versions);

}catch (Exception tt)
{
    System.out.println(7+" "+tt.getMessage());
}



/*            Element agent=tenable. getElementsByClass("tab-pane active").first().getElementsByClass("col-md-4").first();
            String cvss=agent.toString();
            if(1==1)
            {

                /////agent
                if(cvss.contains("Agent"))
                {
                    ag=    agent. getElementsByTag("p").get(5).getElementsByTag("span").first().toString();
                    ag=ag.substring(ag.indexOf("<span>") ,ag.indexOf("</span>"));
                    Agent=ag.substring(6);
                    family=    agent. getElementsByTag("p").get(6).getElementsByTag("span").first().getElementsByTag("a").first().text();


                    ag=    agent. getElementsByTag("p").get(8).getElementsByTag("span").first().toString();
                    ag=ag.substring(ag.indexOf("<span>") ,ag.indexOf("</span>"));
                    Datee =ag.substring(6);
                    /////////////////////cve
                    if(cvss.contains("CVSS Score Source"))
                    CVE=    agent. getElementsByTag("p").get(10).getElementsByTag("a").first().ownText();
                }
                else
                {
                    family=    agent. getElementsByTag("p").get(5).getElementsByTag("span").first().getElementsByTag("a").first().text();

                    ag=    agent. getElementsByTag("p").get(7).getElementsByTag("span").first().toString();
                    ag=ag.substring(ag.indexOf("<span>") ,ag.indexOf("</span>"));
                    Datee =ag.substring(6);
                    /////////////////////cve
                    if(cvss.contains("CVSS Score Source"))
                    try {
                        CVE=    agent. getElementsByTag("p").get(8).getElementsByTag("a").first().ownText();

                    }catch (Exception eeeee)
                    {
                        CVE=    agent. getElementsByTag("p").get(9).getElementsByTag("a").first().ownText();

                    }
                }
                Element synpo=tenable.getElementsByClass("tab-pane active").first().    getElementsByClass("col-md-8").first();
                Element syn=synpo.getElementsByTag("span").first();
                synp=syn.text();
                ////////////////name
                name=tenable.getElementsByTag("h1").first().text();
                /////////////////ID
                ID=iii;
                ////////////////////// soloution
                Element sol=tenable.getElementsByClass("tab-pane active").first().getElementsByClass("col-md-8").first().getElementsByClass("mb-3").get(2);;
                Element solo=sol.getElementsByTag("span").first();
                soloution=solo.text();
                SimpleDateFormat Formatter1 = new SimpleDateFormat("dd/MM/yyyy");
                 Date LastUpdate=Formatter1.parse(Datee);

                Elements producs=tenable.getElementsByClass("col-md-4");
                if(producs.size()==2)
                {
                    producs=producs.get(1).getElementsByTag("section");
                    producs=producs .first().getElementsByTag("p");
                    producs=producs .first().getElementsByTag("span");
                    for (Element p :producs)
                    {
                        String version="";
                        String text=p.ownText();

                        try{
                            text=text.substring(text.indexOf(":")+1);
                            text=text.substring(text.indexOf(":")+1);
                            text=text.substring(text.indexOf(":")+1);
                            int ven=text.indexOf(":");
                            String vendor=text.substring(0,ven);
                            text=text.substring(ven+1);
                            int ven2=text.indexOf(":");
                            String Pro="";
                            if(ven2!=-1)
                            {
                                Pro =text.substring(0,ven2);
                                text=text.substring(ven2+1);
                                int ven3=text.indexOf(":");

                                version=text.substring(0,ven3);
                            }
                            else {
                                Pro=text;
                            }
                            products.add(Pro);
                            vendors.add(vendor);
                            versions.add(version);
                        }catch (Exception ee)
                        {
                             text=p.ownText();

                            text=text.substring(text.indexOf(":")+1);
                            text=text.substring(text.indexOf(":")+1);
                            int ven=text.indexOf(":");
                            String vendor=text.substring(0,ven);
                            text=text.substring(ven+1);
                            int ven2=text.indexOf(":");
                            String Pro="";
                            if(ven2!=-1)
                            {
                                Pro =text.substring(0,ven2);
                                text=text.substring(ven2+1);
                                int ven3=text.indexOf(":");

                                version=text.substring(0,ven3);
                            }
                            else {
                                Pro=text;
                            }
                            products.add(Pro);
                            vendors.add(vendor);
                            versions.add(version);
                        }




                    }
                }
                else
                {
                    producs=producs.first().getElementsByTag("section");
                    producs=producs .first().getElementsByTag("p");
                    producs=producs .first().getElementsByTag("span");
                    for (Element p :producs)
                    {
                        String version="";
                        String text=p.ownText();
                        text=text.substring(text.indexOf(":")+1);
                        text=text.substring(text.indexOf(":")+1);

                        int ven=text.indexOf(":");
                        String vendor=text.substring(0,ven);
                        text=text.substring(ven+1);
                        int ven2=text.indexOf(":");
                        String Pro="";
                        if(ven2!=-1)
                        {
                            Pro =text.substring(0,ven2);
                            text=text.substring(ven2+1);
                            version=text;
                        }
                        else {
                            Pro=text;
                        }
                        products.add(Pro);
                        vendors.add(vendor);
                        versions.add(version);

                    }
                }


            }*/
        }catch (Exception e){
                System.out.println("fedora->   "+e.getMessage()+ String.valueOf(iii));
        }
    }
    public void  recent_tenable( ) throws SQLException {
        String Tedad = "";
            String uuu = "https://www.tenable.com/plugins/search?q=cve&sort=newest&page=1";
            Document tenable = null;
            try {
                tenable = general.getDocument2(uuu);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int cnt=0;
            while(true)
            {
                Elements table = tenable.getElementsByClass("results-table table").first().getElementsByTag("tbody");
                Element tr = table.get(0).getElementsByTag("tr").get(cnt);
                Elements el = tr.getElementsByTag("td");
                Element idd = el.first();
                Tedad = idd.text();
                String pro=table.get(0).getElementsByTag("tr").get(cnt).getElementsByTag("td").get(2).text();
                if(pro.equals("Nessus"))
                            break;
                else
                    cnt=cnt+1;
            }


        Connection MyConnection = null;


            MyConnection = connection;
        PreparedStatement MyStatement2 = null;
        int vuln_id=-1;
        String query="";
        MyStatement2 = MyConnection.prepareStatement("select max(\"ID\") from public.\"Tenable\"");


        ResultSet res = MyStatement2.executeQuery();

        if (res.next()) {
            vuln_id = res.getInt("max");
        }



            for (int iii = Integer.valueOf(Tedad); iii >= vuln_id; iii = iii - 1) {
                int finalIii = iii;
/*
                    try {
                        Tenable(finalIii);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
*/

                Runnable r = new Runnable() {
                    public void run() {
                        try {
                            Tenable(finalIii);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                general.pool.execute(r);
            }




    }
    public    void  allyears (int path) throws InterruptedException {
        int countt=0;
        System.out.println("--------------------------------------------"+path);
        CVE CVEObject=new CVE();
        CVEObject.Product_name=new ArrayList<>();
        CVEObject.Product_version=new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        int numberofcveinjson=0;
        try (FileReader reader = new FileReader("./download/nvdcve-1.1-"+String.valueOf(path) +".json")) {
            Object obj = jsonParser.parse(reader);
            org.json.simple.JSONObject cve= (org.json.simple.JSONObject) obj;
            org.json.simple.JSONArray CVE_Items=(  org.json.simple.JSONArray)cve.get("CVE_Items");
            SimpleDateFormat Formatter1 = new SimpleDateFormat("yyyy-MM-dd");
            int count=0;
            for ( Object Cve :CVE_Items)
            {
                countt=countt+1;
                 CVEObject=new CVE();
                CVEObject.Product_name=new ArrayList<>();
                CVEObject.Product_version=new ArrayList<>();
                try
                {
                    count=count+1;
                    if(count>-1) {
                 //       System.err.println(" ttttttttttt -> add ->" + count + " TO database " + Thread.currentThread().getId());
                        org.json.simple.JSONObject CVE = (  org.json.simple.JSONObject) Cve;
                        String Reported = null;
                        String modified = null;
                        Reported = ((String) CVE.get("publishedDate")).substring(0, 10);
                        CVEObject.BroadcastDate = Formatter1.parse(Reported);
                        modified = ((String) CVE.get("lastModifiedDate")).substring(0, 10);
                        CVEObject.LastUpdate = Formatter1.parse(modified);
                          org.json.simple.JSONObject cv = (  org.json.simple.JSONObject) CVE.get("cve");
                          org.json.simple.JSONObject CVE_data_meta = (  org.json.simple.JSONObject) cv.get("CVE_data_meta");
//////////////////////////////////////////////////////////////////
                        org.json.simple.JSONObject problemtype = (  org.json.simple.JSONObject) cv.get("problemtype");
                        org.json.simple.JSONArray problemtype_data= (  org.json.simple.JSONArray) problemtype.get("problemtype_data");
                        org.json.simple.JSONObject des = (  org.json.simple.JSONObject) problemtype_data.get(0);
                        org.json.simple.JSONArray descriptions = (  org.json.simple.JSONArray) des.get("description");
                        org.json.simple.JSONObject cwe = (  org.json.simple.JSONObject) descriptions.get(0);
                        CVEObject.CWE = (String) cwe.get("value");
                          /////////////////////////////////////////////////////
                        org.json.simple.JSONObject description = (  org.json.simple.JSONObject) cv.get("description");
                        org.json.simple.JSONArray description_data = (  org.json.simple.JSONArray) description.get("description_data");
                        org.json.simple.JSONObject desc = (  org.json.simple.JSONObject) description_data.get(0);
                        CVEObject.Desc = (String) desc.get("value");
////////////////////////////////////////////
                        int stop=0;
                        CVEObject.CVEName = (String) CVE_data_meta.get("ID");
                        if(CVEObject.CVEName.equals("CVE-2022-23959"))
                            stop=stop;
                        try {
                              org.json.simple.JSONObject imapct = (  org.json.simple.JSONObject) CVE.get("impact");
                              org.json.simple.JSONObject baseMetricV3 = (  org.json.simple.JSONObject) imapct.get("baseMetricV3");
                              org.json.simple.JSONObject cvssV3 = (  org.json.simple.JSONObject) baseMetricV3.get("cvssV3");
                            CVEObject.Distro = "";
                            CVEObject.CVSS = String.valueOf(cvssV3.get("baseScore"));
                            CVEObject.Distro = "";
                            CVEObject.Comments = "";
                            CVEObject.Platforms = new ArrayList<>();
                            CVEObject.RefferencesLinks = new ArrayList<>();
                            CVEObject.CVSS_details = "NVD-V3.1";
                            CVEObject.ImpactType = String.valueOf(cvssV3.get("scope"));
                            CVEObject.VulnurabiltyType = "";
                            CVEObject.AttackVector = (String) cvssV3.get("attackVector");
                            CVEObject.AccessComplexity = (String) cvssV3.get("attackComplexity");
                            CVEObject.PrevilagesRequired = (String) cvssV3.get("privilegesRequired");
                            CVEObject.Authentication = (String) cvssV3.get("userInteraction");
                            CVEObject.ConfidentialityImpact = (String) cvssV3.get("confidentialityImpact");
                            CVEObject.IntegrityImpact = (String) cvssV3.get("integrityImpact");
                            CVEObject.AvailibilityImpact = (String) cvssV3.get("availabilityImpact");
                        } catch (Exception e) {
                            org.json.simple.JSONObject imapct = (  org.json.simple.JSONObject) CVE.get("impact");
                            org.json.simple.JSONObject baseMetricV3 = (  org.json.simple.JSONObject) imapct.get("baseMetricV2");
                            org.json.simple.JSONObject cvssV3 = (  org.json.simple.JSONObject) baseMetricV3.get("cvssV2");
                            CVEObject.CVSS = String.valueOf(cvssV3.get("baseScore"));
                            CVEObject.Distro = "";
                            CVEObject.Comments = "";
                            CVEObject.Platforms = new ArrayList<>();
                            CVEObject.RefferencesLinks = new ArrayList<>();
                            CVEObject.CVSS_details = "NVD-V2";
                            CVEObject.ImpactType ="";
                            CVEObject.VulnurabiltyType = "";
                            CVEObject.AttackVector = (String) cvssV3.get("accessVector");
                            CVEObject.AccessComplexity = (String) cvssV3.get("accessComplexity");
                            CVEObject.PrevilagesRequired = "";
                            CVEObject.Authentication = (String) cvssV3.get("authentication");
                            CVEObject.ConfidentialityImpact = (String) cvssV3.get("confidentialityImpact");
                            CVEObject.IntegrityImpact = (String) cvssV3.get("integrityImpact");
                            CVEObject.AvailibilityImpact = (String) cvssV3.get("availabilityImpact");
                        }
                        CVEObject.PatchLink = new ArrayList<>();
                        CVEObject.Date_Changed = CVEObject.LastUpdate;
                        CVEObject.GainedAccess = "";
                          org.json.simple.JSONObject configurations = (  org.json.simple.JSONObject) CVE.get("configurations");

                        com.atlas.crawler.core.CVE finalCVEObject = CVEObject;
                        numberofcveinjson=numberofcveinjson+1;

                        int finalNumberofcveinjson = numberofcveinjson;

                        Runnable r = new Runnable() {
                            public void run() {
                                try {

                        GetEveryThingOfCVE(finalCVEObject,null,null,null,null,null,null, finalNumberofcveinjson);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (InvalidFormatException e) {
                                    e.printStackTrace();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                } catch (DataFormatException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        };
                        general.  pool.execute(r);


                    }
                }
                catch (Exception e)
                {
                }
            }
            CVEObject=null;

             jsonParser =null;
             System.gc();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }
    public    void  allyears_tenable (int path) throws InterruptedException {
        int countt=0;
        System.out.println("--------------------------------------------"+path);
      String    CVEID;

        JSONParser jsonParser = new JSONParser();
        int numberofcveinjson=0;
        try (FileReader reader = new FileReader("./download/nvdcve-1.1-"+String.valueOf(path) +".json")) {
            Object obj = jsonParser.parse(reader);
            org.json.simple.JSONObject cve= (org.json.simple.JSONObject) obj;
            org.json.simple.JSONArray CVE_Items=(  org.json.simple.JSONArray)cve.get("CVE_Items");

            int count=0;
            for ( Object Cve :CVE_Items)
            {
                countt=countt+1;


                try
                {
                    count=count+1;
                    if(count>-1) {
                        org.json.simple.JSONObject CVE = (  org.json.simple.JSONObject) Cve;

                        org.json.simple.JSONObject cv = (  org.json.simple.JSONObject) CVE.get("cve");
                        org.json.simple.JSONObject CVE_data_meta = (  org.json.simple.JSONObject) cv.get("CVE_data_meta");



                        CVEID = (String) CVE_data_meta.get("ID");


                        String finalCVEID = CVEID;
                        Runnable r = new Runnable() {
                            public void run() {
                                try {

                                   Get_Tenable_by_cve(finalCVEID);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        };
                        general.  pool.execute(r);


                    }
                }
                catch (Exception e)
                {
                }
            }

            System.gc();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }
    public void Get_Tenable_by_cve(String cveName) throws InterruptedException {
        String Tedad = "";
        String uuu = "https://www.tenable.com/cve/"+cveName+"/plugins";

        Document tenable = null;
        try {
            tenable = general.getDocument2(uuu);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Elements table=null;
        try {
            table = tenable.getElementsByClass("results-table table").first().getElementsByTag("tbody");

        }catch (Exception pe)
        {
            table=null;
        }
       if(table!=null) {
           Elements trr = null;
           try {
               trr = table.first().getElementsByTag("tr");
           } catch (Exception ee)
           {
               trr=null;
       }
       if(trr!=null)
       {
           System.err.println(cveName);
           for(Element tr :trr)
           {
               Elements el = tr.getElementsByTag("td");
               Element idd = el.first();
               Tedad = idd.text();



               int finalIii = Integer.parseInt(Tedad);
               Tenable(finalIii);

           }
       }


       }


    }

    public  void recent_getdata()
    {
        int countt=0;
        System.out.println("-------------------------------------------recent");
        CVE CVEObject=new CVE();
        CVEObject.Product_name=new ArrayList<>();
        CVEObject.Product_version=new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        int numberofcveinjson=0;
        try (FileReader reader = new FileReader("./download/nvdcve-1.1-modified.json")) {
            Object obj = jsonParser.parse(reader);
            org.json.simple.JSONObject cve= (org.json.simple.JSONObject) obj;
            org.json.simple.JSONArray CVE_Items=(  org.json.simple.JSONArray)cve.get("CVE_Items");
            SimpleDateFormat Formatter1 = new SimpleDateFormat("yyyy-MM-dd");
            int count=0;
            for ( Object Cve :CVE_Items)
            {
                countt=countt+1;
                CVEObject=new CVE();
                CVEObject.Product_name=new ArrayList<>();
                CVEObject.Product_version=new ArrayList<>();
                try
                {
                    count=count+1;
                    if(count>-1) {
                        //       System.err.println(" ttttttttttt -> add ->" + count + " TO database " + Thread.currentThread().getId());
                        org.json.simple.JSONObject CVE = (  org.json.simple.JSONObject) Cve;
                        String Reported = null;
                        String modified = null;
                        Reported = ((String) CVE.get("publishedDate")).substring(0, 10);
                        CVEObject.BroadcastDate = Formatter1.parse(Reported);
                        modified = ((String) CVE.get("lastModifiedDate")).substring(0, 10);
                        CVEObject.LastUpdate = Formatter1.parse(modified);
                        org.json.simple.JSONObject cv = (  org.json.simple.JSONObject) CVE.get("cve");
                        org.json.simple.JSONObject CVE_data_meta = (  org.json.simple.JSONObject) cv.get("CVE_data_meta");
//////////////////////////////////////////////////////////////////
                        org.json.simple.JSONObject problemtype = (  org.json.simple.JSONObject) cv.get("problemtype");
                        org.json.simple.JSONArray problemtype_data= (  org.json.simple.JSONArray) problemtype.get("problemtype_data");
                        org.json.simple.JSONObject des = (  org.json.simple.JSONObject) problemtype_data.get(0);
                        org.json.simple.JSONArray descriptions = (  org.json.simple.JSONArray) des.get("description");
                        org.json.simple.JSONObject cwe = (  org.json.simple.JSONObject) descriptions.get(0);
                        CVEObject.CWE = (String) cwe.get("value");
                        /////////////////////////////////////////////////////
                        org.json.simple.JSONObject description = (  org.json.simple.JSONObject) cv.get("description");
                        org.json.simple.JSONArray description_data = (  org.json.simple.JSONArray) description.get("description_data");
                        org.json.simple.JSONObject desc = (  org.json.simple.JSONObject) description_data.get(0);
                        CVEObject.Desc = (String) desc.get("value");
////////////////////////////////////////////
                        int stop=0;
                        CVEObject.CVEName = (String) CVE_data_meta.get("ID");
                        if(CVEObject.CVEName.equals("CVE-2022-23959"))
                            stop=stop;
                        try {
                            org.json.simple.JSONObject imapct = (  org.json.simple.JSONObject) CVE.get("impact");
                            org.json.simple.JSONObject baseMetricV3 = (  org.json.simple.JSONObject) imapct.get("baseMetricV3");
                            org.json.simple.JSONObject cvssV3 = (  org.json.simple.JSONObject) baseMetricV3.get("cvssV3");
                            CVEObject.Distro = "";
                            CVEObject.CVSS = String.valueOf(cvssV3.get("baseScore"));
                            CVEObject.Distro = "";
                            CVEObject.Comments = "";
                            CVEObject.Platforms = new ArrayList<>();
                            CVEObject.RefferencesLinks = new ArrayList<>();
                            CVEObject.CVSS_details = "NVD-V3.1";
                            CVEObject.ImpactType = String.valueOf(cvssV3.get("scope"));
                            CVEObject.VulnurabiltyType = "";
                            CVEObject.AttackVector = (String) cvssV3.get("attackVector");
                            CVEObject.AccessComplexity = (String) cvssV3.get("attackComplexity");
                            CVEObject.PrevilagesRequired = (String) cvssV3.get("privilegesRequired");
                            CVEObject.Authentication = (String) cvssV3.get("userInteraction");
                            CVEObject.ConfidentialityImpact = (String) cvssV3.get("confidentialityImpact");
                            CVEObject.IntegrityImpact = (String) cvssV3.get("integrityImpact");
                            CVEObject.AvailibilityImpact = (String) cvssV3.get("availabilityImpact");
                        } catch (Exception e) {
                            org.json.simple.JSONObject imapct = (  org.json.simple.JSONObject) CVE.get("impact");
                            org.json.simple.JSONObject baseMetricV3 = (  org.json.simple.JSONObject) imapct.get("baseMetricV2");
                            org.json.simple.JSONObject cvssV3 = (  org.json.simple.JSONObject) baseMetricV3.get("cvssV2");
                            CVEObject.CVSS = String.valueOf(cvssV3.get("baseScore"));
                            CVEObject.Distro = "";
                            CVEObject.Comments = "";
                            CVEObject.Platforms = new ArrayList<>();
                            CVEObject.RefferencesLinks = new ArrayList<>();
                            CVEObject.CVSS_details = "NVD-V2";
                            CVEObject.ImpactType ="";
                            CVEObject.VulnurabiltyType = "";
                            CVEObject.AttackVector = (String) cvssV3.get("accessVector");
                            CVEObject.AccessComplexity = (String) cvssV3.get("accessComplexity");
                            CVEObject.PrevilagesRequired = "";
                            CVEObject.Authentication = (String) cvssV3.get("authentication");
                            CVEObject.ConfidentialityImpact = (String) cvssV3.get("confidentialityImpact");
                            CVEObject.IntegrityImpact = (String) cvssV3.get("integrityImpact");
                            CVEObject.AvailibilityImpact = (String) cvssV3.get("availabilityImpact");
                        }
                        CVEObject.PatchLink = new ArrayList<>();
                        CVEObject.Date_Changed = CVEObject.LastUpdate;
                        CVEObject.GainedAccess = "";
                        org.json.simple.JSONObject configurations = (  org.json.simple.JSONObject) CVE.get("configurations");

                        com.atlas.crawler.core.CVE finalCVEObject = CVEObject;
                        numberofcveinjson=numberofcveinjson+1;

                        int finalNumberofcveinjson = numberofcveinjson;
                     //   System.out.println(  numberofcveinjson=numberofcveinjson+1);
                        Runnable r = new Runnable() {
                            public void run() {
                                try {

                                    GetEveryThingOfCVE(finalCVEObject,null,null,null,null,null,null, finalNumberofcveinjson);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (InvalidFormatException e) {
                                    e.printStackTrace();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                } catch (DataFormatException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        };
                        general.  pool.execute(r);


                    }
                }
                catch (Exception e)
                {
                }
            }
            CVEObject=null;

            jsonParser =null;
            System.gc();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }
    public  void recenet() throws SQLException {

            String path = FileSystems.getDefault().getPath("./download/download.zip").toString();
            Path Path = FileSystems.getDefault().getPath("./download/download.zip");


                try {
                    String path_r=System.getProperty("user.dir")+"\\download\\recent.zip";
                    URL url = new URL("https://nvd.nist.gov/feeds/json/cve/1.1/nvdcve-1.1-modified.json.zip");
                    URLConnection con;
                    DataInputStream dis;
                    FileOutputStream fos;
                    byte[] fileData;
                    con = url.openConnection(); // open the url connection.
                    dis = new DataInputStream(con.getInputStream());
                    fileData = new byte[con.getContentLength()];
                    for (int q = 0; q < fileData.length; q++) {
                        fileData[q] = dis.readByte();
                    }
                    dis.close(); // close the data input stream
                    fos = new FileOutputStream(new File(path_r ) ); //FILE Save Location goes here
                    fos.write(fileData);  // write out the file we want to save.
                    fos.close(); // close the output stream writer
                    File destDir = new File(System.getProperty("user.dir")+"\\download\\");
                    byte[] buffer = new byte[1024];
                    ZipInputStream zis = new ZipInputStream(new FileInputStream(path_r));
                    ZipEntry zipEntry = zis.getNextEntry();
                    while (zipEntry != null) {
                        File newFile = newFile(destDir, zipEntry);
                        if (zipEntry.isDirectory()) {
                            if (!newFile.isDirectory() && !newFile.mkdirs()) {
                                throw new IOException("Failed to create directory " + newFile);
                            }
                        } else {
                            // fix for Windows-created archives
                            File parent = newFile.getParentFile();
                            if (!parent.isDirectory() && !parent.mkdirs()) {
                                throw new IOException("Failed to create directory " + parent);
                            }

                            // write file content
                            fos = new FileOutputStream(newFile);
                            int len;
                            while ((len = zis.read(buffer)) > 0) {
                                fos.write(buffer, 0, len);
                            }
                            fos.close();
                        }
                        zipEntry = zis.getNextEntry();
                    }
                    zis.closeEntry();
                    zis.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


        recent_tenable( );

       recent_getdata();


        Connection MyConnection = null;


        MyConnection = connection;
        PreparedStatement MyStatement2 = null;
        PreparedStatement MyStatement3 = null;
        try{
            MyStatement2 = MyConnection.prepareStatement("insert into vendor_product_table(vendor,product)\n" +
                    "select package,product_name from vendor_product on conflict(vendor,product) do nothing");


        MyStatement2.executeUpdate();

            MyStatement3 = MyConnection.prepareStatement("insert into public.\"Tenable_vulns\" (\"vulns_id\",\"tenable_id\")\n" +
                    "select t.id as vulns_id , p.\"ID\" as Tenable_id from vulns t \n" +
                    "inner join public.\"Tenable\" p on p.\"CVE\"=t.cve \n" +
                    "on conflict (\"vulns_id\",\"tenable_id\") do nothing");


            MyStatement3.executeUpdate();
        }catch (Exception eee)
        {
            System.out.println(eee.getMessage());
        }

    System.err.println("All done");

    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public  void run() {
        if (ReportController.searchByPackage_flag && ReportController.UseDB) {
            ReportController.general_method = "fedora";
            general.start();
            try {
                general.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Crawler : FEDORA");
            boolean flag = false;
            rref = general.reference;
            general.rref = general.reference;
            if (rref.contains("nvd")) {
                String path = FileSystems.getDefault().getPath("./download/download.zip").toString();
                Path Path = FileSystems.getDefault().getPath("./download/download.zip");
                Date d = new Date();
                Calendar instance = Calendar.getInstance();
                int year = instance.get(Calendar.YEAR);
                int Lyear = -1;
                if (rref.contains("all"))
                    Lyear = 2002;
                else
                    Lyear = Integer.valueOf(year);

                for (int i = Lyear; i <= year; i++) {
                    try {
                        String path_r = System.getProperty("user.dir") + "\\download\\" + Integer.valueOf(i) + ".zip";
                        URL url = new URL("https://nvd.nist.gov/feeds/json/cve/1.1/nvdcve-1.1-" + i + ".json.zip");
                        URLConnection con;
                        DataInputStream dis;
                        FileOutputStream fos;
                        byte[] fileData;
                        con = url.openConnection(); // open the url connection.
                        dis = new DataInputStream(con.getInputStream());
                        fileData = new byte[con.getContentLength()];
                        for (int q = 0; q < fileData.length; q++) {
                            fileData[q] = dis.readByte();
                        }
                        dis.close(); // close the data input stream
                        fos = new FileOutputStream(new File(path_r)); //FILE Save Location goes here
                        fos.write(fileData);  // write out the file we want to save.
                        fos.close(); // close the output stream writer
                        File destDir = new File(System.getProperty("user.dir") + "\\download\\");
                        byte[] buffer = new byte[1024];
                        ZipInputStream zis = new ZipInputStream(new FileInputStream(path_r));
                        ZipEntry zipEntry = zis.getNextEntry();
                        while (zipEntry != null) {
                            File newFile = newFile(destDir, zipEntry);
                            if (zipEntry.isDirectory()) {
                                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                                    throw new IOException("Failed to create directory " + newFile);
                                }
                            } else {
                                // fix for Windows-created archives
                                File parent = newFile.getParentFile();
                                if (!parent.isDirectory() && !parent.mkdirs()) {
                                    throw new IOException("Failed to create directory " + parent);
                                }

                                // write file content
                                fos = new FileOutputStream(newFile);
                                int len;
                                while ((len = zis.read(buffer)) > 0) {
                                    fos.write(buffer, 0, len);
                                }
                                fos.close();
                            }
                            zipEntry = zis.getNextEntry();
                        }
                        zis.closeEntry();
                        zis.close();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                for (int i = year; i >= Lyear; i--) {
                    try {
                        allyears(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            String Tedad = "";
            if (rref.contains("tenable")) {
                String path = FileSystems.getDefault().getPath("./download/download.zip").toString();
                Path Path = FileSystems.getDefault().getPath("./download/download.zip");
                Date d = new Date();
                Calendar instance = Calendar.getInstance();
                int year = instance.get(Calendar.YEAR);
                int Lyear = -1;
                rref="tenable_all";
                if (rref.contains("all"))
                    Lyear = 2002;
                else
                    Lyear = Integer.valueOf(year);

                  for (int i = Lyear; i <= year; i++) {
                    try {
                        String path_r = System.getProperty("user.dir") + "\\download\\" + Integer.valueOf(i) + ".zip";
                        URL url = new URL("https://nvd.nist.gov/feeds/json/cve/1.1/nvdcve-1.1-" + i + ".json.zip");
                        URLConnection con;
                        DataInputStream dis;
                        FileOutputStream fos;
                        byte[] fileData;
                        con = url.openConnection(); // open the url connection.
                        dis = new DataInputStream(con.getInputStream());
                        fileData = new byte[con.getContentLength()];
                        for (int q = 0; q < fileData.length; q++) {
                            fileData[q] = dis.readByte();
                        }
                        dis.close(); // close the data input stream
                        fos = new FileOutputStream(new File(path_r)); //FILE Save Location goes here
                        fos.write(fileData);  // write out the file we want to save.
                        fos.close(); // close the output stream writer
                        File destDir = new File(System.getProperty("user.dir") + "\\download\\");
                        byte[] buffer = new byte[1024];
                        ZipInputStream zis = new ZipInputStream(new FileInputStream(path_r));
                        ZipEntry zipEntry = zis.getNextEntry();
                        while (zipEntry != null) {
                            File newFile = newFile(destDir, zipEntry);
                            if (zipEntry.isDirectory()) {
                                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                                    throw new IOException("Failed to create directory " + newFile);
                                }
                            } else {
                                // fix for Windows-created archives
                                File parent = newFile.getParentFile();
                                if (!parent.isDirectory() && !parent.mkdirs()) {
                                    throw new IOException("Failed to create directory " + parent);
                                }

                                // write file content
                                fos = new FileOutputStream(newFile);
                                int len;
                                while ((len = zis.read(buffer)) > 0) {
                                    fos.write(buffer, 0, len);
                                }
                                fos.close();
                            }
                            zipEntry = zis.getNextEntry();
                        }
                        zis.closeEntry();
                        zis.close();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("++++++++++++++++++");
                }
                for (int i = year; i >= Lyear; i--) {
                    try {
                        allyears_tenable(i);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
            if(rref.contains(("recent")))
            {
                try {
                    recenet();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public   File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }
    public void setCutTime(String  reference) throws SQLException,IOException,ParseException{
        if (general.FedoraCurrent_Time_Checking == true)
        {
            Connection MyConnection = null;
            PreparedStatement MyStatement = null;
            ResultSet RS_Last_CVE = null;
            general.FedoraCurrent_Time_Checking = false;
            try {
                MyConnection = connection;
                MyStatement = MyConnection.prepareStatement("select max(updated_at) from vulns where distro in (select id from lookups where val=?) and reference_list=?");
                MyStatement.setString(1, "Fedora");
                MyStatement.setString(2, reference);
                RS_Last_CVE = MyStatement.executeQuery();
                if (RS_Last_CVE.next()) {
                    general.Long_Date = (RS_Last_CVE.getLong("max") * 1000);
                    if (general.Long_Date == 0){
                        general.Long_Date = 1609462800000L;
                    }
                    else {
                        general.Long_Date = general.Long_Date - general.CentosDay * 86400000;
                    }
                }
                else {
                    general.Long_Date = 1609462800000L;
                }
                general.Cut_Time=new Date(general.Long_Date);
            }catch (Exception e){
            }
            finally {
                try {
                    MyStatement.close();
                    RS_Last_CVE.close();
                }catch (Exception e){
                }
            }
        }
        else return;
    }
    public   void AnalyzeEachCVE( Document BugzillaFirstPageDoc, Document EachSummaryDoc, Element CVEElement, String EachSummaryLink, CVE cve,String reference) {
        try {
            GetEveryThingOfCVE(null,EachSummaryLink, CVEElement, EachSummaryDoc,  BugzillaFirstPageDoc, cve,reference,0);
        } catch (Exception e) {
            if (e.getMessage() == null) {
                return;
            }

            }
        }
    public   void GetEveryThingOfCVE(CVE CVEObject,String EachSummaryLink, Element CVEElement, Document EachSummaryDoc,  Document BugzillaFirstPageDoc, CVE cve, String ref,int numberofcveinjson) throws Exception {
        general.c1=general.c1+1;
System.out.println("--------------   getEveryThing"+ CVEObject.CVEName +"       "+general.c1);


      EachSummaryLink=CVEObject.CVEName;
        Document CVENVD=null;
        Document CVEDetailsDoc=null;
        Document tenable=null;
        Document reconshell=null;
        String  AccessRedHatDocOfEachCVE=null;
            try {
try {

    Document test =general. getDocument2("https://nvd.nist.gov/vuln/detail/"+EachSummaryLink+"/cpes?expandCpeRanges=true");
   String tet2=test.toString();
    Element test2=test.getElementById("cveTreeJsonDataHidden");
    String test3=test2.getElementsByAttribute("value").val();
    int counter=0;
    int strat=    test3.indexOf("cpe:2.3",counter);
    counter=strat+1;
    strat=    test3.indexOf("cpe:2.3",counter);
    int end=test3.indexOf("*",strat+1);
    String cpe=test3.substring(strat,end);
    cpe=cpe.substring(10);
    String prev_ven=cpe.substring(0,cpe.indexOf(':'));
    cpe=cpe.substring(cpe.indexOf(':')+1);
    String prev_pack=cpe.substring(0,cpe.indexOf(':'));
    cpe=cpe.substring(cpe.indexOf(':')+1);
    ArrayList<String> productname = new ArrayList<>();
    ArrayList<String> package_name = new ArrayList<>();
    ArrayList<String> productversion = new ArrayList<>();
    CVEObject.PackageName.add(prev_ven);
    CVEObject.Product_version=new ArrayList<>();
    CVEObject.Product_name=new ArrayList<>();
    while (true)
    {
        try {
            strat=    test3.indexOf("cpe:2.3",counter);
            if(strat==-1)
            {
                CVEObject.Product_name.add(productname);
                CVEObject.Product_version.add(productversion);
                break;
            }
            counter=strat+1;
            end=test3.indexOf("*",strat+1);
            cpe=test3.substring(strat,end);
            cpe=cpe.substring(10);
            String ven=cpe.substring(0,cpe.indexOf(':'));
            cpe=cpe.substring(cpe.indexOf(':')+1);
            String pack=cpe.substring(0,cpe.indexOf(':'));
            int kk=0;
            if((ven.equals(prev_ven))&&(prev_pack.equals(pack)))
            {
                cpe=cpe.substring(cpe.indexOf(':')+1);
                String ver=cpe.substring(0,cpe.indexOf(':'));
                productname.add(pack);
                productversion.add(ver);
            }
            else
            {
                CVEObject.Product_name.add(productname);
                CVEObject.Product_version.add(productversion);
                productname = new ArrayList<>();
                productversion = new ArrayList<>();
                prev_ven=ven;
                prev_pack=pack;
                counter=strat+1;
                CVEObject.  PackageName.add(prev_ven);
            }
        }catch (Exception eeeee)
        {
        }
    }
}catch (Exception new_EE)
{
}
//////////////////////
            } catch (Exception e) {
            }
            try {
            //    CVEDetailsDoc =general. getDocument3("http://www.cvedetails.com/cve/" + EachSummaryLink);
            }catch (Exception e){
            }
            try {
           //     reconshell =general. getDocument3("https://cve.reconshell.com/cve/" + EachSummaryLink);
            }catch (Exception e){
            }
            try {
                CVENVD =general. getDocument3("https://nvd.nist.gov/vuln/detail/" + EachSummaryLink);
            } catch (Exception e) {
                CVENVD = null;
            }
            Bugzilla bugzilla = new Bugzilla(connection);
            try {
                CVEObject = GetProduct(CVEObject, CVEDetailsDoc,CVENVD,EachSummaryLink);
                if(CVEObject.Product_name.size()==0)
                    System.err.println("----------------------------------------empty product  =>"+CVEObject.CVEName);
            }catch (Exception e)
            {
            }
            CVEObject.Distro = "Fedora";
            try {
                CVEObject.RefferencesLinks.add("https://nvd.nist.gov/vuln/detail/" + CVEObject.CVEName);
            }catch (Exception e){
            }
            if (CVEObject.PackageName.size() == 0) {
                CVEObject.PackageName.add("Not Clear");
            }
                CVEObject.Date_Changed = CVEObject.LastUpdate;
                CVEObject.Synopsis="";
                CVEObject.Family="";
                CVEObject.Products_Affected="";
                CVEObject.Name="";
                CVEObject.Agent="";
        general.c2=general.c2+1;
        System.err.println("+++++++++++++++++++++++  EndofgetEveryThing"+ CVEObject.CVEName +"      "+general.c2);
        general.   Print(CVEObject, numberofcveinjson,ref);


    }

    public   CVE GetProduct(CVE CVEObject , Document CVEDetails ,Document CVENVD,String CVEElement){
        try {

            if(CVEObject.Product_name==null )
            {
                CveDetails cveDetails=new CveDetails(connection);
                CVEObject=cveDetails.GetProduct(CVEObject,CVEDetails,CVEElement);
            }
        }catch (Exception e){
            return  CVEObject;

        }
        return CVEObject;
    }
}
