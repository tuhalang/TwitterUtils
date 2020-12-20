package com.tuhalang.twitterutils.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuhalang.twitterutils.config.GlobalConfig;
import com.tuhalang.twitterutils.model.ResponseTag;
import com.tuhalang.twitterutils.model.Tag;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ApiUtils {

    private static final Logger LOGGER = Logger.getLogger(ApiUtils.class);

    public static String search(ArrayList<NameValuePair> queryParameters) throws URISyntaxException, IOException {
        String searchResponse = null;

        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();

        URIBuilder uriBuilder = new URIBuilder(GlobalConfig.API_SEARCH_RECENT);
        uriBuilder.addParameters(queryParameters);

        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.setHeader("Authorization", String.format("Bearer %s", GlobalConfig.BEARER_TOKEN));
        httpGet.setHeader("Content-Type", "application/json");

        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (null != entity) {
            searchResponse = EntityUtils.toString(entity, "UTF-8");
        }
        return searchResponse;
    }

    public static String[] getAllRules() throws URISyntaxException, IOException {
        String[] rulesResponse = null;

        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();

        URIBuilder uriBuilder = new URIBuilder(GlobalConfig.API_RULES);

        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.setHeader("Authorization", String.format("Bearer %s", GlobalConfig.BEARER_TOKEN));
        httpGet.setHeader("Content-Type", "application/json");

        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String responseStr = null;
        if (null != entity) {
            responseStr = EntityUtils.toString(entity, "UTF-8");
        }
        LOGGER.info(responseStr);
        ObjectMapper mapper = new ObjectMapper();
        ResponseTag responseTag = mapper.readValue(responseStr, ResponseTag.class);
        rulesResponse = new String[responseTag.getData().length];
        for(int i=0; i<responseTag.getData().length; i++){
            rulesResponse[i] = responseTag.getData()[i].getId();
        }
        return rulesResponse;
    }

    public static void deleteAllRules() throws URISyntaxException, IOException {
        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();

        ObjectMapper mapper = new ObjectMapper();
        URIBuilder uriBuilder = new URIBuilder(GlobalConfig.API_RULES);
        HttpPost httpPost = new HttpPost((uriBuilder.build()));
        httpPost.setHeader("Authorization", String.format("Bearer %s", GlobalConfig.BEARER_TOKEN));
        httpPost.setHeader("Content-Type", "application/json");

        String[] ids = getAllRules();
        Map<String, Map<String, String[]>> payload = new HashMap<>();
        Map<String, String[]> mapIds = new HashMap<>();
        mapIds.put("ids", ids);
        payload.put("delete", mapIds);

        StringEntity entity = new StringEntity(mapper.writeValueAsString(payload));
        httpPost.setEntity(entity);

        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity responseEntity = response.getEntity();
        String responseStr = null;
        if (null != responseEntity) {
            responseStr = EntityUtils.toString(responseEntity, "UTF-8");
        }
        LOGGER.info(responseStr);
    }

    public static void addRules(Map<String, String>[] rules) throws URISyntaxException, IOException {
        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();

        ObjectMapper mapper = new ObjectMapper();
        URIBuilder uriBuilder = new URIBuilder(GlobalConfig.API_RULES);
        HttpPost httpPost = new HttpPost((uriBuilder.build()));
        httpPost.setHeader("Authorization", String.format("Bearer %s", GlobalConfig.BEARER_TOKEN));
        httpPost.setHeader("Content-Type", "application/json");

        Map<String, Map<String, String>[]> payload = new HashMap<>();
        payload.put("add", rules);

        StringEntity entity = new StringEntity(mapper.writeValueAsString(payload));
        httpPost.setEntity(entity);

        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity responseEntity = response.getEntity();
        String responseStr = null;
        if (null != responseEntity) {
            responseStr = EntityUtils.toString(responseEntity, "UTF-8");
        }
        LOGGER.info(responseStr);
    }

}
