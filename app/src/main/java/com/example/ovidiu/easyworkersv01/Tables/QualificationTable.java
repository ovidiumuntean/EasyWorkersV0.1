package com.example.ovidiu.easyworkersv01.Tables;

import android.content.ContentValues;

import com.example.ovidiu.easyworkersv01.Entity.Qualification;
import com.example.ovidiu.easyworkersv01.Util.UsefullyFunctions;

/**
 * Created by Ovidiu on 08/12/2017.
 */

public class QualificationTable {

    private static final String TABLE_NAME = "Qualification";
    private static final String COL_ID = "ID";
    private static final String COL_SERIAL_NUMBER = "SERIAL_NUMBER";
    private static final String COL_TITLE = "TITLE";
    private static final String COL_DESCRIPTION = "DESCRIPTION";
    private static final String COL_VALID_FROM = "VALID_FROM";
    private static final String COL_VALID_TO = "VALID_TO";
    private static final String COL_EXPERIENCE = "EXPERIENCE";
    private static final String COL_EMPLOYEE_ID = "EMPLOYEE_ID";

    public QualificationTable() {
    }

    public String createTableQuery() {
        String createEmployeeTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                COL_SERIAL_NUMBER + " TEXT NOT NULL, " +
                COL_TITLE + " TEXT NOT NULL, " +
                COL_DESCRIPTION + " TEXT NOT NULL, " +
                COL_VALID_FROM + " DATE NOT NULL, " +
                COL_VALID_TO + " DATE NOT NULL, " +
                COL_EXPERIENCE + " TEXT NOT, " +
                COL_EMPLOYEE_ID + " INTEGER, " +
                "FOREIGN KEY(" + COL_EMPLOYEE_ID + ") REFERENCES Employee(ID));";
        return createEmployeeTable;
    }

    public ContentValues createInsertValues(Qualification qualification) {
        UsefullyFunctions util = new UsefullyFunctions();
        ContentValues values = new ContentValues();
        values.put(COL_SERIAL_NUMBER, qualification.getSerial_number());
        values.put(COL_TITLE, qualification.getTitle());
        values.put(COL_DESCRIPTION, qualification.getDescription());
        values.put(COL_VALID_FROM, util.convertDateToString(qualification.getValid_from(), "dd/MM/yyyy"));
        values.put(COL_VALID_TO, util.convertDateToString(qualification.getValid_to(), "dd/MM/yyyy"));
        values.put(COL_EXPERIENCE, qualification.getExperience());
        values.put(COL_EMPLOYEE_ID, qualification.getEmployee().getId());
        return values;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getColId() {
        return COL_ID;
    }

    public static String getColSerialNumber() {
        return COL_SERIAL_NUMBER;
    }

    public static String getColTitle() {
        return COL_TITLE;
    }

    public static String getColDescription() {
        return COL_DESCRIPTION;
    }

    public static String getColValidFrom() {
        return COL_VALID_FROM;
    }

    public static String getColValidTo() {
        return COL_VALID_TO;
    }

    public static String getColExperience() {
        return COL_EXPERIENCE;
    }

    public static String getColEmployeeId() {
        return COL_EMPLOYEE_ID;
    }
}
