package com.atlas.crawler.model;

public class Vendor {

    private String VendorName;
    private String count;

    public Vendor(){

    }

    public String getVendorName() {
        return VendorName;
    }

    public void setVendorName(String scanName) {
        this.VendorName = scanName;
    }

    public String getcount() {
        return count;
    }

    public void setcount(String ipAddress) {
        this.count = ipAddress;
    }


}
