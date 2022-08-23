package com.atlas.crawler.model;

public class NmapCve {
    private String packageName;
    private String distribution;
    private String createdDate;
    private String platform;
    private String cveName;
    private String publishDate;
    private String lastModifiedDate;
    private String description;
    private String type;
    private String attackVector;
    private String attackComplexity;
    private String cvss;
    private String cvssText;
    private String authentication;
    private String typeEffect;
    private String integrityEffect;
    private String confidentialityEffect;
    private String availabilityEffect;
    private String privilegesRequires;
    private String gainedAccess;
    private String referenceLink;
    private String comments;
    private String patch;

    private String scanName;
    private String ipAddress;
    private Integer portId;
    private String portProtocol;
    private String serviceName;
    private String productName;
    private String version;
    private String info;


    public NmapCve(){

    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDistribution() {
        return distribution;
    }

    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getCveName() {
        return cveName;
    }

    public void setCveName(String cveName) {
        this.cveName = cveName;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAttackVector() {
        return attackVector;
    }

    public void setAttackVector(String attackVector) {
        this.attackVector = attackVector;
    }

    public String getAttackComplexity() {
        return attackComplexity;
    }

    public void setAttackComplexity(String attackComplexity) {
        this.attackComplexity = attackComplexity;
    }

    public String getCvss() {
        return cvss;
    }

    public void setCvss(String cvss) {
        this.cvss = cvss;
    }

    public String getCvssText() {
        return cvssText;
    }

    public void setCvssText(String cvssText) {
        this.cvssText = cvssText;
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public String getTypeEffect() {
        return typeEffect;
    }

    public void setTypeEffect(String typeEffect) {
        this.typeEffect = typeEffect;
    }

    public String getIntegrityEffect() {
        return integrityEffect;
    }

    public void setIntegrityEffect(String integrityEffect) {
        this.integrityEffect = integrityEffect;
    }

    public String getConfidentialityEffect() {
        return confidentialityEffect;
    }

    public void setConfidentialityEffect(String confidentialityEffect) {
        this.confidentialityEffect = confidentialityEffect;
    }

    public String getAvailabilityEffect() {
        return availabilityEffect;
    }

    public void setAvailabilityEffect(String availabilityEffect) {
        this.availabilityEffect = availabilityEffect;
    }

    public String getPrivilegesRequires() {
        return privilegesRequires;
    }

    public void setPrivilegesRequires(String privilegesRequires) {
        this.privilegesRequires = privilegesRequires;
    }

    public String getGainedAccess() {
        return gainedAccess;
    }

    public void setGainedAccess(String gainedAccess) {
        this.gainedAccess = gainedAccess;
    }

    public String getReferenceLink() {
        return referenceLink;
    }

    public void setReferenceLink(String referenceLink) {
        this.referenceLink = referenceLink;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getPatch() {
        return patch;
    }

    public void setPatch(String patch) {
        this.patch = patch;
    }

    public String getScanName() {
        return scanName;
    }

    public void setScanName(String scanName) {
        this.scanName = scanName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getPortId() {
        return portId;
    }

    public void setPortId(Integer portId) {
        this.portId = portId;
    }

    public String getPortProtocol() {
        return portProtocol;
    }

    public void setPortProtocol(String portProtocol) {
        this.portProtocol = portProtocol;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
