package com.atlas.crawler.core;

import org.apache.poi.ss.formula.functions.Na;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CVE {

    public List<String> PackageName = new ArrayList<String>();//0
    public List<String> Platforms = new ArrayList<String>();//1
    public List<String> PatchLink = new ArrayList<String>();//2
    public List<String> RefferencesLinks = new ArrayList<>();//3
    public String CVEName;//4
    public String Distro;//5
    public Date BroadcastDate;//6
    public Date LastUpdate;//7
    public String Desc;//8
    public String VulnurabiltyType;//9
    public String AttackVector;//10
    public String AccessComplexity;//11
    public String CVSS;//12
    public String  CVSS_details;
    public String Authentication;//13
    public String ImpactType;//14
    public String IntegrityImpact;//15
    public String ConfidentialityImpact;//16
    public String AvailibilityImpact;//17
    public String PrevilagesRequired;//18
    public String GainedAccess;//19
    public Date Date_Changed;//20
    public String Comments;//21
    public  String CWE;//22
    public  String Products_Affected;//23
    public  String Name;//24
    public  String Synopsis;//25
    public String Agent;//26
    public String Family;//27
    public  ArrayList<ArrayList<String>> Product_name ;
    public  ArrayList<ArrayList<String>> Product_version ;


    public CVE()
    {
        ArrayList<ArrayList<String>> Product_name = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> Product_version = new ArrayList<ArrayList<String>>();

    }
    public CVE(List<String> packageName, List<String> platforms, List<String> patchLink,
               List<String> refferencesLinks, String CVEName, String distro, Date broadcastDate,
               Date lastUpdate, String desc, String vulnurabiltyType, String attackVector,
               String accessComplexity, String CVSS,String CVSS_details, String authentication,
               String impactType, String integrityImpact, String confidentialityImpact,
               String availibilityImpact, String previlagesRequired, String gainedAccess,
               Date date_Changed ,String comments,String cwe,String products_Affected,String name,String synopsis,
               String agent,String family,ArrayList<ArrayList<String>> product_name ,
               ArrayList<ArrayList<String>> product_version ) {
        Products_Affected=products_Affected;
        Name=name;
        Synopsis=synopsis;
        Agent=agent;
        Family=family;
        CWE=cwe;
        PackageName = packageName;
        Platforms = platforms;
        PatchLink = patchLink;
        RefferencesLinks = refferencesLinks;
        this.CVEName = CVEName;
        Distro = distro;
        BroadcastDate = broadcastDate;
        LastUpdate = lastUpdate;
        Desc = desc;
        VulnurabiltyType = vulnurabiltyType;
        AttackVector = attackVector;
        AccessComplexity = accessComplexity;
        this.CVSS = CVSS;
        this.CVSS_details = CVSS_details;
        Authentication = authentication;
        ImpactType = impactType;
        IntegrityImpact = integrityImpact;
        ConfidentialityImpact = confidentialityImpact;
        AvailibilityImpact = availibilityImpact;
        PrevilagesRequired = previlagesRequired;
        GainedAccess = gainedAccess;
        Date_Changed = date_Changed;
        Comments=comments;
        Product_name=product_name;
        Product_version=product_version;
    }
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
         "null instanceof [type]" also returns false */
        if (!(o instanceof CVE)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        CVE c = (CVE) o;

        // Compare the data members and return accordingly
        return CVEName.equals(c.CVEName);

    }
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append( this.getClass().getName() );
        result.append( " Object {" );
        result.append("---");

        //determine fields declared in this class only (no fields of superclass)
        Field[] fields = this.getClass().getDeclaredFields();

        //print field names paired with their values
        for ( Field field : fields  ) {
            result.append("  ");
            try {
                result.append( field.getName() );
                result.append(": ");
                //requires access to private field:
                result.append( field.get(this) );
            } catch ( IllegalAccessException ex ) {
                System.out.println(ex);
            }
            result.append("---");
        }
        result.append("}");

        return result.toString();
    }
}