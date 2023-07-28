/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.adapter;

import com.atlas.crawler.controller.ReportController;
import com.atlas.crawler.core.CrawlerApi;
import com.atlas.crawler.core.General;
import com.atlas.crawler.core.Nvd;
import com.atlas.crawler.model.CVE;
import com.atlas.crawler.model.NMAP;
import com.atlas.crawler.model.NmapCve;
import com.atlas.crawler.model.Vendor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.DataFormatException;

import static java.time.LocalDate.now;

public class Adapter extends Thread{
    public  void run() {
/*
        ReportController report=new ReportController();
        if(report.adapter_method=="search")
        {
             report.cves= getPackage();


        }*/


    }

    public static boolean isStartUpdateDataBase = false;
    private Connection connection;
    public Adapter(Connection connection){
        this.connection = connection;
    }

    public List<CVE> getPackage(){

        try {

            CrawlerApi c= new CrawlerApi(connection);

            c.start();
            c.join();
            Collection<List<String>> collectionCVE = ReportController.resultrecods;

            List<CVE> cves = convertToCVEModel(collectionCVE);
            general.reset_static();
            crawlerApi.reset_static();
            return cves;
        }catch (Exception e){
            throw new RuntimeException();
        }
    }
        General general= new General(connection);
        CrawlerApi crawlerApi=new CrawlerApi(connection);

    private List<CVE> convertToCVEModel(Collection<List<String>> collection){
        List<CVE> cves = new ArrayList<>();
        Iterator<List<String>> iterator = collection.iterator();
        int p=0;
        while (iterator.hasNext()){
            p=p+1;
            if(p==86)
                p=86;
            List<String> myList = iterator.next();
            CVE cve = new CVE();
            cve.setPackageName(myList.get(0));
            cve.setDistribution(myList.get(1));
            cve.setCreatedDate(myList.get(2));
            cve.setPlatform(myList.get(3));
            cve.setCveName(myList.get(4));
            cve.setPublishDate(myList.get(5));
            cve.setLastModifiedDate(myList.get(6));
            cve.setDescription(myList.get(7));
            cve.setType(myList.get(8));
            cve.setAttackVector(myList.get(9));
            cve.setAttackComplexity(myList.get(10));
            cve.setCvss(myList.get(11));
            cve.setCvssText(myList.get(12));
            cve.setAuthentication(myList.get(13));
            cve.setTypeEffect(myList.get(14));
            cve.setIntegrityEffect(myList.get(15));
            cve.setConfidentialityEffect(myList.get(16));
            cve.setAvailabilityEffect(myList.get(17));
            cve.setPrivilegesRequires(myList.get(18));
            cve.setGainedAccess(myList.get(19));
            cve.setReferenceLink(myList.get(20));
            cve.setComments(myList.get(21));
            cve.setPatch(myList.get(22));
            cve.setProduct(myList.get(23));
            cve.setVersion(myList.get(24));

            cves.add(cve);
        }

        return cves;
    }

