/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.export;

import com.atlas.crawler.model.CVE;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CveExcelExporter {

    private List<CVE> cveList;

    public CveExcelExporter(List<CVE> cveList){
        this.cveList = cveList;
    }


    public void exportCsv(HttpServletResponse response) throws IOException {

        response.setContentType("text/csv");

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=CVE_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);


        String[] csvHeader = {"Package Name","Distribution","Product","Version","Created Date",
                "Platform","Cve Name","Publish Date","Last Modified Date","Description",
                "Type","Attack Vector","Attack Complexity","Cvss","Cvss Text","Authentication",
                "Type Effect","Integrity Effect","Confidentiality Effect","Availability Effect",
                "Privileges Requires","Gained Access","Reference Link","Comments","Path"};

        String[] nameMapping ={"packageName","distribution","product","version","createdDate",
                "platform","cveName","publishDate","lastModifiedDate","description",
                "type","attackVector","attackComplexity","cvss","cvssText","authentication",
                "typeEffect","integrityEffect","confidentialityEffect","availabilityEffect",
                "privilegesRequires","gainedAccess","referenceLink","comments","patch"};

        csvWriter.writeHeader(csvHeader);

        for (CVE cve:cveList) {
            csvWriter.write(cve,nameMapping);
        }
        csvWriter.close();

    }

    public void exportCsv2(HttpServletResponse response) throws IOException {

        response.setContentType("text/csv");

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=CVE_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);


        String[] csvHeader = {"Package Name", "Distribution", "Product", "Version", "Created Date",
                "Platform", "Cve Name", "Publish Date", "Last Modified Date", "Description",
                "Type", "Attack Vector", "Attack Complexity", "Cvss", "Cvss Text", "Authentication",
                "Type Effect", "Integrity Effect", "Confidentiality Effect", "Availability Effect",
                "Privileges Requires", "Gained Access", "Reference Link", "Comments", "Path"};

        String[] nameMapping = {"packageName", "distribution", "product", "version", "createdDate",
                "platform", "cveName", "publishDate", "lastModifiedDate", "description",
                "type", "attackVector", "attackComplexity", "cvss", "cvssText", "authentication",
                "typeEffect", "integrityEffect", "confidentialityEffect", "availabilityEffect",
                "privilegesRequires", "gainedAccess", "referenceLink", "comments", "patch"};

        csvWriter.writeHeader(csvHeader);

        for (CVE cve : cveList) {
            csvWriter.write(cve, nameMapping);
        }
        csvWriter.close();
    }







    }
