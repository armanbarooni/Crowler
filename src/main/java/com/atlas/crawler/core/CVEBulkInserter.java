package com.atlas.crawler.core;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
@Component

public class CVEBulkInserter extends  Thread{

    public static void insert(Connection connection, CVE obj) throws SQLException {
        int min = Math.min(obj.PackageName.size(),
                Math.min(obj.Product_name.size(), obj.Product_version.size()));



        for (int i = 0; i < min; i++) {
            if (obj.Product_name.size() == 0 || obj.Product_version.size() == 0 || obj.PackageName.size() == 0) {
                return;
            }

            String p_name = obj.PackageName.get(i);
            List<String> products = obj.Product_name.get(i);
            List<String> versions = obj.Product_version.get(i);

            int vulnId = insertOrUpdateVuln(connection, obj, p_name);
            insertProducts(connection, products, versions, vulnId);
        }
    }

    private static int insertOrUpdateVuln(Connection connection, CVE obj, String packageName) throws SQLException {
        String query = "INSERT INTO vulns (CWE, package, cve, reported_at, updated_at, description, patch, "
                + "attack_vector, access_complexity, cvss , authentication, impact_type, integrity_impact, confidentiality_impact,"
                + " availability_impact, privileges_require, reference)"
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
                + " ON CONFLICT (package,cve) DO UPDATE SET "
                + " CWE=excluded.CWE, package=excluded.package, cve=excluded.cve, reported_at=excluded.reported_at, "
                + " description=excluded.description, patch=excluded.patch, attack_vector=excluded.attack_vector, "
                + " access_complexity=excluded.access_complexity, cvss=excluded.cvss, authentication=excluded.authentication, "
                + " impact_type=excluded.impact_type, integrity_impact=excluded.integrity_impact, "
                + " confidentiality_impact=excluded.confidentiality_impact, availability_impact=excluded.availability_impact, "
                + " privileges_require=excluded.privileges_require, reference=excluded.reference, "
                + " updated_at=excluded.updated_at RETURNING id";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            int idx = 1;
            stmt.setString(idx++, obj.CWE);
            stmt.setString(idx++, packageName);
            stmt.setString(idx++, obj.CVEName);
            stmt.setLong(idx++, obj.BroadcastDate.getTime() / 1000);
            stmt.setLong(idx++, obj.LastUpdate.getTime() / 1000);
            stmt.setString(idx++, obj.Desc);
            stmt.setString(idx++, ArrayToString(obj.PatchLink));
            stmt.setString(idx++, obj.AttackVector);
            stmt.setString(idx++, obj.AccessComplexity);
            stmt.setDouble(idx++, Double.parseDouble(obj.CVSS));
            stmt.setString(idx++, obj.Authentication);
            stmt.setString(idx++, obj.ImpactType);
            stmt.setString(idx++, obj.IntegrityImpact);
            stmt.setString(idx++, obj.ConfidentialityImpact);
            stmt.setString(idx++, obj.AvailibilityImpact);
            stmt.setString(idx++, obj.PrevilagesRequired);
            stmt.setString(idx++, ArrayToString(obj.RefferencesLinks));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }

        return -1;
    }

    private static void insertProducts(Connection connection, List<String> names, List<String> versions, int vulnId) throws SQLException {
        if (vulnId == -1) return;

        StringBuilder sb = new StringBuilder("INSERT INTO product (product_name, version, vulns_id) VALUES ");
        for (int i = 0; i < names.size(); i++) {
            sb.append("('").append(names.get(i).replace("'", "''")).append("', '")
                    .append(versions.get(i).replace("'", "''")).append("', ")
                    .append(vulnId).append("),");
        }
        sb.setLength(sb.length() - 1); // remove last comma
        sb.append(" ON CONFLICT (product_name,version,vulns_id) DO NOTHING");

        try (PreparedStatement stmt = connection.prepareStatement(sb.toString())) {
            stmt.executeUpdate();
        }
    }

    private static String ArrayToString(List<String> list) {
        return String.join(",", list);
    }
}
