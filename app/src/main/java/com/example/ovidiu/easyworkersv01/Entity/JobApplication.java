package com.example.ovidiu.easyworkersv01.Entity;

/**
 * Created by Ovidiu on 12/12/2017.
 */

public class JobApplication {
    private Job job;
    private Employee employee;
    private int status;

    public JobApplication() {
    }

    public JobApplication(Job job, Employee employee, int status) {
        this.job = job;
        this.employee = employee;
        this.status = status;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
