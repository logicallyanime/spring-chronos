package com.timezonescheduler.chronos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

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
    public void updateGroup(String groupId, String name, ArrayList<User> userList, Event meeting, ArrayList<Event> eventList){
        Group group = groupRepo.findById(groupId).orElseThrow(() -> new IllegalStateException(
                "group with id " + groupId + " does not exist."));
        boolean changed = false;
        if(name != null &&
                name.length() > 0 &&
                !Objects.equals(group.getName(), name)) {
            group.setName(name);
            changed = true;
        }

        if(userList != null &&
                !Objects.equals(group.getUserList(), userList)){
            group.setUserList(userList);
            changed = true;
        }
        if(meeting != null && !Objects.equals(group.getMeeting(), meeting)){
            group.setMeeting(meeting);
            changed = true;
        }
        if(eventList != null && !Objects.equals(group.getEventList(), eventList)){
            group.setEventList(eventList);
            changed = true;
        }
        if(changed) {
            groupRepo.save(group);
        }
    }

    public void addUserToGroup(String groupId, User user) {
        Group group = getGroup(groupId).get();
        group.addUser(user);
        user.addGroup(group);
        updateGroup(groupId, group.getName(), group.getUserList(), group.getMeeting(), group.getEventList());
    }

    public void removeUserFromGroup(String groupId, User user) {
        Group group = getGroup(groupId).get();
        group.removeUser(user);
        if(group.getUserList().size() == 0){
            removeGroup(groupId);
        }
        user.removeGroup(group);
        updateGroup(groupId, group.getName(), group.getUserList(), group.getMeeting(), group.getEventList());
    }

    public void addUserCalendar(String groupId, ArrayList<Event> eventList){
        Group group = getGroup(groupId).get();
        group.addEvents(eventList);
        updateGroup(groupId, group.getName(), group.getUserList(), group.getMeeting(), group.getEventList());
    }
}
