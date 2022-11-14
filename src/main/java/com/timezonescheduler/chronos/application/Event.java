package com.timezonescheduler.chronos.application;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;

public class Event {
    private DateTimeFormat start;
    private DateTimeFormat end;
    private String location;
    private ArrayList<User> attendees;

    public Event (DateTimeFormat s, DateTimeFormat e, String l) {
        start = s;
        end = e;
        location = l;
        attendees = null;
    }

    public DateTimeFormat getStart () {
        return start;
    }

    public DateTimeFormat getEnd () {
        return end;
    }

    public String getLocation(){
        return location;
    }

    public void setStart (DateTimeFormat newStart){
        start = newStart;
    }

    public void setEnd (DateTimeFormat newEnd) {
        end = newEnd;
    }

    public void setLocation (String newLocation) {
        location = newLocation;
    }
}
