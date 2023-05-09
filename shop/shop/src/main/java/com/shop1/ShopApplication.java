package com.shop1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@PropertySource("classpath:config.properties")

public class ShopApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(ShopApplication.class, args);
	}
	@GetMapping(value = "/")
	public String HelloWorld() {
		return "Hello World";
	}
}
