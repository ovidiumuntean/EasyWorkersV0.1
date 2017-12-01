package com.example.ovidiu.easyworkersv01;

import android.content.ContentValues;

/**
 * Created by Ovidiu on 01/12/2017.
 */

public class EmployeeTable {

    private static final String TABLE_NAME="Employee";
    private static final String COL_ID= "ID";
    private static final String COL_FIRST_NAME = "FIRST_NAME";
    private static final String COL_SURNAME = "SURNAME";
    private static final String COL_BIRTHDAY = "BIRTHDAY";
    private static final String COL_ADDRESS = "ADDRESS";
    private static final String COL_PHONE_NO = "PHONE_NO";
    private static final String COL_STATUS = "STATUS";
    private static final String COL_EMAIL = "EMAIL";

    public EmployeeTable() {
    }

    public String createTableQuery(){
        String createEmployeeTable = "CREATE TABLE " + TABLE_NAME + " ( " +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL" +
                COL_FIRST_NAME + " TEXT NOT NULL, " +
                COL_SURNAME + " TEXT NOT NULL, " +
                COL_BIRTHDAY + " DATE NOT NULL," +
                COL_ADDRESS + " TEXT," +
                COL_PHONE_NO + " TEXT NOT NULL" +
                COL_EMAIL + " TEXT NOT NULL, " +
                COL_STATUS + " BOOLEAN DEFAULT 1";
        return createEmployeeTable;
    }

    public ContentValues createValues(Employee employee){
        ContentValues values = new ContentValues();
        values.put(COL_FIRST_NAME, employee.getFirst_name());
        values.put(COL_SURNAME, employee.getSurname());
        values.put(COL_BIRTHDAY, employee.getBirthday().toString());
        values.put(COL_ADDRESS, employee.getAddress());
        values.put(COL_PHONE_NO, employee.getPhone_no());
        values.put(COL_EMAIL, employee.getEmail());
        values.put(COL_STATUS, employee.getStatus());
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
}
