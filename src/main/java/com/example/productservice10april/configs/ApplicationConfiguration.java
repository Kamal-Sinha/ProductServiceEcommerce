package com.example.productservice10april.configs;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfiguration {

    // first object to be created to be injectable is RestTemplate

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}

