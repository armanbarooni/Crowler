package com.atlas.crawler.core;

import com.atlas.crawler.controller.ReportController;
import com.atlas.crawler.model.CVE;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

public class GetcveFromcore  implements  Runnable  {
    private  volatile boolean finish;
    private General general;
        private Connection connection;
    public GetcveFromcore(Connection connection){
            this.connection = connection;
            general = new General(connection);
            packageNames =new String();
            Versions=new String();
       cves = new ArrayList<>();

    }
    public  String  startDatee, finishDatee , distributionTypee;
    public   int usevv;
    public  List<String> packagess;


    private List<CVE> cves;
    String packageNames;
    String Versions;
    public  List<Integer> wholeIds = new ArrayList<Integer>();
    @Override
    public void run() {

          int pack;
        int Use_version=-1;
        String distro;
        pack = usevv;

        if(startDatee.equals(""))
            startDatee="p";
        if(finishDatee.equals(""))
            finishDatee="p";
        if (startDatee.equals("p")) {

           startDatee= "2001-01-01";
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
        }

        if (finishDatee.equals("p")) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
          finishDatee = "2010-01-01";
           finishDatee = dtf.format(now);
        }
        if(pack==1)
        {
            Use_version=0;
            usevv=0;
        }
        else  if(pack==0)
        {
            Use_version=1;
            usevv=1;
        }
        else
        {
            Use_version=2;
            usevv=2;
        }
        int startint=0;
        int finishint=0;
        distro=distributionTypee;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date start = formatter.parse(startDatee);
            Date finish = formatter.parse(finishDatee);
             startint = (int) (start.getTime() / 1000);
             finishint = (int) (finish.getTime() / 1000);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        getPackageNameFromFile(packagess);


//////////////////////////////////////////////////////////////

