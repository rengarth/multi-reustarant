package com.education.restaurantservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic orderReadyForPaymentTopic() {
        return TopicBuilder.name("order-ready-for-payment").build();
    }

    @Bean
    public NewTopic orderStatusUpdatesTopic() {
        return TopicBuilder.name("order-updates").build();
    }
}
