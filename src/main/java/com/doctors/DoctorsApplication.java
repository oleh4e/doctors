package com.doctors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;


@SpringBootApplication
@EnableRetry
public class DoctorsApplication {
    public static void main(String[] args) {
        SpringApplication.run(DoctorsApplication.class, args);
    }
}
