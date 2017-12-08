package com.example.ovidiu.easyworkersv01.Entity;

import java.util.Date;

/**
 * Created by Ovidiu on 08/12/2017.
 */

public class Qualification {
    private int id;
    private String serial_number;
    private String title;
    private String description;
    private Date valid_from;
    private Date valid_to;
    private long experience;
    private Employee employee;

    public Qualification() {
    }

    public Qualification(int id, String serial_number, String title, String description, Date valid_from, Date valid_to, long experience, Employee employee) {
        this.id = id;
        this.serial_number = serial_number;
        this.title = title;
        this.description = description;
        this.valid_from = valid_from;
        this.valid_to = valid_to;
        this.experience = experience;
        this.employee = employee;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getValid_from() {
        return valid_from;
    }

    public void setValid_from(Date valid_from) {
        this.valid_from = valid_from;
    }

    public Date getValid_to() {
        return valid_to;
    }

    public void setValid_to(Date valid_to) {
        this.valid_to = valid_to;
    }

    public long getExperience() {
        return experience;
    }

    public void setExperience(long experience) {
        this.experience = experience;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
