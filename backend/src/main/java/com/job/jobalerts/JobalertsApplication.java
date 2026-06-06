package com.job.jobalerts;

import com.job.jobalerts.config.EnvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling  // Remove @EnableFeignClients for now
public class JobalertsApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(JobalertsApplication.class);

		// Add our custom environment configuration
		app.addInitializers(new EnvConfig());

		// Run the application
		app.run(args);
	}
}