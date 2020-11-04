package com.example.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot main class to trigger batch execution.
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        //System.exit(SpringApplication.exit(SpringApplication.run(Application.class, args)));
        SpringApplication.run(Application.class, args);
    }
}
