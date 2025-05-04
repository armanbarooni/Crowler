package com.atlas.crawler.core;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CVEInsertScheduler {
    private final Connection connection;

    public CVEInsertScheduler(Connection connection) {
        this.connection = connection;
    }

    public void bulkInsertCVEs() {
        try {
            List<CVE> toInsert = new ArrayList<>();
            CveQueueManager.cveQueue.drainTo(toInsert);

            if (!toInsert.isEmpty()) {
                System.out.println("⏳ Inserting " + toInsert.size() + " CVEs to DB...");

                int batchSize = 1000;
                for (int i = 0; i < toInsert.size(); i += batchSize) {
                    int end = Math.min(i + batchSize, toInsert.size());
                    List<CVE> batch = toInsert.subList(i, end);

                    connection.setAutoCommit(false);
                    for (CVE cve : batch) {
                        CVEBulkInserter.insert(connection, cve);
                    }
                    connection.commit();
                    System.out.println("✅ Batch committed: " + batch.size());
                }

                System.out.println("✅ All " + toInsert.size() + " CVEs inserted.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("⚠️ Failed to reset autoCommit: " + e.getMessage());
            }
        }
    }
}
