package com.mystore.store;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class StoreApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		new StoreApplication().configure(new SpringApplicationBuilder(StoreApplication.class)).run(args);
	}
}
