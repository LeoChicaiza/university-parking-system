package com.university.parking.entry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class EntryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EntryServiceApplication.class, args);
    }

    /**
     * RestTemplate bean
     * Se usa para la comunicación síncrona
     * entre microservicios (Vehicle, Parking-Space, Parking-Lot)
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
