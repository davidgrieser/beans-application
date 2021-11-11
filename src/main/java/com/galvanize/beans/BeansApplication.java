package com.galvanize.beans;

import com.galvanize.beans.security.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BeansApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeansApplication.class, args);
	}

	@Bean
	public JwtProperties getJwtProperties() {
		return new JwtProperties();
	}
}
