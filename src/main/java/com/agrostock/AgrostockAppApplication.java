package com.agrostock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.agrostock") // S'assure que tout sous com.agrostock est scanné
public class AgrostockAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(AgrostockAppApplication.class, args);
	}
}