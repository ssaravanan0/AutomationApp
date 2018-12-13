package com.waitrose.app.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * 
 * @author Saravanan
 *
 */
@Configuration
public class DBConfiguration {
	
	private static final Logger logger = LoggerFactory.getLogger(DBConfiguration.class);

	@Autowired
	private Environment env;
	
    @Bean
    public DataSource dataSource() {
    	String path = env.getProperty("db.path");
    	
    	logger.info("db path >>>>>>>>>>> " + path);
    	
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.sqlite.JDBC");
        dataSourceBuilder.url("jdbc:sqlite:"+path);
        return dataSourceBuilder.build();
    }

}
