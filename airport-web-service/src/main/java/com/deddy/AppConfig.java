package com.deddy;

import com.deddy.model.AirportDocument;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@Configuration
class AppConfig {

    @Value("${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value("${kafka.clientId}")
    private String clientId;

    @Value("${kafka.groupId}")
    private String groupId;

    @Value("${kafka.topic}")
    private String topic;

    @Bean
    KafkaReceiver<Integer, AirportDocument> kafkaReceiver(){
        //String bootstrapAddress = "localhost:9092";

        Map<String, Object> props = new HashMap<>();
        props.put( ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put( ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        props.put( ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put( ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put( JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        return KafkaReceiver.create(ReceiverOptions.<Integer, AirportDocument>create(props).subscription(Collections.singleton(topic)));
    }
}
