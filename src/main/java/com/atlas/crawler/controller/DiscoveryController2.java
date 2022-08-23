package com.atlas.crawler.controller;

import com.atlas.crawler.adapter.Adapter;
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
@RequestMapping("/ord/disc")
public class DiscoveryController2 {


    @Autowired
    private CoreService coreService;



    @ResponseBody

    @GetMapping("/assetdiscovery")
    public String HomeReports3() {

        return "discovery2/nmap-download";
    }


    @ResponseBody
    @PostMapping("/nmap-download")
    public void NMAPSdownload(@RequestParam("command") String command
    ,  HttpServletResponse response             ) throws IOException {

        System.out.println(command);

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


}
