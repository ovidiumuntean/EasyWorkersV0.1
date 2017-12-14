package com.example.ovidiu.easyworkersv01;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ovidiu.easyworkersv01.Adapters.JobAdapter;
import com.example.ovidiu.easyworkersv01.Adapters.JobApplicationAdapter;
import com.example.ovidiu.easyworkersv01.Entity.Employee;
import com.example.ovidiu.easyworkersv01.Entity.Job;
import com.example.ovidiu.easyworkersv01.Entity.JobApplication;
import com.example.ovidiu.easyworkersv01.Util.AlertDialogManager;
import com.example.ovidiu.easyworkersv01.Util.DatabaseManager;
import com.example.ovidiu.easyworkersv01.Util.SessionManager;

import java.util.ArrayList;

public class SearchedJob extends AppCompatActivity {
    private DatabaseManager myDb;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_job);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        myDb = new DatabaseManager(this, null, null, 1);
        // Session Manager
        session = new SessionManager(getApplicationContext());

//        LayoutInflater inflater = (LayoutInflater)   getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        LinearLayout mContainer = (LinearLayout)     inflater.inflate(R.layout., null);
        final ListView listView = (ListView) findViewById(R.id.job_list_search);
        Bundle extras = getIntent().getExtras();
        if (extras.getString("CHOICE").equalsIgnoreCase("Search")) {
            searchJobs(extras, listView);
        } else if(extras.getString("CHOICE").equalsIgnoreCase("Applications")){
            this.setTitle("Applications");
            getJobApplications(listView);
        }
    }

    public void searchJobs(Bundle extras, final ListView listView) {
        String jobTitle = extras.getString("TITLE_KEY").trim();
        String location = extras.getString("LOCATION_KEY").trim();
        String category = extras.getString("CATEGORY_KEY").trim();
        // Defined Array values to show in ListView
        ArrayList<Job> jobs = new ArrayList<Job>();
        jobs = myDb.searchJobs(jobTitle, location, category);
        if (jobs != null) {

            // Assign adapter to ListView
            final JobAdapter myadapter = new
                    JobAdapter(SearchedJob.this, R.layout.list_job_row, jobs);
            listView.setAdapter(myadapter);
            // ListView Item Click Listener
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // ListView Clicked item index
                    int itemPosition = position;
                    // ListView Clicked item value
                    Job obj = (Job) listView.getItemAtPosition(position);
                    // String itemValue = (String)
                    listView.getItemAtPosition(position).toString();
                    String itemValue = obj.getTitle();
                    // Show Alert
                    Toast.makeText(getApplicationContext(),
                            "Position :" + itemPosition + " ListItem : " +
                                    itemValue, Toast.LENGTH_LONG)
                            .show();
                    showDialog(obj);

                }
            });
        } else {
            Toast.makeText(this, "No jobs available in the database!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getJobApplications(final ListView listView) {
        ArrayList<JobApplication> mJobApplicationList = myDb.getEmpJobApplication(session.getUserDetails().get(SessionManager.KEY_EMAIL));

        if (mJobApplicationList != null) {

            // Assign adapter to ListView
            final JobApplicationAdapter jobAppAdapter = new
                    JobApplicationAdapter(SearchedJob.this, R.layout.list_job_row, mJobApplicationList);
            listView.setAdapter(jobAppAdapter);
            //ListView Item Click Listener
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    int itemPosition = position;
                    JobApplication obj = (JobApplication) listView.getItemAtPosition(position);
                    listView.getItemAtPosition(position).toString();
                    showAppDialog(obj);

                }
            });
        } else {
            Toast.makeText(this, "No jobs available in the database!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAppDialog(JobApplication jobApplication) {
        final Dialog dialog = new Dialog(SearchedJob.this);
        dialog.setContentView(R.layout.custom);
        dialog.setTitle("Job Applications");
        //Creating an alert dialog
        final AlertDialogManager alert = new AlertDialogManager();
        TextView titleView = (TextView) dialog.findViewById(R.id.JobTitle);
        titleView.setText(jobApplication.getJob().getTitle());
        TextView descriptionView = (TextView) dialog.findViewById(R.id.jobDescription);
        descriptionView.setText(jobApplication.getJob().getDescription());
        TextView CategoryView = (TextView) dialog.findViewById(R.id.jobCategory);
        CategoryView.setText(jobApplication.getJob().getCategory());
        TextView typeView = (TextView) dialog.findViewById(R.id.jobType);
        if (typeView.getText().toString().equalsIgnoreCase("0")) {
            typeView.setText("Part Time");
        } else
            typeView.setText("Full Time");
        TextView createdView = (TextView) dialog.findViewById(R.id.createdOn);
        createdView.setText(String.valueOf(jobApplication.getJob().getJobcreated()));
        final JobApplication jobAppFinal = jobApplication;
        //Getting the action buttons and adding click listeners
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);

        dialogButton.setText("OK");
        Button deleteButton = (Button) dialog.findViewById(R.id.dialogButtonDelete);
        deleteButton.setText("Cancel");
        if (jobAppFinal.getEmployee() != null) {
            //Setting the employee name on dialog
            TextView empHeadingView = (TextView) dialog.findViewById(R.id.employeeNameHeading);
            TextView empNameView = (TextView) dialog.findViewById(R.id.employeeName);
            empHeadingView.setVisibility(View.VISIBLE);
            empNameView.setVisibility(View.VISIBLE);
            empNameView.setText(jobAppFinal.getEmployee().getFull_name());

            //Setting up the job Status
            //Setting the employee name on dialog
            TextView statusHeadingView = (TextView) dialog.findViewById(R.id.statusNameHeading);
            TextView statusNameView = (TextView) dialog.findViewById(R.id.statusContent);
            statusHeadingView.setVisibility(View.VISIBLE);
            statusNameView.setVisibility(View.VISIBLE);
            String status = "";
            statusNameView.setTextSize(16);
            switch (jobAppFinal.getStatus()) {
                case 0:
                    status = "Not Applied";
                    break;
                case 1:
                    status = "Applied";
                    statusNameView.setTextColor(Color.BLUE);
                    break;
                case 2:
                    status = "Accepted";
                    statusNameView.setTextColor(Color.GREEN);
                    break;
                case 3:
                    status = "Rejected";
                    statusNameView.setTextColor(Color.RED);
                    break;
                default:
                    status = "Not Found";
                    break;
            }
            statusNameView.setText(status);

            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    if (myDb.addUpdateJobApplication(jobAppFinal.getJob().getId(), jobAppFinal.getEmployee().getId(), 2)) {
//                        ContentValues values = new ContentValues();
//                        values.put("STATUS", 1);
//                        if(myDb.updateEmployee(values, jobAppFinal.getEmployee().getId())){
//                            alert.showAlertDialog(getContext(), "SUCCESS..", "Employee employed successfully!", true);
//                        } else {
//                            alert.showAlertDialog(getContext(), "ERROR..", "Employee not employed!", false);
//                        }
//                        alert.showAlertDialog(getContext(), "SUCCESS..", "Job application accepted successfully!", true);
//                        myDb.rejectJobApplications(jobAppFinal.getJob().getId(), jobAppFinal.getEmployee().getId());
//                        mJobApplicationList = myDb.getCompJobApplication(company);
//                        myadapter.clear();
//                        if(mJobApplicationList != null) {
//                            myadapter.addAll(mJobApplicationList);
//                        }
//                        myadapter.notifyDataSetChanged();
//                    } else {
//                        alert.showAlertDialog(getContext(), "ERROR..", "Error in accepting the job application!", false);
//                    }

                    dialog.dismiss();
                }
            });
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                }
            });
        } else {
            alert.showAlertDialog(SearchedJob.this, "ERROR..", "Employee not found!", false);
        }


        dialog.show();
    }

    public void showDialog(final Job job) {
        final Job j = job;
        final Dialog dialog = new Dialog(SearchedJob.this);
        dialog.setContentView(R.layout.custom);
        dialog.setTitle("Job");
        //Creating an alert dialog
        final AlertDialogManager alert = new AlertDialogManager();
        //Getting the details of employee
        final Employee employee = myDb.searchEmployeeByEmail(session.getUserDetails().get(SessionManager.KEY_EMAIL));
        //Setting the dialog details
        TextView titleView = (TextView) dialog.findViewById(R.id.JobTitle);
        titleView.setText(job.getTitle());
        TextView descriptionView = (TextView) dialog.findViewById(R.id.jobDescription);
        descriptionView.setText(job.getDescription());
        TextView CategoryView = (TextView) dialog.findViewById(R.id.jobCategory);
        CategoryView.setText(job.getCategory());
        TextView typeView = (TextView) dialog.findViewById(R.id.jobType);
        if (typeView.getText().toString().equalsIgnoreCase("0")) {
            typeView.setText("Part Time");
        } else
            typeView.setText("Full Time");
        TextView createdView = (TextView) dialog.findViewById(R.id.createdOn);
        createdView.setText(String.valueOf(job.getJobcreated()));

        //Getting the action buttons and adding click listeners
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        dialogButton.setText("Apply");
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (employee != null) {
                    if (myDb.searchJobApplication(job.getId(), employee.getId()) == null) {
                        if (myDb.addUpdateJobApplication(job.getId(), employee.getId())) {
                            alert.showAlertDialog(SearchedJob.this, "SUCCESS..", "Job application added successfully!", true);
                        } else {
                            alert.showAlertDialog(SearchedJob.this, "ERROR..", "Error in adding the job application!", false);
                        }
                    } else {
                        alert.showAlertDialog(SearchedJob.this, "APPLIED..", "You already applied for this job!", true);
                    }
                } else {
                    alert.showAlertDialog(SearchedJob.this, "ERROR..", "Employee not found!", false);
                }
                dialog.dismiss();
            }
        });
        Button deleteButton = (Button) dialog.findViewById(R.id.dialogButtonDelete);
        deleteButton.setText("Cancel");
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();

    }

}
