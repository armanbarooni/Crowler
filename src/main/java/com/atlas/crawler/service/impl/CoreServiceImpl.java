/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.service.impl;

import com.atlas.crawler.service.CoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class CoreServiceImpl implements CoreService {

    @Autowired
    private DataSource dataSource;

    @Override
    public Connection getConnection(){
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return conn;
    }
}
