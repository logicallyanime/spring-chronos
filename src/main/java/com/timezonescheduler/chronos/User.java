package com.timezonescheduler.chronos;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
//import javax.persistence.*;

import java.util.ArrayList;

@Data
@Document
public class User {
    private String _id;
    private String name;
    private String email;
    private double timezone; //UTC -12 =< t >= +14
    //Google oath ID or whatever?
    private Calendar calendar;
    private ArrayList<Group> groups;

    public User() {}

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.calendar = null;
        this.timezone = 0;
        this.groups = null;
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
}
