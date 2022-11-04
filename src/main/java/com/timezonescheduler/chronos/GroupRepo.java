package com.timezonescheduler.chronos;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupRepo extends MongoRepository<Group, String> {
}
