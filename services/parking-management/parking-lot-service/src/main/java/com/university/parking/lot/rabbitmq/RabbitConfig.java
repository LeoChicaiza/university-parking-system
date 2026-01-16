package com.university.parking.lot.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    
    public static final String EXCHANGE_NAME = "parking.exchange";
    public static final String QUEUE_NAME = "parking.exit.queue";
    public static final String ROUTING_KEY = "exit.event";
    
    @Bean
    public TopicExchange parkingExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }
    
    @Bean
    public Queue exitQueue() {
        return new Queue(QUEUE_NAME, true);
    }
    
    @Bean
    public Binding exitBinding() {
        return BindingBuilder
            .bind(exitQueue())
            .to(parkingExchange())
            .with(ROUTING_KEY);
    }
}