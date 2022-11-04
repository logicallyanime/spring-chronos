package com.timezonescheduler.chronos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
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

    public void removeGroup(Long groupId){
        boolean exists = groupRepo.existsById(String.valueOf(groupId));
        if(!exists){
            throw new IllegalStateException("group with id " + groupId + " does not exist.");
        }
        groupRepo.deleteById(String.valueOf(groupId));
    }

    @Transactional
    public void updateGroup(long groupId, String name, ArrayList<User> userList){
        Group group = groupRepo.findById(String.valueOf(groupId)).orElseThrow(() -> new IllegalStateException(
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
