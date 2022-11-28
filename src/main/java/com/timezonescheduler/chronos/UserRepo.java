package com.timezonescheduler.chronos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends MongoRepository<User, String> {


    //Changed ("SELECT s FROM User s WHERE s.email= ?1") to
    @Query("{'email' : ?0 }")
    Optional<User> findUserByEmail(String email);
}
