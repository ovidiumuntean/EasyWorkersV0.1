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
    private EditText mEmailView;
    private EditText mBirthDayView;
    private EditText mAddressView;
    private EditText mIdView;
    private EditText mStatusView;

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
        // Get user Details from DB
        employee = myDb.searchEmployeeByEmail(session.getUserDetails().get(SessionManager.KEY_EMAIL));
        if(employee != null && session.isLoggedIn()) {
            this.setTitle(employee.getFirst_name());
            this.setEmpProfileData();
        } else {
            session.logoutUser();
            finish();
        }

    }

    public void setEmpProfileData(){

                // GETTING THE EDIT TEXT FROM VIEW
                mIdView = (EditText) findViewById(R.id.idProfE);
                mFirstNameView = (EditText) findViewById(R.id.firstNameProfE);
                mPhoneNoView = (EditText) findViewById(R.id.phoneNoProfE);
                mEmailView = (EditText) findViewById(R.id.emailProfE);
                mSurnameView = (EditText) findViewById(R.id.surnameProfE);
                mBirthDayView = (EditText) findViewById(R.id.birthdayProfE);
                mAddressView = (EditText) findViewById(R.id.addressProfE);
                mStatusView = (EditText) findViewById(R.id.statusProfE);

                //SETTING THE EDIT TEXTS CONTENT
                mIdView.setText(String.valueOf(employee.getId()));
                mFirstNameView.setText(employee.getFirst_name());
                mSurnameView.setText(employee.getSurname());
                mBirthDayView.setText(employee.getBirthday().toString());
                if(!employee.getAddress().equals("")){
                    mAddressView.setText(employee.getAddress());
                }
                mPhoneNoView.setText(employee.getPhone_no());
                mEmailView.setText(employee.getEmail());
                if(employee.getStatus() == 1) {
                    mStatusView.setText("Employed");
                } else {
                    mStatusView.setText("Unemployed");
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
