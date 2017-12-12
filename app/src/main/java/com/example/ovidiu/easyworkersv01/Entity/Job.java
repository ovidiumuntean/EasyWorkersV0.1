package com.example.ovidiu.easyworkersv01.Entity;

import java.lang.ref.SoftReference;
import java.util.Date;

/**
 * Created by anamali on 08/12/2017.
 */

public class Job {
    private int id;
    private String title;
    private String description;
    private int type;
    private long experience;
    private String category;
    private Company company;
    private Date jobcreated;



    public Job() {
    }

    public Job(int id, String title, String description, int type, long experience, Date jobcreated, String category, Company company){
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.experience = experience;
        this.category = category;
        this.company = company;
        this.jobcreated = jobcreated;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Company getCompany() {
        return company;
    }

    public long getExperience() {
        return experience;
    }


    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public int getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setExperience(long experience) {
        this.experience = experience;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getJobcreated() {
        return jobcreated;
    }

    public void setJobcreated(Date jobcreated) {
        this.jobcreated = jobcreated;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
