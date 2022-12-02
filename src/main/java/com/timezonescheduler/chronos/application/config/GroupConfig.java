package com.timezonescheduler.chronos.application.config;

import com.timezonescheduler.chronos.application.model.Group;
import com.timezonescheduler.chronos.application.model.User;
import com.timezonescheduler.chronos.application.repo.GroupRepo;
import com.timezonescheduler.chronos.application.repo.UserRepo;
import com.timezonescheduler.chronos.application.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class GroupConfig {

//    @Bean
//    CommandLineRunner commandLineRunner(GroupRepo repo, UserService us, UserRepo ur){
//        return args -> {
//            Group exampleGroup = new Group(
//                    "Example Group",
//                      us.getUser("6386d0b09cf2190a3a89b56a").orElseThrow()//new User("Spin Unknown", "spinnerunknown2@gmail.com")
//            );
//            Group prototypeGroup = new Group(
//                    "Prototype Group",
//                    us.getUser("6386d0b09cf2190a3a89b56a").orElseThrow()
//            );
//            User gl = prototypeGroup.getGroupLeader();
//            ArrayList<Group> ga = new ArrayList<>(List.of(exampleGroup, prototypeGroup));
//            gl.setGroups(ga);
//            ur.save(gl);
//
//            repo.saveAll(
//                    List.of(exampleGroup, prototypeGroup)
//            );
//        };
//    }
}
