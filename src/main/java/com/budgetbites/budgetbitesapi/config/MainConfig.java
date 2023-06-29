package com.budgetbites.budgetbitesapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;
import java.time.LocalDate;
import java.util.Date;

@Configuration
public class MainConfig {

    @Bean
    public HttpClient getHttpClient() {
        return HttpClient.newHttpClient();
    }
}
