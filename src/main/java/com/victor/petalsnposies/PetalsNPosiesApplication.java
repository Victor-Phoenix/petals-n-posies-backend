package com.victor.petalsnposies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PetalsNPosiesApplication {

	public static void main(String[] args) {
		System.out.println("DB_USERNAME=[" + System.getenv("DB_USERNAME") + "]");
		System.out.println("DB_PASSWORD length=" + System.getenv("DB_PASSWORD").length());
		SpringApplication.run(PetalsNPosiesApplication.class, args);
	}

}
