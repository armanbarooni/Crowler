/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.controller;

import com.atlas.crawler.adapter.Adapter;
import com.atlas.crawler.entity.User;
import com.atlas.crawler.model.NmapCve;
import com.atlas.crawler.model.Vendor;
import com.atlas.crawler.service.CoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.util.List;

@Controller
@RequestMapping("/alarm")
public class AlarmController {

    @Autowired
    private CoreService coreService;

    @GetMapping("/Vendors")
    public String updateDatabaseIndex(HttpServletRequest request) {


        return "ordinary/alarm-index";
    }


    @ResponseBody
    @PostMapping("/Find-vendors")
    public Object findVendors(@RequestParam("Vendor_name") String vendor_name) {
        List<Vendor> Vendors = null;


        Vendors=getVendorsBasedOnVendorName(vendor_name);
        return Vendors;

    }

    @ResponseBody
    @PostMapping("/Find-products")
    public Object findVendors2(@RequestParam("Vendor_name") String vendor_name) {
        List<Vendor> Vendors = null;


        Vendors=getVendorsBasedOnVendorName2(vendor_name);
        return Vendors;

    }

    @ResponseBody
    @PostMapping("/add-products")
    public Object addproducts(@RequestParam("Vendor_name") String vendor_name,
                              @RequestParam("Vendor_name2") String vendor_name2) {
        List<Vendor> Vendors = null;


        Vendors=getVendorsBasedOnVendorName3(vendor_name,vendor_name2);
        return Vendors;

    }

    @ResponseBody
    @PostMapping("/add-products2")
    public Object addproducts2(@RequestParam("Vendor_name") String vendor_name,
                               @RequestParam("product_name") String product_name
                               ) {
        List<Vendor> Vendors = null;


        Vendors=getVendorsBasedOnVendorName4(vendor_name,product_name);
        return Vendors;

    }  //view_subscribed

    @ResponseBody
    @PostMapping("/view-subscribed")
    public Object view_subscribed(@RequestParam("Vendor_name") String vendor_name

    ) {
        List<Vendor> Vendors = null;


        Vendors=getVendorsBasedOnVendorName5(vendor_name);
        return Vendors;

    }  //view_subscribed

    @ResponseBody
    @PostMapping("/view-subscribed2")
    public Object view_subscribed2(@RequestParam("Vendor_name") String vendor_name

    ) {
        List<Vendor> Vendors = null;


        Vendors=getVendorsBasedOnVendorName6(vendor_name);
        return Vendors;

    }  //view_subscribed




    @GetMapping("/show-product/{vendorName}")
    public String showUser(@PathVariable String vendorName) {


        return "manager/show-user";
    }

    private List<Vendor> getVendorsBasedOnVendorName(String name) {
        Connection connection = coreService.getConnection();
        Adapter adapter = new Adapter(connection);
        List<Vendor> Vendors = adapter.getVendor(name);
        return Vendors;
    }
    private List<Vendor> getVendorsBasedOnVendorName2(String name) {
        Connection connection = coreService.getConnection();
        Adapter adapter = new Adapter(connection);
        List<Vendor> Vendors = adapter.getVendor2(name);
        return Vendors;
    }
    private List<Vendor> getVendorsBasedOnVendorName3(String name,String name2) {
        Connection connection = coreService.getConnection();
        Adapter adapter = new Adapter(connection);
        List<Vendor> Vendors = adapter.getVendor3(name,name2);
        return Vendors;
    }
    private List<Vendor> getVendorsBasedOnVendorName4(String name,String pname) {
        Connection connection = coreService.getConnection();
        Adapter adapter = new Adapter(connection);
        List<Vendor> Vendors = adapter.getVendor4(name,pname);
        return Vendors;
    }

    private List<Vendor> getVendorsBasedOnVendorName5(String name) {
        Connection connection = coreService.getConnection();
        Adapter adapter = new Adapter(connection);
        List<Vendor> Vendors = adapter.getVendor5(name);
        return Vendors;
    }

    private List<Vendor> getVendorsBasedOnVendorName6(String name) {
        Connection connection = coreService.getConnection();
        Adapter adapter = new Adapter(connection);
        List<Vendor> Vendors = adapter.getVendor6(name);
        return Vendors;
    }

}
