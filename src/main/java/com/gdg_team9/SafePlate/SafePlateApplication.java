package com.gdg_team9.SafePlate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SafePlateApplication {

	public static void main(String[] args) {
		SpringApplication.run(SafePlateApplication.class, args);
	}

}
