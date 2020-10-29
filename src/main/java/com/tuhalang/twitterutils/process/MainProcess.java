/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuhalang.twitterutils.process;

import com.tuhalang.twitterutils.config.GlobalConfig;
import com.tuhalang.twitterutils.thread.RecentSearch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author hungpv
 */
public class MainProcess {
    public static void main(String[] args) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'");

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        Date fromDate = cal.getTime();
        String fromDateStr = dateFormat.format(fromDate);

        GlobalConfig.FROM_DATE = fromDateStr;

        GlobalConfig.loadConfig("config.properties");
        String[] tags = GlobalConfig.TAGS.split(";");
        for(String tag : tags){
            RecentSearch recentSearch = new RecentSearch(GlobalConfig.TAGS, GlobalConfig.FROM_DATE, GlobalConfig.MAX_RESULTS);
            Thread thread = new Thread(recentSearch);
            thread.start();
        }
    }
}
