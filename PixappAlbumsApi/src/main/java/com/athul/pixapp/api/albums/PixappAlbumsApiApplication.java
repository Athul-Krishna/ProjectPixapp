package com.athul.pixapp.api.albums;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PixappAlbumsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PixappAlbumsApiApplication.class, args);
	}

}

