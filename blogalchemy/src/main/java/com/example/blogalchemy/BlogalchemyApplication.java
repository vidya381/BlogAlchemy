package com.example.blogalchemy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
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
