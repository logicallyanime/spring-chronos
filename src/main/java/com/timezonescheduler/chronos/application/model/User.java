package com.timezonescheduler.chronos.application.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.api.services.calendar.Calendar;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.*;

import java.util.ArrayList;

@Data
@Document
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String name;
    private String email;
    private double timezone; //UTC -12 =< t >= +14
    //Google oath ID or whatever?
    private Calendar calendar;
    @DBRef//(db = "group")
    private ArrayList<Group> groups;
    private String googleId;
    private String providerId;
    private String password;

    public User() {}

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.calendar = null;
        this.timezone = 0;
        this.groups = new ArrayList<>();
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

    public void addGroup(Group group){
        if(groups == null){
            groups = new ArrayList<>();
        }
        groups.add(group);
    }

    public void removeGroup(Group group){
        for(int i = 0; i < groups.size(); i++){
            if(groups.get(i).equals(group.getId())){
                groups.remove(i);
                break;
            }
        }
    }
    public ArrayList<Group> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }




    public void setImageUrl(String imageUrl) {
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getId() {
        return this.id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
