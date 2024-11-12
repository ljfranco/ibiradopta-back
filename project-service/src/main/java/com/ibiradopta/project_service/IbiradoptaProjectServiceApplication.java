package com.ibiradopta.project_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class IbiradoptaProjectServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IbiradoptaProjectServiceApplication.class, args);
	}

}
