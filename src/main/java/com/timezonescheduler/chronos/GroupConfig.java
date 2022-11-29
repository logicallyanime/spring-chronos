package com.timezonescheduler.chronos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Configuration
public class GroupConfig {

    @Bean
    CommandLineRunner commandLineRunner( GroupRepo groupRepo, UserRepo userRepo ){
        return args -> {
//            Group exampleGroup = new Group(
//                    "Example Group",
//                    new User("Patrick Schmitt", "pschmitt@oswego.edu")
//            );
//            Group prototypeGroup = new Group(
//                    "Prototype Group",
//                    new User("Batrick Bmitt", "bbmitt@oswego.edu")
//            );
//
//            repo.saveAll(
//                    List.of(exampleGroup, prototypeGroup)
//            );
            User bob = new User(
                    "bob saget",
                    "bobthesaget@gmail.com"
            );
            User mary = new User(
                    "mary rachet",
                    "marytherachet@gmail.com"
            );
            User billie = new User(
                    "billie eyelash",
                    "billie@eyelash.com"
            );
            User karen = new User(
                    "karen smith",
                    "LetMeSpeakToYourManager@pr1v1leged.com"
            );
            User ari = new User(
                    "arianne venti",
                    "eeeeeeeeeeee@yuh.com"
            );



            Group yuh = new Group(
                    "~yuh~ The Musical",
                    ari
            );
            userRepo.save(ari);
            groupRepo.save(yuh);

            User[] userlist = new User[] {bob, mary, billie, karen, ari};

            for (User u:
                 userlist) {
                yuh.addUser(u);
                u.addGroup(yuh);
                userRepo.save(u);
            }


        };
    }
}
