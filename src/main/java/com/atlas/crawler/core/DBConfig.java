package com.atlas.crawler.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class DBConfig {

    private final DataSource dataSource;

    public DBConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public Connection connection() throws SQLException {
        return dataSource.getConnection();
    }
}
