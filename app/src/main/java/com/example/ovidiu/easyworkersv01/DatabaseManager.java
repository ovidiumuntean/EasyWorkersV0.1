package com.example.ovidiu.easyworkersv01;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ovidiu on 20/11/2017.
 */

public class DatabaseManager extends SQLiteOpenHelper {
    // define constants related to DB schema such as DB name,
    private static final String DATABASE_NAME = "EasyWorkers.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_EMPLOYEE="Employee";
    private static final String COL_ID= "ID";
    private static final String COL_FIRST_NAME = "FIRST_NAME";
    private static final String COL_SURNAME = "SURNAME";
    private static final String COL_BIRTHDAY = "BIRTHDAY";
    private static final String COL_ADDRESS = "ADDRESS";
    private static final String COL_PHONE_NO = "PHONE_NO";
    private static final String COL_STATUS = "STATUS";
    private static final String COL_EMAIL = "EMAIL";
    /*******/
    // the constructor of this class must call the super class(i.e.
    //SQLiteHelper)


    public DatabaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate( SQLiteDatabase db ) {
        // build sql create statement to create tables in DB BookDB
        String createEmployeeTable = "CREATE TABLE " + TABLE_EMPLOYEE + " ( " +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL" +
                COL_FIRST_NAME + " TEXT NOT NULL, " +
                COL_SURNAME + " TEXT NOT NULL, " +
                COL_BIRTHDAY + " DATE NOT NULL," +
                COL_ADDRESS + " TEXT," +
                COL_PHONE_NO + " TEXT NOT NULL" +
                COL_EMAIL + " TEXT NOT NULL, " +
                COL_STATUS + " BOOLEAN DEFAULT 1";
        db.execSQL(createEmployeeTable );
    }

    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
        // Drop old table if it exists and create new tables, or alter table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEE);
        onCreate(db);
    }

//    private void createEmployeeTable(){
//        String createEmployeeTable = "CREATE TABLE " + TABLE_EMPLOYEE + " ( " +
//                COL_FIRST_NAME + " TEXT NOT NULL, " +
//                COL_SURNAME + " TEXT NOT NULL, " +
//                COL_EMAIL + " TEXT NOT NULL, " +
//                "PRIMARY KEY(email) )";
//        SQLiteDatabase db = this.getWritableDatabase( );
//        db.execSQL(createEmployeeTable );
//        db.close( );
//    }

    public boolean addEmployee(Employee employee) { // can haveparameters if needed
        ContentValues values = new ContentValues();
        values.put(COL_FIRST_NAME, employee.getFirst_name());
        values.put(COL_SURNAME, employee.getSurname());
        values.put(COL_BIRTHDAY, employee.getBirthday().toString());
        values.put(COL_ADDRESS, employee.getAddress());
        values.put(COL_PHONE_NO, employee.getPhone_no());
        values.put(COL_EMAIL, employee.getEmail());
        values.put(COL_STATUS, employee.getStatus());
        SQLiteDatabase db = this.getWritableDatabase( );
        long result = db.insert(TABLE_EMPLOYEE, null, values);
        db.close( );
        if(result == -1 ){
            return false;
        } else {
            return true;
        }

    }

    //Delete a employee from the database
    public void deleteEmployee(String employeeEmail){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_EMPLOYEE + " WHERE " + COL_EMAIL + "=\'" + employeeEmail + "\';");
    }

    //Search Employee by email
    public Employee  searchEmployeeByEmail(String email){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_EMPLOYEE + " WHERE " + COL_EMAIL + "=\'" + email + "\' AND " + COL_STATUS + " = 1;";
        Employee employee = new Employee();
        Cursor c = db.rawQuery(query, null);

        if(c.getCount() > 0) {
            while (c.moveToNext()) {
                employee.setId(c.getInt(c.getColumnIndex(COL_ID)));
                employee.setFirst_name(c.getString(c.getColumnIndex(COL_FIRST_NAME)).toString());
                employee.setSurname(c.getString(c.getColumnIndex(COL_SURNAME)).toString());
                employee.setBirthday(new Date(c.getString(c.getColumnIndex(COL_EMAIL)).toString()));
                employee.setAddress(c.getString(c.getColumnIndex(COL_ADDRESS)).toString());
                employee.setPhone_no(c.getString(c.getColumnIndex(COL_PHONE_NO)).toString());
                employee.setEmail(c.getString(c.getColumnIndex(COL_EMAIL)).toString());
                employee.setStatus(c.getInt(c.getColumnIndex(COL_STATUS)));
            }
        }
        db.close();
        return employee;
    }

    //Get all Employee
    public ArrayList<Employee> getEmployee(){
        String query = "SELECT * FROM " + TABLE_EMPLOYEE;
        ArrayList<Employee> employees = new ArrayList<Employee>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        if(c.getCount() > 0){
            c.moveToFirst();
            do {
                employees.add(new Employee(c.getInt(0), c.getString(1).toString(), c.getString(2).toString(),
                        new Date(c.getString(3).toString()), c.getString(4).toString(), c.getString(5).toString(), c.getString(6).toString(),
                        c.getInt(7)));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return employees;
    }

    //Print out the database as a String
    public String databaseToString(){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_EMPLOYEE + " WHERE 1";

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(query, null);
        //Move to the first row in your results
        c.moveToFirst();

        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("EMAIL")) != null){
                dbString += c.getString(c.getColumnIndex("EMAIL"));
                dbString += "\n";
            }
        }
        db.close();
        return dbString;
    }

}
