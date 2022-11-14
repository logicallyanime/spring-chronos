package com.timezonescheduler.chronos.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jdo.annotations.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class GroupService {

    private final GroupRepo groupRepo;

    @Autowired
    public GroupService(GroupRepo groupRepo) {
        this.groupRepo = groupRepo;
    }

    public List<Group> getGroups(){
        return groupRepo.findAll();
    }

    public void addGroup(Group group) {
        groupRepo.save(group);
    }

    public Optional<Group> getGroup(String groupId) {
        boolean exists = groupRepo.existsById(groupId);
        if (!exists) {
            throw new IllegalStateException("user with id " + groupId + " does not exist");
        }
        return groupRepo.findById(groupId);
    }

    public void removeGroup(String groupId){
        boolean exists = groupRepo.existsById(groupId);
        if(!exists){
            throw new IllegalStateException("group with id " + groupId + " does not exist.");
        }
        groupRepo.deleteById(groupId);
    }

    @Transactional
    public void updateGroup(String groupId, String name, ArrayList<User> userList){
        Group group = groupRepo.findById(groupId).orElseThrow(() -> new IllegalStateException(
                "group with id " + groupId + " does not exist."));
        if(name != null &&
                name.length() > 0 &&
                !Objects.equals(group.getName(), name)) {
            group.setName(name);
        }

        if(userList != null &&
                !Objects.equals(group.getUserList(), userList)){
            group.setUserList(userList);
        }
    }
}
