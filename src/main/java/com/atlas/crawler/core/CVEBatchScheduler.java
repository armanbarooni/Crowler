package com.atlas.crawler.core;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Component
public class CVEBatchScheduler extends  Thread{

    private final General general;
    private Connection connection;

    public CVEBatchScheduler(Connection connection) {
        this.connection = connection;
        general = new General(connection);
    }
    @Scheduled(fixedRate = 30000)
    public void insertBatchCVEs() {
        try {
            general.bulkInsertCVEs(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
