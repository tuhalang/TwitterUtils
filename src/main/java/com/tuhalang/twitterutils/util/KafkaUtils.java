package com.tuhalang.twitterutils.util;

import com.tuhalang.twitterutils.config.GlobalConfig;
import com.tuhalang.twitterutils.model.Tweet;
import org.apache.kafka.clients.producer.*;
import org.apache.log4j.Logger;

import java.util.Properties;

public class KafkaUtils {

    private static final Logger LOGGER = Logger.getLogger(KafkaUtils.class);

    private static Properties config() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, GlobalConfig.BOOTSTRAP_SERVERS_CONFIG);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, GlobalConfig.KEY_SERIALIZER_CLASS_CONFIG);
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GlobalConfig.VALUE_SERIALIZER_CLASS_CONFIG);
        return properties;
    }

    public static void sendTweet(Tweet tweet){
        Producer producer = new KafkaProducer(config());
        ProducerRecord<String, Tweet> record = new ProducerRecord<>(GlobalConfig.TOPIC_NAME, tweet.getId(), tweet);
        try {
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception e) {
                    LOGGER.info("Message sent to topic -> " + metadata.topic()
                            + " stored at offset -> " + metadata.offset()
                            + " stored at partition -> " + metadata.partition());
                }
            });
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
        }finally {
            producer.flush();
            producer.close();
        }
    }
}
