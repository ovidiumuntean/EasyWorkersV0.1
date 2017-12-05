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
import com.example.ovidiu.easyworkersv01.Util.EmailValidator;
import com.example.ovidiu.easyworkersv01.Util.SessionManager;
import com.example.ovidiu.easyworkersv01.Util.UsefullyFunctions;

import java.util.Date;

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
    private boolean empUpdate = false;
    private UsefullyFunctions util;

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

        util = new UsefullyFunctions();

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
                mBirthDayView.setText(util.convertDateToString(employee.getBirthday()));
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

    public void onEditPhoneE(View v) {
        if (mPhoneNoView.isEnabled()) {
            mPhoneNoView.setEnabled(false);
            String newPhone = mPhoneNoView.getText().toString();
            if(!employee.getPhone_no().equals(newPhone)){
                employee.setPhone_no(newPhone);
                empUpdate = true;
            }
        } else {
            mPhoneNoView.setEnabled(true);
            mPhoneNoView.requestFocus();
        }
    }

    public void onEditFirstNameE(View v){
        if(mFirstNameView.isEnabled()){
            mFirstNameView.setEnabled(false);
            String newFirstName = mFirstNameView.getText().toString();
            if(!employee.getFirst_name().equals(newFirstName)){
                employee.setFirst_name(newFirstName);
                empUpdate = true;
            }
        } else {
            mFirstNameView.setEnabled(true);
            mFirstNameView.requestFocus();
        }
    }

    public void onEditSurnameE(View v){
        if(mSurnameView.isEnabled()){
            mSurnameView.setEnabled(false);
            String newValue = mSurnameView.getText().toString();
            if(!employee.getSurname().equals(newValue)){
                employee.setSurname(newValue);
                empUpdate = true;
            }
        } else {
            mSurnameView.setEnabled(true);
            mSurnameView.requestFocus();
        }
    }

    public void onEditBirthdayE(View v){
        if(mBirthDayView.isEnabled()){
            mBirthDayView.setEnabled(false);
            Date newValue = util.convertStringToDate(mBirthDayView.getText().toString());
            if(newValue != null) {
                if (employee.getBirthday().compareTo(newValue) != 0) {
                    employee.setBirthday(newValue);
                    empUpdate = true;
                }
            } else {
                mBirthDayView.setError(getString(R.string.error_invalid_date));
                mBirthDayView.requestFocus();
                mBirthDayView.setText("");
                mBirthDayView.setEnabled(false);
            }
        } else {
            mBirthDayView.setEnabled(true);
            mBirthDayView.requestFocus();
        }
    }

    public void onEditAddressE(View v){
        if(mAddressView.isEnabled()){
            mAddressView.setEnabled(false);
            String newValue = mAddressView.getText().toString();
            if(!employee.getAddress().equals(newValue)){
                employee.setAddress(newValue);
                empUpdate = true;
            }
        } else {
            mAddressView.setEnabled(true);
            mAddressView.requestFocus();
        }
    }

    public void onEditEmailE (View v){
        if(mEmailView.isEnabled()){
            mEmailView.setEnabled(false);
            String newValue = mEmailView.getText().toString();
            EmailValidator emailValidator = new EmailValidator();
            if(emailValidator.validate(newValue)) {
                if (!employee.getAddress().equals(newValue)) {
                    employee.setAddress(newValue);
                    empUpdate = true;
                }
            } else {
                mEmailView.setError(getString(R.string.error_invalid_email));
                mEmailView.requestFocus();
            }
        } else {
            mEmailView.setEnabled(true);
            mEmailView.requestFocus();
        }
    }
}
