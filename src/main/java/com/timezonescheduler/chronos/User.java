package com.timezonescheduler.chronos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
//import javax.persistence.*;

import java.util.ArrayList;

@Data
@Document
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "name")
public class User {
    private String id;
    private String name;
    private String email;
    private double timezone; //UTC -12 =< t >= +14
    //Google oath ID or whatever?
    private Calendar calendar;
    @DBRef
    private ArrayList<Group> groups;


    public User() {}

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.calendar = null;
        this.timezone = 0;
        this.groups = new ArrayList<Group>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public double getTimezone() {
        return timezone;
    }

    public void setTimezone(double timezone) {
        this.timezone = timezone;
    }
    public ArrayList<Group> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }
    public void addGroup(Group group) {
        this.groups.add(group);
    }



}
