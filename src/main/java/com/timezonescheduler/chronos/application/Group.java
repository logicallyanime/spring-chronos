package com.timezonescheduler.chronos.application;

import java.util.ArrayList;

public class Group {
    private String _id;
    private String name;
    private ArrayList<User> userList = new ArrayList<User>();
    private String meeting = null;
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
