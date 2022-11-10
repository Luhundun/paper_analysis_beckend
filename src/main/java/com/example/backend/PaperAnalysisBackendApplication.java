package com.example.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.example.backend.mapper")
public class PaperAnalysisBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaperAnalysisBackendApplication.class, args);
    }

}
