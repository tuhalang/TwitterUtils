package com.tuhalang.twitterutils.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

public class TweetSerializer implements Serializer<Tweet> {
    @Override
    public byte[] serialize(String s, Tweet tweet) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsBytes(tweet);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
