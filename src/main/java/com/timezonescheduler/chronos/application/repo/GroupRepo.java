package com.timezonescheduler.chronos.application.repo;

import com.timezonescheduler.chronos.application.model.Group;
import com.timezonescheduler.chronos.application.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepo extends MongoRepository<Group, String> {
    Optional<Group[]> findAllByUserListContains(User user);

    boolean existsById(String id);
}
