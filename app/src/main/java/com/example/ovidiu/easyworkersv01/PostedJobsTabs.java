package com.example.ovidiu.easyworkersv01;

import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ovidiu.easyworkersv01.Adapters.JobAdapter;
import com.example.ovidiu.easyworkersv01.Adapters.MyAdapter;
import com.example.ovidiu.easyworkersv01.Entity.Company;
import com.example.ovidiu.easyworkersv01.Entity.Job;
import com.example.ovidiu.easyworkersv01.Tables.JobTable;
import com.example.ovidiu.easyworkersv01.Util.DatabaseManager;
import java.util.ArrayList;
import java.util.Objects;

public class PostedJobsTabs extends Fragment {

    private static final String TAG = "Tab1Fragment";
    private static final int REQUEST_READ_CONTACTS = 0;
    private MyAdapter custtomCursorAdapter;
    private DatabaseManager myDb;
    private ArrayList<Job> mJobList;
    private JobTable jobTable;
    private Button btnTEST;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_posted_jobs_tabs,container,false);
        myDb = new DatabaseManager(this.getContext(), null, null, 1);
        final ListView listView = (ListView) view.findViewById(R.id.listView);
        Company company = myDb.searchCompanyByEmail(EmployeeLogin.email);

        mJobList = myDb.getJob(company);

        if(mJobList.size() > 0) {

            // Assign adapter to ListView
            final JobAdapter myadapter = new
                    JobAdapter(this.getContext(), R.layout.list_job_row, mJobList);
            listView.setAdapter(myadapter);
            //ListView Item Click Listener
           listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                  int itemPosition = position;
                    Job obj = (Job) listView.getItemAtPosition(position);
                    listView.getItemAtPosition(position).toString();
                    String itemValue = obj.getTitle();
                    showDialog(obj);

                //    Toast.makeText(getContext(), itemPosition, Toast.LENGTH_SHORT).show();

                }
            });



        } else{
            Toast.makeText(this.getContext(), "No jobs available in the database!", Toast.LENGTH_SHORT).show();
        }
        return view;
    }


    public void showDialog(Job job){
        final Job j = job;
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom);
        dialog.setTitle("Job");

        TextView titleView = (TextView) dialog.findViewById(R.id.JobTitle);
        titleView.setText(job.getTitle());
        TextView descriptionView = (TextView) dialog.findViewById(R.id.jobDescription);
        descriptionView.setText(job.getDescription());
        TextView CategoryView = (TextView) dialog.findViewById(R.id.jobCategory);
        CategoryView.setText(job.getCategory());
        TextView typeView = (TextView) dialog.findViewById(R.id.jobType);
        if(typeView.getText().toString().equalsIgnoreCase("0")){
            typeView.setText("Part Time");
        }else
            typeView.setText("Full Time");
        TextView createdView = (TextView) dialog.findViewById(R.id.createdOn);
        createdView.setText(String.valueOf(job.getJobcreated()));
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button deleteButton = (Button) dialog.findViewById(R.id.dialogButtonDelete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteJob(j);
                startActivity(new Intent(getContext(), CompanyTabs.class));
            }
        });



        dialog.show();

    }
    public void deleteJob(Job job){
        myDb.deleteJob(job.getId());


    }
}