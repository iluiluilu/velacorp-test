package com.linhnt.velaecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class VelaEcommerceApplication {
	final Environment env;
	public static String appName;

	public VelaEcommerceApplication(Environment env) {
		this.env = env;
		String envAppName = env.getProperty("spring.application.name");
		appName = envAppName == null ? "VELA_ECOMMERCE_API" : envAppName;
	}
	public static void main(String[] args) {
		SpringApplication.run(VelaEcommerceApplication.class, args);
	}
}
