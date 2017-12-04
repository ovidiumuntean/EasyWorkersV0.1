package com.example.ovidiu.easyworkersv01.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ovidiu.easyworkersv01.Tables.CompanyTable;
import com.example.ovidiu.easyworkersv01.Tables.EmployeeTable;
import com.example.ovidiu.easyworkersv01.Entity.Company;
import com.example.ovidiu.easyworkersv01.Entity.Employee;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ovidiu on 20/11/2017.
 */

public class DatabaseManager extends SQLiteOpenHelper {
    // define constants related to DB schema such as DB name,
    private static final String DATABASE_NAME = "EasyWorkers.db";
    private static final int DATABASE_VERSION = 1;
    private static final EmployeeTable empTable = new EmployeeTable();
    private static final CompanyTable compTable = new CompanyTable();
    /*******/
    // the constructor of this class must call the super class(i.e.
    //SQLiteHelper)


    public DatabaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate( SQLiteDatabase db ) {
        // build sql create statement to create tables in DB BookDB
        db.execSQL(empTable.createTableQuery());
        db.execSQL(compTable.createCompanyTable());
    }

    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
        // Drop old table if it exists and create new tables, or alter table
        db.execSQL("DROP TABLE IF EXISTS " + empTable.getTableName());
        db.execSQL("DROP TABLE IF EXISTS " + compTable.getTableName());
        onCreate(db);
    }

    public boolean addEmployee(Employee employee) { // can haveparameters if needed
        Employee emp = searchEmployeeByEmail(employee.getEmail());
        if(emp == null) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = empTable.createValues(employee);
            long result = db.insert(empTable.getTableName(), null, values);
            db.close();
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }

    }

    public boolean addCompany(Company company) { // can haveparameters if needed
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = compTable.addCompany(company);
        long result = db.insert(compTable.getTableName(), null, values);
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
        db.execSQL("DELETE FROM " + empTable.getTableName() + " WHERE " + empTable.getColEmail() + "=\'" + employeeEmail + "\';");
    }

    public void deleteCompany(String companyEmail){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + compTable.getTableName() + " WHERE " + compTable.getColEmail() + "=\'" + companyEmail + "\';");
    }

    //Search Employee by email
    public Employee  searchEmployeeByEmail(String email){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + empTable.getTableName() + " WHERE " + empTable.getColEmail() + "=\'" + email + "\' AND " + empTable.getColStatus() + " = 1;";
        Employee employee = new Employee();
        Cursor c = db.rawQuery(query, null);

        if(c.getCount() > 0) {
            while (c.moveToNext()) {
                employee.setId(c.getInt(c.getColumnIndex(empTable.getColId())));
                employee.setFirst_name(c.getString(c.getColumnIndex(empTable.getColFirstName())).toString());
                employee.setSurname(c.getString(c.getColumnIndex(empTable.getColSurname())).toString());
                employee.setBirthday(new Date(c.getString(c.getColumnIndex(empTable.getColBirthday())).toString()));
                employee.setAddress(c.getString(c.getColumnIndex(empTable.getColAddress())).toString());
                employee.setPhone_no(c.getString(c.getColumnIndex(empTable.getColPhoneNo())).toString());
                employee.setEmail(c.getString(c.getColumnIndex(empTable.getColEmail())).toString());
                employee.setPassword(c.getString(c.getColumnIndex(empTable.getColPassword())).toString());
                employee.setStatus(c.getInt(c.getColumnIndex(empTable.getColStatus())));
            }
            db.close();
            return employee;
        } else {
            db.close();
            return null;
        }
    }

    //Search Employee by email
    public Employee  employeeLogin(String email, String password){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + empTable.getTableName() + " WHERE " + empTable.getColEmail() + "=\'" + email +
                "\' AND " + empTable.getColPassword() + "= \'" + password + "\' AND " + empTable.getColStatus() + " = 1;";
        Employee employee = new Employee();
        Cursor c = db.rawQuery(query, null);

        if(c.getCount() > 0) {
            while (c.moveToNext()) {
                employee.setId(c.getInt(c.getColumnIndex(empTable.getColId())));
                employee.setFirst_name(c.getString(c.getColumnIndex(empTable.getColFirstName())).toString());
                employee.setSurname(c.getString(c.getColumnIndex(empTable.getColSurname())).toString());
                employee.setBirthday(new Date(c.getString(c.getColumnIndex(empTable.getColBirthday())).toString()));
                employee.setAddress(c.getString(c.getColumnIndex(empTable.getColAddress())).toString());
                employee.setPhone_no(c.getString(c.getColumnIndex(empTable.getColPhoneNo())).toString());
                employee.setEmail(c.getString(c.getColumnIndex(empTable.getColEmail())).toString());
                employee.setPassword(c.getString(c.getColumnIndex(empTable.getColPassword())).toString());
                employee.setStatus(c.getInt(c.getColumnIndex(empTable.getColStatus())));
            }
            db.close();
            return employee;
        } else {
            db.close();
            return null;
        }

    }

    public Company  searchCompanyByEmail(String name){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + compTable.getTableName() + " WHERE " + compTable.getColName() + "='" + name+ "'";
        Company company = new Company();
        Cursor c = db.rawQuery(query, null);

        if(c.getCount() > 0) {
            while (c.moveToNext()) {
                company.setId(c.getInt(c.getColumnIndex(compTable.getColId())));
                company.setName(c.getString(c.getColumnIndex(compTable.getColName())).toString());
                company.setRegNum(c.getString(c.getColumnIndex(compTable.getColRegnum())).toString());
                company.setAddress(c.getString(c.getColumnIndex(compTable.getColAddress())).toString());
                company.setPhoneNum(c.getString(c.getColumnIndex(compTable.getColPhoneNo())).toString());
                company.setEmail(c.getString(c.getColumnIndex(empTable.getColEmail())).toString());

            }
        }
        db.close();
        return company;
    }

    //Get all Employees
    public ArrayList<Employee> getEmployee(){
        String query = "SELECT * FROM " + empTable.getTableName();
        ArrayList<Employee> employees = new ArrayList<Employee>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        if(c.getCount() > 0){
            c.moveToFirst();
            do {
                employees.add(new Employee(c.getInt(0), c.getString(1).toString(), c.getString(2).toString(),
                        new Date(c.getString(3).toString()), c.getString(4).toString(), c.getString(5).toString(), c.getString(6).toString(),
                        c.getString(7).toString(), c.getInt(8)));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return employees;
    }

    public ArrayList<Company> getCompany(){
        String query = "SELECT * FROM " + compTable.getTableName();
        ArrayList<Company> companies = new ArrayList<Company>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        if(c.getCount() > 0){
            c.moveToFirst();
            do {
                companies.add(new Company(c.getInt(0), c.getString(1).toString(), c.getString(2).toString(),
                        c.getString(3).toString(), c.getString(4).toString(), c.getString(5).toString(), c.getString(6).toString()));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return companies;
    }

    //Print out the database as a String
    public String databaseToString(){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + empTable.getTableName() + " WHERE 1";

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
