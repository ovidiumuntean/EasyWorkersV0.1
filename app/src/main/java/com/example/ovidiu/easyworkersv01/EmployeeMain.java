package com.example.ovidiu.easyworkersv01;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ovidiu.easyworkersv01.Adapters.JobAdapter;
import com.example.ovidiu.easyworkersv01.Adapters.MyAdapter;
import com.example.ovidiu.easyworkersv01.Entity.Company;
import com.example.ovidiu.easyworkersv01.Entity.Employee;
import com.example.ovidiu.easyworkersv01.Entity.Job;
import com.example.ovidiu.easyworkersv01.Util.AlertDialogManager;
import com.example.ovidiu.easyworkersv01.Util.DatabaseManager;
import com.example.ovidiu.easyworkersv01.Util.SessionManager;

import java.util.ArrayList;

import static android.view.View.VISIBLE;

public class EmployeeMain extends AppCompatActivity {

    private DatabaseManager myDb;
    private SearchView searchJobView;
    private Spinner locSpinner;
    private Spinner catSpinner;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMainE);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabMainE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myDb = new DatabaseManager(this, null, null, 1);
        // Session Manager
        session = new SessionManager(getApplicationContext());

        final ListView listView = (ListView) findViewById(R.id.jobsListView);

        // Defined Array values to show in ListView
        ArrayList<Job> jobs = new ArrayList<Job>();
        jobs = myDb.getAllJobs();
        if (jobs != null) {

            // Assign adapter to ListView
            final JobAdapter myadapter = new
                    JobAdapter(EmployeeMain.this, R.layout.list_job_row, jobs);
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
        locSpinner = (Spinner) findViewById(R.id.spLocationsMainE);
        catSpinner = (Spinner) findViewById(R.id.spCategoriesMainE);

        searchJobView = (SearchView) findViewById(R.id.searchMainE);
        searchJobView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = locSpinner.getSelectedItem().toString();
                String category = catSpinner.getSelectedItem().toString();
                ArrayList<Job> jobs1 = myDb.searchJobs(query, location, category);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                locSpinner.setVisibility(VISIBLE);
                catSpinner.setVisibility(VISIBLE);
                return true;
            }


        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_employee_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_empProfile) {
            Intent empProfIntent = new Intent(this, EmployeeProfile.class);
            startActivity(empProfIntent);
            return true;
        } else if (id == R.id.action_addQualification) {

        } else if (id == android.R.id.home){
            session.logoutUser();
        }

        return true;
    }

    public void showDialog(final Job job){
        final Job j = job;
        final Dialog dialog = new Dialog(EmployeeMain.this);
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
        if(typeView.getText().toString().equalsIgnoreCase("0")){
            typeView.setText("Part Time");
        }else
            typeView.setText("Full Time");
        TextView createdView = (TextView) dialog.findViewById(R.id.createdOn);
        createdView.setText(String.valueOf(job.getJobcreated()));

        //Getting the action buttons and adding click listeners
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        dialogButton.setText("Apply");
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(employee != null) {
                    if (myDb.searchJobApplication(job.getId(), employee.getId()) == null) {
                        if (myDb.addUpdateJobApplication(job.getId(), employee.getId())) {
                            alert.showAlertDialog(EmployeeMain.this, "SUCCESS..", "Job application added successfully!", true);
                        } else {
                            alert.showAlertDialog(EmployeeMain.this, "ERROR..", "Error in adding the job application!", false);
                        }
                    } else {
                        alert.showAlertDialog(EmployeeMain.this, "APPLIED..", "You already applied for this job!", true);
                    }
                } else {
                    alert.showAlertDialog(EmployeeMain.this, "ERROR..", "Employee not found!", false);
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

//    public void onSearchMainE(View v){
//        Spinner locSpinner = (Spinner) findViewById(R.id.spLocationsMainE);
//        Spinner catSpinner = (Spinner) findViewById(R.id.spCategoriesMainE);
//
//        locSpinner.setVisibility(VISIBLE);
//        catSpinner.setVisibility(VISIBLE);
//
//    }
}
