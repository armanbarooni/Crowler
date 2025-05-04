package com.atlas.crawler.core;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Component
public class CVEBatchScheduler {

    private final CVEInsertScheduler insertScheduler;

    public CVEBatchScheduler(CVEInsertScheduler insertScheduler) {
        this.insertScheduler = insertScheduler;
    }

    @Scheduled(fixedRate = 30000)
    public void insertBatchCVEs() {
        insertScheduler.bulkInsertCVEs();
    }
}
