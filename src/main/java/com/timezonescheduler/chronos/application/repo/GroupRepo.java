package com.timezonescheduler.chronos.application.repo;

import com.timezonescheduler.chronos.application.model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepo extends MongoRepository<Group, String> {
}
