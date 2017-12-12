package com.example.ovidiu.easyworkersv01.Tables;

import android.content.ContentValues;

import com.example.ovidiu.easyworkersv01.Entity.Job;
import com.example.ovidiu.easyworkersv01.Util.UsefullyFunctions;

/**
 * Created by Ovidiu on 12/12/2017.
 */

public class JobApplicationTable {

    private static final String TABLE_NAME = "Job_Application";

    private static final String COL_JOB_ID = "JOB_ID";
    private static final String COL_EMPLOYEE_ID = "EMPLOYEE_ID";
    private static final String COL_STATUS = "STATUS";


    public JobApplicationTable() {
    }

    public String createTableQuery() {
        String createJobTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                COL_JOB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                COL_EMPLOYEE_ID + " INTEGER NOT NULL, " +
                COL_STATUS + " TEXT NOT NULL, " +
                "FOREIGN KEY(JOB_ID) REFERENCES job(id) , " +
                "FOREIGN KEY(EMPLOYEE_ID) REFERENCES employee(id) , " +
                "PRIMARY KEY (JOB_ID, EMPLOYEE_ID));";
        return createJobTable;
    }

    public ContentValues createValues(int jobId, int empId, int status) {
        ContentValues values = new ContentValues();
        values.put(COL_JOB_ID, jobId);
        values.put(COL_EMPLOYEE_ID, empId);
        values.put(COL_STATUS, status);
        return values;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getColJobId() {
        return COL_JOB_ID;
    }

    public static String getColEmployeeId() {
        return COL_EMPLOYEE_ID;
    }

    public static String getColStatus() {
        return COL_STATUS;
    }
}
