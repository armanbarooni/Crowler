package com.atlas.crawler.core;

import com.atlas.crawler.adapter.Adapter;
import com.atlas.crawler.controller.ReportController;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.Settings;
import com.machinepublishers.jbrowserdriver.Timezone;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import javax.management.Query;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;


public class General extends  Thread{
    public static int counterr=0;
    public  void run()
    {


        try {
            if(ReportController.general_method.contains("fedora"))
            {
                GetFromDB(iStart,iFinish, "Fedora");
                searchByPackage(PackageNames,Versions, "Fedora");
            }
            else
            {
                GetFromDB(iStart,iFinish, "Centos");
                searchByPackage(PackageNames,Versions, "Centos");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DataFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }


    }

    static HashMap<String,JBrowserDriver> driverHashMap=new HashMap<>();
    public  static   Collection<List<String>> resultrecods=null;

    private static  boolean EC = false;
    static  int Use_version = 1;
    static  String reference=null;
    static boolean report = false;
    static boolean ErrataException = false;



    public   int finder=0;


    static long Long_Date = 0;
    static int ProblemCounter1 = 0;
    public  static  String  disstro="";

    static int ProblemCounter2 = 0;
    static List<String> AddedCVEs = new ArrayList<String>();
    static Date Today;
    static boolean FinishBodhi = false;
    static Date Cut_Time = null;
    public  static  int fedora_count=0;
    public static   int centos_count=0;
    public  static  int fedora_count1=0;
    public  static  int centos_count1=0;
    public  static  int fedora_count_b=0;
    public static   int centos_count_b=0;
    public  static  int fedora_count1_b=0;
    public  static  int centos_count1_b=0;


    public  static  int c1=0;
    public  static  int c2=0;
    public  static  int c3=0;
    public  static  int c4=0;





    static final int CentosDay = -1  ;
    static boolean RunFedora = false;
    static boolean RunCentos = true;
    static boolean FedoraCurrent_Time_Checking = true; // loop
    static  boolean CentosCurrent_Time_Checking = true; // centos
    static  String iStart = null;
    static String iFinish = null;
    public static String PackageNames;
    public static String Versions;
    public  static boolean isrunung=false;
    static final Logger LOGGER = Logger.getLogger(General.class.getName());
    public  static   boolean site=false;
    int processors = Runtime.getRuntime().availableProcessors();
    static String rref="";
    public     ExecutorService pool = Executors.newFixedThreadPool(processors);
    private Connection connection;
    public General(Connection connection){
        this.connection = connection;
    }

    public    boolean netisavail()
    {
        try {
            final URL url=new URL("http://www.google.com");
            final URLConnection conn=url.openConnection();
            conn.connect();
            conn.getInputStream().close();
            return  true;
        }catch (IOException e)
        {
            return  false;
        }
    }
    public   void download_patch(String link,String directory,String disstro,String ver,char harf){
        if(disstro.equals("centos") )
        {
            String last_ling="https://vault.centos.org/8.3.2011/BaseOS/Source/SPackages/"+link;

            URL url;
            URLConnection con;
            DataInputStream dis;
            FileOutputStream fos;
            byte[] fileData;
            try {
                url = new URL(last_ling); //File Location goes here
                con = url.openConnection(); // open the url connection.
                dis = new DataInputStream(con.getInputStream());
                fileData = new byte[con.getContentLength()];
                for (int q = 0; q < fileData.length; q++) {
                    fileData[q] = dis.readByte();
                }
                dis.close(); // close the data input stream
                fos = new FileOutputStream(new File(directory+"/"+link)); //FILE Save Location goes here
                fos.write(fileData);  // write out the file we want to save.
                fos.close(); // close the output stream writer
            }
            catch(Exception m) {
                System.out.println(m);
            }
        }
        else {
            String last_ling=null;
            if(ver.equals("32")){
                last_ling="https://dl.fedoraproject.org/pub/fedora/linux/updates/32/Everything/SRPMS/Packages/"+harf+"/"+link;
            }
            else if(ver.equals("30")){
                last_ling="https://archives.fedoraproject.org/pub/archive/fedora/linux/updates/30/Everything/SRPMS/Packages/"+harf+"/"+link;

            }

            URL url;
            URLConnection con;
            DataInputStream dis;
            FileOutputStream fos;
            byte[] fileData;
            try {
                url = new URL(last_ling); //File Location goes here
                con = url.openConnection(); // open the url connection.
                dis = new DataInputStream(con.getInputStream());
                fileData = new byte[con.getContentLength()];
                for (int q = 0; q < fileData.length; q++) {
                    fileData[q] = dis.readByte();
                }
                dis.close(); // close the data input stream
                fos = new FileOutputStream(new File(directory+"/"+link)); //FILE Save Location goes here
                fos.write(fileData);  // write out the file we want to save.
                fos.close(); // close the output stream writer
            }
            catch(Exception m) {
                System.out.println(m);
            }
        }

    }
    public  void reset_static(){
        ReportController. wholeIds = new ArrayList<Integer>();
        ReportController. resultrecods=null;
        EC = false;
        Use_version = 1;
        reference=null;
        ReportController. UseDB = false;
        ReportController. searchByPackage_flag = false;
        report = false;

        ErrataException = false;

        Long_Date = 0;
        ProblemCounter1 = 0;
        String  disstro="";

        ProblemCounter2 = 0;
        AddedCVEs = new ArrayList<String>();
        FinishBodhi = false;
        Cut_Time = null;
        int fedora_count=0;
        int centos_count=0;
        int fedora_count1=0;
        int centos_count1=0;
        int CentosDay = 1;
        RunFedora = false;
        RunCentos = true;
        FedoraCurrent_Time_Checking = true; // loop
        CentosCurrent_Time_Checking = true; // centos
        iStart = null;
        iFinish = null;


    }
    public   void datting(Date date,String output)
    {
        try{
            FileWriter fstream = new FileWriter("date.txt",true);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write( output+"    " + date+"\n");
            out.close();

        }catch (Exception e){
            System.err.println("Error while writing to file: " +
                    e.getMessage());
        }
        return;
    }
    public  void searchByPackage2(String packageNames, String Versions) throws SQLException, DataFormatException, IOException, InvalidFormatException {

        Connection MyConnection = null;
        int flag = 0;
        PreparedStatement MyStatement1 = null;
        String[] split_package = packageNames.split(",");
        String[] split_version = Versions.split(",");
        try {
            int it = 0;
            int itt = 0;
            String subQuery = "";
            /////////////////////// B: this section is add in version 4.4
            for (int i = 0; i < split_package.length; i++) {
                split_package[i] = split_package[i].replace("'", "");//B :this code was added to prevent sali
                split_version[i] = split_version[i].replace("'", "");
                split_version[i] = split_version[i].replace(" ", "");


                it = it + 1;
                flag = 0;
                if (split_package[i].equals("#")) {
                    split_package[i] = "";

                }
                if (split_package[i].equals("kernel")) {
                    split_package[i] = "linux_kernel";

                }
                if (split_version[i].equals("-")) {
                    split_version[i] = "";
                }
                subQuery += " select id from product where LOWER(package_name) like LOWER('%"+split_package[i]+"%') and version like '%"+split_version[i]+"%' and distribution like '%" + "" + "%' " + " union";  //merge all queries in  one query   created by REza deHghani
            }
            subQuery = subQuery.substring(0, subQuery.length() - 5);   //this code remove last  union of  subquery
            MyConnection = connection;
            String final_query = "select vulns.id,vulns.package,product.package_name,product.version from vulns,product,product_vulns where product_vulns.package_id=product.id and product_vulns.vulns_id=vulns.id  and  vulns.id in(select vulns_id from product_vulns where package_id in(" + subQuery + "))   and product.id in (select package_id from product_vulns where package_id in  ( " + subQuery + "))  limit 5";
            MyStatement1 = MyConnection.prepareStatement(final_query);
            PreparedStatement my=MyConnection.prepareStatement(final_query);
            ResultSet r = MyStatement1.executeQuery();
            //    String name = CreateName2(distro, distro);
           /* ResultSet t=my.executeQuery();
            int coun=0;
            while(t.next())
                coun=coun+1;
            System.out.println("number of cve that we find ->"+coun);*/
            resultrecods = new ArrayList<>();

            while (r.next()) {
                int idNumber = r.getInt("id");
                String packagename=r.getString("package_name");
                String version=r.getString("version");


                List<String> record = FillExcelRow(Integer.toString(idNumber),disstro);
                //    record.add(packagename);
                //      record.add(version);

                //    if(site==true)
                resultrecods.add(record);
                //     if(site==false)
                //      WriteOnExcel(record.toArray(new String[0]), name);
            }

            //T





            if (flag == 1) {
            }

        } catch (Exception e) {
        } finally {
            try {
                ;
                MyStatement1.close();
            } catch (Exception e) {
            }
        }
        EC = false;
    }

    public  int WeekDayToNumber(String WeekDay) {
        int num = -1;
        switch (WeekDay.toLowerCase()) {

            case "sun":
                num = 1;
                break;
            case "mon":
                num = 2;
                break;
            case "tue":
                num = 3;
                break;
            case "wed":
                num = 4;
                break;
            case "thu":
                num = 5;
                break;
            case "fri":
                num = 6;
                break;
            case "sat":
                num = 7;
                break;
        }
        return num;

    }

    public  Document getDocument(String url) throws IOException, InterruptedException,java.io.IOException,java.lang.NullPointerException {
        if(fedora_count1<10)
            System.err.println("jbrowser testing...");
        if(centos_count<10)
            System.err.println("jbrowser testing...");

        boolean continues=true;
        Document document=null;

        JBrowserDriver driver;
        int counter1=0;
        while (continues)
        {
            continues=false;
            try {

                driver=new JBrowserDriver(Settings.builder().timezone(Timezone.AMERICA_NEWYORK).build());
                try {
                    driver.get(url);

                    document = Jsoup.parse(driver.getPageSource());
                }catch (Exception e){

                }

                driver.quit();
            } catch (Exception e) {

                continues=true;
                Thread.sleep(1000);

                counter1=counter1+1;
                if(counter1==5)
                {
                    boolean netisavail=netisavail();
                    if(netisavail)
                        continues=false;
                    counter1=0;

                }

            }
            if(continues==false)
            {
                if(fedora_count1<10)
                    System.err.println("jbrowser testin success 3/4");
                if(centos_count<10)
                    System.err.println("jbrowser testin success 3/4");
            }

        }

        return document;

    }
    public  Document getDocument2(String url) throws IOException, InterruptedException {
        int counter1=0;
        boolean continues=true;
        Document document=null;
        while (continues)
        {
            continues=false;
            try {
                document =  Jsoup.connect(url).maxBodySize(0).timeout(200000).get();;
            } catch (Exception e) {
                Thread.sleep(1500);

                if(e.toString().contains("No route to host: connect") || e.toString().contains("java.net.UnknownHostException") || e.getMessage().contains("Connection refused")||e.getMessage().contains("org.openqa.selenium.NoSuchElementException: Element not found or does not exist"))
                {

                    continues=true;
                    Thread.sleep(3000);
                }else {
                    //   e.printStackTrace();
                }
                counter1=counter1+1;
                if(counter1==5)
                {
                    boolean netisavail=netisavail();
                    if(netisavail)
                        continues=false;
                    counter1=0;

                }

            }
        }

        return document;

    }
    public  Document getDocument3(String url) throws IOException, InterruptedException {
        int counter1=0;
        boolean continues=true;
        Document document=null;
        while (continues)
        {
            continues=false;
            try {
                document =         Jsoup.connect(url).get();

            } catch (Exception e) {
                Thread.sleep(1500);

                if(e.toString().contains("No route to host: connect") || e.toString().contains("java.net.UnknownHostException") || e.getMessage().contains("Connection refused")||e.getMessage().contains("org.openqa.selenium.NoSuchElementException: Element not found or does not exist"))
                {

                    continues=true;
                    Thread.sleep(3000);
                }else {
                    e.printStackTrace();
                }
                counter1=counter1+1;
                if(counter1==5)
                {
                    boolean netisavail=netisavail();
                    if(netisavail)
                        continues=false;
                    counter1=0;

                }

            }
        }

        return document;
    }

    public  Date ToDate(Element A_Row_of_Bugzilla_List, Document Bugzilla_List) throws ParseException {
        String Date = A_Row_of_Bugzilla_List.getElementsByClass("bz_changeddate_column").first().ownText();
        String CurrentDateString = Bugzilla_List.getElementsByClass("bz_query_timestamp").first().ownText();
        Date CurrentDate = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss ", Locale.ENGLISH).parse(CurrentDateString);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(CurrentDate);
        if (Date.contains("-"))//2017-03-17
        {
            CurrentDate = new SimpleDateFormat("yyyy-MM-dd").parse(Date);
        } else if (Date.toLowerCase().matches(".*[a-z].*"))//Fri 11:43
        {
            int CurrentWeekDay = calendar.get(Calendar.DAY_OF_WEEK);
            int PreviousWeekDay = WeekDayToNumber(Date.split("\\s+")[0]);
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
        } else //11:45:34
        {
            CurrentDate = new SimpleDateFormat("HH:mm:ss,E MMM dd yyyy").parse(Date + "," + CurrentDateString);
        }
        //Obj.Date_Changed = CurrentDate.toString();
        return CurrentDate;
    }

    public  boolean Check_Period(String Start, String Finish, Element A_Row_of_Bugzilla_List, Document Bugzilla_List) throws ParseException {
        if (Start == null && Finish == null) {
            return true;
        }
        Date StartDate = new SimpleDateFormat("yyyy-MM-dd").parse(Start);
        Date FinishDate = new SimpleDateFormat("yyyy-MM-dd").parse(Finish);
        Date CVE_Date = ToDate(A_Row_of_Bugzilla_List, Bugzilla_List);
        if (CVE_Date.before(FinishDate) && CVE_Date.after(StartDate)) {
            return true;
        } else {
            return false;
        }
    }

    public  CVE GetCVEName(CVE Obj, Element element1) {
        Obj.CVEName = element1.ownText();
        return Obj;
    }//filling cve name


    public  void Print(CVE Obj, int i,String ref) throws FileNotFoundException, IOException, InvalidFormatException, SQLException, ParseException, DataFormatException {
        int id = 0;
        try {
            id = Add_CVE_to_Database(Obj,ref,i);
        } catch (Exception e) {

        }
        // AddedCVEs.add(Integer.toString(id));
        if (i == 0) {

        } else {
        }
    }

    ;
    public  int counter = 0;
    int counnnt=0;
    public  void add_cve_to_database2(int id,
                                      String Agent,
                                      String synp,
                                      String  name,
                                      String CVE,
                                      String soloution,
                                      String family,
                                      Date Datee,
                                      ArrayList<String >  v,
                                      ArrayList<String >  p,
                                      ArrayList<String >  ve
    ) throws SQLException {
        try {
            System.out.println("ADDTODATABASE->     "+String.valueOf(id));
            Connection MyConnection = null;
            MyConnection = connection;

            PreparedStatement MyStatement = null;

            Long ddate=(Long) (Datee.getTime() / 1000);
            for(int i=0;i<=v.size()-1;i++)
            {
                MyStatement = MyConnection.prepareStatement("insert into \"Tenable\" (\"CVE\" , \"ID\" , \"agent\", \"family\", \"soloution\", \"synopsis\", \"title\", \"date\" ,  \"Vendor\" , \"Products\"  ,\"Versions\") values (?,?,?,?,?,?,?,?,?,?,?) ON CONFLICT (\"ID\" ,\"CVE\",\"Vendor\",\"Products\",\"Versions\" ) DO UPDATE SET date=? ") ;
                MyStatement.setString(1,CVE);
                MyStatement.setInt(2,id);
                MyStatement.setString(3,Agent);
                MyStatement.setString(4,family);
                MyStatement.setString(5,soloution);
                MyStatement.setString(6,synp);
                MyStatement.setString(7,name);
                MyStatement.setLong(8,ddate);
                MyStatement.setLong(12,ddate);
                MyStatement.setString(9,v.get(i));
                MyStatement.setString(10,p.get(i));
                MyStatement.setString(11,ve.get(i));

                MyStatement.executeUpdate();

            }
            System.out.println("ADD_Done->     "+String.valueOf(id));

        }catch (Exception e)
        {
            System.out.println("general->     "+e.getMessage());
        }

    }

    public  int Add_CVE_to_Database(CVE Obj,String ref,int numberofcveinjson) throws SQLException, DataFormatException, UnsupportedEncodingException {
        datting(Obj.LastUpdate,Obj.CVEName);
        int ids = 0;
        General general= new General(connection);
        System.out.println("ADD  "+ Obj.CVEName +"         "+general.c3);
        general.c3=general.c3+1;
        for(int counterr=0;counterr<Obj.PackageName.size();counterr=counterr+1) {
            String p_name = Obj.PackageName.get(counterr);
            ArrayList<String> productha = new ArrayList<>();
            ArrayList<String> versionha = new ArrayList<>();
            productha = Obj.Product_name.get(counterr);
            versionha = Obj.Product_version.get(counterr);

            PreparedStatement MyStatement26 = null;
            Connection MyConnection = null;


            try {
                MyConnection = connection;


                MyStatement26 = MyConnection.prepareStatement(
                        "insert into vulns (CWE, package, cve, reported_at, updated_at, description, patch, "
                                + "attack_vector, access_complexity, cvss , authentication, impact_type, integrity_impact, confidentiality_impact,"
                                + " availability_impact, privileges_require, reference)"
                                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
                                + " ON CONFLICT (package,cve) DO UPDATE SET "
                                + "CWE= ?, package=?,cve=?,  reported_at=?,"
                                + " description=?, patch=?, "
                                + "attack_vector=?, access_complexity=?, cvss=?,"
                                + "authentication= ?, impact_type=?, integrity_impact=?,"
                                + "confidentiality_impact=?,availability_impact=?,"
                                + "privileges_require=?, reference=?, "
                                + "updated_at=?");
                MyStatement26.setString(1, Obj.CWE);
                MyStatement26.setString(2, p_name);
                MyStatement26.setString(3, Obj.CVEName);
                MyStatement26.setLong(4, (Long) (Obj.BroadcastDate.getTime() / 1000));
                MyStatement26.setLong(5, (Long) (Obj.LastUpdate.getTime() / 1000));
                MyStatement26.setString(6, Obj.Desc);
                MyStatement26.setString(7, ArrayToString(Obj.PatchLink));
                MyStatement26.setString(8, Obj.AttackVector);
                MyStatement26.setString(9, Obj.AccessComplexity);
                MyStatement26.setDouble(10, Double.parseDouble(Obj.CVSS));
                MyStatement26.setString(11, Obj.Authentication);
                MyStatement26.setString(12, Obj.ImpactType);
                MyStatement26.setString(13, Obj.IntegrityImpact);
                MyStatement26.setString(14, Obj.ConfidentialityImpact);
                MyStatement26.setString(15, Obj.AvailibilityImpact);
                MyStatement26.setString(16, Obj.PrevilagesRequired);
                MyStatement26.setString(17, ArrayToString(Obj.RefferencesLinks));

                MyStatement26.setString(18, Obj.CWE);
                MyStatement26.setString(19, p_name);
                MyStatement26.setString(20, Obj.CVEName);
                MyStatement26.setLong(21, (Long) (Obj.BroadcastDate.getTime() / 1000));
                MyStatement26.setString(22, Obj.Desc);
                MyStatement26.setString(23, ArrayToString(Obj.PatchLink));
                MyStatement26.setString(24, Obj.AttackVector);
                MyStatement26.setString(25, Obj.AccessComplexity);
                MyStatement26.setDouble(26, Double.parseDouble(Obj.CVSS));
                MyStatement26.setString(27, Obj.Authentication);
                MyStatement26.setString(28, Obj.ImpactType);
                MyStatement26.setString(29, Obj.IntegrityImpact);
                MyStatement26.setString(30, Obj.ConfidentialityImpact);
                MyStatement26.setString(31, Obj.AvailibilityImpact);
                MyStatement26.setString(32, Obj.PrevilagesRequired);
                MyStatement26.setString(33, ArrayToString(Obj.RefferencesLinks));
                MyStatement26.setLong(34, (Long) (Obj.LastUpdate.getTime() / 1000));

                ids = MyStatement26.executeUpdate();
                } finally {
                try {

                    MyStatement26.close();

                } catch (Exception e) {

                }
            }
                try {
                    PreparedStatement MyStatement2 = null;
                    PreparedStatement MyStatement3 = null;
                    int vuln_id=-1;
                    String query="";
                    MyStatement2 = MyConnection.prepareStatement("select id from vulns where cve=? and package=?  ORDER BY id DESC  LIMIT 1");
                    MyStatement2.setString(1,Obj.CVEName);
                    MyStatement2.setString(2,p_name);

                    ResultSet res = MyStatement2.executeQuery();

                    if (res.next()) {
                        vuln_id = res.getInt("id");
                    }

                    query=query+ "insert into product (product_name,version,vulns_id) values ";
                    for(int counterr1=0;counterr1<Obj.Product_name.get(counterr) .size();counterr1=counterr1+1) {
                            query+= "    (  '"+Obj.Product_name.get(counterr).get(counterr1)+"'  , '"+Obj.Product_version.get(counterr).get(counterr1)+"' , "+vuln_id+")  ,";

                    }
                    query=query.substring(0,query.length()-1);
                    query += " ON CONFLICT (product_name,version,vulns_id) DO nothing ";
                    try {
                        MyStatement3 = MyConnection.prepareStatement(query);
                        MyStatement3.executeUpdate();
                    }catch (Exception e)
                    {
                        System.out.println(Obj.CVEName+"  "+ e.getMessage());
                    }

                } catch (Exception e) {

                }





            productha=null;
            versionha=null;
        }
        System.gc();
        System.err.println(" EndADD"+ Obj.CVEName +"      "+general.c4);
        general.c4=general.c4+1;
        return ids;



    }

    public  String ArrayToString(List<String> Input) {
        Set<String> hs = new HashSet<>();
        hs.addAll(Input);
        Input.clear();
        Input.addAll(hs);
        String listString = "";
        for (String s : Input) {
            listString += (s + "\n");
        }
        return listString;
    }

    public  List<String> devideToWeeks(String StartDate, String FinishDate) throws ParseException {
        Date sdate = new SimpleDateFormat("yyyy-MM-dd").parse(StartDate);
        Date fdate = new SimpleDateFormat("yyyy-MM-dd").parse(FinishDate);
        List<String> collection = new ArrayList<String>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        Calendar fcal = Calendar.getInstance();
        fcal.setTime(fdate);

        while (true) {
            Calendar scal = Calendar.getInstance();
            scal.setTime(sdate);
            Calendar yesterdayscal = Calendar.getInstance();
            yesterdayscal.setTime(sdate);
            scal.add(Calendar.DATE, 8);
            yesterdayscal.add(Calendar.DATE, 7);
            String endOfTheWeekString = scal.get(Calendar.YEAR) + "-" + (scal.get(Calendar.MONTH) + 1) + "-" + scal.get(Calendar.DAY_OF_MONTH);
            Date endOfTheWeekDate = new SimpleDateFormat("yyyy-MM-dd").parse(endOfTheWeekString);
            if (endOfTheWeekDate.after(fdate)) {
                collection.add(df.format(sdate) + "," + df.format(fdate));
                break;
            } else
                collection.add(df.format(sdate) + "," + df.format(endOfTheWeekDate));
            sdate = yesterdayscal.getTime();
        }
        return collection;
    }

    public  List<String> FillExcelRow(String ID,String distro) throws SQLException, DataFormatException, UnsupportedEncodingException {
        List<String> row = new ArrayList<String>();
        Connection MyConnection = null;
        PreparedStatement MyStatement1 = null;
        PreparedStatement MyStatement2 = null;
        ResultSet cve_row = null;
        ResultSet cve_row2 = null;
        ResultSet cve_row3 = null;
        ResultSet cve_row4 = null;
        ResultSet cve_row5 = null;
        ResultSet cve_row6 = null;
        ResultSet cve_row7 = null;
        ResultSet cve_row8 = null;
        ResultSet cve_row9 = null;
        ResultSet cve_row10 = null;
        ResultSet cve_row11 = null;
        ResultSet cve_row12 = null;
        ResultSet cve_row13 = null;
        ResultSet cve_row14 = null;
        ResultSet cve_row15 = null;
        try {
            MyConnection = connection;
            MyStatement1 = MyConnection.prepareStatement("select * from vulns where id =?");
            MyStatement1.setInt(1, Integer.valueOf(ID));
            cve_row = MyStatement1.executeQuery();
            cve_row.next();

            MyStatement1 = MyConnection.prepareStatement("select val from lookups where id =? and type='PCK' ");
            MyStatement1.setInt(1, Integer.valueOf(cve_row.getString("package")));
            cve_row2 = MyStatement1.executeQuery();
            cve_row2.next();
            MyStatement1 = MyConnection.prepareStatement("select val from lookups where id =? and type='DIS' ");
            MyStatement1.setInt(1, Integer.valueOf(cve_row.getString("distro")));
            cve_row3 = MyStatement1.executeQuery();
            cve_row3.next();
            MyStatement1 = MyConnection.prepareStatement("select val from lookups where id =? and type='PLT' ");
            MyStatement1.setInt(1, Integer.valueOf(cve_row.getString("platform")));
            cve_row4 = MyStatement1.executeQuery();
            cve_row4.next();
            MyStatement1 = MyConnection.prepareStatement("select val from lookups where id =? and type='TYP' ");
            MyStatement1.setInt(1, Integer.valueOf(cve_row.getString("vuln_type")));
            cve_row5 = MyStatement1.executeQuery();
            cve_row5.next();
            MyStatement1 = MyConnection.prepareStatement("select val from lookups where id =? and type='ATV' ");
            MyStatement1.setInt(1, Integer.valueOf(cve_row.getString("attack_vector")));
            cve_row6 = MyStatement1.executeQuery();
            cve_row6.next();
            MyStatement1 = MyConnection.prepareStatement("select val from lookups where id =? and type='ACC' ");
            MyStatement1.setInt(1, Integer.valueOf(cve_row.getString("access_complexity")));
            cve_row7 = MyStatement1.executeQuery();
            cve_row7.next();
            MyStatement1 = MyConnection.prepareStatement("select val from lookups where id =? and type='AUT' ");
            MyStatement1.setInt(1, Integer.valueOf(cve_row.getString("authentication")));
            cve_row8 = MyStatement1.executeQuery();
            cve_row8.next();
            MyStatement1 = MyConnection.prepareStatement("select val from lookups where id =? and type='IMT' ");
            MyStatement1.setInt(1, Integer.valueOf(cve_row.getString("impact_type")));
            cve_row9 = MyStatement1.executeQuery();
            cve_row9.next();
            MyStatement1 = MyConnection.prepareStatement("select val from lookups where id =? and type='INI' ");
            MyStatement1.setInt(1, Integer.valueOf(cve_row.getString("integrity_impact")));
            cve_row10 = MyStatement1.executeQuery();
            cve_row10.next();//
            MyStatement1 = MyConnection.prepareStatement("select val from lookups where id =? and type='COI' ");
            MyStatement1.setInt(1, Integer.valueOf(cve_row.getString("confidentiality_impact")));
            cve_row11 = MyStatement1.executeQuery();
            cve_row11.next();
            MyStatement1 = MyConnection.prepareStatement("select val from lookups where id =? and type='AVA' ");
            MyStatement1.setInt(1, Integer.valueOf(cve_row.getString("availability_impact")));
            cve_row12 = MyStatement1.executeQuery();
            cve_row12.next();
            MyStatement1 = MyConnection.prepareStatement("select val from lookups where id =? and type='PRI' ");
            MyStatement1.setInt(1, Integer.valueOf(cve_row.getString("privileges_require")));
            cve_row13 = MyStatement1.executeQuery();
            cve_row13.next();

            MyStatement1 = MyConnection.prepareStatement("select val from lookups where id =? and type='GAA' ");
            MyStatement1.setInt(1, Integer.valueOf(cve_row.getString("gained_access")));
            cve_row14 = MyStatement1.executeQuery();
            cve_row14.next();

        } catch (Exception e) {
        } finally {
            try {
                row.add(cve_row2.getString("val"));//pck
                row.add(distro);//dis
                row.add(new SimpleDateFormat("dd/MM/yyyy").format(new Date(cve_row.getLong("current_times") * 1000)));
                row.add(cve_row4.getString("val"));//plt
                row.add(cve_row.getString("cve"));
                //     System.out.println(cve_row.getString("cve"));
                row.add(new SimpleDateFormat("dd/MM/yyyy").format(new Date(cve_row.getLong("reported_at") * 1000)));
                row.add(new SimpleDateFormat("dd/MM/yyyy").format(new Date(cve_row.getLong("updated_at") * 1000)));
                row.add(cve_row.getString("description"));
                row.add(cve_row5.getString("val"));//type
                row.add(cve_row6.getString("val"));//atv
                row.add(cve_row7.getString("val"));//acc
                row.add(cve_row.getString("cvss_v3"));
                row.add(cve_row.getString("cvss").replace("\n"," __ "));
                row.add(cve_row8.getString("val"));//aut
                row.add(cve_row9.getString("val"));//imt
                row.add(cve_row10.getString("val"));//int
                row.add(cve_row11.getString("val"));//coi
                row.add(cve_row12.getString("val"));//ava
                row.add(cve_row13.getString("val"));//pri
                row.add(cve_row14.getString("val"));//gaa
                row.add(cve_row.getString("reference").replace("\n"," __ "));
                row.add(cve_row.getString("comments").replace("\n"," __ "));
                row.add(cve_row.getString("patch").replace("\n"," __ "));

                MyStatement1.close();
                ;
                cve_row.close();
            } catch (Exception e) {

            }
        }

        return row;
    }

    public  List<String> FillExcelRow_2(JsonObject obj, String package_name, String platform) throws SQLException, DataFormatException, UnsupportedEncodingException {
        List<String> row = new ArrayList<String>();
        row.add(platform); //package
        row.add("");  //distro
        row.add(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        row.add(package_name); //platform

        row.add(obj.get("cve").getAsJsonObject().get("CVE_data_meta").getAsJsonObject().get("ID").getAsString());
        row.add(obj.get("publishedDate").getAsString());
        row.add(obj.get("lastModifiedDate").getAsString());
        JsonArray description = obj.getAsJsonObject("cve").get("description").getAsJsonObject().getAsJsonArray("description_data");
        row.add(description.get(0).getAsJsonObject().get("value").getAsString());
        row.add("");
        row.add("");
        row.add(obj.get("impact").getAsJsonObject().get("baseMetricV3").getAsJsonObject().get("cvssV3").getAsJsonObject().get("attackVector").getAsString());
        row.add(obj.get("impact").getAsJsonObject().get("baseMetricV3").getAsJsonObject().get("cvssV3").getAsJsonObject().get("attackComplexity").getAsString());
        row.add(obj.get("impact").getAsJsonObject().get("baseMetricV3").getAsJsonObject().get("cvssV3").getAsJsonObject().get("baseScore").getAsString());
        row.add("RedHat-V3");
        row.add("");
        row.add(obj.get("impact").getAsJsonObject().get("baseMetricV3").getAsJsonObject().get("cvssV3").getAsJsonObject().get("baseSeverity").getAsString());
        row.add(obj.get("impact").getAsJsonObject().get("baseMetricV3").getAsJsonObject().get("cvssV3").getAsJsonObject().get("integrityImpact").getAsString());
        row.add(obj.get("impact").getAsJsonObject().get("baseMetricV3").getAsJsonObject().get("cvssV3").getAsJsonObject().get("confidentialityImpact").getAsString());
        row.add(obj.get("impact").getAsJsonObject().get("baseMetricV3").getAsJsonObject().get("cvssV3").getAsJsonObject().get("availabilityImpact").getAsString());
        row.add(obj.get("impact").getAsJsonObject().get("baseMetricV3").getAsJsonObject().get("cvssV3").getAsJsonObject().get("privilegesRequired").getAsString());
        row.add("");
        row.add(obj.getAsJsonObject("cve").get("references").getAsJsonObject().get("reference_data").getAsJsonArray().get(0).getAsJsonObject().get("url").getAsString());
        row.add("");

        return row;
    }

    public  void GetFromDB(String iStart, String iFinish, String distro) throws ParseException, SQLException, DataFormatException, IOException, InvalidFormatException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        List<String> PeriodCollection = devideToWeeks(iStart, iFinish);
        Connection MyConnection = null;
        PreparedStatement Statement = null;
        ResultSet IDs = null;
        try {

            MyConnection = connection;
            for (String period : PeriodCollection) {
                EC = false;
                Date start = formatter.parse(period.split(",")[0]);
                Date finish = formatter.parse(period.split(",")[1]);
                int startint = (int) (start.getTime() / 1000);
                int finishint = (int) (finish.getTime() / 1000);

                //--------------------------------------------------------------------------

                Statement = MyConnection.prepareStatement("select id from vulns where reported_at >=? and reported_at <= ? ");
                Statement.setInt(1, startint);
                Statement.setInt(2, finishint);
                //       Statement.setString(3, distro);
                IDs = Statement.executeQuery();
                while (IDs.next()) {
                    int idNumber = IDs.getInt("id");
                    ReportController.    wholeIds.add(idNumber);

                }


            }
        } catch (Exception e) {


        } finally {
            try {
                Statement.close();
                ;
                IDs.close();
            } catch (Exception e) {

            }
        }


    }

    public  String CreateName(String s, Date start, Date finish) {
        String Name = "";
        Name = Name + s.toUpperCase() + "-" + "CVE";
        SimpleDateFormat formatter1 = new SimpleDateFormat("ddMMM");
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy");
        Name = Name + "List-" + formatter1.format(start) + "-to-" + formatter1.format(finish) + "-" + formatter2.format(start);
        Name = Name.toUpperCase();
        return Name;
    }

    public  String CreateName2(String s, String distro) {
        String Name = "";
        Name = Name + s.toUpperCase() + "-" + "CVE";

        Name = Name + "List";
        Name = Name + "-" + distro;
        Name = Name.toUpperCase();
        Date date  =new java.util.Date();
        Name=Name+date.toString();
        Name=Name.replace(" ","-");
        Name= Name.replace(":","");
        return Name;
    }


    public  void searchByPackage(String packageNames, String Versions, String distro) throws SQLException, DataFormatException, IOException, InvalidFormatException {



        ReportController.  resultrecods = new ArrayList<>();

        Connection MyConnection = null;
        int flag = 0;
        PreparedStatement MyStatement1 = null;
        String[] split_package = packageNames.split(",");
        String[] split_version = Versions.split(",");
        String vv=null;
        try {
            int it = 0;
            int itt = 0;
            String subQuery = "";

            /////////////////////// B: this section is add in version 4.4
            for (int i = 0; i < split_package.length; i++) {
                vv="";
                split_package[i] = split_package[i].replace("=", "");//B :this code was added to prevent sali
                split_package[i] = split_package[i].replace(" and ", "");//B :this code was added to prevent sali

                split_package[i] = split_package[i].replace(" or ", "");//B :this code was added to prevent sali
                split_package[i] = split_package[i].replace("-", "");//B :this code was added to prevent sali

                split_package[i] = split_package[i].replace("'", "");//B :this code was added to prevent sali
                if(split_version.length>i){
                    split_version[i] = split_version[i].replace("'", "");
                    split_version[i] = split_version[i].replace("-", "");
                    split_version[i] = split_version[i].replace("=", "");
                    split_version[i] = split_version[i].replace(" or ", "");
                    split_version[i] = split_version[i].replace(" and ", "");

                    split_version[i] = split_version[i].replace(" ", "");
                    vv=split_version[i];
                    if ((Use_version==0)) {
                        vv = "";
                    }
                }
                it = it + 1;
                flag = 0;
                if (split_package[i].equals("#")) {
                    split_package[i] = "";

                }
                if (split_package[i].equals("kernel")) {
                    split_package[i] = "linux_kernel";

                }
                if(!vv.equals("")){
                    if (vv.equals("*")) {
                        vv = "";
                    }
                }
                if(Use_version==2)
                {
                    subQuery += " select id from product where LOWER(package_name) like LOWER('"+split_package[i]+"%')  and version = '"+vv+"' and distribution like '%" + distro + "%' " + " union";  //merge all queries in  one query   created by REza deHghani

                }
                else
                    subQuery += " select id from product where LOWER(package_name) like LOWER('"+split_package[i]+"%')  and version like '"+vv+"%' and distribution like '%" + distro + "%' " + " union";  //merge all queries in  one query   created by REza deHghani
            }
            subQuery = subQuery.substring(0, subQuery.length() - 5);   //this code remove last  union of  subquery
            MyConnection = connection;
            String final_query = "select  vulns.id,vulns.package,product.package_name,product.version from vulns,product,product_vulns where product_vulns.package_id=product.id and product_vulns.vulns_id=vulns.id  and  vulns.id in(select vulns_id from product_vulns where package_id in(" + subQuery + "))   and product.id in (select package_id from product_vulns where package_id in  ( " + subQuery + ")) and cvss_v3 >7 order by package_name limit 10000";
            MyStatement1 = MyConnection.prepareStatement(final_query);
            PreparedStatement my=MyConnection.prepareStatement(final_query);
            ResultSet r = MyStatement1.executeQuery();
            String name = CreateName2(distro, distro);


            while (r.next()) {
                int idNumber = r.getInt("id");
                String packagename=r.getString("package_name");
                String version=r.getString("version");

                if ( ReportController. wholeIds.contains(idNumber)) {
                    flag = 1;

                    List<String> record = FillExcelRow(Integer.toString(idNumber),distro);
                    if((!record.get(0).contains("Not Clear") && (!record.get(0).contains("vulnerability"))) )
                    {
                        record.add(packagename);
                        record.add(version);
                        ReportController. resultrecods.add(record);

                    }

                }


            }



            if (flag == 1) {
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                ;
                MyStatement1.close();
            } catch (Exception e) {
            }
        }
        EC = false;
    }

    public  boolean isValidFormat(String format, String value, Locale locale) {
        LocalDateTime ldt = null;
        DateTimeFormatter fomatter = DateTimeFormatter.ofPattern(format, locale);

        try {
            ldt = LocalDateTime.parse(value, fomatter);
            String result = ldt.format(fomatter);
            return result.equals(value);
        } catch (DateTimeParseException e) {
            try {
                LocalDate ld = LocalDate.parse(value, fomatter);
                String result = ld.format(fomatter);
                return result.equals(value);
            } catch (DateTimeParseException exp) {
                try {
                    LocalTime lt = LocalTime.parse(value, fomatter);
                    String result = lt.format(fomatter);
                    return result.equals(value);
                } catch (DateTimeParseException e2) {//-u fedora Ari_ba74 postgres 1234

                }
            }
        }

        return false;
    }
}

