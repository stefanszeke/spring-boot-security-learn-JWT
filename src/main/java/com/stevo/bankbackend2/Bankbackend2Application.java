package com.stevo.bankbackend2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@SpringBootApplication
// @EnableWebSecurity(debug = true)
public class Bankbackend2Application {

	public static void main(String[] args) {
		SpringApplication.run(Bankbackend2Application.class, args);

    System.out.println("\n*** Bankbackend2Application started ***\n");
	}

}
