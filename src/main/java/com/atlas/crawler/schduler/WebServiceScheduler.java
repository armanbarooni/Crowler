/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.schduler;

import com.atlas.crawler.adapter.Adapter;
import com.atlas.crawler.service.CoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Component
public class WebServiceScheduler {

    @Autowired
    private CoreService coreService;

    private static Boolean lastStartCompleted = true;

    @Scheduled(cron = "0 0 */2 * * *",zone = "Asia/Tehran")
    public void updateCveDatabase() {

        try {
            if (lastStartCompleted && !Adapter.isStartUpdateDataBase) {
                lastStartCompleted = false;

                Connection connection = coreService.getConnection();
                Adapter adapter = new Adapter(connection);
                adapter.updateDatabase("p", "recent");

                lastStartCompleted = true;

            }
        } catch (Exception e) {
            System.err.println("We have ERROR in update Database!!!");
        }


    }
}
