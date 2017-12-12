package com.example.ovidiu.easyworkersv01.Util;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteBlobTooBigException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Image;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.ovidiu.easyworkersv01.Entity.Job;
import com.example.ovidiu.easyworkersv01.Entity.JobApplication;
import com.example.ovidiu.easyworkersv01.Entity.Qualification;
import com.example.ovidiu.easyworkersv01.Tables.CompanyTable;
import com.example.ovidiu.easyworkersv01.Tables.EmployeeTable;
import com.example.ovidiu.easyworkersv01.Entity.Company;
import com.example.ovidiu.easyworkersv01.Entity.Employee;
import com.example.ovidiu.easyworkersv01.Tables.JobApplicationTable;
import com.example.ovidiu.easyworkersv01.Tables.JobTable;
import com.example.ovidiu.easyworkersv01.Tables.QualificationTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.DataFormatException;

/**
 * Created by Ovidiu on 20/11/2017.
 */

public class DatabaseManager extends SQLiteOpenHelper {
    // define constants related to DB schema such as DB name,
    private static final String DATABASE_NAME = "EasyWorkers.db";
    private static final int DATABASE_VERSION = 6;
    private static final EmployeeTable empTable = new EmployeeTable();
    private static final CompanyTable compTable = new CompanyTable();
    private static final QualificationTable qualTable = new QualificationTable();
    private static final JobTable jobTable = new JobTable();
    private static final JobApplicationTable jobAppTable = new JobApplicationTable();
    private static UsefullyFunctions util = new UsefullyFunctions();

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /*******/
    // the constructor of this class must call the super class(i.e.
    //SQLiteHelper)
    public DatabaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // build sql create statement to create tables in DB BookDB
        db.execSQL(empTable.createTableQuery());
        db.execSQL(compTable.createCompanyTable());
        db.execSQL(jobTable.createTableQuery());
        db.execSQL(jobAppTable.createTableQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old table if it exists and create new tables, or alter table
//        db.execSQL("DROP TABLE IF EXISTS " + empTable.getTableName());
//        db.execSQL("DROP TABLE IF EXISTS " + compTable.getTableName());
        onCreate(db);
    }

    //******************************         ADD METHODS        ************************************

