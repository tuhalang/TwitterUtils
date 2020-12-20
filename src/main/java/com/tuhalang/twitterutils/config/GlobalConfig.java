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

    public static Long SLEEP_TIME;

    public static String API_RULES;

    public static String API_STREAM;

    public static Boolean IS_KAFKA;
    public static String BOOTSTRAP_SERVERS_CONFIG;
    public static String KEY_SERIALIZER_CLASS_CONFIG;
    public static String VALUE_SERIALIZER_CLASS_CONFIG;
    public static String TOPIC_NAME;
    
    
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

            API_RULES = pros.getProperty("API_RULES");
            API_STREAM = pros.getProperty("API_STREAM");

            SLEEP_TIME = Long.parseLong(pros.getProperty("SLEEP_TIME", "10000"));

            IS_KAFKA = Boolean.valueOf(pros.getProperty("IS_KAFKA", "false"));
            BOOTSTRAP_SERVERS_CONFIG = pros.getProperty("BOOTSTRAP_SERVERS_CONFIG", "");
            KEY_SERIALIZER_CLASS_CONFIG = pros.getProperty("KEY_SERIALIZER_CLASS_CONFIG", "");
            VALUE_SERIALIZER_CLASS_CONFIG = pros.getProperty("VALUE_SERIALIZER_CLASS_CONFIG", "");
            TOPIC_NAME = pros.getProperty("TOPIC_NAME", "");

        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }
    
}
