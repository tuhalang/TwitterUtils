/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuhalang.twitterutils.process;

import com.tuhalang.twitterutils.config.GlobalConfig;
import com.tuhalang.twitterutils.thread.RealTimeStream;
import com.tuhalang.twitterutils.thread.RecentSearch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

        List<Thread> threads = new ArrayList<>();
//        for(int i=0 ; i< tags.length; i++){
//            RecentSearch recentSearch = new RecentSearch(
//                    tags[i], GlobalConfig.FROM_DATE, GlobalConfig.MAX_RESULTS);
//            Thread thread = new Thread(recentSearch);
//            threads.add(thread);
//        }
        Thread thread = new Thread(new RealTimeStream());
        threads.add(thread);
        for(Thread th : threads){
            th.start();
        }
    }
}
