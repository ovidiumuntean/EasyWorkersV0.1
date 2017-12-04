package com.example.ovidiu.easyworkersv01.Tables;

import android.content.ContentValues;

import com.example.ovidiu.easyworkersv01.Entity.Company;

/**
 * Created by anamali on 01/12/2017.
 */

public class CompanyTable {


    private static final String TABLE_NAME = "Company";
    private static final String COL_ID = "ID";
    private static final String COL_REGNUM = "REG_NUM";
    private static final String COL_NAME = "NAME";
    private static final String COL_EMAIL = "EMAIL";
    private static final String COL_PASSWORD = "PASSWORD";
    private static final String COL_PHONE_NO = "PHONE_NO";
    private static final String COL_ADDRESS = "ADDRESS";


    public String createCompanyTable() {

        String createCompanyTable = "CREATE TABLE " + TABLE_NAME + " ( " +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL" +
                COL_REGNUM + " TEXT NOT NULL, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_PHONE_NO + " NUMBER NOT NULL UNIQUE," +
                COL_ADDRESS + " TEXT," +
                COL_PASSWORD + " TEXT NOT NULL," +
                COL_EMAIL + " TEXT NOT NULL UNIQUE)";


        return createCompanyTable;


    }

    public ContentValues addCompany(Company company) { // can haveparameters if needed
        ContentValues values = new ContentValues();
        values.put(COL_REGNUM, company.getRegNum());
        values.put(COL_NAME, company.getName());
        values.put(COL_PASSWORD, company.getPassword());
        values.put(COL_ADDRESS, company.getAddress());
        values.put(COL_PHONE_NO, company.getPhoneNum());
        values.put(COL_EMAIL, company.getEmail());

        return values;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getColId() {
        return COL_ID;
    }

    public static String getColName() {
        return COL_NAME;
    }

    public static String getColRegnum() {
        return COL_REGNUM;
    }


    public static String getColAddress() {
        return COL_ADDRESS;
    }

    public static String getColPhoneNo() {
        return COL_PHONE_NO;
    }

    public static String getColStatus() {
        return COL_PASSWORD;
    }

    public static String getColEmail() {
        return COL_EMAIL;
    }
}
