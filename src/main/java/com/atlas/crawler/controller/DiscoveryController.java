package com.atlas.crawler.controller;

import com.atlas.crawler.adapter.Adapter;
import com.atlas.crawler.export.CveExcelExporter;
import com.atlas.crawler.export.CveExcelExporter2;
import com.atlas.crawler.model.NMAP;
import com.atlas.crawler.model.NmapCve;
import com.atlas.crawler.service.CoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@Controller
@RequestMapping("/ordinary/discovery")
public class DiscoveryController {


    @Autowired
    private CoreService coreService;


    @GetMapping("/assetdiscovery")
    public String HomeReports() {

        return "ordinary/index-asset";
    }

    @ResponseBody

    @GetMapping("/assetreport")
    public String HomeReports2() {

        return "ordinary/index-asset-report";
    }

    @ResponseBody
    @PostMapping("/nmap-scan")
    public void ScanNMAPS(@RequestParam("command") String command , HttpServletResponse response) throws IOException {
        List<NMAP> nmaps = getNMAPFromCore(command);
        CveExcelExporter2 cveExcelExporter = new CveExcelExporter2(nmaps);

        cveExcelExporter.exportCsv(response);
        System.out.println(response.getHeaderNames());

    }

    @PostMapping("/nmap-downlaod")
    public void ScanNMAPS2(@RequestParam("command") String command , HttpServletResponse response) throws IOException {

        List<NMAP> nmaps = getNMAPFromCore(command);
        CveExcelExporter2 cveExcelExporter = new CveExcelExporter2(nmaps);

        cveExcelExporter.exportCsv(response);
        return;
    }

    private List<NMAP> getNMAPFromCore(String command) {

        Connection connection = coreService.getConnection();
        Adapter adapter = new Adapter(connection);
        List<NMAP> nmaps = adapter.getAssetDiscovery(command);
        return nmaps;
    }

    @ResponseBody
    @PostMapping("/nmap-list")
    public List<NmapCve> findNMAPS(@RequestParam("ip") String ip) {

        List<NmapCve> nmapCve = null;
        if (ip.length() <4){

            nmapCve = getNmapCveFromCoreBaseOnLastScan();

        }else{

            nmapCve = getNmapCveFromCoreBaseOnIp(ip);
        }

        return nmapCve;
    }

    private List<NmapCve> getNmapCveFromCoreBaseOnLastScan() {
        Connection connection = coreService.getConnection();
        Adapter adapter = new Adapter(connection);
        List<NmapCve> nmapCve = adapter.getNmapCveBaseOnLastScan();
        return nmapCve;
    }

    private List<NmapCve> getNmapCveFromCoreBaseOnIp(String ip) {
        Connection connection = coreService.getConnection();
        Adapter adapter = new Adapter(connection);
        List<NmapCve> nmapCve = adapter.getNmapCveBaseOnIp(ip);
        return nmapCve;
    }
}
