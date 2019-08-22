package org.finra.schemamatch.application;

import org.finra.schemamatch.security.CredStashStringConverter;
import org.finra.schemamatch.security.CredentialService;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

@SpringBootApplication
public class SchemaMatchApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchemaMatchApiApplication.class, args);
	}


	@Bean(name = "conversionService")
	public ConversionService conversionService(CredentialService credentialService, StringEncryptor encryptor) {
		DefaultConversionService service = new DefaultConversionService();
		service.addConverter(new CredStashStringConverter(credentialService, encryptor));
		return service;
	}
}
