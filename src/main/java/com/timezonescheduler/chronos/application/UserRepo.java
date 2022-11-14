package com.timezonescheduler.chronos.application;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends MongoRepository<User, String> {

    @Query("SELECT s FROM User s WHERE s.email= ?1")
    Optional<User> findUserByEmail(String email);
}
