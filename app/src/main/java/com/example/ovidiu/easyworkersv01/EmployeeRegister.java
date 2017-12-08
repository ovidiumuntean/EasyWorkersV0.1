
package com.example.ovidiu.easyworkersv01;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ovidiu.easyworkersv01.Entity.Employee;
import com.example.ovidiu.easyworkersv01.Util.AlertDialogManager;
import com.example.ovidiu.easyworkersv01.Util.DatabaseManager;
import com.example.ovidiu.easyworkersv01.Util.EmailValidator;
import com.example.ovidiu.easyworkersv01.Util.SessionManager;
import com.example.ovidiu.easyworkersv01.Util.UsefullyFunctions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class EmployeeRegister extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mConfPasswordView;
    private EditText mFirstNameView;
    private EditText mSurnameView;
    private EditText mPhoneNoView;
    private EditText mBirthDay;
    private View mProgressView;
    private View mLoginFormView;
    private DatePickerDialog datePickerDialog;


    // Database Manager Class
    private DatabaseManager myDb;

    // Session Manager Class
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_register);

        // Session Manager
        session = new SessionManager(getApplicationContext());
        //Database Manager
        myDb = new DatabaseManager(this, null, null, 1);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.emailRegE);
        mFirstNameView = (EditText) findViewById(R.id.firstNameRegE);
        mPhoneNoView = (EditText) findViewById(R.id.phoneNoRegE);
        mSurnameView = (EditText) findViewById(R.id.surnameRegE);

        mBirthDay = (EditText) findViewById(R.id.birthdayRegE);
        mBirthDay.setTag(mBirthDay.getKeyListener());
        mBirthDay.setKeyListener(null);
        mBirthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR) -18; // current year
                final int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(EmployeeRegister.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                String month = String.valueOf(monthOfYear + 1);
                                String day = String.valueOf(dayOfMonth);
                                if (day.length() == 1) {
                                    day = "0" + day;
                                }
                                if (month.length() == 1) {
                                    month = "0" + month;
                                }

                                mBirthDay.setText(day + "/"
                                        + (month) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        mPasswordView = (EditText) findViewById(R.id.passwordRegE);


        mConfPasswordView = (EditText) findViewById(R.id.password_confirmedRegE);
        mConfPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });


        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegister() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mFirstNameView.setError(null);
        mSurnameView.setError(null);
        mPhoneNoView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String confirmPassword = mConfPasswordView.getText().toString();
        String firstName = mFirstNameView.getText().toString();
        String surname = mSurnameView.getText().toString();
        String phoneNo = mPhoneNoView.getText().toString();
        String birthDay = mBirthDay.getText().toString();

        boolean cancel = false;
        View focusView = null;
        UsefullyFunctions util = new UsefullyFunctions();



        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid confirmed password, if the user entered one.
        if (!TextUtils.isEmpty(confirmPassword) && !isPasswordValid(confirmPassword)) {
            mConfPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mConfPasswordView;
            cancel = true;
        } else {
            if(!confirmPassword.equals(password)){
                mConfPasswordView.setError(getString(R.string.error_invalid_confirmed_password));
                focusView = mConfPasswordView;
                cancel = true;
            }
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if(TextUtils.isEmpty(firstName)){
            mFirstNameView.setError(getString(R.string.error_field_required));
            focusView = mFirstNameView;
            cancel = true;
        }

        if(TextUtils.isEmpty(surname)){
            mSurnameView.setError(getString(R.string.error_field_required));
            focusView = mSurnameView;
            cancel = true;
        }

        if(TextUtils.isEmpty(phoneNo)){
            mPhoneNoView.setError(getString(R.string.error_field_required));
            focusView = mPhoneNoView;
            cancel = true;
        }

        if(TextUtils.isEmpty(birthDay)){
            mBirthDay.setError(getString(R.string.error_field_required));
            focusView = mBirthDay;
            cancel = true;
        } else {
            if(util.convertStringToDate(birthDay) == null){
                mBirthDay.setError(getString(R.string.error_invalid_date));
                focusView = mBirthDay;
                cancel = true;
            }
        }

        if (cancel) {
            // There was an error; don't attempt register and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            Employee newEmployee = new Employee(0 , firstName, surname, new Date(birthDay), 0, "", phoneNo, email, password, 1);
            mAuthTask = new UserLoginTask(newEmployee);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        EmailValidator emailValidator = new EmailValidator();
        return emailValidator.validate(email);
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(EmployeeRegister.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private Employee employee;

        UserLoginTask(Employee emp) {
            this.employee = emp;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            Employee emp1;
            try {
                // Simulate network access.
                emp1 = myDb.searchEmployeeByEmail(employee.getEmail());
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            if(emp1 == null){
                return true;
            } else {
                return false;
            }
            // TODO: register the new account here.

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            AlertDialogManager alert = new AlertDialogManager();
            if (success) {
                if(myDb.addEmployee(employee)) {
                    if (session.isLoggedIn()) {
                        session.logoutUser();
                    }
                    session.createLoginSession(employee.getFirst_name(), employee.getEmail());
                    Toast.makeText(EmployeeRegister.this, "User " + employee.getFirst_name() + " successfully registered!", Toast.LENGTH_SHORT).show();
                    //alert.showAlertDialog(EmployeeRegister.this, "Registration successfully..", "User " + employee.getFirst_name() + " successfully registered!", false);
                    //EmployeeRegister.super.onDestroy();
                    Intent empProfIntent = new Intent(EmployeeRegister.this, EmployeeProfile.class);
                    startActivity(empProfIntent);
                } else {
                    mConfPasswordView.requestFocus();
                    alert.showAlertDialog(EmployeeRegister.this, "Registration failed..", "Invalid Data!", false);
                }

            } else {
                //Toast.makeText(this, "Please Try Again!", Toast.LENGTH_SHORT).show();
                alert.showAlertDialog(EmployeeRegister.this, "Registration failed..", "You are already registered! Please Login!", false);
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

