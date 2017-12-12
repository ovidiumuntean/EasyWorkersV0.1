package com.example.ovidiu.easyworkersv01;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ovidiu.easyworkersv01.Adapters.JobAdapter;
import com.example.ovidiu.easyworkersv01.Entity.Job;
import com.example.ovidiu.easyworkersv01.Util.DatabaseManager;

import java.util.ArrayList;

import static android.view.View.VISIBLE;

public class EmployeeMain2 extends AppCompatActivity {

    private DatabaseManager myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        myDb = new DatabaseManager(this, null, null, 1);

        final ListView listView = (ListView) findViewById(R.id.jobsListView);

        // Defined Array values to show in ListView
        ArrayList<Job> jobs = new ArrayList<Job>();
        jobs = myDb.getAllJobs();
        if(jobs.size() > 0) {

            // Assign adapter to ListView
            final JobAdapter myadapter = new
                    JobAdapter(EmployeeMain2.this, R.layout.list_job_row, jobs);
            listView.setAdapter(myadapter);
            //ListView Item Click Listener
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
//                myadapter.remove(obj);
//                myadapter.notifyDataSetChanged();

                }
            });
        } else{
            Toast.makeText(this, "No friends available in the database!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onSearchMainE(View v){
        Spinner locSpinner = (Spinner) findViewById(R.id.spLocationsMainE);
        Spinner catSpinner = (Spinner) findViewById(R.id.spCategoriesMainE);

        locSpinner.setVisibility(VISIBLE);
        catSpinner.setVisibility(VISIBLE);

    }

}
