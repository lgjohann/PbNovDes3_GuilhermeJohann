package com.johann.msticketmanager.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {


    @Value("${mq.queue.send-email}")
    private String sendEmailQueue;


    @Bean
    public Queue queueSendEmail(){
        return new Queue(sendEmailQueue, true);
    }
}
