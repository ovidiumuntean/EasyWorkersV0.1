package com.example.ovidiu.easyworkersv01.Entity;

/**
 * Created by Ovidiu on 12/12/2017.
 */

public class JobApplication {
    private int jobId;
    private int employeeId;
    private int status;

    public JobApplication(int jobId, int employeeId, int status) {
        this.jobId = jobId;
        this.employeeId = employeeId;
        this.status = status;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
