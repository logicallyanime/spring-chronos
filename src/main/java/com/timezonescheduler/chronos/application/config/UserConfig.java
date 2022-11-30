package com.timezonescheduler.chronos.application.config;

import com.timezonescheduler.chronos.application.model.User;
import com.timezonescheduler.chronos.application.repo.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner commandLineRunner (UserRepo userRepo) {
        return args -> {
            User bob = new User(
                    "bob saget",
                    "bobthesaget@gmail.com"
            );

            User mary = new User(
                    "mary rachet",
                    "marytherachet@gmail.com"
            );
        };
    }
}
