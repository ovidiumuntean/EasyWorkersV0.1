package com.example.ovidiu.easyworkersv01;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ovidiu.easyworkersv01.Entity.Employee;
import com.example.ovidiu.easyworkersv01.Util.DatabaseManager;
import com.example.ovidiu.easyworkersv01.Util.SessionManager;

public class EmployeeProfile1 extends AppCompatActivity {

    private Employee employee;
    private EditText mPasswordView;
    private EditText mConfPasswordView;
    private EditText mFirstNameView;
    private EditText mSurnameView;
    private EditText mPhoneNoView;
    private EditText mBirthDay;

    // Session Manager Class
    SessionManager session;

    // Database Manager Class
    private DatabaseManager myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_profile1);
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

        // Session Manager
        session = new SessionManager(getApplicationContext());
        //Database Manager
        myDb = new DatabaseManager(this, null, null, 1);

        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();


        /**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         * */
        session.checkLogin();

        if(session.isLoggedIn()){
//            mFirstNameView = (EditText) findViewById(R.id.firstNameRegE);
            mPhoneNoView = (EditText) findViewById(R.id.phoneNoProfE);
//            mSurnameView = (EditText) findViewById(R.id.surnameRegE);
//            mBirthDay = (EditText) findViewById(R.id.birthdayRegE);
            employee = myDb.searchEmployeeByEmail(session.getUserDetails().get(SessionManager.KEY_EMAIL));
            mPhoneNoView.setText(employee.getPhone_no());
            this.setTitle(employee.getFirst_name() + " " + employee.getSurname());
        } else {
            Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();
        }
    }

    public void onEditPhone(View v) {
        if (mPhoneNoView.isEnabled()) {
            mPhoneNoView.setEnabled(false);
        } else {
            mPhoneNoView.setEnabled(true);
            mPhoneNoView.requestFocus();
        }
    }
}
