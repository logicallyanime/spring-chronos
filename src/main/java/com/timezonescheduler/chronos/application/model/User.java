package com.timezonescheduler.chronos.application.model;

import com.google.api.services.calendar.model.Calendar;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.*;

import java.util.ArrayList;

@Data
@Document
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String _id;
    private String name;
    private String email;
    private double timezone; //UTC -12 =< t >= +14
    //Google oath ID or whatever?
    private Calendar calendar;
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

    public void setImageUrl(String imageUrl) {
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getId() {
        return this._id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
