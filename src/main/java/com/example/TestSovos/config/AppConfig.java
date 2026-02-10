package com.example.TestSovos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public String ejemploBean() {
        return "beanEjemplo";
    }

}
