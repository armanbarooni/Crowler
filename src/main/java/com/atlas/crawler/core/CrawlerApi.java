package com.atlas.crawler.core;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.atlas.crawler.controller.ReportController;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.DataFormatException;

public class CrawlerApi extends Thread {
    public  void run(){
/*        ReportController report=new ReportController();
        if(report.adapter_method=="search")
        {
            try {
                setFile();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }*/
    }
    public static int choise = 0;
    public static int pack = -1;


    public static String distro = null;
    public static String task = null;
    public static List<String> packages = null;

    private General general;
    private Connection connection;

    public CrawlerApi(Connection connection) {
        this.connection = connection;
        general = new General(connection);
    }

        public  void  reset_static()
        {
              choise = 0;
            pack = -1;


           distro = null;
            task = null;
             packages = null;
        }
    public  ArrayList<Integer> GetGeraph(String iStart, String iFinish,String packages,String distro) throws ParseException, SQLException, DataFormatException, IOException, InvalidFormatException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        distro="";
        Connection MyConnection = null;
        PreparedStatement Statement = null;
        ResultSet IDs = null;
        ArrayList<Integer> cvss=new ArrayList<>() ;

        try {
            packages = packages.replace("'", "");
            packages = packages.replace("-", "");
            packages = packages.replace("=", "");
            packages = packages.replace("!", "");
            distro = distro.replace("'", "");
            distro = distro.replace("-", "");
            distro = distro.replace("=", "");
            distro = distro.replace("!", "");


            MyConnection = connection;
            Date start = formatter.parse(iStart);
            Date finish = formatter.parse(iFinish);
            int startint = (int) (start.getTime() / 1000);
            int finishint = (int) (finish.getTime() / 1000);

            //--------------------------------------------------------------------------

            Statement = MyConnection.prepareStatement("select  count (distinct(vulns.id) )from vulns,product,product_vulns where product_vulns.package_id=product.id and product_vulns.vulns_id=vulns.id  and  vulns.id in(select vulns_id from product_vulns where package_id in(select id from product where LOWER(package_name) like LOWER(?)))   and product.id in (select package_id from product_vulns where package_id in  ( select id from product where LOWER(package_name) like LOWER(?)   )) and updated_at >=? and updated_at <= ? and cvss_v3>9");

            Statement.setString(1,'%'+packages+'%');
            Statement.setString(2,'%'+packages+'%');

            Statement.setInt(3, startint);
            Statement.setInt(4, finishint);
            //       Statement.setString(3, distro);
            int idNumber=0;
            int count=0;
            int temp0=0,temp1=0,temp2=0,temp3=0;
            IDs = Statement.executeQuery();
            while (IDs.next()) {
                temp0=IDs.getInt("count");
                count=count+temp0;
                cvss.add (temp0);
            }
            Statement = MyConnection.prepareStatement("select  count (distinct(vulns.id) )from vulns,product,product_vulns where product_vulns.package_id=product.id and product_vulns.vulns_id=vulns.id  and  vulns.id in(select vulns_id from product_vulns where package_id in(select id from product where LOWER(package_name) like LOWER(?) ))   and product.id in (select package_id from product_vulns where package_id in  ( select id from product where LOWER(package_name) like LOWER(?)   )) and updated_at >=? and updated_at <= ? and cvss_v3>=7 and cvss_v3<9  ");
            Statement.setString(1,'%'+packages+'%');
            Statement.setString(2,'%'+packages+'%');


            Statement.setInt(3, startint);
            Statement.setInt(4, finishint);
            IDs = Statement.executeQuery();
            while (IDs.next()) {
                temp1=IDs.getInt("count");
                count=count+temp1;
                cvss.add (temp1);            }

            Statement = MyConnection.prepareStatement("select  count (distinct(vulns.id) )from vulns,product,product_vulns where product_vulns.package_id=product.id and product_vulns.vulns_id=vulns.id  and  vulns.id in(select vulns_id from product_vulns where package_id in(select id from product where LOWER(package_name) like LOWER(?) ))   and product.id in (select package_id from product_vulns where package_id in  ( select id from product where LOWER(package_name) like LOWER(?)   )) and updated_at >=? and updated_at <= ? and cvss_v3>=4 and cvss_v3<7  ");
            Statement.setString(1,'%'+packages+'%');
            Statement.setString(2,'%'+packages+'%');


            Statement.setInt(3, startint);
            Statement.setInt(4, finishint);
            IDs = Statement.executeQuery();
            while (IDs.next()) {

                temp2=IDs.getInt("count");
                count=count+temp2;

                cvss.add (temp2);            }

            Statement = MyConnection.prepareStatement("select  count (distinct(vulns.id) )from vulns,product,product_vulns where product_vulns.package_id=product.id and product_vulns.vulns_id=vulns.id  and  vulns.id in(select vulns_id from product_vulns where package_id in(select id from product where LOWER(package_name) like LOWER(?) ))   and product.id in (select package_id from product_vulns where package_id in  ( select id from product where LOWER(package_name) like LOWER(?)   )) and updated_at >=? and updated_at <= ? and cvss_v3>=1 and cvss_v3<4  ");
            Statement.setString(1,'%'+packages+'%');
            Statement.setString(2,'%'+packages+'%');


            Statement.setInt(3, startint);
            Statement.setInt(4, finishint);
            IDs = Statement.executeQuery();
            while (IDs.next()) {
                temp3=IDs.getInt("count");
                count=count+temp3;

                cvss.add (temp3);
            }
            int tmp=(int)(  ((float)temp0/count*100));
            cvss.add(tmp);
            tmp=(int)(  ( (float) temp1/count*100));
            cvss.add(tmp);
            tmp=(int)(  ( (float) temp2/count*100));
            cvss.add(tmp);
            tmp=(int)(  ( (float) temp3/count*100));
            cvss.add(tmp);




        } catch (Exception e) {


        } finally {
            try {
                Statement.close();
                ;
                IDs.close();
            } catch (Exception e) {

            }
        }
        return  cvss;
    }

    public  Collection<List<String>> getVendorBaseOnName (String name) throws SQLException {
        General c= new General(connection);

        Connection myconnection=null;
        myconnection = connection;
        PreparedStatement add_asset=null;
        ResultSet rs=null;
        /*
        add_asset=myconnection.prepareStatement("SELECT  t.vendor , count(t.*) cont FROM public.vendor_product_table t\n" +
                "where t.vendor like '%"+name+"%'\n" +
                "group by t.vendor\n" +
                "order by cont desc\n" +
                "\n") ;*/
        add_asset=myconnection.prepareStatement("SELECT   t.product as vendor , t.vendor as cont FROM public.vendor_product_table t where t.vendor = ?  and t.product not in \n" +
                "\t\t\t\t(select sb.product_name from public.subscribed_vendors sb) ");
        add_asset.setString(1,name);
        rs=add_asset.executeQuery();
        List<String>final_list=new ArrayList<>();
        Collection<List<String>> final_res=new ArrayList<>();

        while (rs.next()) {
            final_list = new ArrayList<>();
            String vendor = rs.getString("vendor");
            String cont=rs.getString("cont");
            final_list.add(vendor);
            final_list.add(((String.valueOf(cont))));

            final_res.add(final_list);

        }

        return final_res;


    }




    public  Collection<List<String>> getVendorBaseOnName2 (String name) throws SQLException {
        General c= new General(connection);

        Connection myconnection=null;
        myconnection = connection;
        PreparedStatement add_asset=null;
        ResultSet rs=null;
        /*
        add_asset=myconnection.prepareStatement("SELECT  t.vendor , count(t.*) cont FROM public.vendor_product_table t\n" +
                "where t.vendor like '%"+name+"%'\n" +
                "group by t.vendor\n" +
                "order by cont desc\n" +
                "\n") ;*/

        add_asset=myconnection.prepareStatement("SELECT   t.product as vendor , t.vendor as cont FROM public.vendor_product_table\n" +
                "t where t.product like '%"+name+"%'  and t.product not in \n" +
                "\t\t\t\t(select sb.product_name from public.subscribed_vendors sb) ");
        rs=add_asset.executeQuery();
        List<String>final_list=new ArrayList<>();
        Collection<List<String>> final_res=new ArrayList<>();

        while (rs.next()) {
            final_list = new ArrayList<>();
            String vendor = rs.getString("vendor");
            String cont=rs.getString("cont");
            final_list.add(vendor);
            final_list.add(((String.valueOf(cont))));

            final_res.add(final_list);

        }

        return final_res;


    }

    public  Collection<List<String>> getVendorBaseOnName3 (String name , String name2) throws SQLException {
        General c= new General(connection);

        Connection myconnection=null;
        myconnection = connection;
        PreparedStatement add_asset=null;
        ResultSet rs=null;
        add_asset=myconnection.prepareStatement("INSERT INTO public.subscribed_vendors(\n" +
                "\tproduct_name, user_id, email)\n" +
                "\tVALUES (?, ?, ?);");
        add_asset.setString(1,name2);

        add_asset.setInt(2,1);

        add_asset.setString(3,"armanbarooni@gmail.com");
        add_asset.executeUpdate();




        add_asset=myconnection.prepareStatement("SELECT   t.product as vendor , t.vendor as cont FROM public.vendor_product_table\n" +
                "t where t.product like '%"+name+"%'  and t.product not in \n" +
                "\t\t\t\t(select sb.product_name from public.subscribed_vendors sb)   ");
        rs=add_asset.executeQuery();
        List<String>final_list=new ArrayList<>();
        Collection<List<String>> final_res=new ArrayList<>();

        while (rs.next()) {
            final_list = new ArrayList<>();
            String vendor = rs.getString("vendor");
            String cont=rs.getString("cont");
            final_list.add(vendor);
            final_list.add(((String.valueOf(cont))));

            final_res.add(final_list);

        }

        return final_res;


    }
    public  Collection<List<String>> getVendorBaseOnName4 (String name,String pname) throws SQLException {
        General c= new General(connection);

        Connection myconnection=null;
        myconnection = connection;
        PreparedStatement add_asset=null;
        ResultSet rs=null;
        add_asset=myconnection.prepareStatement("INSERT INTO public.subscribed_vendors(\n" +
                "\tproduct_name, user_id, email)\n" +
                "\tVALUES (?, ?, ?);");
        add_asset.setString(1,pname);

        add_asset.setInt(2,1);

        add_asset.setString(3,"armanbarooni@gmail.com");
        add_asset.executeUpdate();



        add_asset=myconnection.prepareStatement("SELECT   t.product as vendor , t.vendor as cont FROM public.vendor_product_table t where t.vendor = ?  and t.product not in \n" +
                "\t\t\t\t(select sb.product_name from public.subscribed_vendors sb) ");
        add_asset.setString(1,name);
        rs=add_asset.executeQuery();
        List<String>final_list=new ArrayList<>();
        Collection<List<String>> final_res=new ArrayList<>();

        while (rs.next()) {
            final_list = new ArrayList<>();
            String vendor = rs.getString("vendor");
            String cont=rs.getString("cont");
            final_list.add(vendor);
            final_list.add(((String.valueOf(cont))));

            final_res.add(final_list);

        }

        return final_res;


    }
    public  Collection<List<String>> getVendorBaseOnName5 (String name) throws SQLException {
        General c= new General(connection);

        Connection myconnection=null;
        myconnection = connection;
        PreparedStatement add_asset=null;
        ResultSet rs=null;



        add_asset=myconnection.prepareStatement("SELECT   t.product as vendor , t.vendor as cont FROM public.vendor_product_table\n" +
                "                t where  t.product  in \n" +
                "\t\t\t\t(select sb.product_name from public.subscribed_vendors sb)");
        rs=add_asset.executeQuery();
        List<String>final_list=new ArrayList<>();
        Collection<List<String>> final_res=new ArrayList<>();

        while (rs.next()) {
            final_list = new ArrayList<>();
            String vendor = rs.getString("vendor");
            String cont=rs.getString("cont");
            final_list.add(vendor);
            final_list.add(((String.valueOf(cont))));

            final_res.add(final_list);

        }

        return final_res;


    }

    public  Collection<List<String>> getVendorBaseOnName6 (String name) throws SQLException {
        General c= new General(connection);

        Connection myconnection=null;
        myconnection = connection;
        PreparedStatement add_asset=null;
        ResultSet rs=null;

        add_asset=myconnection.prepareStatement("delete from public.subscribed_vendors t where t.product_name=? ");
        add_asset.setString(1,name);

        add_asset.executeUpdate();


        add_asset=myconnection.prepareStatement("SELECT   t.product as vendor , t.vendor as cont FROM public.vendor_product_table\n" +
                "                t where  t.product  in \n" +
                "\t\t\t\t(select sb.product_name from public.subscribed_vendors sb)");
        rs=add_asset.executeQuery();
        List<String>final_list=new ArrayList<>();
        Collection<List<String>> final_res=new ArrayList<>();

        while (rs.next()) {
            final_list = new ArrayList<>();
            String vendor = rs.getString("vendor");
            String cont=rs.getString("cont");
            final_list.add(vendor);
            final_list.add(((String.valueOf(cont))));

            final_res.add(final_list);

        }

        return final_res;


    }





    public  Collection<List<String>> getNmapCveBaseOnIp (String ip)
    {
        Collection<List<String>> final_res=null;
        General c= new General(connection);
        //        resultrecods=new ArrayList<>();
        PreparedStatement add_asset=null;
        ResultSet rs=null;
        Connection myconnection=null;
        try {

            myconnection = connection;
            final_res= new ArrayList<>();
            add_asset=myconnection.prepareStatement("select * from assets where  ip_address='"+ip+"' ") ;

            rs=add_asset.executeQuery();
            List<String>final_list=new ArrayList<>();

            while (rs.next())
            {
                final_list=new ArrayList<>();
                String package1=rs.getString("product_name");
                String port=rs.getString("port_protocol");
                String service_name=rs.getString("service_name");
                String info=rs.getString("info");
                int port_id=rs.getInt("port_id");
                String scan_name=rs.getString("scan_name");
                String pac[]=package1.split(" ");
                String version1=rs.getString("version");
                final_list.add(scan_name);
                final_list.add(ip);
                final_list.add(((String.valueOf(port_id))));
                final_list.add(port);
                final_list.add(service_name);
                final_list.add(package1);
                final_list.add(version1);
                final_list.add(info);
                if(!pac[0].equals("-"))
                {
                    c.searchByPackage2(pac[0],version1);
                    Iterator<List<String>> iterator = general.resultrecods.iterator();
                    general.resultrecods=new ArrayList<>();
                    while (iterator.hasNext()) {
                        List<String> myList = iterator.next();
                        List<String> ended=new ArrayList<>();
                        ended.addAll(final_list);
                        ended.addAll(myList);
                        final_res.add(ended);
                    }
                }

            }



        }catch (Exception e){
            //   logging(e.getMessage());
        }
        return  final_res;

    }
    public void downloadpatch(String dist,String ver,List<String> packages) throws IOException, InterruptedException {
        for (String pckg:packages) {
            if(dist.toLowerCase().equals("centos"))
            {

                String path_r=System.getProperty("user.dir")+"/centos/"+ver+"/"+pckg;
                File theDir = new File(path_r);
                if (!theDir.exists()){
                    theDir.mkdirs();
                }
                String url="https://vault.centos.org/8.3.2011/BaseOS/Source/SPackages/";
                Document Centos=null;
                General general= new General(connection);
                Centos=
                        general. getDocument2(url);
                Element indexlist=Centos.getElementById("indexlist");
                Elements Tr=indexlist.getElementsByTag("tr");
                for(Element tr:Tr ){
                    try{
                        Elements index=tr.getElementsByClass("indexcolname").first().getElementsByTag("a");
                        Element ind=index.get(0);
                        if(ind.text().toLowerCase().contains(pckg.toLowerCase())){
                            String link=ind.attr("href");
                            general.   download_patch(link,path_r,dist,ver,'t');
                        }
                    }catch (Exception e){
                    }
                }
            }
            else {
                String path_r=System.getProperty("user.dir")+"/fedora/"+"/"+ver+"/"+pckg;
                File theDir = new File(path_r);
                if (!theDir.exists()){
                    theDir.mkdirs();
              }
            char harf=pckg.toLowerCase().charAt(0);
                String url=null;
                if(ver.equals("32")){

                    url="https://dl.fedoraproject.org/pub/fedora/linux/updates/32/Everything/SRPMS/Packages/"+harf;
                }
                else  if(ver.equals("30")){
                    url="https://archives.fedoraproject.org/pub/archive/fedora/linux/updates/30/Everything/SRPMS/Packages/"+harf;
                }
                if(url!=null)
                {
                    Document Centos=null;
                    Centos=general. getDocument2(url);
                    Elements Tr=Centos.getElementsByTag("a");
                    for(Element tr:Tr ) {
                        try{
                            if( tr.text().toLowerCase().contains(pckg.toLowerCase()))
                            {
                                String link= null;
                                link=tr.attr("href");
                                general.      download_patch(link,path_r,dist,ver,harf);
                            }
                        }catch (Exception e){

                        }
                    }
                }


            }
        }
    }
    public void setFile(  ) throws IOException, SQLException {
       // ReportController report= new ReportController();

      /*  List<String> packages=report.packagess;
        String startDate=report.startDatee;
        String finishDate=report.finishDatee;
        int usev=report.usevv;
        String distributionType=report.distributionTypee;*/
      /*  general.site = true;
        pack = usev;
        if(startDate.equals(""))
            startDate="p";
        if(finishDate.equals(""))
            finishDate="p";
        this.packages = packages;
        if (distributionType.equals("centos")) {
            choise = 2;
            general.iStart = startDate;
            general.iFinish = finishDate;
            distro = "2";
            task = "2";
        }      //ad
        else if (distributionType.equals("fedora")) {
            choise = 1;
            general.iStart = startDate;
            general.iFinish = finishDate;
            distro = "1";
            task = "2";
        } else if (distributionType.equals("p")) {
            choise = 3;
            general.iStart = startDate;
            general.iFinish = finishDate;
            distro = "p";
            task = "2";

        }
*/

        starter();

    }



    public  Collection<List<String>> setNMAPCommand(String command) throws IOException, InterruptedException {
        int radif=0;
        Process process=null;
        general.resultrecods=new ArrayList<>();
        try {
       //     process = Runtime.getRuntime().exec(command);

        }catch (Exception e)
        {

        }
        System.out.println("scan is loading...");
      //  process.waitFor();
        System.out.println("scan is done...");

        List<String> host=new ArrayList<String>();
        BufferedReader reader;
        reader=new BufferedReader(new FileReader("D:\\output2.txt"));
        String ip=new String();
        String port_ip=new String();
        String port_protocle=new String();
        String  service_name=new String();
        String product_name=new String();
        String product_version=new String();
        String info=new String();
        String line=reader.readLine();
        String splited[]=line.split(" ") ;
        String name="scan_"+splited[9]+"_"+splited[6]+"_"+splited[7]+"_"+splited[8];
        while(line!=null)
        {
            host=new ArrayList<String>();

            product_name="-";
            product_version="-";
            info="-";
            String splited_feature[]=line.split(" ") ;
            if(line.contains("Nmap scan report for"))
            {
                ip=splited_feature[4];
            }
            else if(line.contains("/udp") || (line.contains("/tcp")))
            {
                if(splited_feature.length==3){
                    String port[]=splited_feature[0].split("/");
                    port_ip=port[0];
                    port_protocle=port[1];
                    service_name=splited_feature[2];

                }
                else{
                    boolean end=false;
                    String port[]=splited_feature[0].split("/");
                    port_ip=port[0];
                    port_protocle=port[1];
                    int itrator=2;

                    while(splited_feature[itrator].equals("") || splited_feature[itrator].equals("open"))
                    {
                        if(itrator+1==splited_feature.length)
                        {
                            end=true;
                            break;

                        }
                        itrator=itrator+1;

                    }
                    if(!end)
                    {
                        service_name=splited_feature[itrator];
                        if(itrator+1==splited_feature.length)
                        {
                            end=true;

                        }
                        itrator=itrator+1;

                    }
                    if(!end)
                    {
                        while(splited_feature[itrator].equals("") && !end)
                        {

                            if(itrator+1==splited_feature.length)
                            {
                                end=true;
                                break;

                            }
                            itrator=itrator+1;
                        }
                    }


                    if(!end){
                        char feature=splited_feature[itrator].charAt(0);
                        while (Character.isDigit(feature)==false)
                        {
                            if(product_name!="-")
                                product_name=product_name+" ";
                            else if(product_name=="-")
                                product_name="";
                            product_name=product_name+ splited_feature[itrator];
                            if(itrator+1==splited_feature.length)
                            {
                                end=true;
                                break;

                            }
                            itrator=itrator+1;
                            if(splited_feature[itrator]=="")
                                break;

                            feature=splited_feature[itrator].charAt(0);
                        }
                    }
                    if(!end){
                        while(splited_feature[itrator].equals(""))
                        {
                            if(itrator+1==splited_feature.length)
                            {
                                end=true;
                                break;

                            }
                            itrator=itrator+1;

                        }
                    }

                    if(!end) {
                        product_version=splited_feature[itrator];
                        if(itrator+1==splited_feature.length){
                            info=splited_feature[itrator];

                        }
                        itrator=itrator+1;
                        if(itrator<splited_feature.length) {
                            while (itrator != splited_feature.length) {
                                if (info != "-")
                                    info = info + " ";
                                else if (info == "-")
                                    info = "";
                                info = info + splited_feature[itrator];

                                itrator = itrator + 1;
                            }
                        }
                    }

                    host.add(name);
                    host.add(ip);
                    host.add(port_ip);
                    host.add(port_protocle);
                    host.add(service_name);
                    host.add(product_name);
                    host.add(product_version);
                    host.add(info);

                    general.resultrecods.add(host);


                }
            }
            line=reader.readLine();
        }

        PreparedStatement add_asset=null;
        ResultSet rs=null;
        Connection myconnection=null;
        try {
            Iterator<List<String>> iterator =general.resultrecods.iterator();

            myconnection = connection;
            while (iterator.hasNext()){
                List<String> myList = iterator.next();
                add_asset=myconnection.prepareStatement("insert  into assets (scan_name, ip_address,port_id,port_protocol,service_name,product_name,version,info)values ('"+myList.get(0)+"','"+myList.get(1)+"', "+Integer.parseInt(myList.get(2)) +", '"+myList.get(3)+"', '"+myList.get(4)+"','"+myList.get(5)+"','"+myList.get(6)+"','"+myList.get(7)+"')");
                add_asset.executeUpdate();

            }

        }catch (Exception e){
            int y=6;
            //(e.getMessage());
        }


        return  general.resultrecods;
    }







    public void starter() throws IOException, SQLException {



        

        switch (task) {
            case "1":
                general.RunFedora = true;
                general.RunCentos = true;
                general.FedoraCurrent_Time_Checking = true;
                general.CentosCurrent_Time_Checking = true;
                general.report = false;
                ReportController .searchByPackage_flag = false;
                ReportController.UseDB = false;
                break;
            case "2":
                general.report = true;
                ReportController .searchByPackage_flag = true;
                ReportController.UseDB = true;
                break;
            default:

        }
        if (general.report) {


            try {
                boolean resualt = getPackageNameFromFile(packages);
            } catch (Exception e) {
                //     System.out.println("File Not found or curropted");
            }

        }
        if (general.report) {
            switch (distro) {
                case "1":
                    general.RunFedora = true;
                    general.RunCentos = false;
                    break;
                case "2":
                    general.RunCentos = true;
                    general.RunFedora = false;
                    break;
                case "p":
                    distro = "";
                    general.RunFedora = true;
                    general.RunCentos = true;
                    break;

                default:
                    //    System.out.println("wrong choice! Exiting");
                    break;
            }
        }
        if (general.report) {
            if (general.iStart.equals("p")) {

            general.iStart = "2010-01-01";
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            general.iFinish = dtf.format(now);
        } else if (!general.isValidFormat("yyyy-MM-dd", general.iStart, Locale.ENGLISH)) {
            System.out.println(general.iStart);
            System.out.println("wrong format! must be in format yyyy-MM-dd");
            general.iStart = "";
            System.exit(0);

        }

        if (general.iFinish.equals("p")) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            general.iStart = "2010-01-01";
            general.iFinish = dtf.format(now);
        } else if (!general.isValidFormat("yyyy-MM-dd", general.iFinish, Locale.ENGLISH)) {
            System.out.println("wrong format!  must be in format yyyy-MM-dd");
            general.iFinish = "";
            System.exit(0);
        }
    }
        try {
            CrawlerApi c = new CrawlerApi(connection);
            c.Run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean getPackageNameFromFile(List<String> pkg) {
        general.PackageNames = "";
        general.Versions = "";
        try {
            int counter = 0;
            for (String st : pkg) {
                counter = counter + 1;
                int index = st.indexOf(" ");  //  B: we change - to "  "   to seprate package from version
                int index2 = st.indexOf("*");  //B : if we dont have version
                String v =null;

                if(general.Use_version==0){
                    index=st.length();
                    st = st.substring(0, index);

                }
        else{
                    if (index != -1)  // B: if package has version
                    {

                        v = st.substring(index + 1, st.length()); // B:  we change st.length-1 to st.length becuse it didnt work correct
                        st = st.substring(0, index);
                    } else if(index2!=-1 )  // B : if package has no version
                    {

                        v = st.substring(index2, st.length()); // B:  we change st.length-1 to st.length becuse it didnt work correct
                        st = st.substring(0, index2);
                    }
                    else {
                        v="";

                    }

                }

                if (!general.PackageNames.isEmpty())
                    general.PackageNames += ",";
                general.PackageNames += st;
                general.Versions += v;
                if (!general.Versions.isEmpty())
                    general.Versions += ",";


            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public void Run() throws Exception {

System.err.println("test--------------------------------------");
            choise=3;
        switch (choise) {
            case 1:
                if (general.RunFedora) {

                            String countinue = "1";

                    if (countinue.equals("1")) {
                        int countinue2 = pack;
                        if (countinue2 == 1) {
                            general.Use_version = 0;
                        }
                        else if(countinue2==0)
                        {
                            general.Use_version=1;
                        }
                        else
                            general.Use_version=2;
                        Fedora fedora = new Fedora(connection);
                        fedora.start();
                        fedora.join();

                    }

                }
                break;
            case 2:
                if (general.RunCentos) {

                  String countinue = "1";
                    if (countinue.equals("1")) {
                        //    System.out.println("if you dont want to search by considerring  product_version press 1 other wise press any key you want ");
                        int countinue2 = pack;
                        if (countinue2 == 1) {
                            general.Use_version = 0;
                        }
                        else if(countinue2==0)
                        {
                            general.Use_version=1;
                        }
                        else
                            general.Use_version=2;
                        System.out.println("processing centos ....");
                        Centos centos = new Centos(connection);
                        centos.start();
                        centos.join();
                    }


                }
                break;
            case 3:
                        System.out.println("processing fedora ....");
                        Fedora fedora = new Fedora(connection);
                        fedora.start();
                        fedora.join();
                break;
            default:
                break;
        }


    }

    public void update(String distro,String references) throws IOException, SQLException {

        task="1";
            general.reference=references;

        starter();
    }
    public  void add_cve(CVE cve) throws SQLException, InvalidFormatException, DataFormatException, ParseException, IOException {
        general.Print(cve,1,"null");
        CrawlerApi c= new CrawlerApi(connection);

        general.reset_static();
        c.reset_static();
    }

}