    public  ArrayList<Integer> buildChart(String iStart, String iFinish,String pack, String dis,String error) throws ParseException, SQLException, DataFormatException, IOException, InvalidFormatException {
        CrawlerApi c= new CrawlerApi(connection);
        ArrayList<Integer > cvss = new ArrayList<>() ;
        if(error=="10")
        {
            cvss.add(-111);
        }
        else
        {
            if(iFinish.equals(""))
            {
                iFinish="2019-01-01";

            }
            if(iStart.equals(""))
            {
                iStart="2079-01-01";

            }

            if(pack==null)
                pack="";
            if(dis=="")
                dis="";
            cvss=c.GetGeraph(iStart,iFinish,pack,dis);
        }
        connection.close();
        return  cvss;

    }
    public  void  updateDatabase2(String packagee) throws IOException, InterruptedException, SQLException {
        ArrayList<String> pacs=new ArrayList<>();
        if(packagee.equals("all"))
        {
            ResultSet RS_Last_CVE = null;
            PreparedStatement MyStatement = null;
            Connection MyConnection = null;
            MyConnection = connection;
            MyStatement = MyConnection.prepareStatement("select * from other_package");
            RS_Last_CVE = MyStatement.executeQuery();
            while ((RS_Last_CVE.next())){
                    long lastupdate=RS_Last_CVE.getInt("last_update");
                    String p=RS_Last_CVE.getString("package");
                    pacs.add(p);
                    pacs.add(String.valueOf(lastupdate));
                }

        }
        else
        {
            ResultSet RS_Last_CVE = null;
            PreparedStatement MyStatement = null;
            Connection MyConnection = null;
            MyConnection = connection;
            MyStatement = MyConnection.prepareStatement("select last_update from other_package where LOWER(package)=?");
            MyStatement.setString(1, packagee);
            RS_Last_CVE = MyStatement.executeQuery();
            if (RS_Last_CVE.next()) {
                        long lastupdate=RS_Last_CVE.getInt("last_update");
                        pacs.add(packagee);
                        pacs.add(String.valueOf(lastupdate));
            }
            else    {
                MyConnection = connection;
                 MyStatement = MyConnection.prepareStatement("insert into other_package (package, last_update) values (?,0)");
                MyStatement.setString(1, packagee);
                int test=    MyStatement.executeUpdate();
                pacs.add(packagee);
                pacs.add(String.valueOf(0));

            }

        }
        Elements RowsInBugzillaFirstPage_index=new Elements();
        Document other=null;
        Nvd nvd=new Nvd(connection);

        for(int k=0;k<pacs.size();k=k+2)
            {
                try{
                    packagee=pacs.get(k);
                    long lastupdate=Integer.valueOf( pacs.get(k+1));
                    General general=new General(connection);
                    LocalDate date = now();
                    int year=date.getYear();
                    long biggest_last_update=0;
                    for(int i=year-3;i<=year;i++)
                    {
                        String url="https://access.redhat.com/security/security-updates/#/cve?q="+packagee+"&p=1&sort=cve_publicDate%20desc&rows=100&cve_publicDate=%5B"+i+"-01-01T00:00:00.000Z%20TO%20"+i+"-01-01T00:00:00.000Z%2B1YEAR%5D&documentKind=Cve";
                        other=general.getDocument(url);
                        try {
                            Elements      RowsInBugzillaFirstPage_index1=other.getElementsByClass("ng-scope").first().getElementsByTag("tr");
                            for(Element element:RowsInBugzillaFirstPage_index1)
                            {
                                long last=0;
                                try {
                                    String cases = element.getElementsByTag("th").first().getElementsByTag("span").text();
                                    Document   CVENVD =general. getDocument3("https://nvd.nist.gov/vuln/detail/" + cases);
                                    String modified=null;
                                    Date LastUpdate=null;
                                    SimpleDateFormat Formatter1 = new SimpleDateFormat("MM/dd/yyyy");
                                    modified = CVENVD.getElementsByAttributeValue("data-testid", "vuln-last-modified-on").first().ownText();
                                    LastUpdate = Formatter1.parse(modified);
                                    last =LastUpdate.getTime();
                                    if(last>biggest_last_update)
                                        biggest_last_update=last;
                                    if(last>lastupdate)
                                        RowsInBugzillaFirstPage_index.add(element);
                                }catch (Exception e)
                                {

                                }


                            }

                        }catch (Exception e)
                        {
                            System.out.println(e.getMessage());
                        }


                    }
                }catch (Exception eee)
                {

                }
                general.fedora_count=RowsInBugzillaFirstPage_index.size();

                for (Element EachRow :RowsInBugzillaFirstPage_index) {

                    Document finalOther = other;
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            nvd.   AnalyzeEachRow_nvd(EachRow, finalOther,"other");

                        }
                    };
                    general.   pool.execute(r);



                }
            //    if(general.pool.)
            }

        connection.close();

    }
    public List<NMAP> getAssetDiscovery(String command) throws SQLException {
        try {
            command="nmap  -Pn -sS -sV -oN D:\\output.txt "+command;
            CrawlerApi c= new CrawlerApi(connection);

            Collection<List<String>> collectionNMAP = null;

            collectionNMAP =   c.setNMAPCommand(command);

            List<NMAP> nmaps = convertToNMAPModel(collectionNMAP);
            connection.close();
            return nmaps;

        }catch (Exception e){
            connection.close();
            throw new RuntimeException();
        }
    }
    private List<NMAP> convertToNMAPModel(Collection<List<String>> collectionNMAP) throws SQLException {
        List<NMAP> nmaps = new ArrayList<>();
        Iterator<List<String>> iterator = collectionNMAP.iterator();
        while (iterator.hasNext()){
            List<String> myList = iterator.next();
            NMAP nmap = new NMAP();

            nmap.setScanName(myList.get(0));
            nmap.setIpAddress(myList.get(1));
            nmap.setPortId(Integer.parseInt(myList.get(2)));
            nmap.setPortProtocol(myList.get(3));
            nmap.setServiceName(myList.get(4));
            nmap.setProductName(myList.get(5));
            nmap.setVersion(myList.get(6));
            nmap.setInfo(myList.get(7));

            nmaps.add(nmap);
        }
        connection.close();
        return nmaps;
    }

    public void  updateDatabase(String distro, String ref) throws SQLException {

        isStartUpdateDataBase = true;

        CrawlerApi c= new CrawlerApi(connection);
        try {
            c.update(distro,ref);
        } catch (Exception e) {
            throw new RuntimeException();
        }finally {
            isStartUpdateDataBase = false;
        }
        general.reset_static();
        crawlerApi.reset_static();
        connection.close();
    }

    public void saveCve(CVE cve) throws SQLException {
        CrawlerApi c= new CrawlerApi(connection);
        try {
            com.atlas.crawler.core.CVE cve1 = convertCveModelToCveCore(cve);
            c.add_cve(cve1);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        general.reset_static();
        crawlerApi.reset_static();
        connection.close();
    }

    private com.atlas.crawler.core.CVE convertCveModelToCveCore(CVE cve) throws SQLException {

        List<String> packageName = new ArrayList<>();
        packageName.add(cve.getPackageName());

        List<String> platforms = new ArrayList<>();
        platforms.add(cve.getPlatform());

        List<String> patchLink = new ArrayList<>();
        patchLink.add(cve.getPatch());

        List<String> referenceLinks = convertStringToListBySplit(cve.getReferenceLink());

        Date publishDate = convertStringToDate(cve.getPublishDate());

        Date lastModifiedDate = convertStringToDate(cve.getLastModifiedDate());

        List<List<String>> pv = createProductVersionList(cve.getProduct());

        ArrayList<ArrayList<String>> product = new ArrayList<>();
        product.add((ArrayList<String>) pv.get(0));

        ArrayList<ArrayList<String>> version = new ArrayList<>();
        version.add((ArrayList<String>) pv.get(1));

        com.atlas.crawler.core.CVE myCve = new com.atlas.crawler.core.CVE(
                packageName,platforms,patchLink,referenceLinks,cve.getCveName(),cve.getDistribution(),
                publishDate,lastModifiedDate,cve.getDescription(),cve.getType(),cve.getAttackVector(),
                cve.getAttackComplexity(),cve.getCvss(),cve.getCvssText(),cve.getAuthentication(),
                cve.getTypeEffect(),cve.getIntegrityEffect(),cve.getConfidentialityEffect(),
                cve.getAvailabilityEffect(), cve.getPrivilegesRequires(),cve.getGainedAccess(),
                new Date(),cve.getComments(),cve.getCWE(),cve.getProducts_Affected(),cve.getCveName(),cve.getSynopsis(),cve.getAgent(),cve.getFamily(),product,version
        );


        connection.close();
        return myCve;
    }

    private List<String> convertStringToListBySplit(String myString) throws SQLException {

        List<String> result = Arrays.asList(myString.split("\\r?\\n").clone());
        connection.close();
        return result;
    }

    private Date convertStringToDate(String time) throws SQLException {

        String[] splitStringDate = time.split("/");

        Integer year  = Integer.valueOf(splitStringDate[0]);
        Integer month = Integer.valueOf(splitStringDate[1]);
        Integer day   = Integer.valueOf(splitStringDate[2]);

        GregorianCalendar calendar = new GregorianCalendar(year,month,day);
        connection.close();
        return calendar.getTime();
    }

    private List<List<String>> createProductVersionList(String productVersion) throws SQLException {
        List<String> rowProductVersion = convertStringToListBySplit(productVersion);
        List<String> products = new ArrayList<>();
        List<String> versions = new ArrayList<>();
        for (String pv:rowProductVersion) {
            String[] temp = pv.split(" ");
            if(temp.length>0){
                String pr = temp[0];
                String vr ="*";
                if(temp.length>1){
                    vr = temp[1];
                }
                products.add(pr);
                versions.add(vr);
            }
        }

        List<List<String>> result = new ArrayList<>();
        result.add(products);
        result.add(versions);
        connection.close();
        return result;
    }

    public void downloadPatchs(String distribution,String version,List<String> packages) throws IOException, InterruptedException, SQLException {
        int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newFixedThreadPool(processors);
        CrawlerApi c= new CrawlerApi(connection);

        Runnable r = new Runnable() {
            public void run() {
                try {
                    c.downloadpatch(distribution,version,packages);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        pool.execute(r);
        connection.close();
    }
    private List<NmapCve> convertToNmapCveModel(Collection<List<String>> collectionNmapCve) throws SQLException {
        List<NmapCve> nmapCves = new ArrayList<>();
        Iterator<List<String>> iterator = collectionNmapCve.iterator();
        while (iterator.hasNext()){
            List<String> myList = iterator.next();
            NmapCve nmapCve = new NmapCve();

            nmapCve.setScanName(myList.get(0));
            nmapCve.setIpAddress(myList.get(1));
            nmapCve.setPortId(Integer.parseInt(myList.get(2)));
            nmapCve.setPortProtocol(myList.get(3));
            nmapCve.setServiceName(myList.get(4));
            nmapCve.setProductName(myList.get(5));
            nmapCve.setVersion(myList.get(6));
            nmapCve.setInfo(myList.get(7));

            nmapCve.setPackageName(myList.get(8));
            nmapCve.setDistribution(myList.get(9));
            nmapCve.setCreatedDate(myList.get(10));
            nmapCve.setPlatform(myList.get(11));
            nmapCve.setCveName(myList.get(12));
            nmapCve.setPublishDate(myList.get(13));
            nmapCve.setLastModifiedDate(myList.get(14));
            nmapCve.setDescription(myList.get(15));
            nmapCve.setType(myList.get(16));
            nmapCve.setAttackVector(myList.get(17));
            nmapCve.setAttackComplexity(myList.get(18));
            nmapCve.setCvss(myList.get(19));
            nmapCve.setCvssText(myList.get(20));
            nmapCve.setAuthentication(myList.get(21));
            nmapCve.setTypeEffect(myList.get(22));
            nmapCve.setIntegrityEffect(myList.get(23));
            nmapCve.setConfidentialityEffect(myList.get(24));
            nmapCve.setAvailabilityEffect(myList.get(25));
            nmapCve.setPrivilegesRequires(myList.get(26));
            nmapCve.setGainedAccess(myList.get(27));
            nmapCve.setReferenceLink(myList.get(28));
            nmapCve.setComments(myList.get(29));
            nmapCve.setPatch(myList.get(30));

            nmapCves.add(nmapCve);
        }
        connection.close();
        return nmapCves;
    }
    public List<NmapCve> getNmapCveBaseOnIp(String ip) {
        try {
            CrawlerApi c= new CrawlerApi(connection);

            Collection<List<String>> collectionNmapCve = null;

            collectionNmapCve =   c.getNmapCveBaseOnIp(ip);

            List<NmapCve> nmapCve = convertToNmapCveModel(collectionNmapCve);
            connection.close();
            return nmapCve;

        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
//            throw new RuntimeException();
            return null;
        }
    }
    public List<Vendor> getVendor(String name) {
        try {
            CrawlerApi c= new CrawlerApi(connection);

            Collection<List<String>> collectionvendor = null;

            collectionvendor =   c.getVendorBaseOnName(name);

            List<Vendor> vendors = convertToVendor(collectionvendor);
            connection.close();
            return vendors;

        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
//            throw new RuntimeException();
            return null;
        }
    }
    public List<Vendor> getVendor2(String name) {
        try {
            CrawlerApi c= new CrawlerApi(connection);

            Collection<List<String>> collectionvendor = null;

            collectionvendor =   c.getVendorBaseOnName2(name);

            List<Vendor> vendors = convertToVendor(collectionvendor);
            connection.close();
            return vendors;

        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
//            throw new RuntimeException();
            return null;
        }
    }
    public List<Vendor> getVendor3(String name,String name2) {
        try {
            CrawlerApi c= new CrawlerApi(connection);

            Collection<List<String>> collectionvendor = null;

            collectionvendor =   c.getVendorBaseOnName3(name , name2);

            List<Vendor> vendors = convertToVendor(collectionvendor);
            connection.close();
            return vendors;

        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
//            throw new RuntimeException();
            return null;
        }
    }
    public List<Vendor> getVendor4(String name, String pname) {
        try {
            CrawlerApi c= new CrawlerApi(connection);

            Collection<List<String>> collectionvendor = null;

            collectionvendor =   c.getVendorBaseOnName4(name,pname);

            List<Vendor> vendors = convertToVendor(collectionvendor);
            connection.close();
            return vendors;

        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
//            throw new RuntimeException();
            return null;
        }
    }
    public List<Vendor> getVendor5(String name) {
        try {
            CrawlerApi c= new CrawlerApi(connection);

            Collection<List<String>> collectionvendor = null;

            collectionvendor =   c.getVendorBaseOnName5(name);

            List<Vendor> vendors = convertToVendor(collectionvendor);
            connection.close();
            return vendors;

        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
//            throw new RuntimeException();
            return null;
        }
    }
    public List<Vendor> getVendor6(String name) {
        try {
            CrawlerApi c= new CrawlerApi(connection);

            Collection<List<String>> collectionvendor = null;

            collectionvendor =   c.getVendorBaseOnName6(name);

            List<Vendor> vendors = convertToVendor(collectionvendor);
            connection.close();
            return vendors;

        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
//            throw new RuntimeException();
            return null;
        }
    }



    private List<Vendor> convertToVendor(Collection<List<String>> collectionVendor) throws SQLException {
        List<Vendor> Vendors = new ArrayList<>();
        Iterator<List<String>> iterator = collectionVendor.iterator();
        while (iterator.hasNext()){
            List<String> myList = iterator.next();
            Vendor vendor = new Vendor();

            vendor.setVendorName(myList.get(0));
            vendor.setcount(myList.get(1));


            Vendors.add(vendor);
        }
        connection.close();
        return Vendors;
    }

    public List<NmapCve> getNmapCveBaseOnLastScan() {
        try {
            CrawlerApi c= new CrawlerApi(connection);

            Collection<List<String>> collectionNmapCve = null;

            //collectionNmapCve =   c.setNmapCveLastScan();

            List<NmapCve> nmapCve = convertToNmapCveModel(collectionNmapCve);
            connection.close();
            return nmapCve;

        }catch (Exception e){
            throw new RuntimeException();
        }
    }

}
