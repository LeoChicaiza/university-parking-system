package com.university.parking.exit.logging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class LoggingConfig {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.apikey}")
    private String supabaseApiKey;

    @Value("${spring.application.name}")
    private String serviceName;

    @Bean
    public SupabaseLogClient supabaseLogClient() {
        return new SupabaseLogClient(
                supabaseUrl,
                supabaseApiKey,
                serviceName
        );
    }
}

