package com.ecommerce.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//@EnableJpaRepositories("com.ecommerce.application.model.persistence.repositories")
//@EntityScan("com.ecommerce.application.model.persistence")
@SpringBootApplication
public class EcommerceApplication {

	private static final Logger log = LogManager.getLogger(EcommerceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
		log.info("eCommerce Application Started");
	}
}
