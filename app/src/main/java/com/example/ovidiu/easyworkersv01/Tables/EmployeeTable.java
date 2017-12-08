package com.example.ovidiu.easyworkersv01.Tables;

import android.content.ContentValues;

import com.example.ovidiu.easyworkersv01.Entity.Employee;
import com.example.ovidiu.easyworkersv01.Util.UsefullyFunctions;

/**
 * Created by ovidiu on 01/12/2017.
 */

public class EmployeeTable {

    private static final String TABLE_NAME = "Employee";
    private static final String COL_ID = "ID";
    private static final String COL_FIRST_NAME = "FIRST_NAME";
    private static final String COL_SURNAME = "SURNAME";
    private static final String COL_BIRTHDAY = "BIRTHDAY";
    private static final String COL_GENDER = "GENDER";
    private static final String COL_ADDRESS = "ADDRESS";
    private static final String COL_PHONE_NO = "PHONE_NO";
    private static final String COL_STATUS = "STATUS";
    private static final String COL_EMAIL = "EMAIL";
    private static final String COL_PASSWORD = "PASSWORD";
    private static final String COL_COMPANY = "COMPANY_ID";
    private static final String COL_PICTURE = "PICTURE";


    public EmployeeTable() {
    }

    public String createTableQuery() {
        String createEmployeeTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                COL_FIRST_NAME + " TEXT NOT NULL, " +
                COL_SURNAME + " TEXT NOT NULL, " +
                COL_BIRTHDAY + " DATE NOT NULL, " +
                COL_GENDER + " BOOLEAN, " +
                COL_ADDRESS + " TEXT, " +
                COL_PHONE_NO + " TEXT NOT NULL UNIQUE, " +
                COL_EMAIL + " TEXT NOT NULL UNIQUE, " +
                COL_STATUS + " BOOLEAN DEFAULT 0, " +
                COL_PASSWORD + " TEXT NOT NULL , " +
                COL_PICTURE + " BLOB);";
                //COL_COMPANY + " );";
        return createEmployeeTable;
    }

    public ContentValues createValues(Employee employee) {
        UsefullyFunctions util = new UsefullyFunctions();
        ContentValues values = new ContentValues();
        values.put(COL_FIRST_NAME, employee.getFirst_name());
        values.put(COL_SURNAME, employee.getSurname());
        values.put(COL_BIRTHDAY, util.convertDateToString(employee.getBirthday(), "dd/MM/yyyy"));
        values.put(COL_GENDER, employee.getGender());
        values.put(COL_ADDRESS, employee.getAddress());
        values.put(COL_PHONE_NO, employee.getPhone_no());
        values.put(COL_EMAIL, employee.getEmail());
        values.put(COL_STATUS, employee.getStatus());
        values.put(COL_PASSWORD, employee.getPassword());
        values.put(COL_PICTURE, employee.getImage());
        return values;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getColId() {
        return COL_ID;
    }

    public static String getColFirstName() {
        return COL_FIRST_NAME;
    }

    public static String getColSurname() {
        return COL_SURNAME;
    }

    public static String getColBirthday() {
        return COL_BIRTHDAY;
    }

    public static String getColAddress() {
        return COL_ADDRESS;
    }

    public static String getColPhoneNo() {
        return COL_PHONE_NO;
    }

    public static String getColStatus() {
        return COL_STATUS;
    }

    public static String getColEmail() {
        return COL_EMAIL;
    }

    public static String getColPassword() {
        return COL_PASSWORD;
    }

    public static String getColCompany() {
        return COL_COMPANY;
    }

    public static String getColPicture() {
        return COL_PICTURE;
    }

    public static String getColGender() {
        return COL_GENDER;
    }
}
