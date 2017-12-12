
package com.example.ovidiu.easyworkersv01;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ovidiu.easyworkersv01.Entity.Company;
import com.example.ovidiu.easyworkersv01.Entity.Employee;
import com.example.ovidiu.easyworkersv01.Entity.Job;
import com.example.ovidiu.easyworkersv01.Tables.JobTable;
import com.example.ovidiu.easyworkersv01.Util.AlertDialogManager;
import com.example.ovidiu.easyworkersv01.Util.DatabaseManager;
import com.example.ovidiu.easyworkersv01.Util.EmailValidator;
import com.example.ovidiu.easyworkersv01.Util.SessionManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class AddJobActivity extends AppCompatActivity {


    private static final int REQUEST_READ_CONTACTS = 0;

    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };


    // UI references.
    private EditText mTitleView;
    private EditText mDescriptionView;
    private EditText mCategoryView;
    private EditText mExperienceView;
    private RadioGroup mTypeRadioGroup;
    static JobTable jobTable;
    static Job job = null;
    int type = 0;



    // Database Manager Class
    private DatabaseManager myDb;

    // Session Manager Class
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);

        // Session Manager
        session = new SessionManager(getApplicationContext());
        //Database Manager
        myDb = new DatabaseManager(this, null, null, 1);

        // Set up the login form.

        mTitleView = (EditText) findViewById(R.id.title);
        mDescriptionView = (EditText) findViewById(R.id.Description);
        mCategoryView = (EditText) findViewById(R.id.category);
        mExperienceView = (EditText) findViewById(R.id.experience);
        mTypeRadioGroup = (RadioGroup) findViewById(R.id.type);




        Button mAddJob = (Button) findViewById(R.id.addJob);
        mAddJob.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptAddJob();
            }
        });



    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptAddJob() {



        // Store values at the time of the login attempt.

        String title = mTitleView.getText().toString();
        String description = mDescriptionView.getText().toString();
        String category = mCategoryView.getText().toString();
        long experience = Long.parseLong(mExperienceView.getText().toString());




        mTypeRadioGroup = (RadioGroup) findViewById(R.id.type);
        mTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radio_parttime:
                        type = 0;
                        break;
                    case R.id.radio_fulltime:
                       type =  1;
                        break;
                }
            }
        });


            Company company = myDb.searchCompanyByEmail(session.getUserDetails().get(SessionManager.KEY_EMAIL));
            Date d = new Date();
            job = new Job(0 , title, description, type, experience, d, category, company);
            if(myDb.addJob(job)) {
                Toast.makeText(AddJobActivity.this, "Job added successfully", Toast.LENGTH_SHORT).show();
                this.finish();
            } else {
                Toast.makeText(AddJobActivity.this, "Error", Toast.LENGTH_SHORT).show();
                this.finish();
            }

        }
}

