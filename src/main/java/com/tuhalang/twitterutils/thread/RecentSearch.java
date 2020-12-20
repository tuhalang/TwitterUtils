/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuhalang.twitterutils.thread;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuhalang.twitterutils.config.GlobalConfig;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;

import com.tuhalang.twitterutils.db.InsertDBQueue;
import com.tuhalang.twitterutils.model.ResponseQuery;
import com.tuhalang.twitterutils.model.Tweet;
import com.tuhalang.twitterutils.util.ApiUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author hungpv
 */
public class RecentSearch implements Runnable{
    
    private static final Logger LOGGER = Logger.getLogger(RecentSearch.class);
    
    private final String tag;
    private final String fromDate;
    private final String maxResults;

    public RecentSearch(String tag, String fromDate, String maxResults) {
        this.tag = tag;
        this.fromDate = fromDate;
        this.maxResults = maxResults;
    }
    

    private ArrayList<NameValuePair> genParams(String tag, String fromDate, String maxResults, String nextToken){
        ArrayList<NameValuePair> queryParameters = new ArrayList<>();
        queryParameters.add(new BasicNameValuePair("query", tag));
        queryParameters.add(new BasicNameValuePair("tweet.fields", "created_at"));
        queryParameters.add(new BasicNameValuePair("start_time", fromDate));
        queryParameters.add(new BasicNameValuePair("max_results", maxResults));

        if(nextToken != null && !nextToken.equals("")){
            queryParameters.add(new BasicNameValuePair("next_token", nextToken));
        }

        return queryParameters;
    }

    @Override
    public void run() {

        String nextToken = null;

        while(true){
            try {
                ArrayList<NameValuePair> queryParameters = genParams(tag, fromDate, maxResults, nextToken);

                String response = ApiUtils.search(queryParameters);

                ObjectMapper mapper = new ObjectMapper();

                ResponseQuery responseQuery = mapper.readValue(response, ResponseQuery.class);

                Map<String, String> meta = responseQuery.getMeta();
                nextToken = meta.get("next_token");
                Tweet[] tweets = responseQuery.getData();

                for(Tweet tweet : tweets){
                    tweet.setTags(tag);
                    InsertDBQueue.getInstance().enqueue(tweet);
                }

                LOGGER.info("("+tag+")NextToken: " + nextToken);
                LOGGER.info("("+tag+")NumOfTweet:" + tweets.length);

                Thread.sleep(1000);

            } catch (URISyntaxException ex) {
                LOGGER.error(ex.getMessage(), ex);
                nextToken = null;
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'");

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -7);
                Date fromDate = cal.getTime();
                String fromDateStr = dateFormat.format(fromDate);

                GlobalConfig.FROM_DATE = fromDateStr;

                GlobalConfig.FROM_DATE = fromDateStr;
            } catch (IOException ex) {
                LOGGER.error(ex.getMessage(), ex);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'");

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -7);
                Date fromDate = cal.getTime();
                String fromDateStr = dateFormat.format(fromDate);

                GlobalConfig.FROM_DATE = fromDateStr;
            } catch (Exception ex){
                LOGGER.error(ex.getMessage(), ex);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'");

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -7);
                Date fromDate = cal.getTime();
                String fromDateStr = dateFormat.format(fromDate);

                GlobalConfig.FROM_DATE = fromDateStr;
            }
        }
    }

}
