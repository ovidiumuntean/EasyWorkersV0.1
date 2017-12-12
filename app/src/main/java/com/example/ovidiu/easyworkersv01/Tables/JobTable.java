package com.example.ovidiu.easyworkersv01.Tables;

import android.content.ContentValues;

import com.example.ovidiu.easyworkersv01.Entity.Job;
import com.example.ovidiu.easyworkersv01.Util.UsefullyFunctions;

/**
 * Created by ovidiu on 01/12/2017.
 */

public class JobTable {

    private static final String TABLE_NAME = "Job";
    private static final String COL_ID = "ID";
    private static final String COL_TITLE = "TITLE";
    private static final String COL_DESCRIPTION = "DESCRIPTION";
    private static final String COL_TYPE = "TYPE";
    private static final String COL_EXPERIENCE = "EXPERIENCE";
   // private static final String COL_JOBSCREATED= "JOBSCREATED";
    private static final String COL_CATEGORY= "CATEGORY";
    private static final String COL_COMPANY_ID = "COMPANY_ID";


    public JobTable() {
    }

    public String createTableQuery() {
        String createJobTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                COL_TITLE + " TEXT NOT NULL, " +
                COL_DESCRIPTION + " TEXT NOT NULL, " +
                COL_TYPE + " TEXT NOT NULL, " +
                COL_EXPERIENCE + " LONG, " +
           //     COL_JOBSCREATED + " DATETIME, " +
                COL_CATEGORY + " TEXT NOT NULL, " +
                COL_COMPANY_ID + " INTEGER, " + " FOREIGN KEY(COMPANY_ID) REFERENCES company(id) )";
        return createJobTable;
    }

    public ContentValues createValues(Job job) {
        UsefullyFunctions util = new UsefullyFunctions();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, job.getTitle());
        values.put(COL_DESCRIPTION, job.getDescription());
        values.put(COL_TYPE, job.getType());
        values.put(COL_EXPERIENCE, job.getExperience());
        values.put(COL_CATEGORY, job.getCategory());
        values.put(COL_COMPANY_ID, job.getCompany().getId());
        return values;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getColId() {
        return COL_ID;
    }

    public static String getColCompanyId() {
        return COL_COMPANY_ID;
    }

    public static String getColDescription() {
        return COL_DESCRIPTION;
    }

    public static String getColExperience() {
        return COL_EXPERIENCE;
    }

   // public static String getColJobscreated() {
      //  return COL_JOBSCREATED;
    //}

    public static String getColTitle() {
        return COL_TITLE;
    }

    public static String getColType() {
        return COL_TYPE;
    }

    public static String getColCategory() {
        return COL_CATEGORY;
    }
}
