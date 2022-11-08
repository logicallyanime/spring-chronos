package com.timezonescheduler.chronos;

import java.util.ArrayList;

public class Group {
    private Long id;
    private String name;
    private ArrayList<User> userList = new ArrayList<User>();
    private String meeting = null;

    public Group (String groupName, User groupLeader) {
        name = groupName;
        userList.add(groupLeader);
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
