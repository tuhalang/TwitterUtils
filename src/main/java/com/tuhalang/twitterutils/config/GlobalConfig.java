/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuhalang.twitterutils.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author hungpv
 */
public class GlobalConfig {
    
    private static final Logger LOGGER = Logger.getLogger(GlobalConfig.class); 
    
    public static int NUM_OF_THREAD_DB;
    public static int BATCH_SIZE;
    
    public static String DRIVER_CLASS_NAME;
    public static String JDBC_URL;
    public static String USERNAME;
    public static String PASSWORD;
    
    
    public static String TAGS;
    public static String FROM_DATE;
    public static String MAX_RESULTS;
    
    public static String API_SEARCH_RECENT;
    public static String BEARER_TOKEN;
    
    
    public static void loadConfig(String path){
        try {
            InputStream is = new GlobalConfig().getClass().getClassLoader().getResourceAsStream(path);
            Properties pros = new Properties();
            pros.load(is);
            
            NUM_OF_THREAD_DB = Integer.parseInt(pros.getProperty("NUM_OF_THREAD_DB"));
            BATCH_SIZE = Integer.parseInt(pros.getProperty("BATCH_SIZE"));
            
            DRIVER_CLASS_NAME = pros.getProperty("DRIVER_CLASS_NAME");
            JDBC_URL = pros.getProperty("JDBC_URL");
            USERNAME = pros.getProperty("USERNAME");
            PASSWORD = pros.getProperty("PASSWORD");
            
            TAGS = pros.getProperty("TAGS");
            MAX_RESULTS = pros.getProperty("MAX_RESULTS");
            
            API_SEARCH_RECENT = pros.getProperty("API_SEARCH_RECENT");
            BEARER_TOKEN = pros.getProperty("BEARER_TOKEN");
            
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }
    
}
