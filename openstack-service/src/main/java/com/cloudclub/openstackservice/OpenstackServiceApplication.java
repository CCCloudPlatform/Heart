package com.cloudclub.openstackservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class OpenstackServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenstackServiceApplication.class, args);
	}

}
