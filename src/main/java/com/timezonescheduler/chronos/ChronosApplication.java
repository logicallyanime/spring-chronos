package com.timezonescheduler.chronos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ChronosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChronosApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(UserRepo repo)	{
		return args -> {
			User user = new User("Bobby Saget","bobsaget@fakemail.com");
			repo.insert(user);
		};

	}

}
