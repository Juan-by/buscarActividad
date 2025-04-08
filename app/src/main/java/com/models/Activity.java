package com.models;

import java.util.Date;

public class Activity {

    private String title;
    private String description;
    private Date date;
    private String location;
    private String status;

    public Activity(String title, String description, Date date, String location, String status) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.location = location;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getStatus() {
        return status;
    }
}
