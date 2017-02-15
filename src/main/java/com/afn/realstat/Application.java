package com.afn.realstat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	// private static final Logger log =
	// LoggerFactory.getLogger("app");

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

	/*
	 * TODO sample, remove eventually
	 * 
	 * @Bean public CommandLineRunner initialize(RealPropertyRepository
	 * repository) { return (args) -> { // save a couple of customers
	 * repository.save(new RealProperty("11-11-11"));
	 * log.info("Test Saving Property 11-11-11"); }; }
	 * 
	 * @Bean public CommandLineRunner initial(PropertyTransactionRepository
	 * repository) { return (args) -> { // save a couple of customers
	 * repository.save(new PropertyTransaction(0)); log.info("0"); }; }
	 */

}
