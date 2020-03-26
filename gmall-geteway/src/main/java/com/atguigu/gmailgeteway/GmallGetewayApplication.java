package com.atguigu.gmailgeteway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GmallGetewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GmallGetewayApplication.class, args);
	}

}