    // ADD PICTURE TO EMP
    public int addEmpPicture(int id, byte[] img) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(empTable.getColPicture(), img);
        int res = -10;
        try {
            res = db.update(empTable.getTableName(), values, empTable.getColId() + "=?", new String[]{"" + id});
        } catch (SQLiteBlobTooBigException e) {
            db.close();
            return -1;
        }
        db.close();
        return res;
    }

    //ADD EMPLOYEE
    public boolean addEmployee(Employee employee) { // can haveparameters if needed
        Employee emp = searchEmployeeByEmail(employee.getEmail());
        if (emp == null) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = empTable.createValues(employee);
            long result;
            try {
                db.insert(empTable.getTableName(), null, values);
                db.close();
                return true;
            } catch (SQLException e) {
                db.close();
                e.printStackTrace();
                return false;
            }

        } else {
            return false;
        }

    }

    //ADD COMPANY
    public boolean addCompany(Company company) { // can haveparameters if needed
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = compTable.addCompany(company);
        long result = db.insert(compTable.getTableName(), null, values);
        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    //ADD JOB APPLICATION
    //Job Status --> 0 = not Applied, 1 = Applied , 2 = Accepted, 3 = Rejected
    public boolean addUpdateJobApplication(int jobId, int employeeId, Integer... status) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        long result;
        if(status.length == 1){
            values.put(jobAppTable.getColStatus(), status[0]);
            result = db.update(jobAppTable.getTableName(), values, "JOB_ID=? AND EMPLOYEE_ID=?", new String[]{String.valueOf(jobId), String.valueOf(employeeId)});
        } else {
            values = jobAppTable.createValues(jobId, employeeId, 1);
            result = db.insert(jobAppTable.getTableName(), null, values);
        }

        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    //ADD JOB APPLICATION
    //Job Status --> 0 = not Applied, 1 = Applied , 2 = Accepted, 3 = Rejected
    public boolean rejectJobApplications(int jobId, int employeeId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        long result;
        values.put(jobAppTable.getColStatus(), 3);
        result = db.update(jobAppTable.getTableName(), values, "JOB_ID=? AND EMPLOYEE_ID!=?", new String[]{String.valueOf(jobId), String.valueOf(employeeId)});


        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }



    //ADD QUALIFICATION
    public boolean addQualification(Qualification qualification) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = qualTable.createInsertValues(qualification);
        try {
            db.insert(qualTable.getTableName(), null, values);
            db.close();
            return true;
        } catch (SQLException e) {
            db.close();
            e.printStackTrace();
            return false;
        }
    }

    //ADD JOB
    public boolean addJob(Job job) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = jobTable.createValues(job);
        try {
            db.insert(jobTable.getTableName(), null, values);
            db.close();
            return true;
        } catch (SQLException e) {
            db.close();
            e.printStackTrace();
            return false;
        }
    }

    // *******************************   DELETE METHODS  *******************************************
    //DELETE EMPLOYEE BY EMAIL
    public void deleteEmployee(String employeeEmail) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + empTable.getTableName() + " WHERE " + empTable.getColEmail() + "=\'" + employeeEmail + "\';");
    }

    //DELETE COMPANY BY EMAIL
    public void deleteCompany(String companyEmail) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + compTable.getTableName() + " WHERE " + compTable.getColEmail() + "=\'" + companyEmail + "\';");
    }


    // **************************    SEARCH METHODS       ************************************
    //Search Employee by email
    public Employee searchEmployeeByEmail(String email) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + empTable.getTableName() + " WHERE " + empTable.getColEmail() + "=\'" + email + "\' AND " + empTable.getColStatus() + " = 1;";
        Employee employee = new Employee();
        Cursor c = db.rawQuery(query, null);

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                employee.setId(c.getInt(c.getColumnIndex(empTable.getColId())));
                employee.setFirst_name(c.getString(c.getColumnIndex(empTable.getColFirstName())).toString());
                employee.setSurname(c.getString(c.getColumnIndex(empTable.getColSurname())).toString());
                employee.setBirthday(new Date(c.getString(c.getColumnIndex(empTable.getColBirthday())).toString()));
                employee.setGender(c.getInt(c.getColumnIndex(empTable.getColGender())));
                employee.setAddress(c.getString(c.getColumnIndex(empTable.getColAddress())).toString());
                employee.setPhone_no(c.getString(c.getColumnIndex(empTable.getColPhoneNo())).toString());
                employee.setEmail(c.getString(c.getColumnIndex(empTable.getColEmail())).toString());
                employee.setPassword(c.getString(c.getColumnIndex(empTable.getColPassword())).toString());
                employee.setStatus(c.getInt(c.getColumnIndex(empTable.getColStatus())));
                employee.setImage(c.getBlob(c.getColumnIndex(empTable.getColPicture())));
            }
            db.close();
            return employee;
        } else {
            db.close();
            return null;
        }
    }

    //Search Employee by ID
    public Employee searchEmployeeById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + empTable.getTableName() + " WHERE " + empTable.getColId() + "=" + id + " AND " + empTable.getColStatus() + " = 1;";
        Employee employee = new Employee();
        Cursor c = db.rawQuery(query, null);

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                employee.setId(c.getInt(c.getColumnIndex(empTable.getColId())));
                employee.setFirst_name(c.getString(c.getColumnIndex(empTable.getColFirstName())).toString());
                employee.setSurname(c.getString(c.getColumnIndex(empTable.getColSurname())).toString());
                employee.setBirthday(new Date(c.getString(c.getColumnIndex(empTable.getColBirthday())).toString()));
                employee.setGender(c.getInt(c.getColumnIndex(empTable.getColGender())));
                employee.setAddress(c.getString(c.getColumnIndex(empTable.getColAddress())).toString());
                employee.setPhone_no(c.getString(c.getColumnIndex(empTable.getColPhoneNo())).toString());
                employee.setEmail(c.getString(c.getColumnIndex(empTable.getColEmail())).toString());
                employee.setPassword(c.getString(c.getColumnIndex(empTable.getColPassword())).toString());
                employee.setStatus(c.getInt(c.getColumnIndex(empTable.getColStatus())));
                employee.setImage(c.getBlob(c.getColumnIndex(empTable.getColPicture())));
            }
            db.close();
            return employee;
        } else {
            db.close();
            return null;
        }
    }

    //Search Employee by email and password
    public Employee employeeLogin(String email, String password) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + empTable.getTableName() + " WHERE " + empTable.getColEmail() + "=\'" + email +
                "\' AND " + empTable.getColPassword() + "= \'" + password + "\' AND " + empTable.getColStatus() + " = 1;";
        Employee employee = new Employee();
        Cursor c = db.rawQuery(query, null);

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                employee.setId(c.getInt(c.getColumnIndex(empTable.getColId())));
                employee.setFirst_name(c.getString(c.getColumnIndex(empTable.getColFirstName())).toString());
                employee.setSurname(c.getString(c.getColumnIndex(empTable.getColSurname())).toString());
                employee.setBirthday(new Date(c.getString(c.getColumnIndex(empTable.getColBirthday())).toString()));
                employee.setGender(c.getColumnIndex(empTable.getColGender()));
                employee.setAddress(c.getString(c.getColumnIndex(empTable.getColAddress())).toString());
                employee.setPhone_no(c.getString(c.getColumnIndex(empTable.getColPhoneNo())).toString());
                employee.setEmail(c.getString(c.getColumnIndex(empTable.getColEmail())).toString());
                employee.setPassword(c.getString(c.getColumnIndex(empTable.getColPassword())).toString());
                employee.setStatus(c.getInt(c.getColumnIndex(empTable.getColStatus())));
                employee.setImage(c.getBlob(c.getColumnIndex(empTable.getColPicture())));
            }
            db.close();
            return employee;
        } else {
            db.close();
            return null;
        }

    }


    //SEARCH COMPANY BY EMAIL
    public Company searchCompanyByEmail(String email) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + compTable.getTableName() + " WHERE " + compTable.getColEmail() + "=\'" + email + "\';";
        Company company = new Company();
        Cursor c = db.rawQuery(query, null);

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                company.setId(c.getInt(c.getColumnIndex(compTable.getColId())));
                company.setName(c.getString(c.getColumnIndex(compTable.getColName())).toString());
                company.setRegNum(c.getString(c.getColumnIndex(compTable.getColRegnum())).toString());
                company.setAddress(c.getString(c.getColumnIndex(compTable.getColAddress())).toString());
                company.setPhoneNum(c.getString(c.getColumnIndex(compTable.getColPhoneNo())).toString());
                company.setEmail(c.getString(c.getColumnIndex(empTable.getColEmail())).toString());
            }
        } else {
            c.close();
            db.close();
            return null;
        }
        c.close();
        db.close();
        return company;
    }

    public Company searchCompanyById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + compTable.getTableName() + " WHERE " + compTable.getColId() + "=" + id + ";";
        Company company = new Company();
        Cursor c = db.rawQuery(query, null);

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                company.setId(c.getInt(c.getColumnIndex(compTable.getColId())));
                company.setName(c.getString(c.getColumnIndex(compTable.getColName())).toString());
                company.setRegNum(c.getString(c.getColumnIndex(compTable.getColRegnum())).toString());
                company.setAddress(c.getString(c.getColumnIndex(compTable.getColAddress())).toString());
                company.setPhoneNum(c.getString(c.getColumnIndex(compTable.getColPhoneNo())).toString());
                company.setEmail(c.getString(c.getColumnIndex(empTable.getColEmail())).toString());
            }
        } else {
            db.close();
            return null;
        }
        db.close();
        return company;
    }

    public Job searchJobById(int id, Company... comp) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + jobTable.getTableName() + " WHERE " + jobTable.getColId() + "=" + id + ";";
        Job job = new Job();
        Cursor c = db.rawQuery(query, null);
        int compId;
        if (c.getCount() > 0) {
            SimpleDateFormat sd = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            Date date = null;
            c.moveToFirst();
            compId = c.getInt(c.getColumnIndex(jobTable.getColCompanyId()));


            while (c.moveToNext()) {
                try {
                    date = sd.parse(c.getString(c.getColumnIndex(jobTable.getColJobscreated())).toString());
                } catch (ParseException e) {
                    return null;
                }
                job.setId(c.getInt(c.getColumnIndex(jobTable.getColId())));
                job.setTitle(c.getString(c.getColumnIndex(jobTable.getColTitle())).toString());
                job.setDescription(c.getString(c.getColumnIndex(jobTable.getColDescription())).toString());
                job.setType(c.getInt(c.getColumnIndex(jobTable.getColType())));
                job.setExperience(c.getLong(c.getColumnIndex(jobTable.getColExperience())));
                job.setCategory(c.getString(c.getColumnIndex(jobTable.getColCategory())).toString());
                job.setJobcreated(date);
            }
        } else {
            db.close();
            return null;
        }
        db.close();
        if(comp.length == 1){
            job.setCompany(comp[0]);
        } else {
            job.setCompany(searchCompanyById(compId));
        }
        return job;
    }

    public Company companyLogin(String email, String password) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + compTable.getTableName() + " WHERE " + compTable.getColEmail() + "=\'" + email +
                "\' AND " + empTable.getColPassword() + "= \'" + password + "\';";
        Company company = new Company();
        Cursor c = db.rawQuery(query, null);

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                company.setId(c.getInt(c.getColumnIndex(compTable.getColId())));
                company.setName(c.getString(c.getColumnIndex(compTable.getColName())).toString());
                company.setRegNum(c.getString(c.getColumnIndex(compTable.getColRegnum())).toString());
                company.setAddress(c.getString(c.getColumnIndex(compTable.getColAddress())).toString());
                company.setPhoneNum(c.getString(c.getColumnIndex(compTable.getColPhoneNo())).toString());
                company.setEmail(c.getString(c.getColumnIndex(empTable.getColEmail())).toString());
                company.setPassword(c.getString(c.getColumnIndex(empTable.getColPassword())).toString());
                ;
            }
            db.close();
            return company;
        } else {
            db.close();
            return null;
        }

    }

    //Get all Employees
    public ArrayList<Employee> getEmployees() {
        String query = "SELECT * FROM " + empTable.getTableName();
        ArrayList<Employee> employees = new ArrayList<Employee>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                employees.add(new Employee(c.getInt(0), c.getString(1).toString(), c.getString(2).toString(),
                        new Date(c.getString(3).toString()), c.getInt(4), c.getString(5).toString(), c.getString(6).toString(),
                        c.getString(7).toString(), c.getString(8).toString(), c.getInt(9)));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return employees;
    }

    public ArrayList<Company> getCompanies() {
        String query = "SELECT * FROM " + compTable.getTableName();
        ArrayList<Company> companies = new ArrayList<Company>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        if (c.getCount() > 0) {
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

    //Get Company jobs
    public ArrayList<Job> getJob(Company comp) {
        String query = "SELECT * FROM " + jobTable.getTableName() + " WHERE COMPANY_ID=" + comp.getId();
        ArrayList<Job> jobs = new ArrayList<Job>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        if (c.getCount() > 0) {
            SimpleDateFormat sd = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            Date date = null;
            c.moveToFirst();
            do {
                try {
                    date = sd.parse(c.getString(5));
                } catch (ParseException e) {
                    return null;
                }
                jobs.add(new Job(c.getInt(0), c.getString(1).toString(), c.getString(2).toString(),
                        c.getInt(3), c.getLong(4), date, c.getString(6), comp));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        for (Job b : jobs) {
            b.setCompany(searchCompanyById(b.getCompany().getId()));
        }
        return jobs;

    }

    //Get Company job applications
    // Job Status --> 0 = not Applied, 1 = Applied , 2 = Accepted, 3 = Rejected
    public ArrayList<JobApplication> getCompJobApplication(Company comp) {
        String query = "SELECT * FROM " + jobAppTable.getTableName() + " WHERE STATUS=1 AND JOB_ID IN (SELECT JOB_ID FROM " +
                jobTable.getTableName() + " WHERE COMPANY_ID=" + comp.getId() + ");";
        ArrayList<JobApplication> jobApplications = new ArrayList<JobApplication>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            Job job = new Job();
            Employee emp = new Employee();

            do {
                job.setId(c.getInt(0));
                emp.setId(c.getInt(1));
                jobApplications.add(new JobApplication(job, emp, c.getInt(2)));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        if(jobApplications.size() > 0){
            for (JobApplication j: jobApplications){
                j.setJob(searchJobById(j.getJob().getId()));
                j.setEmployee(searchEmployeeById(j.getEmployee().getId()));
            }
            return jobApplications;
        } else {
            return null;
        }

    }


    // Searching for job application by jobId, empId and optional status
    // Job Status --> 0 = not Applied, 1 = Applied , 2 = Accepted, 3 = Rejected
    public JobApplication searchJobApplication(int jobId, int employeeId, Integer... status) {
        String query = "SELECT * FROM " + jobAppTable.getTableName() + " WHERE JOB_ID=" +
                jobId + " AND EMPLOYEE_ID=" + employeeId;
        if(status.length == 1) {
            query += " AND STATUS=" + status[0];
        }
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        JobApplication jobApp = null;
        if (c.getCount() > 0) {
            c.moveToFirst();
            Job job = new Job();
            Employee emp = new Employee();
            do {
                job.setId(c.getInt(0));
                emp.setId(c.getInt(1));
                jobApp = new JobApplication(job, emp, c.getInt(2));
            } while (c.moveToNext());
        }
        c.close();
        db.close();

        if(jobApp != null){
            jobApp.setJob(searchJobById(jobApp.getJob().getId()));
            jobApp.setEmployee(searchEmployeeById(jobApp.getEmployee().getId()));
        }
        return jobApp;

    }

    public ArrayList<Job> getAllJobs() {
        String query = "SELECT * FROM " + jobTable.getTableName();
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Job> jobs = new ArrayList<Job>();
        Cursor c = db.rawQuery(query, null);

        Company comp = null;
        if (c.getCount() > 0) {
            SimpleDateFormat sd = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            Date date = null;
            c.moveToFirst();
            comp = new Company(c.getInt(7));
            do {
                try {
                    date = sd.parse(c.getString(5));
                } catch (ParseException e) {
                    return null;
                }
                jobs.add(new Job(c.getInt(0), c.getString(1).toString(), c.getString(2).toString(),
                        c.getInt(3), c.getLong(4), date, c.getString(6), comp));
            } while (c.moveToNext());
        }
        c.close();
        db.close();

        for (Job b : jobs) {
            comp = searchCompanyById(b.getCompany().getId());
            if(comp != null)
                b.setCompany(comp);
            else
                return null;
        }
        return jobs;

    }

    //Searching jobs byLocations, cattergory and query
    public ArrayList<Job> searchJobs(String query, String location, String category) {

        String query1 = "SELECT * FROM " + jobTable.getTableName() + " WHERE " + jobTable.getColTitle() + " LIKE %" +
                query + "% AND " + jobTable.getColCategory() + " LIKE %" + category + "% AND " + jobTable.getColCompanyId()
                + " IN ( SELECT COMPANY_ID FROM Company WHERE ADDRESS LIKE %" + location + "%);";
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Job> jobs = new ArrayList<Job>();
        Cursor c = db.rawQuery(query, null);

        Company comp = null;
        if (c.getCount() > 0) {
            SimpleDateFormat sd = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            Date date = null;
            c.moveToFirst();
            comp = new Company(c.getInt(7));
            do {
                try {
                    date = sd.parse(c.getString(5));
                } catch (ParseException e) {
                    return null;
                }
                jobs.add(new Job(c.getInt(0), c.getString(1).toString(), c.getString(2).toString(),
                        c.getInt(3), c.getLong(4), date, c.getString(6), comp));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        for (Job b : jobs) {
            comp = searchCompanyById(b.getCompany().getId());
            if(comp != null)
            b.setCompany(comp);
            else
                return null;
        }
        return jobs;
    }

    // *********************************        UPDATE METHODS      ****************************
    public boolean updateEmployee(ContentValues values, int id) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            return (db.update(empTable.getTableName(), values, empTable.getColId() + "=?", new String[]{"" + id}) > 0);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateCompany(ContentValues values, int id) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            return (db.update(compTable.getTableName(), values, compTable.getColId() + "=?", new String[]{"" + id}) > 0);
        } catch (Exception e) {
            return false;
        }
    }


    //Print out the database as a String
    public String databaseToString() {
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + empTable.getTableName() + " WHERE 1";

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(query, null);
        //Move to the first row in your results
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("EMAIL")) != null) {
                dbString += c.getString(c.getColumnIndex("EMAIL"));
                dbString += "\n";
            }
        }
        db.close();
        return dbString;
    }

    //Changing the job Status 0 = not Applied 1 = Applied , 2 = Accepted, 3 = Rejected
    public boolean changeJobStatus(int jobId, int employeeId, int status) {
        return true;
    }

    // *********************************        Delete METHODS      ****************************

    public void deleteJob(int jobId) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM " + jobTable.getTableName() + " WHERE " + jobTable.getColId() + "= '" + jobId + "'");
        database.close();

    }


    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}
