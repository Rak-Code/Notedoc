package com.rakeshgupta.notedoc_backend;

import com.rakeshgupta.notedoc_backend.config.DotEnvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties
@EnableScheduling
public class NotedocApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(NotedocApplication.class);
		app.addInitializers(new DotEnvConfig());
		app.run(args);
	}

}
