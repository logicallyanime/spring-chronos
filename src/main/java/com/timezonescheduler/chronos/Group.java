package com.timezonescheduler.chronos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "name")
public class Group {
    private String id;
    private String name;
    @DBRef
    private ArrayList<User> userList = new ArrayList<User>();
    private String meeting = null;
    @DBRef
    private User groupLeader;

    public Group (String name, User groupLeader) {
        this.name = name;
        this.groupLeader = groupLeader;
        this.userList.add(groupLeader);
    }

    public String getName () {
        return name;
    }

    public void setName (String newName) {
        name = newName;
    }

    public void addUser (User addedUser) {
        userList.add(addedUser);
    }

    public void removeUser (User removedUser) {
        //int removedUserIndex = userList.indexOf(removedUser);
        userList.remove(removedUser);
    }

    public ArrayList<User> getUserList(){ return userList; }

    public void setUserList(ArrayList<User> newUserList){
        userList = newUserList;
    }

    public void addMeeting (String date) {
        meeting = date;
    }

    public void removeMeeting () {
        meeting = null;
    }

    //Need to figure out
    /*
    public void addMeetingToCalendar () {
    }
     */
}
