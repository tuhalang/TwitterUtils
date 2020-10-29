/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuhalang.twitterutils.db;

import com.tuhalang.twitterutils.config.GlobalConfig;
import com.tuhalang.twitterutils.model.Tweet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author hungpv
 */
public class InsertDBProcess implements Runnable{
    private static final Logger LOGGER = Logger.getLogger(InsertDBProcess.class);
    private int stepSleep = 0;
    private final String name;
    private int indexThread = -1;
    private static final long DEFAULT_SLEEP = 5000;
    private static final String INSERT_SQL = "insert into tweet(id, text, tags, created_at) values (?,?,?,?)";

    public InsertDBProcess(int indexThread) {
        this.indexThread = indexThread;
        this.name = ("QueryDBQueue-" + indexThread);
    }

    public void start() {
        Thread t = new Thread(this);
        t.setName(this.name + "_" + indexThread);
//        t.setDaemon(true);
        t.start();
    }

    private String getName() {
        return this.name;
    }

    @Override
    public void run() {
        while (true) {
            try {
                List<Tweet> datas = InsertDBQueue.getInstance().dequeue();
                if (datas == null) {
                    Thread.sleep(DEFAULT_SLEEP);
                    continue;
                }

                process(datas);
           
                int length = datas.size();
                LOGGER.info("QueryDBQueue processed bacth queue = " + length);
                datas.clear();

                long sleep = 1000 * GlobalConfig.BATCH_SIZE / length; //m s
                if (sleep > DEFAULT_SLEEP) {
                    sleep = DEFAULT_SLEEP;
                }
                Thread.sleep(sleep);
                
            } catch (Exception ex) {
                LOGGER.error(getName() + " error when excute: ", ex);
            }
        }
    }

    private void process(List<Tweet> datas) {
        LOGGER.info(getName() + " - start process");
        long startTime = System.currentTimeMillis();
        
        Connection conn = null;
        PreparedStatement preStatement = null;
        
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);
            
            preStatement = conn.prepareCall(INSERT_SQL);
            preStatement.setQueryTimeout(10);
            
            for(Tweet tweet : datas){
                preStatement.setString(1, tweet.getId());
                preStatement.setString(2, tweet.getText());
                preStatement.setString(3, tweet.getTags());
                preStatement.setDate(4, new java.sql.Date(tweet.getCreatedAt().getTime()));
                
                preStatement.addBatch();
            }
            
            preStatement.executeBatch();
            
            conn.commit();
            
        } catch (SQLException ex) {
            LOGGER.error(ex.getMessage(), ex);
            InsertDBQueue.getInstance().enqueue(datas);
        } finally{
            try {
                preStatement.close();
            } catch (SQLException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
            try {
                conn.close();
            } catch (SQLException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        }
        // TODO insert db 
        long endTime = System.currentTimeMillis();
        LOGGER.info(getName() + " - end process ("+(endTime-startTime)+" ms)" );
    }
}
