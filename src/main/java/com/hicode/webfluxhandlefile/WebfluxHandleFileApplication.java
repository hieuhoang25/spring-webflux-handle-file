package com.hicode.webfluxhandlefile;

import com.hicode.webfluxhandlefile.service.FileStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WebfluxHandleFileApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebfluxHandleFileApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(FileStorageService fileStorageService){
	return args -> {fileStorageService.init();};
	}
}
