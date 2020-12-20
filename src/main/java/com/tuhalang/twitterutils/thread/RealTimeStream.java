package com.tuhalang.twitterutils.thread;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuhalang.twitterutils.config.GlobalConfig;
import com.tuhalang.twitterutils.db.InsertDBQueue;
import com.tuhalang.twitterutils.model.ResponseStream;
import com.tuhalang.twitterutils.model.Tweet;
import com.tuhalang.twitterutils.util.ApiUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RealTimeStream implements Runnable{

    private static final Logger LOGGER = Logger.getLogger(RealTimeStream.class);


    @Override
    public void run() {
        try {
            String[] tags = GlobalConfig.TAGS.split(";");
            HashMap<String, String>[] rules = new HashMap[tags.length];
            for(int i=0; i<tags.length; i++) {
                HashMap<String, String> rule = new HashMap<>();
                rule.put("tag", tags[i]);
                rule.put("value", String.valueOf(i));
                rules[i] = rule;
            }
            try {
                ApiUtils.deleteAllRules();
            }catch (Exception e){
                LOGGER.error(e.getMessage(), e);
            }
            try {
                ApiUtils.addRules(rules);
            }catch (Exception e){
                LOGGER.error(e.getMessage(), e);
            }
            stream();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void stream() throws IOException {
        URL url = new URL(GlobalConfig.API_STREAM);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("Authorization", String.format("Bearer %s", GlobalConfig.BEARER_TOKEN));
        urlConnection.setRequestProperty("Content-Type", "application/json");

        int responseCode = urlConnection.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String inputLine;
        ObjectMapper mapper = new ObjectMapper();
        while ((inputLine = in.readLine()) != null) {
            LOGGER.info("STREAM: " + inputLine);
            try {
                ResponseStream responseStream = mapper.readValue(inputLine, ResponseStream.class);
                Tweet tweet = responseStream.getData();
                tweet.setCreatedAt(new Date());
                tweet.setTags(responseStream.getRulesStr());
                InsertDBQueue.getInstance().enqueue(tweet);
            }catch (Exception e){
                LOGGER.error(e.getMessage(), e);
            }
        }
        in.close();
    }
}
