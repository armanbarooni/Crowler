package com.atlas.crawler.core;

import com.atlas.crawler.controller.ReportController;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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


public class Centos extends  Thread{

    static final Logger LOGGER = Logger.getLogger(Centos.class.getName());

    private General general;
    private Connection connection;
    public Centos(Connection connection){
        this.connection = connection;
        general = new General(connection);
    }
    Bugzilla bugzilla=new Bugzilla(connection);
    Nvd nvd=new Nvd(connection);
    CveDetails cveDetails=new CveDetails(connection);
    public void run() {
        System.err.println("Crawler : CENTOS");
        boolean flag = false;
        if (ReportController.searchByPackage_flag && ReportController.UseDB) {

            ReportController.general_method="centos";
            general.start();
            try {
                general.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } else {
            try {
                setCutTime(general.reference);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Document Bugzilla_All_CVEs_Doc =null;
            Fedora fedora =new Fedora(connection);
            String rref="";
            System.err.println(general.rref);
        if(general.rref=="")
            rref=general.reference;
            else
                rref=general.rref;
            if(rref.contains("bugzilla"))
            {
                System.err.println("bugzilaa for centos 1/4");
                general.reference="bugzilla";
                try {
                    setCutTime("bugzilla");
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    ArrayList<String> bugzila_fedora_list=new ArrayList<>();
                    String StartingURL = "https://bugzilla.redhat.com/buglist.cgi?bug_status=NEW&bug_status=VERIFIED&bug_status=ASSIGNED&bug_status=MODIFIED&bug_status=ON_DEV&bug_status=ON_QA&bug_status=RELEASE_PENDING&bug_status=POST&chfieldfrom=2019-1-01&field0-0-0=product&field0-0-1=component&field0-0-2=alias&field0-0-3=short_desc&field0-0-4=status_whiteboard&field0-0-5=content&limit=0&order=changeddate%20DESC%2Cpriority%2Cbug_severity&query_format=advanced&type0-0-0=substring&type0-0-1=substring&type0-0-2=substring&type0-0-3=substring&type0-0-4=substring&type0-0-5=matches&value0-0-0=cve&value0-0-1=cve&value0-0-2=cve&value0-0-3=cve&value0-0-4=cve&value0-0-5=%22cve%22";
                    boolean test_time = false;
                    Unirest.setTimeouts(5000, 5000);
                    HttpResponse<String> response = null;
                    try {
                        response = Unirest.post("https://bugzilla.redhat.com/jsonrpc.cgi")
                                .header("Content-Type", " application/json")
                                .header("path", "/jsonrpc.cgi")
                                .header("Cookie", "Bugzilla_login_request_cookie=40Kfhm2BKI; LASTORDER=priority%2Cbug_severity")
                                .body("{\"draw\":1,\"columns\":[{\"data\":\"id\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"relevance\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"product\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"component\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"assigned_to\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"status\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"resolution\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"summary\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"last_change_time\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}}],\"order\":[{\"column\":8,\"dir\":\"desc\"}],\"start\":0,\"length\":20,\"search\":{\"value\":\"\",\"regex\":false},\"Bugzilla_api_token\":\"\",\"id\":1,\"method\":\"Bug.search\",\"params\":[{\"chfieldfrom\":\"2019-1-01\",\"field0-0-0\":\"product\",\"field0-0-1\":\"component\",\"field0-0-2\":\"alias\",\"field0-0-3\":\"short_desc\",\"field0-0-4\":\"status_whiteboard\",\"field0-0-5\":\"content\",\"include_fields\":[\"id\",\"relevance\",\"product\",\"component\",\"assigned_to\",\"status\",\"resolution\",\"summary\",\"last_change_time\",\"bug_classes\"],\"limit\":20,\"query_format\":\"advanced\",\"status\":[\"NEW\",\"VERIFIED\",\"ASSIGNED\",\"MODIFIED\",\"ON_DEV\",\"ON_QA\",\"RELEASE_PENDING\",\"POST\"],\"type0-0-0\":\"substring\",\"type0-0-1\":\"substring\",\"type0-0-2\":\"substring\",\"type0-0-3\":\"substring\",\"type0-0-4\":\"substring\",\"type0-0-5\":\"matches\",\"value0-0-0\":\"cve\",\"value0-0-1\":\"cve\",\"value0-0-2\":\"cve\",\"value0-0-3\":\"cve\",\"value0-0-4\":\"cve\",\"value0-0-5\":\"\\\"cve\\\"\",\"offset\":0,\"order\":\"changeddate desc\"}]}")
                                .asString();

                    } catch (UnirestException e) {
                        e.printStackTrace();
                    }
                    String lenght=response.getBody().substring(response.getBody().indexOf("total_matches")+15,response.getBody().indexOf("total_matches")+19);
                    if(47>=(int)(lenght.charAt(3)) || (int)(lenght.charAt(3)) >=58)
                    {
                        lenght=lenght.substring(0,3);
                    }
                    int len=Integer.parseInt(lenght);
                    for(int i=0;i<len;i=i+20)
                    {
                        System.out.println(i);
                        Unirest.setTimeouts(5000, 5000);
                        try {
                            response = Unirest.post("https://bugzilla.redhat.com/jsonrpc.cgi")
                                    .header("Content-Type", " application/json")
                                    .header("path", "/jsonrpc.cgi")
                                    .header("Cookie", "Bugzilla_login_request_cookie=40Kfhm2BKI; LASTORDER=priority%2Cbug_severity")
                                    .body("{\"draw\":1,\"columns\":[{\"data\":\"id\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"relevance\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"product\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"component\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"assigned_to\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"status\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"resolution\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"summary\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"last_change_time\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}}],\"order\":[{\"column\":8,\"dir\":\"desc\"}],\"start\":"+i+",\"length\":20,\"search\":{\"value\":\"\",\"regex\":false},\"Bugzilla_api_token\":\"\",\"id\":1,\"method\":\"Bug.search\",\"params\":[{\"chfieldfrom\":\"2019-1-01\",\"field0-0-0\":\"product\",\"field0-0-1\":\"component\",\"field0-0-2\":\"alias\",\"field0-0-3\":\"short_desc\",\"field0-0-4\":\"status_whiteboard\",\"field0-0-5\":\"content\",\"include_fields\":[\"id\",\"relevance\",\"product\",\"component\",\"assigned_to\",\"status\",\"resolution\",\"summary\",\"last_change_time\",\"bug_classes\"],\"limit\":20,\"query_format\":\"advanced\",\"status\":[\"NEW\",\"VERIFIED\",\"ASSIGNED\",\"MODIFIED\",\"ON_DEV\",\"ON_QA\",\"RELEASE_PENDING\",\"POST\"],\"type0-0-0\":\"substring\",\"type0-0-1\":\"substring\",\"type0-0-2\":\"substring\",\"type0-0-3\":\"substring\",\"type0-0-4\":\"substring\",\"type0-0-5\":\"matches\",\"value0-0-0\":\"cve\",\"value0-0-1\":\"cve\",\"value0-0-2\":\"cve\",\"value0-0-3\":\"cve\",\"value0-0-4\":\"cve\",\"value0-0-5\":\"\\\"cve\\\"\",\"offset\":"+i+",\"order\":\"changeddate desc\"}]}")
                                    .asString();

                        } catch (UnirestException e) {
                            i=i-20;
                            e.printStackTrace();
                        }
                        String json=response.getBody().substring(response.getBody().indexOf('['),response.getBody().lastIndexOf(']')+1)  ;
                        JSONArray packages=new JSONArray(json);
                        for (int j=0;j<packages.length();j++)
                        {
                            JSONObject pack=packages.getJSONObject(j);
                            String id=String.valueOf( pack.getInt("id"));
                            String Time=pack.getString("last_change_time");
                            Time=Time.substring(0,10);
                            SimpleDateFormat Formatter1 = new SimpleDateFormat("yyyy/MM/dd");
                            Time= Time.replace('-','/');
                            Date date = null;
                            try {
                                date = Formatter1.parse(Time);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            long compare_time=date.getTime();
                            if(general. Cut_Time.getTime()<=compare_time)
                            {
                                bugzila_fedora_list.add(id);
                            }
                            else
                                test_time= true;
                            if(test_time)
                                break;
                        }
                        if(test_time)
                            break;
                    }
                    Collections.reverse(bugzila_fedora_list);
                    try {
                        Bugzilla_All_CVEs_Doc = general.getDocument(StartingURL);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Bugzilla bugzilla = new Bugzilla(connection);
                    System.err.println("list is complete 2/4");
                    general.centos_count_b=bugzila_fedora_list.size();
                    try {
                        bugzilla.LoopOverEachRow1(bugzila_fedora_list, Bugzilla_All_CVEs_Doc, flag,general.reference);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (InvalidFormatException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (DataFormatException e) {
                        e.printStackTrace();
                    }
                }
                catch (NullPointerException e){
                    Nvd nvd = new Nvd(connection);
                    CveDetails cveDetails = new CveDetails(connection);
                    try {
                        general.reference="nvd";
                        setCutTime("nvd");
                        ArrayList<Elements> NVDallPageDoc =  new ArrayList<>();
                        NVDallPageDoc = nvd. GetNVDallPage1();
                        nvd. LoopOverEachRow_NVD1(NVDallPageDoc, Bugzilla_All_CVEs_Doc, flag,general.reference);
                    }catch (IOException | NullPointerException | SQLException | ParseException | InterruptedException e10) {
                        try {
                            ArrayList<Elements> CVEallpage = cveDetails.GetCVEDetails();
                            cveDetails.LoopOverEachRow_CVE(CVEallpage, Bugzilla_All_CVEs_Doc, flag,general.reference);
                        }
                        catch (IOException | NullPointerException | SQLException | ParseException e11){
                        } catch (InvalidFormatException ex) {
                            ex.printStackTrace();
                        } catch (DataFormatException ex) {
                            ex.printStackTrace();
                        }

                    } catch (InvalidFormatException ex) {
                        ex.printStackTrace();
                    } catch (DataFormatException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            if(rref.contains("nvd"))
            {
                System.err.println("nvd for centos 1/4");

                general.reference="nvd";
                try {
                    setCutTime("nvd");
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Nvd nvd = new Nvd(connection);
                Bugzilla bugzilla = new Bugzilla(connection);
                CveDetails cveDetails = new CveDetails(connection);
                try {
                    ArrayList<Elements> NVDallPageDoc =  new ArrayList<>();
                    NVDallPageDoc = nvd. GetNVDallPage1();
                    general. centos_count=(NVDallPageDoc.size()*20);
                    nvd. LoopOverEachRow_NVD1(NVDallPageDoc, Bugzilla_All_CVEs_Doc,
                            flag,general.reference);
                }
                catch (IOException | NullPointerException | InterruptedException ee){
                    try {


                        ArrayList<String> bugzila_fedora_list=new ArrayList<>();
                        String StartingURL = "https://bugzilla.redhat.com/buglist.cgi?bug_status=NEW&bug_status=VERIFIED&bug_status=ASSIGNED&bug_status=MODIFIED&bug_status=ON_DEV&bug_status=ON_QA&bug_status=RELEASE_PENDING&bug_status=POST&chfieldfrom=2019-1-01&field0-0-0=product&field0-0-1=component&field0-0-2=alias&field0-0-3=short_desc&field0-0-4=status_whiteboard&field0-0-5=content&limit=0&order=changeddate%20DESC%2Cpriority%2Cbug_severity&query_format=advanced&type0-0-0=substring&type0-0-1=substring&type0-0-2=substring&type0-0-3=substring&type0-0-4=substring&type0-0-5=matches&value0-0-0=cve&value0-0-1=cve&value0-0-2=cve&value0-0-3=cve&value0-0-4=cve&value0-0-5=%22cve%22";
                        boolean test_time = false;
                        Unirest.setTimeouts(5000, 5000);
                        HttpResponse<String> response = null;
                        try {
                            response = Unirest.post("https://bugzilla.redhat.com/jsonrpc.cgi")
                                    .header("Content-Type", " application/json")
                                    .header("path", "/jsonrpc.cgi")
                                    .header("Cookie", "Bugzilla_login_request_cookie=40Kfhm2BKI; LASTORDER=priority%2Cbug_severity")
                                    .body("{\"draw\":1,\"columns\":[{\"data\":\"id\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"relevance\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"product\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"component\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"assigned_to\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"status\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"resolution\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"summary\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"last_change_time\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}}],\"order\":[{\"column\":8,\"dir\":\"desc\"}],\"start\":0,\"length\":20,\"search\":{\"value\":\"\",\"regex\":false},\"Bugzilla_api_token\":\"\",\"id\":1,\"method\":\"Bug.search\",\"params\":[{\"chfieldfrom\":\"2019-1-01\",\"field0-0-0\":\"product\",\"field0-0-1\":\"component\",\"field0-0-2\":\"alias\",\"field0-0-3\":\"short_desc\",\"field0-0-4\":\"status_whiteboard\",\"field0-0-5\":\"content\",\"include_fields\":[\"id\",\"relevance\",\"product\",\"component\",\"assigned_to\",\"status\",\"resolution\",\"summary\",\"last_change_time\",\"bug_classes\"],\"limit\":20,\"query_format\":\"advanced\",\"status\":[\"NEW\",\"VERIFIED\",\"ASSIGNED\",\"MODIFIED\",\"ON_DEV\",\"ON_QA\",\"RELEASE_PENDING\",\"POST\"],\"type0-0-0\":\"substring\",\"type0-0-1\":\"substring\",\"type0-0-2\":\"substring\",\"type0-0-3\":\"substring\",\"type0-0-4\":\"substring\",\"type0-0-5\":\"matches\",\"value0-0-0\":\"cve\",\"value0-0-1\":\"cve\",\"value0-0-2\":\"cve\",\"value0-0-3\":\"cve\",\"value0-0-4\":\"cve\",\"value0-0-5\":\"\\\"cve\\\"\",\"offset\":0,\"order\":\"changeddate desc\"}]}")
                                    .asString();

                        } catch (UnirestException e) {
                            e.printStackTrace();
                        }
                        String lenght=response.getBody().substring(response.getBody().indexOf("total_matches")+15,response.getBody().indexOf("total_matches")+19);
                        if(47>=(int)(lenght.charAt(3)) || (int)(lenght.charAt(3)) >=58)
                        {
                            lenght=lenght.substring(0,3);
                        }
                        int len=Integer.parseInt(lenght);
                        for(int i=0;i<len;i=i+20)
                        {
                            System.out.println(i);
                            Unirest.setTimeouts(5000, 5000);
                            try {
                                response = Unirest.post("https://bugzilla.redhat.com/jsonrpc.cgi")
                                        .header("Content-Type", " application/json")
                                        .header("path", "/jsonrpc.cgi")
                                        .header("Cookie", "Bugzilla_login_request_cookie=40Kfhm2BKI; LASTORDER=priority%2Cbug_severity")
                                        .body("{\"draw\":1,\"columns\":[{\"data\":\"id\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"relevance\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"product\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"component\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"assigned_to\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"status\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"resolution\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"summary\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"last_change_time\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}}],\"order\":[{\"column\":8,\"dir\":\"desc\"}],\"start\":"+i+",\"length\":20,\"search\":{\"value\":\"\",\"regex\":false},\"Bugzilla_api_token\":\"\",\"id\":1,\"method\":\"Bug.search\",\"params\":[{\"chfieldfrom\":\"2019-1-01\",\"field0-0-0\":\"product\",\"field0-0-1\":\"component\",\"field0-0-2\":\"alias\",\"field0-0-3\":\"short_desc\",\"field0-0-4\":\"status_whiteboard\",\"field0-0-5\":\"content\",\"include_fields\":[\"id\",\"relevance\",\"product\",\"component\",\"assigned_to\",\"status\",\"resolution\",\"summary\",\"last_change_time\",\"bug_classes\"],\"limit\":20,\"query_format\":\"advanced\",\"status\":[\"NEW\",\"VERIFIED\",\"ASSIGNED\",\"MODIFIED\",\"ON_DEV\",\"ON_QA\",\"RELEASE_PENDING\",\"POST\"],\"type0-0-0\":\"substring\",\"type0-0-1\":\"substring\",\"type0-0-2\":\"substring\",\"type0-0-3\":\"substring\",\"type0-0-4\":\"substring\",\"type0-0-5\":\"matches\",\"value0-0-0\":\"cve\",\"value0-0-1\":\"cve\",\"value0-0-2\":\"cve\",\"value0-0-3\":\"cve\",\"value0-0-4\":\"cve\",\"value0-0-5\":\"\\\"cve\\\"\",\"offset\":"+i+",\"order\":\"changeddate desc\"}]}")
                                        .asString();

                            } catch (UnirestException e) {
                                i=i-20;
                                e.printStackTrace();
                            }
                            String json=response.getBody().substring(response.getBody().indexOf('['),response.getBody().lastIndexOf(']')+1)  ;
                            JSONArray packages=new JSONArray(json);
                            for (int j=0;j<packages.length();j++)
                            {
                                JSONObject pack=packages.getJSONObject(j);
                                String id=String.valueOf( pack.getInt("id"));
                                String Time=pack.getString("last_change_time");
                                Time=Time.substring(0,10);
                                SimpleDateFormat Formatter1 = new SimpleDateFormat("yyyy/MM/dd");
                                Time= Time.replace('-','/');
                                Date date = null;
                                try {
                                    date = Formatter1.parse(Time);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                long compare_time=date.getTime();
                                if(general. Cut_Time.getTime()<=compare_time)
                                {
                                    bugzila_fedora_list.add(id);
                                }
                                else
                                    test_time= true;
                                if(test_time)
                                    break;
                            }
                            if(test_time)
                                break;
                        }
                        Collections.reverse(bugzila_fedora_list);
                        try {
                            Bugzilla_All_CVEs_Doc = general.getDocument(StartingURL);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.err.println("list is complete 2/4");
                        general.centos_count_b=bugzila_fedora_list.size();
                        try {
                            bugzilla.LoopOverEachRow1(bugzila_fedora_list, Bugzilla_All_CVEs_Doc, flag,general.reference);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        } catch (InvalidFormatException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (DataFormatException e) {
                            e.printStackTrace();
                        }


                    }catch (Exception eee) {

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (InvalidFormatException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (DataFormatException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public   void setCutTime(String refence) throws SQLException,IOException,ParseException{
        if (general.CentosCurrent_Time_Checking == true) {
            Connection MyConnection = null;
            PreparedStatement MyStatement = null;
            ResultSet RS_Last_CVE = null;
            general.FedoraCurrent_Time_Checking = false;
            try {

                MyConnection = connection;
                MyStatement = MyConnection.prepareStatement("select max(updated_at) from vulns where distro in (select id from lookups where val=?) and reference_list=?");
                MyStatement.setString(1, "Centos");
                MyStatement.setString(2, refence);
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


    public File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }
    public void CheckCurrentTime(Elements RowsInBugzillaFirstPage, Document BugzillaFirstPageDoc) throws SQLException, ParseException {
        int j=0;
        Iterator<Element> i = RowsInBugzillaFirstPage.iterator();
        while (i.hasNext()) {

            try {
                Element a_Row_of_Bugzilla_List = i.next();
                if (general.ToDate(a_Row_of_Bugzilla_List, BugzillaFirstPageDoc).before(general.Cut_Time)) {
                    i.remove();
                    j++;
                 //   System.out.println(j);
                }
            }catch (Exception e)
            {

            }

        }
    }
    public  void Start(String element1, Document Bugzilla_List,String reference) throws IOException {
       general. centos_count1_b=general. centos_count1_b+1;
            System.out.println( "Centos->number of packet that will going for proccess: " +general. centos_count1_b +" of  "+general. centos_count_b);
            String Bugzilla_Link_of_Each_Summary = element1;
            Document Bugzilla_Each_Summary_Doc = null;
            Elements CVE_Tags_Inside_of_Each_Summary = null;
            try {
                Bugzilla_Each_Summary_Doc =general. getDocument3("https://bugzilla.redhat.com/" + Bugzilla_Link_of_Each_Summary);
                CVE_Tags_Inside_of_Each_Summary = Bugzilla_Each_Summary_Doc.select("#short_desc_nonedit_display a");
            } catch (Exception e) {
                if (e.getMessage().equals("HTTP error fetching URL")) {
                } else if (e.getMessage().equals("Connection timed out: connect")) {
                    Start(element1, Bugzilla_List,reference);
                    return;
                } else if (e.getMessage().equals("Read timed out")) {

                    Start(element1, Bugzilla_List,reference);
                } else {
                    if (general. ProblemCounter1 < 5) {
                        general.  ProblemCounter1++;
                        Start(element1, Bugzilla_List,reference);
                        general.    ProblemCounter1 = 0;

                    }
                }
            }
        FileWriter fstream = new FileWriter("bugzilaa_centos.txt",true);
/*        if (CVE_Tags_Inside_of_Each_Summary != null )
        {
            BufferedWriter out = new BufferedWriter(fstream);
            for (Element CVE_Tag_Inside_of_Each_Summary : CVE_Tags_Inside_of_Each_Summary) {

                out.write(CVE_Tag_Inside_of_Each_Summary.ownText() + "\n");
            }
            out.close();
        }*/

            if (CVE_Tags_Inside_of_Each_Summary != null ) {

                if (!CVE_Tags_Inside_of_Each_Summary.isEmpty()) {
                    for (Element CVE_Tag_Inside_of_Each_Summary : CVE_Tags_Inside_of_Each_Summary) {
                        if (element1 != null && Bugzilla_Each_Summary_Doc != null && CVE_Tag_Inside_of_Each_Summary != null && Bugzilla_Link_of_Each_Summary != null) {
                            if(CVE_Tag_Inside_of_Each_Summary.attr("class").contains("bz_status_CLOSED"))
                                continue;

                            Track_Each_CVE( Bugzilla_List, Bugzilla_Each_Summary_Doc, CVE_Tag_Inside_of_Each_Summary, CVE_Tag_Inside_of_Each_Summary.ownText(),reference);

                        }
                    }
                }
            }


    }
    public int op=0;

    public  void Track_Each_CVE( Document Bugzilla_List, Document bugzilla_doc, Element CVE_Tag, String bugzilla_link,String reff) {
        CVE CVECentos_Object = new CVE();
        try {
            //      System.err.println(op);
            op=op+1;
            if(reff=="nvd")
            System.out.println(" centos -> add ->"+general. centos_count1 +" TO database " +Thread.currentThread().getId());
            else
                System.out.println(" centos -> add ->"+general. centos_count1_b +" TO database " +Thread.currentThread().getId());


            CVECentos_Object.AccessComplexity = "Not Clear";
            CVECentos_Object.AttackVector = "Not Clear";
            CVECentos_Object.Authentication = "Not Clear";
            CVECentos_Object.AvailibilityImpact = "Not Clear";
            CVECentos_Object.ConfidentialityImpact = "Not Clear";
            CVECentos_Object.GainedAccess = "Not Clear";
            CVECentos_Object.ImpactType = "Not Clear";
            CVECentos_Object.IntegrityImpact = "Not Clear";
            CVECentos_Object.PrevilagesRequired = "Not Clear";
            CVECentos_Object.VulnurabiltyType = "Not Clear";
            Document CVENVD;
            general. ErrataException = false;
            String redhatAddress = "";
            String AccessRedHatDocOfEachCVE = null;
            CVECentos_Object.CVEName=bugzilla_link;

            try {
                redhatAddress = CVE_Tag.attr("href");
            //    AccessRedHatDocOfEachCVE =general. getDocument(redhatAddress);

                //////////////////////////////////////////////////////
















                try {
                    Unirest.setTimeouts(5000, 5000);
                    HttpResponse<String> response = null;
                    try {
                        response = Unirest.get("https://access.redhat.com/api/redhat_node/"+bugzilla_link+".json")
                                .asString();
                    } catch (UnirestException e) {
                        //  e.printStackTrace();
                    }
                    if(response!=null)
                    {
                        String json=response.getBody();

                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray text=jsonObject.getJSONObject("field_cve_details_text").getJSONArray("und");  //.getJSONObject("und").getJSONObject("0").getJSONObject("value");
                        JSONObject text2= text.getJSONObject(0);
                        AccessRedHatDocOfEachCVE=text2.getString("value");
                        try {
                            JSONObject text4=jsonObject.getJSONObject("rh_cvss3_vector");
                            String Attacvector=text4.getString("Attack Vector");
                            String Integrity_impact=text4.getString("Integrity Impact");
                            String confidentiality=text4.getString("Confidentiality");
                            String Availabilty=text4.getString("Availability Impact");
                            String privialge =text4.getString("Privileges Required");
                            String compelxity=text4.getString("Attack Complexity");
                            CVECentos_Object.AttackVector = Attacvector;
                            CVECentos_Object.PrevilagesRequired =privialge;
                            CVECentos_Object.AccessComplexity = Availabilty;
                            CVECentos_Object.IntegrityImpact = Integrity_impact;
                            CVECentos_Object.ConfidentialityImpact = confidentiality;
                            CVECentos_Object.AvailibilityImpact =compelxity;


                        }catch (Exception e)
                        {
                            try {
                                JSONObject text4=jsonObject.getJSONObject("rh_cvss2_vector");
                                String Attacvector=text4.getString("Attack Vector");
                                String Integrity_impact=text4.getString("Integrity Impact");
                                String confidentiality=text4.getString("Confidentiality");
                                String Availabilty=text4.getString("Availability Impact");
                                String compelxity=text4.getString("Attack Complexity");
                                CVECentos_Object.PrevilagesRequired = "Get From NVD";
                                CVECentos_Object.AttackVector = Attacvector;
                                CVECentos_Object.AccessComplexity = Availabilty;
                                CVECentos_Object.IntegrityImpact = Integrity_impact;
                                CVECentos_Object.ConfidentialityImpact = confidentiality;
                                CVECentos_Object.AvailibilityImpact =compelxity;

                            }catch (Exception ee)
                            {

                            }
                        }


                    }


                }catch (Exception new_E)
                {



                }






                try {

                    Document test =general. getDocument3("https://nvd.nist.gov/vuln/detail/"+bugzilla_link+"/cpes?expandCpeRanges=true");
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

                    CVECentos_Object.PackageName.add(prev_ven);
                    CVECentos_Object.Product_version=new ArrayList<>();
                    CVECentos_Object.Product_name=new ArrayList<>();
                    while (true)
                    {
                        try {
                            strat=    test3.indexOf("cpe:2.3",counter);
                            if(strat==-1)
                            {


                                CVECentos_Object.Product_name.add(productname);
                                CVECentos_Object.Product_version.add(productversion);
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
                                //   System.out.println(pack+"   "+ver);
                                productname.add(pack);
                                productversion.add(ver);
                            }
                            else
                            {
                                CVECentos_Object.Product_name.add(productname);
                                CVECentos_Object.Product_version.add(productversion);
                                productname = new ArrayList<>();
                                productversion = new ArrayList<>();
                                prev_ven=ven;
                                prev_pack=pack;
                                counter=strat+1;

                                CVECentos_Object.  PackageName.add(prev_ven);


                            }
                        }catch (Exception eeeee)
                        {

                        }

                    }

                }catch (Exception new_EE)
                {

                }












                //////////////////////////////////
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
            } catch (Exception e) {
            }

            Document CVE_Details_Doc=null;
            try {
                CVE_Details_Doc =general. getDocument3("http://www.cvedetails.com/cve/" + bugzilla_link);

            }catch (Exception e)
            {

            }
            try {
                CVENVD =general. getDocument3("https://nvd.nist.gov/vuln/detail/" + bugzilla_link);
            } catch (Exception e) {
                CVENVD = null;
            }
            try {
                CVECentos_Object = GetProduct(CVECentos_Object,CVE_Details_Doc,CVENVD,bugzilla_link);//
            }catch (Exception e)
            {
            }
            try {
                CVECentos_Object = Get_Description_Impact(CVECentos_Object,CVENVD, CVE_Details_Doc);//
            }catch (Exception e)
            {

            }
            try {
                CVECentos_Object = GetDates(CVECentos_Object, bugzilla_doc, CVE_Details_Doc, CVENVD, bugzilla_link);//

            }catch (Exception e)
            {

            }
            try {
                CVECentos_Object = GetDetails(CVECentos_Object, bugzilla_doc, CVE_Details_Doc, CVENVD);//

            }catch (Exception e)
            {

            }


            CVECentos_Object = bugzilla.GetPatch(CVECentos_Object, bugzilla_doc);
            try {
                CVECentos_Object = GetComponent(CVECentos_Object, bugzilla_doc);

            }catch (Exception e)
            {

            }
            try {
                CVECentos_Object.RefferencesLinks.add("https://nvd.nist.gov/vuln/detail/" + CVECentos_Object.CVEName);

                CVECentos_Object = Get_Refferences(CVECentos_Object, "https://bugzilla.redhat.com/" + bugzilla_link, "http://www.cvedetails.com/cve/" + CVE_Tag.ownText(), CVE_Tag.attr("href"));


            }catch (Exception e){
            }
            try {

            }catch (Exception e)
            {

            }
            CVECentos_Object.Distro = "Centos";
            if (CVECentos_Object.Platforms.size() == 0) {
                CVECentos_Object.Platforms.add("Not Clear");
            }
            if (CVECentos_Object.PackageName.size() == 0) {
                CVECentos_Object.PackageName.add("Not Clear");
            }

            if (Bugzilla_List != null) {
                try {
                    bugzilla.ToDate( Bugzilla_List, CVECentos_Object);
                }catch (Exception e){
                    CVECentos_Object.Date_Changed = CVECentos_Object.LastUpdate;
                }

                general.   Print(CVECentos_Object, 0,reff);

            }
            else
            {
                CVECentos_Object.Date_Changed = CVECentos_Object.LastUpdate;
                general.   Print(CVECentos_Object, 1,reff   );
            }
        }
        catch (Exception e) {

            if (e.getMessage() == null) {
                return;
            }

        }

    }
    public  CVE GetProduct(CVE CVECentos_Object , Document CVEDetails ,Document CVENVD,String CVEElement){

      //  CVECentos_Object=nvd.GetProduct(CVECentos_Object,CVENVD,CVEElement);

        try {
            if(CVECentos_Object.PackageName==null)
            CVECentos_Object=cveDetails.GetProduct(CVECentos_Object,CVEDetails,CVEElement);
        }
        catch (Exception ee)
        {

        }
        return CVECentos_Object;



    }
    public  CVE GetComponent(CVE CVECentos_Object, Document EachSummaryDoc) {
        String Component = null;
        try {
            Component = EachSummaryDoc.getElementById("component").attr("value");

        } catch (Exception e) {
        }
      //  CVECentos_Object.PackageName.add(Component);
        return CVECentos_Object;
    }

    private  CVE GetDates(CVE CVECentos_Object, Document EachSummaryDoc, Document CVEDetailsDoc, Document CVENVD, String EachSummaryLink) throws ParseException {
        String comment = "";
        try {
            Elements elements = EachSummaryDoc.getElementsByClass("bz_comment_table").get(0).getElementsByTag("tr").get(0).getElementsByTag("td").first().getElementsByClass("bz_comment");

            for (Element element : elements) {
                comment += getComment(element);

            }
        } catch (Exception e) {



            //      LOGGER.log(Level.FINE,"comment exception: " + CVECentos_Object.CVEName + "-" + EachSummaryLink + "\n" + e.toString(),e);
        }
        String Reported=null;
        String modified=null;
        try{
            comment += getHistory(EachSummaryDoc);
            comment = comment.replace("\"", "");
            CVECentos_Object.Comments = comment.replace("'", "");
        }
        catch (Exception e)
        {
        }
        if(general. reference=="nvd")
        {
            try {
                SimpleDateFormat Formatter1 = new SimpleDateFormat("MM/dd/yyyy");
                Reported = CVENVD.getElementsByAttributeValue("data-testid", "vuln-published-on").first().ownText();
                CVECentos_Object.BroadcastDate = Formatter1.parse(Reported);
                modified = CVENVD.getElementsByAttributeValue("data-testid", "vuln-last-modified-on").first().ownText();
                CVECentos_Object.LastUpdate = Formatter1.parse(modified);
            } catch (Exception e) {
            }
        }
        else
        {
            try {
                SimpleDateFormat Formatter2 = new SimpleDateFormat("yyyy-MM-dd");
                Reported = EachSummaryDoc.getElementById("bz_show_bug_column_2").getElementsByTag("tr").first().getElementsByTag("td").text();
                Reported = Reported.substring(0, 10);
                CVECentos_Object.BroadcastDate = Formatter2.parse(Reported);
                String Modified = EachSummaryDoc.getElementById("bz_show_bug_column_2").getElementsByTag("tr").get(1).getElementsByTag("td").text();
                Modified = Modified.substring(0, 10);
                CVECentos_Object.LastUpdate = Formatter2.parse(Modified);
            }catch (Exception e)
            {
            }


        }




        return CVECentos_Object;
    }
    public boolean CheckErrata(Element AccessRedHatDocOfEachCVE) throws ParseException {
        try {
            Elements Errata_Table_Rows = AccessRedHatDocOfEachCVE.getElementById("DataTables_Table_0").getElementsByTag("tbody").first().getElementsByTag("tr");
            //Elements Errata_Table_Rows = AccessRedHatDocOfEachCVE.select("table:contains(Errata)").first().getElementsByTag("tbody").first().getElementsByTag("tr");
            //Element Errata_Table_Platforms = Errata_Table_Rows.getElementsByAttributeValue("headers", "th-platform");
            for (Element Each_Row_Errata : Errata_Table_Rows) {
                if (!Each_Row_Errata.getElementsByAttributeValue("headers", "th-platform1-b th-errata-b").first().text().isEmpty()) {
                    Element dateDoc = Each_Row_Errata.getElementsByAttributeValue("headers", "th-platform1-b th-release-b").first();
                    if (dateDoc.getElementsByTag("pfe-datetime").size() > 0)
                        if (Check_Date(dateDoc.getElementsByTag("pfe-datetime").first().attr("datetime"))) {
                            return true;
                        }
                }
            }
            return false;
        } catch (Exception e) {

            return false;
        }
    }

    public boolean Check_Date(String Date_to_Compare) throws ParseException {

        Date_to_Compare = Date_to_Compare.replace("-", "/");
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date1 = formatter1.parse(Date_to_Compare);
            Date Base_Date = formatter2.parse("21/02/2017");
            //System.out.println(date1);
            if (date1.before(Base_Date)) {
                return false;

            } else {
                return true;
            }
        } catch (Exception e) {

            return true;//----------------------------------------------------Ask
        }

    }


    public  CVE Get_Description_Impact(CVE CVECentos_Object, Document CVENVD, Document CVEDetailsDoc) {
        String Desc = null;
        //   Desc=Desc+.CveDetails.Getdesc(CVEDetailsDoc);
        String des=null;
        if(des!=null)
            Desc=des;
        des=nvd.Getdesc(CVENVD);
        if(des!=null)
            Desc=Desc+des;
        try {
            CVECentos_Object.Desc = Desc;
            if (CVECentos_Object.Desc.toLowerCase().contains("local")) {
                CVECentos_Object.ImpactType = "Local";
            } else if (CVECentos_Object.Desc.toLowerCase().contains("remote")) {
                CVECentos_Object.ImpactType = "Remotely";
            } else {
                CVECentos_Object.ImpactType = "Not Clear";
            }
        } catch (Exception e) {
            CVECentos_Object.Desc = "Not Clear";
            CVECentos_Object.ImpactType = "Not Clear";
        }
        return CVECentos_Object;
    }

    public CVE Get_Patch(CVE Obj, Document AccessRedHatDocOfEachCVE, Document doc2) {

        try {
            Elements Errata_Table_Rows = AccessRedHatDocOfEachCVE.getElementById("DataTables_Table_0").getElementsByTag("tbody").first().getElementsByTag("tr");
            for (Element Each_Package_Inside_Table : Errata_Table_Rows) {
                String platform1 = Each_Package_Inside_Table.getElementsByAttributeValue("headers", "th-platform-b").first().text().toLowerCase();
                if (platform1.contains("red hat enterprise linux 7")) {
                    if (!Each_Package_Inside_Table.getElementsByAttributeValue("headers", "th-platform1-b th-errata-b").first().text().isEmpty()) {
                        Element dateDoc = Each_Package_Inside_Table.getElementsByAttributeValue("headers", "th-platform1-b th-release-b").first();
                        if (dateDoc.getElementsByTag("pfe-datetime").size() > 0)
                            if (Check_Date(dateDoc.getElementsByTag("pfe-datetime").first().attr("datetime"))) {
                                Obj.PatchLink.add(Each_Package_Inside_Table.getElementsByAttributeValue("headers", "th-platform1-b th-errata-b").first().getElementsByTag("a").attr("href"));
                            }
                    }
                }

            }

        } catch (Exception e) {

        }
        Elements Comments_in_Bugzilla_for_Each_Summary = doc2.getElementsByTag("pre");
        for (Element Comment : Comments_in_Bugzilla_for_Each_Summary) {
            if (Comment.ownText().toLowerCase().contains("patch:")) {
                //Obj.PatchLink.add(Comment.getElementsByTag("a").attr("href"));
                Elements CommentLinks = Comment.select("a");
                for (Element CommentLink : CommentLinks) {
                    Obj.PatchLink.add(CommentLink.attr("href"));
                }

            }
            if (Comment.ownText().toLowerCase().contains("commit:")) {
                //Obj.PatchLink.add(Comment.getElementsByTag("a").attr("href"));
                Elements CommentLinks = Comment.select("a");
                for (Element CommentLink : CommentLinks) {
                    Obj.PatchLink.add(CommentLink.attr("href"));
                }
            }
        }
        return Obj;
    }

    public boolean PatchException(CVE Obj, Document doc2) throws ParseException {
        Elements Comments_in_Bugzilla_for_Each_Summary = doc2.getElementsByClass("bz_comment");
        for (Element Comment : Comments_in_Bugzilla_for_Each_Summary) {
            String CommentDate = Comment.getElementsByClass("bz_comment_time").first().ownText();
            CommentDate = CommentDate.split("\\s+")[0];
            if (Check_Date(CommentDate)) {
                if (Comment.ownText().toLowerCase().contains("patch:")) {
                    return true;
                }
                if (Comment.ownText().toLowerCase().contains("commit:")) {
                    return true;
                }
            }
        }
        return false;
    }




    public String getHistory(Document doc_Bugzilla) {
        String history = "";
        try {
            String historyLink = doc_Bugzilla.getElementById("bz_show_bug_column_2").getElementsByTag("tr").get(1).getElementsByTag("td").first().getElementsByTag("a").attr("href");
            Document bugzillaHistoryDoc =general. getDocument3("https://bugzilla.redhat.com/" + historyLink);
            Elements elements = bugzillaHistoryDoc.getElementById("bug_activity").getElementsByTag("tr");
            elements.remove(0);
            String who = "";
            String when = "";
            String what = "";
            String removed = "";
            String Added = "";
            for (Element element : elements) {
                try {
                    Elements rowElements = element.getElementsByTag("td");
                    int i = 0;
                    if (rowElements.get(i).hasAttr("rowspan"))
                        who = rowElements.get(i++).ownText();
                    if (rowElements.get(i).hasAttr("rowspan"))
                        when = rowElements.get(i++).ownText();
                    what = rowElements.get(i++).ownText();
                    removed = rowElements.get(i++).text();
                    Added = rowElements.get(i++).text();
                } catch (Exception e) {
                }

                history += "Who: " + who;
                history += "\nWhen: " + when;
                history += "\nwhat: " + what;
                history += "\nremoved:\n" + removed;
                history += "\nAdded:\n" + Added;
                history += "\n###########################################################\n";

            }


        } catch (Exception e) {
            //LOGGER.log(Level.FINE,"history exception2: \n" + e.toString(),e);
        }
        return history;
    }

    public String getComment(Element element) {
        String comment = "";
        String CommentText = element.getElementsByClass("bz_comment_text").first().text();
        String CommentTime = element.getElementsByClass("bz_comment_time").first().ownText();
        Element userElement = element.getElementsByClass("bz_comment_user").first();
        String CommentUser = "";
        if (userElement.getElementsByClass("fn").size() > 0)
            CommentUser = userElement.getElementsByClass("fn").first().ownText();
        else if (userElement.getElementsByClass("vcard redhat_user").size() > 0)
            CommentUser = userElement.getElementsByClass("vcard redhat_user").first().ownText();
        comment += "Who:" + CommentUser;
        comment += "\nWhen:" + CommentTime;
        comment += "\nwhat:Comment";
        comment += "\nAdded:\n" + CommentText;
        comment += "\n###########################################################\n";
        return comment;
    }


    private   CVE GetDetails(CVE CVECentos_Object, Document EachSummaryDoc,  Document CVEDetailsDoc, Document CVENVD) throws ParseException {
        try{
            try {
                CveDetails cveDetails=new CveDetails(connection);
                CVECentos_Object = cveDetails.GetDetailsByCVEDetails(CVECentos_Object, CVEDetailsDoc);
            }catch (Exception e)
            {

            }
   /*         CVECentos_Object.CVSS="0";
            CVECentos_Object.CVSS_details = "0-";
            try {
                if (AccessRedHatDocOfEachCVE != null) {
                    Bugzilla bugzilla= new Bugzilla(connection);
                    CVECentos_Object =bugzilla.GetDetailsByAccessRedHatv2(CVECentos_Object, AccessRedHatDocOfEachCVE, CVEDetailsDoc);
                }
            }catch (Exception e)
            {

            }*/
            Nvd nvd= new Nvd(connection);

            try {
                CVECentos_Object = nvd.GetDetailsByNVDV2(CVECentos_Object, CVENVD);
            }catch (Exception e)
            {

            }

    /*        try {
                if (AccessRedHatDocOfEachCVE != null) {
                    Bugzilla bugzilla= new Bugzilla(connection);
                    CVECentos_Object = bugzilla.GetDetailsByAccessRedHatv3(CVECentos_Object, AccessRedHatDocOfEachCVE, CVEDetailsDoc);
                }
            }catch (Exception e)
            {

            }*/
            try {
                CVECentos_Object =nvd.GetDetailsByNVDV3(CVECentos_Object, CVENVD);
                if (CVECentos_Object.CVSS_details == null) {
                    CVECentos_Object.CVSS_details = "Not Clear";
                }
            }catch (Exception e)
            {

            }


            return CVECentos_Object;
        } catch (Exception e)
        {
            return  CVECentos_Object;
        }

    }


    public CVE Get_Refferences(CVE Obj, String Link1, String Link2, String Link3) {
        Obj.RefferencesLinks.add(Link1 + "\n");
        Obj.RefferencesLinks.add(Link2 + "\n");
        Obj.RefferencesLinks.add(Link3 + "\n");
        return Obj;
    }
}
