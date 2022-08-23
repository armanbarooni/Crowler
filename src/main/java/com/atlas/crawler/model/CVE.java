/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CVE {

    @NotNull(message = "Package Name is Required")
    @Size(min = 2, message = "طول بخش Package Name خیلی کوتاه است")
    private String packageName;

   @NotNull(message = "Distribution is Required")
    private String distribution;
    private String createdDate;
    private String platform;

    @NotNull(message = "CVE Name is Required")
    @Size(min = 4, message = "طول بخش CVE Name خیلی کوتاه است")
    private String cveName;

    @NotNull(message = "Publish Date is Required")
    @Size(min = 6,max = 8, message = "طول بخش Publish Date صحیح نیست")
    private String publishDate;

    @NotNull(message = "Last Modified Date is Required")
    @Size(min = 6,max = 8, message = "طول بخش Last Modified Date صحیح نیست")
    private String lastModifiedDate;

    private String description;
    private String type;
    private String attackVector;

    private String attackComplexity;

    @NotNull(message = "Cvss is Required")
    @Size(min = 1,max = 10, message = "تعداد ارقام بخش Cvss می تواند حداقل 1 و حداکثر 10 رقم باشد")
    private String cvss;

    @NotNull(message = "Cvss Detail is Required")
    @Size(min = 2, message = "طول بخش Cvss Details خیلی کوتاه است")
    private String cvssText;

    private String authentication;
    private String typeEffect;
    private String integrityEffect;

    private String confidentialityEffect;
    private String availabilityEffect;
    private String privilegesRequires;
    private String  CWE;
    private String gainedAccess;
    private String referenceLink;
    private String comments;

    private String patch;
    private   String Soloution;//23
    private   String Name;//24
    private   String Synopsis;//25
    private String Agent;//26
    private String Family;//27
    @NotNull(message = "Product Name is Required")
    @Size(min = 4, message = "طول بخش products خیلی کوتاه است")
    private String product;

    private String version;

    public CVE(){

    }

    public CVE(String  name,String Soloution,String synopsisy ,String agent, String family, String packageName, String distribution, String createdDate, String platform, String cveName, String publishDate, String lastModifiedDate, String description, String type, String attackVector, String attackComplexity, String cvss, String cvssText, String authentication, String typeEffect, String integrityEffect, String confidentialityEffect, String availabilityEffect, String privilegesRequires, String gainedAccess, String referenceLink, String comments, String patch , String cwe) {
        this.CWE=cwe;
        this.Name=name;
        this.Soloution=Soloution;
        this.Synopsis=synopsisy;
        this.Agent=agent;
        this.Family=family;
        this.packageName = packageName;
        this.distribution = distribution;
        this.createdDate = createdDate;
        this.platform = platform;
        this.cveName = cveName;
        this.publishDate = publishDate;
        this.lastModifiedDate = lastModifiedDate;
        this.description = description;
        this.type = type;
        this.attackVector = attackVector;
        this.attackComplexity = attackComplexity;
        this.cvss = cvss;
        this.cvssText = cvssText;
        this.authentication = authentication;
        this.typeEffect = typeEffect;
        this.integrityEffect = integrityEffect;
        this.confidentialityEffect = confidentialityEffect;
        this.availabilityEffect = availabilityEffect;
        this.privilegesRequires = privilegesRequires;
        this.gainedAccess = gainedAccess;
        this.referenceLink = referenceLink;
        this.comments = comments;
        this.patch = patch;
    }

    public String getPackageName() {
        return packageName;
    }
    public String getCWE() {
        return CWE;
    }
    public String getName() {
        return Name;
    }
    public String getProducts_Affected() {
        return Soloution;
    }
    public String getAgent() {
        return Agent;
    }
    public String getFamily() {
        return Family;
    }
    public String getSynopsis() {
        return Synopsis;
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
    public void setCWE(String CWE) {
        this.CWE = CWE;
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

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
    public void setAgent(String Agent) {
        this.Agent = Agent;
    }
    public void setFamily(String family) {
        this.Family = family;
    }
    public void setsoloution(String souloutin) {
        this.Soloution= souloutin;
    }
    public void setSynopsis(String synopsis) {
        this.Synopsis= synopsis;
    }
    public void setTitle(String title) {
        this.Name= title;
    }
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