        Collection<List<String>>  resultrecods = new ArrayList<>();
        Connection MyConnection = null;
        int flag = 0;
        PreparedStatement MyStatement1 = null;
        String[] split_package = packageNames.split(",");
        String[] split_version = Versions.split(",");
        String vv=null;
        try {
            distributionTypee="package";
            if(distributionTypee.equals("package"))
            {
                int it = 0;
                String subQuery = "";

                /////////////////////// B: this section is add in version 4.4
                for (int i = 0; i < split_package.length; i++) {
                    vv="";
                    split_package[i] = split_package[i].replace("'", "");//B :this code was added to prevent sali


                    if(split_version.length>i){
                        split_version[i] = split_version[i].replace("'", "");
                        split_version[i] = split_version[i].replace(" ", "");
                        vv=split_version[i];
                        if (Use_version==0) {
                            split_version[i] = "";
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

                        subQuery +=     "select vulns_id ,product_name ,version from product  where product_name like '"+split_package[i]+"%' and version  like '"+vv+"'   union ";  //merge all queries in  one query   created by REza deHghani

                    }
                    else
                        subQuery +=     "select vulns_id ,product_name ,version from product  where product_name like '"+split_package[i]+"%' and version  like '"+vv+"%'   union ";  //merge all queries in  one query   created by REza deHghani
                }
                subQuery = subQuery.substring(0, subQuery.length() - 6);   //this code remove last  union of  subquery
                MyConnection = connection;
                String final_query = " select t.* ,  p.product_name , p.version   from cve t " +
                        "inner join" +
                        "("+subQuery+") p " +
                        "on p.vulns_id=t.id   where updated_at>"+String.valueOf(startint) +" and  updated_at<"+String.valueOf(finishint) +" order by cvss desc";

                MyStatement1 = MyConnection.prepareStatement(final_query);
                PreparedStatement my=MyConnection.prepareStatement(final_query);
                ResultSet r = MyStatement1.executeQuery();
                String name = CreateName2(distro, distro);
                boolean T=false;
                System.out.println("getting information please wait....");

                while (r.next()) {
                    List<String> row = new ArrayList<String>();
                    for(int coun=1;coun<=25;coun++)
                    {

                        row.add(r.getString(coun));
                    }
                    resultrecods.add(row);

                }

                System.out.println("collecting information  done....");

                if (flag == 1) {
                }

            }

            else
            {


                int it = 0;
                String subQuery = "";

                /////////////////////// B: this section is add in version 4.4
                for (int i = 0; i < split_package.length; i++) {
                    vv="";
                    split_package[i] = split_package[i].replace("'", "");//B :this code was added to prevent sali


                    if(split_version.length>i){
                        split_version[i] = split_version[i].replace("'", "");
                        split_version[i] = split_version[i].replace(" ", "");
                        vv=split_version[i];
                        if (Use_version==0) {
                            split_version[i] = "";
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

                        subQuery += "select id from lookups where type ='PCK' and val like '%"+ split_package[i] +"%'" + " union";  //merge all queries in  one query   created by REza deHghani

                }
                subQuery = subQuery.substring(0, subQuery.length() - 5);   //this code remove last  union of  subquery
                MyConnection = connection;
                String final_query = "  select vulns.id,vulns.package,product.package_name,product.version from vulns,product,product_vulns where product_vulns.package_id=product.id and product_vulns.vulns_id=vulns.id    and  vulns.package in ("+subQuery+") " ;
                MyStatement1 = MyConnection.prepareStatement(final_query);
                PreparedStatement my=MyConnection.prepareStatement(final_query);
                ResultSet r = MyStatement1.executeQuery();
                String name = CreateName2(distro, distro);
                boolean T=false;
                System.out.println("getting information please wait....");

                while (r.next()) {
                    List<String> row = new ArrayList<String>();
                    for(int i=0;i<=25;i++)
                    {
                        row.add(r.getString(i));
                    }
                    T=true;
/*
                    int idNumber = r.getInt("id");
                    String packagename=r.getString("package_name");
                    String version=r.getString("version");

                    if (wholeIds.contains(idNumber)) {
                        flag = 1;

                        List<String> record = FillExcelRow(Integer.toString(idNumber),distro);
                        if((!record.get(0).contains("Not Clear") && (!record.get(0).contains("vulnerability"))) )
                        {
                            record.add(packagename);
                            record.add(version);
                            resultrecods.add(record);

                        }

                    }
*/

                }

                System.out.println("collecting information  done....");



                if (flag == 1) {
                }


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

        ////////////////////////////////////////////////////////////////////////

        Iterator<List<String>> iterator = resultrecods.iterator();
        int p=0;
        while (iterator.hasNext()){

            List<String> myList = iterator.next();
            CVE cve = new CVE();
            cve.setCveName(myList.get(1));
            cve.setCreatedDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date(Long.parseLong(myList.get(2)) * 1000)));
            cve.setLastModifiedDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date(Long.parseLong(myList.get(3)) * 1000)));
            cve.setDescription(myList.get(4));
            cve.setPatch(myList.get(5));
            cve.setCvss(myList.get(6));
            cve.setReferenceLink(myList.get(7));
            cve.setCWE(myList.get(8));
            cve.setPackageName(myList.get(9));
            cve.setAttackComplexity(myList.get(10));
            cve.setAttackVector(myList.get(11));
            cve.setAuthentication(myList.get(12));
            cve.setTypeEffect(myList.get(13));
            cve.setIntegrityEffect(myList.get(14));
            cve.setConfidentialityEffect(myList.get(15));
            cve.setAvailabilityEffect(myList.get(16));
            cve.setPrivilegesRequires(myList.get(17));
            cve.setAgent(myList.get(18));
            cve.setFamily(myList.get(19));
            cve.setsoloution(myList.get(20));
            cve.setSynopsis(myList.get(21));
            cve.setTitle(myList.get(22));
            cve.setProduct(myList.get(23));
            cve.setVersion(myList.get(24));
            cves.add(cve);
        }

        finish=true;
        synchronized (this)
        {
            this.notify();
        }
    }
    private void getPackageNameFromFile(List<String> pkg) {

        try {
            int counter = 0;
            for (String st : pkg) {
                counter = counter + 1;
                int index = st.indexOf(" ");  //  B: we change - to "  "   to seprate package from version
                int index2 = st.indexOf("*");  //B : if we dont have version
                String v ="";

                if(usevv==0){
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

                if (!packageNames.isEmpty())
                    packageNames += ",";
                packageNames += st;
                Versions += v;
                if (!Versions.isEmpty())
                    Versions += ",";


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    /*
    public  void GetFromDB(String iStart, String iFinish, String distro) throws ParseException, SQLException, DataFormatException, IOException, InvalidFormatException {

        List<String> PeriodCollection = devideToWeeks(iStart, iFinish);
        Connection MyConnection = null;
        PreparedStatement Statement = null;
        ResultSet IDs = null;
        try {

            MyConnection = connection;
            for (String period : PeriodCollection) {

                //--------------------------------------------------------------------------

                Statement = MyConnection.prepareStatement("select id from vulns where updated_at >=? and updated_at<= ? ");
                Statement.setInt(1, startint);
                Statement.setInt(2, finishint);
                //       Statement.setString(3, distro);
                IDs = Statement.executeQuery();
                while (IDs.next()) {
                    int idNumber = IDs.getInt("id");
                     wholeIds.add(idNumber);

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
  */
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
                System.out.println(cve_row.getString("cve"));
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


    public  List<CVE> return_result() throws InterruptedException {
        synchronized (this)
        {
            if(!finish)
            this.wait();
        }
        return  cves;
    }
}
