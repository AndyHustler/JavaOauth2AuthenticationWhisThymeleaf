package ru.greenatom.atsdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import ru.greenatom.atsdb.security.config.RsaProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaProperties.class)
public class AtsdbApplication {

	public static void main(String[] args) {
		SpringApplication.run(AtsdbApplication.class, args);
	}

}
