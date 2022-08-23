package com.atlas.crawler.export;

import com.atlas.crawler.model.CVE;
import com.atlas.crawler.model.NMAP;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CveExcelExporter2 {

    private    List<NMAP> nmaps ;

    public CveExcelExporter2( List<NMAP> nmaps){
        this.nmaps = nmaps;
    }


    public void exportCsv(HttpServletResponse response) throws IOException {

        response.setContentType("text/csv");

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=CVE_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);


        String[] csvHeader = {"ScanName","IpAdress","Port","protocole","ServiceNAme",
                "Product","Version","Extra info"};

        String[] nameMapping ={"scanName","ipAddress","portId","portProtocol","serviceName",
                "productName","version","info"};

        csvWriter.writeHeader(csvHeader);

        for (NMAP nmap:nmaps) {
                csvWriter.write(nmap,nameMapping);


        }
        csvWriter.close();

    }





}
