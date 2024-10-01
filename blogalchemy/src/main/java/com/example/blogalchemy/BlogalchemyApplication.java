package com.example.blogalchemy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BlogalchemyApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogalchemyApplication.class, args);
	}

	// @Bean
	// public CommandLineRunner setupDefaultUser(UserService userService) {
	// 	return args -> {
	// 		userService.createAdminUser("admin", "admin#password");
	// 	};
	// }
}
