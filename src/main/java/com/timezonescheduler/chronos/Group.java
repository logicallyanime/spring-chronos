package com.timezonescheduler.chronos;

import java.util.ArrayList;

public class Group {
    private String _id;
    private String name;
    private ArrayList<User> userList = new ArrayList<User>();
    private ArrayList<Event> eventList = new ArrayList<>();
    private Event meeting = null;
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
        for(User u : userList){
            if(addedUser.getEmail().equals(u.getEmail())){
                throw new IllegalStateException("Group with id " + _id + " already has user with email " + addedUser.getEmail());
            }
        }
        userList.add(addedUser);
    }

    public void removeUser (User removedUser) {
        //int removedUserIndex = userList.indexOf(removedUser);
        for(int i = 0; i < userList.size(); i++){
            if(userList.get(i).getEmail().equals(removedUser.getEmail())){
                userList.remove(i);
            }
        }
        if(groupLeader == null)
        System.out.println(userList.get(0).getEmail() + " ~ " + groupLeader.getEmail());
        if(!userList.get(0).getEmail().equals(groupLeader.getEmail())){
            groupLeader = userList.get(0);
        }
    }

    public ArrayList<User> getUserList(){ return userList; }

    public void setUserList(ArrayList<User> newUserList){
        userList = newUserList;
    }

    public void removeMeeting () {
        meeting = null;
    }

    public Event getMeeting(){
        return meeting;
    }

    public User getGroupLeader(){
        return groupLeader;
    }

    public void setMeeting(Event m){
        meeting = m;
    }

    public ArrayList<Event> getEventList(){
        return eventList;
    }

    public void setEventList(ArrayList<Event> el){
        eventList = el;
    }

    public void addEvents(ArrayList<Event> events){
        for(Event e : events){
            addEvent(e);
        }
    }

    public void addEvent(Event e){
        boolean unique = true;
        for(Event ev : eventList){
            if(ev.equals(e)){
                unique = false;
                break;
            }
        }
        if(unique){
            eventList.add(e);
        }
    }
    public String getId(){
        return _id;
    }

    //Need to figure out
    /*
    public void addMeetingToCalendar () {
    }
     */
}
