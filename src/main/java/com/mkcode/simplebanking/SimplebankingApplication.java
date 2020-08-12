package com.mkcode.simplebanking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SimplebankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimplebankingApplication.class, args);
    }

}
