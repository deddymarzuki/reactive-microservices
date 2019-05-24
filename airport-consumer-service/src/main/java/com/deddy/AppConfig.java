package com.deddy;

import com.deddy.model.AirportDocument;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerializer;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfig {

    @Value("${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value("${kafka.clientId}")
    private String clientId;

    @Bean
    KafkaSender kafkaSender() {

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        //props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        SenderOptions<Integer, AirportDocument> senderOptions = SenderOptions.create(props);
        return KafkaSender.create(senderOptions);
    }


}