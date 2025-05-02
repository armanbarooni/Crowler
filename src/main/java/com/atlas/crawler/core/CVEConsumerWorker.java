package com.atlas.crawler.core;

import com.atlas.crawler.core.CVE;
import com.atlas.crawler.core.General;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
@Component

public class CVEConsumerWorker extends  Thread implements Runnable {

    private General general;
    private Connection connection;

    public CVEConsumerWorker(Connection connection) {
        this.connection = connection;
        general = new General(connection);
    }



    @Override
    public void run() {
        while (true) {
            List<CVE> buffer = new ArrayList<>();
            int batchSize = 100; // اندازه هر batch
            BlockingQueue<CVE> queue = CveQueueManager.cveQueue;

            while (!queue.isEmpty() && buffer.size() < batchSize) {
                CVE cve = queue.poll();
                if (cve != null) {
                    buffer.add(cve);
                }
            }

            if (!buffer.isEmpty()) {
                try {
                    connection.setAutoCommit(false);
                    for (CVE cve : buffer) {
                        CVEBulkInserter.insert(connection, cve);
                    }
                    connection.commit();
                } catch (Exception e) {
                    try {
                        connection.rollback();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
