package com.timezonescheduler.chronos.application.service;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.timezonescheduler.chronos.application.repo.UserRepo;
import com.timezonescheduler.chronos.application.util.ChronosPair;
import com.timezonescheduler.chronos.application.model.Group;
import com.timezonescheduler.chronos.application.model.User;
import com.timezonescheduler.chronos.application.repo.GroupRepo;
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
    private final UserService userService;

    @Autowired
    public GroupService(GroupRepo groupRepo, UserService userService) {

        this.groupRepo = groupRepo;
        this.userService = userService;
    }

//    public Group[] getGroups(String userid){
//        userRepo.
//        return groupRepo.findAllById(userid);
//    }

    public void addGroup(Group group) {
        groupRepo.save(group);
    }

    public Optional<Group> getGroup(String groupId) {
//        boolean exists = groupRepo.existsById(groupId);
//        if (!exists) {
//            throw new IllegalStateException("group with id " + groupId + " does not exist");
//        }
//        return groupRepo.findById(groupId);

        Group group = groupRepo.findById(groupId).orElseThrow(() -> new IllegalStateException(
                "group with id " + groupId + " does not exist."));
        return Optional.ofNullable(group);
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
                userList.size() != 0 &&
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
        //user.addGroup(group);
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

    public ArrayList<ChronosPair<Event, String>> determineMeetingTime(String groupId, String email, long meetingTime, DateTime start){
        Group group = getGroup(groupId).get();
        if(group.getGroupLeader().getEmail().equals(email)) {
            ArrayList<ChronosPair<Event, String>> meetings = new ArrayList<>();
            try {
                meetings = group.determineMeetingTime(meetingTime, start);
            }catch(Exception e){
                System.err.println("Exception when calling Determine Meeting Time function");
                return null;
            }
            System.out.println("Check 2");
            return meetings;
        }else{
            System.err.println("User does not have access to this function");
            return null;
        }
    }

    public void setMeeting(String groupId, Event meeting){
        Group group = getGroup(groupId).get();
        group.setMeeting(meeting);
        updateGroup(group.getId(), null, null, group.getMeeting(), null);
    }
}
