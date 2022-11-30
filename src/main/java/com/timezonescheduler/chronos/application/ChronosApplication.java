package com.timezonescheduler.chronos.application;

import com.timezonescheduler.chronos.application.security.AppProperties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class ChronosApplication{

    public static void main(String[] args) {
        SpringApplication.run(ChronosApplication.class, args);
    }

}
