package com.atlas.crawler.core;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
@Component

public class CVEInsertScheduler extends  Thread implements Runnable {
    private final Connection connection;
    private General general;

    public CVEInsertScheduler(Connection connection) {
        this.connection = connection;
        general = new General(connection);

    }

    @Override
    public void run() {
        while (true) {
            try {
                List<CVE> toInsert = new ArrayList<>();

                // بیرون کشیدن همه آیتم‌ها از صف
                CveQueueManager.cveQueue.drainTo(toInsert);

                if (!toInsert.isEmpty()) {
                    System.out.println("⏳ Inserting " + toInsert.size() + " CVEs to DB...");

                    // شروع تراکنش
                    connection.setAutoCommit(false);
                    for (CVE cve : toInsert) {
                        CVEBulkInserter.insert(connection, cve);
                    }
                    connection.commit();
                    System.out.println("✅ Inserted " + toInsert.size() + " CVEs.");
                }

                // هر ۵ ثانیه چک می‌کنه
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
