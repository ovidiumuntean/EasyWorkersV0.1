package com.example.ovidiu.easyworkersv01;

import android.app.Dialog;
import android.content.ContentValues;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ovidiu.easyworkersv01.Adapters.JobAdapter;
import com.example.ovidiu.easyworkersv01.Adapters.JobApplicationAdapter;
import com.example.ovidiu.easyworkersv01.Entity.Company;
import com.example.ovidiu.easyworkersv01.Entity.Employee;
import com.example.ovidiu.easyworkersv01.Entity.Job;
import com.example.ovidiu.easyworkersv01.Entity.JobApplication;
import com.example.ovidiu.easyworkersv01.Util.AlertDialogManager;
import com.example.ovidiu.easyworkersv01.Util.DatabaseManager;
import com.example.ovidiu.easyworkersv01.Util.SessionManager;

import java.util.ArrayList;


public class ApplicationsTab extends Fragment {

    private static final String TAG = "Tab1Fragment";
    private DatabaseManager myDb;
    private Button btnTEST;
    private ArrayList<JobApplication> mJobApplicationList;
    private ArrayList<Job> mJobList;
    private JobApplicationAdapter myadapter = null;
    private Company company;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_applications_tab, container, false);
        myDb = new DatabaseManager(this.getContext(), null, null, 1);
        final ListView listView = (ListView) view.findViewById(R.id.listView);
        company = myDb.searchCompanyByEmail(EmployeeLogin.email);
        if (company != null) {
            mJobApplicationList = myDb.getCompJobApplication(company);

            if (mJobApplicationList != null) {

                // Assign adapter to ListView
                myadapter = new
                        JobApplicationAdapter(this.getContext(), R.layout.list_job_row, mJobApplicationList);
                listView.setAdapter(myadapter);
                //ListView Item Click Listener
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        int itemPosition = position;
                        JobApplication obj = (JobApplication) listView.getItemAtPosition(position);
                        listView.getItemAtPosition(position).toString();
                        showDialog(obj);

                    }
                });


            }
        }
        return view;
    }

    public void showDialog(JobApplication jobApplication) {
        final Dialog dialog = new Dialog(this.getContext());
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

        dialogButton.setText("Accept");
        Button deleteButton = (Button) dialog.findViewById(R.id.dialogButtonDelete);
        deleteButton.setText("Reject");
        if (jobAppFinal.getEmployee() != null) {
            TextView empHeadingView = (TextView) dialog.findViewById(R.id.employeeNameHeading);
            TextView empNameView = (TextView) dialog.findViewById(R.id.employeeName);
            empHeadingView.setVisibility(View.VISIBLE);
            empNameView.setVisibility(View.VISIBLE);
            empNameView.setText(jobAppFinal.getEmployee().getFull_name());
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (myDb.addUpdateJobApplication(jobAppFinal.getJob().getId(), jobAppFinal.getEmployee().getId(), 2)) {
                        ContentValues values = new ContentValues();
                        values.put("STATUS", 1);
                        if(myDb.updateEmployee(values, jobAppFinal.getEmployee().getId())){
                            alert.showAlertDialog(getContext(), "SUCCESS..", "Employee employed successfully!", true);
                        } else {
                            alert.showAlertDialog(getContext(), "ERROR..", "Employee not employed!", false);
                        }
                        alert.showAlertDialog(getContext(), "SUCCESS..", "Job application accepted successfully!", true);
                        myDb.rejectJobApplications(jobAppFinal.getJob().getId(), jobAppFinal.getEmployee().getId());
                        mJobApplicationList = myDb.getCompJobApplication(company);
                        myadapter.clear();
                        if(mJobApplicationList != null) {
                            myadapter.addAll(mJobApplicationList);
                        }
                        myadapter.notifyDataSetChanged();
                    } else {
                        alert.showAlertDialog(getContext(), "ERROR..", "Error in accepting the job application!", false);
                    }

                    dialog.dismiss();
                }
            });
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (jobAppFinal.getEmployee() != null) {
                        if (myDb.addUpdateJobApplication(jobAppFinal.getJob().getId(), jobAppFinal.getEmployee().getId(), 3)) {
                            mJobApplicationList = myDb.getCompJobApplication(company);
                            myadapter.clear();
                            myadapter.addAll(mJobApplicationList);
                            myadapter.notifyDataSetChanged();
                            alert.showAlertDialog(getContext(), "SUCCESS..", "Job application rejected successfully!", true);
                        } else {
                            alert.showAlertDialog(getContext(), "ERROR..", "Error in rejected the job application!", false);
                        }
                    } else {
                        alert.showAlertDialog(getContext(), "ERROR..", "Employee not found!", false);
                    }
                    dialog.dismiss();
                }
            });
        } else {
            alert.showAlertDialog(getContext(), "ERROR..", "Employee not found!", false);
        }
        ;


        dialog.show();

    }
}
