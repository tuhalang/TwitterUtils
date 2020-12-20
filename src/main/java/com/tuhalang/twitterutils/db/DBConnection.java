/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuhalang.twitterutils.db;

import com.tuhalang.twitterutils.config.GlobalConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 *
 * @author hungpv
 */
public class DBConnection {
    
    private static DBConnection _instance;
    private DataSource dataSource;
    private static Object lock = new Object();
    
    
    private DBConnection(){
        HikariConfig conf = new HikariConfig();
        conf.setDriverClassName(GlobalConfig.DRIVER_CLASS_NAME);
        conf.setJdbcUrl(GlobalConfig.JDBC_URL);
        conf.setUsername(GlobalConfig.USERNAME);
        conf.setPassword(GlobalConfig.PASSWORD);
        dataSource = new HikariDataSource(conf);
    }
    
    public static DBConnection getInstance(){
        if(_instance == null){
            synchronized(lock){
                if(_instance == null){
                    _instance = new DBConnection();
                }
            }
        }
        return _instance;
    }
    
    
    public Connection getConnection() throws SQLException{
        if(this.dataSource != null){
            return this.dataSource.getConnection();
        }else{
            return null;
        }
    }
}
