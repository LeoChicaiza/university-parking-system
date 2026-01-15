package com.university.parking.space.mqtt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.messaging.MessageChannel;

@Configuration
public class MqttInboundConfig {

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MqttPahoMessageDrivenChannelAdapter mqttInbound(
            MqttConfig config) {

        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(
                        "parking-space-subscriber",
                        config.mqttClientFactory(),
                        "parking/space/+/sensor"
                );

        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }
}
