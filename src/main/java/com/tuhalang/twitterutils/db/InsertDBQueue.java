/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuhalang.twitterutils.db;

import com.tuhalang.twitterutils.config.GlobalConfig;
import com.tuhalang.twitterutils.model.Tweet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
/**
 *
 * @author hungpv
 */
public class InsertDBQueue {
    private final LinkedList<Tweet> queue = new LinkedList();
    private final Object obj = new Object();
    private static InsertDBQueue _instance = null;
    private static final Logger log = Logger.getLogger(InsertDBQueue.class);
    
    
    private static long startTime;
    private static long distanceTimeToExecute=5;

    public static synchronized InsertDBQueue getInstance() {
        if (_instance == null) {
            _instance = new InsertDBQueue();
            startTime = System.currentTimeMillis();
            for (int i = 0; i < GlobalConfig.NUM_OF_THREAD_DB; i++) {
                InsertDBProcess executor = new InsertDBProcess(i);
                executor.start();
            }
        }
        return _instance;
    }

    public void enqueue(Tweet[] tweets){
        synchronized (this.obj){
            this.queue.addAll(Arrays.asList(tweets));
            log.info("QueryDBQueue - enqueue(" + this.queue.size() + "): " + tweets);
        }
    }

    public void enqueue(List<Tweet> tweets){
        synchronized (this.obj){
            this.queue.addAll(tweets);
            log.info("QueryDBQueue - enqueue(" + this.queue.size() + "): " + tweets);
        }
    }

    public void enqueue(Tweet tweet) {
        synchronized (this.obj) {
            this.queue.add(tweet);
            log.info("QueryDBQueue - enqueue(" + this.queue.size() + "): " + tweet);
        }
    }
    
    public List<Tweet> dequeue() {
        synchronized (this.obj) {
           
            if (queue.isEmpty() || (startTime + distanceTimeToExecute) > System.currentTimeMillis()) {
                return null;
            }
            List<Tweet> datas = new ArrayList<>();
            int i = 0;
            while (queue.size() > 0 && (i < GlobalConfig.BATCH_SIZE)) {
                Tweet data = queue.poll();
                datas.add(data);
                i++;
            }
            log.info("QueryDBQueue - dequeue remain(" + this.queue.size() + ") poll: " + datas);

            return datas;
        }
        
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=======================================================\n");
        sb.append("=================").append(getClass().getSimpleName()).append("=========================\n");

        sb.append("Task in queue: \n");
        synchronized (this.obj) {
            for (Tweet command : this.queue) {
                sb.append(command.toString()).append("\n");
            }
        }
        sb.append("=======================================================\n");
        sb.append("=======================================================\n");

        return sb.toString();
    }
}
