package com.timezonescheduler.chronos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepo extends MongoRepository<Group, String> {
}