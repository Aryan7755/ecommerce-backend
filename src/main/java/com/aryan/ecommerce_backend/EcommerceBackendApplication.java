package com.aryan.ecommerce_backend;

import com.aryan.ecommerce_backend.security.jwt.JwtProperties;
import com.aryan.ecommerce_backend.user.entity.Role;
import com.aryan.ecommerce_backend.user.entity.User;
import com.aryan.ecommerce_backend.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableConfigurationProperties(JwtProperties.class)
@SpringBootApplication
public class EcommerceBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceBackendApplication.class, args);
	}
	@Bean
	CommandLineRunner testRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			User user = User.builder()
					.name("Test User")
					.email("test@example.com")
					.password(passwordEncoder.encode("plainPassword123"))
					.role(Role.USER)
					.build();
			userRepository.save(user);
			System.out.println("Saved user with hashed password: " + user.getPassword());
		};
	}

}
