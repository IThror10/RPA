package com.RPA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan("com.RPA.entity")
@SpringBootApplication
public class HarmonistMain {
    public static void main(String[] args) {
        SpringApplication.run(HarmonistMain.class, args);
    }
}