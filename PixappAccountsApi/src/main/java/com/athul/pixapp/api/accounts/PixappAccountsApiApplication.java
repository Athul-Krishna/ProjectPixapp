package com.athul.pixapp.api.accounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class PixappAccountsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PixappAccountsApiApplication.class, args);
	}

}
