package com.timezonescheduler.chronos.application.repo;

import com.timezonescheduler.chronos.application.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.security.PermitAll;
import java.util.Optional;

@Repository
@PermitAll
public interface UserRepo extends MongoRepository<User, String> {

    @Query("SELECT s FROM User s WHERE s.email= ?1")
    Optional<User> findUserByEmail(String email);

    boolean existsByEmail(String email);
}
