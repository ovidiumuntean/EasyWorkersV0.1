package com.example.ovidiu.easyworkersv01;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.KeyListener;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.ovidiu.easyworkersv01.Entity.Employee;
import com.example.ovidiu.easyworkersv01.Tables.EmployeeTable;
import com.example.ovidiu.easyworkersv01.Util.AlertDialogManager;
import com.example.ovidiu.easyworkersv01.Util.DatabaseManager;
import com.example.ovidiu.easyworkersv01.Util.EmailValidator;
import com.example.ovidiu.easyworkersv01.Util.SessionManager;
import com.example.ovidiu.easyworkersv01.Util.UsefullyFunctions;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmployeeProfile1 extends AppCompatActivity {

    private Employee employee;
    private EditText mFirstNameView;
    private EditText mSurnameView;
    private EditText mPhoneNoView;
    private EditText mEmailView;
    private EditText mBirthDayView;
    private DatePickerDialog datePickerDialog;
    private EditText mAddressView;
    private EditText mAddressView1;
    private TextView mIdView;
    private TextView mStatusView;
    private RadioGroup mGenderRadioGroup;

    private boolean empUpdate = false;

    // used for util functions
    private UsefullyFunctions util;

    // used for update the emp in database in one go
    private ContentValues values;
    private EmployeeTable empTable;
    private CircleImageView imageViewLoad;
    private Intent intent;
    private static int IMG_RESULT = 1;
    private String ImageDecode;

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

        imageViewLoad = (CircleImageView) findViewById(R.id.profile_image);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(intent, IMG_RESULT);

            }

        });

        // Session Manager
        session = new SessionManager(getApplicationContext());
        //Database Manager
        myDb = new DatabaseManager(this, null, null, 1);
        empTable = new EmployeeTable();
        values = new ContentValues();

        util = new UsefullyFunctions();

        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();


        /**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         * */
        session.checkLogin();
        //Setting the Employee view Data
        this.setEmpProfileData();

        // Adding DATE Picker To Birthday EditText
        mBirthDayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR) -18; // current year
                final int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(EmployeeProfile1.this,
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

                                mBirthDayView.setText(day + "/"
                                        + (month) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        mGenderRadioGroup = (RadioGroup) findViewById(R.id.radioGroupGenderProfE);
        mGenderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.radioButtonMaleProfE:
                        if(employee.getGender() != 0){
                            employee.setGender(0);
                            empUpdate = true;
                            values.put(empTable.getColGender(), 0);
                        }
                        break;
                    case R.id.radioButtonFemaleProfE:
                        if(employee.getGender() != 1){
                            employee.setGender(1);
                            empUpdate = true;
                            values.put(empTable.getColGender(), 1);
                        }
                        break;
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (requestCode == IMG_RESULT && resultCode == RESULT_OK
                    && null != data) {


                Uri URI = data.getData();
                String[] FILE = {MediaStore.Images.Media.DATA};


                Cursor cursor = getContentResolver().query(URI,
                        FILE, null, null, null);

                cursor.moveToFirst();
                AlertDialogManager alert = new AlertDialogManager();

                int columnIndex = cursor.getColumnIndex(FILE[0]);
                ImageDecode = cursor.getString(columnIndex);
                try {
                    myDb.verifyStoragePermissions(this);
                    FileInputStream fis = new FileInputStream(ImageDecode);
                    byte[] image = new byte[fis.available()];
                    Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
                    imageViewLoad.setImageBitmap(bmp);
                    imageViewLoad.refreshDrawableState();
                    fis.read(image);
                    int dbRes = myDb.addEmpPicture(employee.getId(), image);
                    if (dbRes > 0) {
                        this.setEmpProfileData();
                    }
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    alert.showAlertDialog(this, "Invalid File..", "Please choose a png file!", false);
                }
                cursor.close();

//                imageViewLoad.setImageBitmap(BitmapFactory
//                        .decodeFile(ImageDecode));

            }
        } catch (Exception e) {
            Toast.makeText(this, "Please try again", Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    protected void onDestroy() {
        session.logoutUser();
        super.onDestroy();
        Intent mainAct = new Intent(this, MainActivity.class);
        startActivity(mainAct);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onDestroy();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setEmpProfileData() {
        // Get user Details from DB
        employee = myDb.searchEmployeeByEmail(session.getUserDetails().get(SessionManager.KEY_EMAIL));
        if (employee != null && session.isLoggedIn()) {
            this.setTitle(employee.getFirst_name());
        } else {
            session.logoutUser();
            finish();
        }
        // GETTING THE EDIT TEXT FROM VIEW
        mIdView = (TextView) findViewById(R.id.idProfE);
        mFirstNameView = (EditText) findViewById(R.id.firstNameProfE);
        mPhoneNoView = (EditText) findViewById(R.id.phoneNoProfE);
        mEmailView = (EditText) findViewById(R.id.emailProfE);
        mSurnameView = (EditText) findViewById(R.id.surnameProfE);
        mBirthDayView = (EditText) findViewById(R.id.birthdayProfE);
        mAddressView = (EditText) findViewById(R.id.addressProfE);
        mAddressView1 = (EditText) findViewById(R.id.addressProfE1);
        mStatusView = (TextView) findViewById(R.id.statusProfE);
        RadioButton maleRadio = (RadioButton) findViewById(R.id.radioButtonMaleProfE);
        RadioButton femaleRadio = (RadioButton) findViewById(R.id.radioButtonFemaleProfE);


        //SETTING THE EDIT TEXTS CONTENT
        mIdView.setText(String.valueOf(employee.getId()));
        mFirstNameView.setText(employee.getFirst_name());
        mSurnameView.setText(employee.getSurname());
        mBirthDayView.setText(util.convertDateToString(employee.getBirthday(), "dd/MM/yyyy"));
        if(employee.getGender() == 0){
            maleRadio.setChecked(true);
        } else {
            femaleRadio.setChecked(true);
        }
        if (!employee.getAddress().equals("")) {
            String[] address = employee.getAddress().split(",");
            for(int i = 0; i < address.length; i++) {
                if (i == 1) {
                    mAddressView.setText(address[0]);
                    mAddressView1.setText(address[1]);
                } else if(i == 0) {
                    mAddressView.setText(address[0]);
                } else {
                    mAddressView1.setText(mAddressView1.getText() + ", " + address[i]);
                }
            }
        }
        mPhoneNoView.setText(employee.getPhone_no());
        mEmailView.setText(employee.getEmail());
        if (employee.getStatus() == 1) {
            mStatusView.setText("Employed");
        } else {
            mStatusView.setText("Unemployed");
        }
        if (employee.getImage() != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(employee.getImage(), 0, employee.getImage().length);
            imageViewLoad.refreshDrawableState();
            imageViewLoad.setImageBitmap(bmp);
        }

    }

    public void onEditPhoneE(View v) {
        if (v == null)
            v = findViewById(R.id.chkPhoneE);
        if (mPhoneNoView.isEnabled()) {
            String newPhone = mPhoneNoView.getText().toString();
            if(newPhone.trim().equals("") || newPhone == null){
                mPhoneNoView.requestFocus();
                mPhoneNoView.setText("");
                mPhoneNoView.setError(getString(R.string.error_field_required));
            } else {
                v.setBackgroundResource(android.R.drawable.ic_menu_edit);
                mPhoneNoView.setEnabled(false);
                if (!employee.getPhone_no().equals(newPhone)) {
                    employee.setPhone_no(newPhone);
                    empUpdate = true;
                    values.put(empTable.getColPhoneNo(), newPhone);
                }
            }
        } else {
            v.setBackgroundResource(android.R.drawable.ic_menu_save);
            v.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
            mPhoneNoView.setEnabled(true);
            mPhoneNoView.requestFocus();
        }
    }

    public void onEditFirstNameE(View v) {
        if (v == null)
            v = findViewById(R.id.chkFirstNameE);
        if (mFirstNameView.isEnabled()) {
            String newFirstName = mFirstNameView.getText().toString();
            if(newFirstName.trim().equals("") || newFirstName == null){
                mFirstNameView.requestFocus();
                mFirstNameView.setText("");
                mFirstNameView.setError(getString(R.string.error_field_required));
            } else {
                mFirstNameView.setEnabled(false);
                v.setBackgroundResource(android.R.drawable.ic_menu_edit);
                if (!employee.getFirst_name().equals(newFirstName)) {
                    employee.setFirst_name(newFirstName);
                    empUpdate = true;
                    values.put(empTable.getColFirstName(), newFirstName);
                }
            }
        } else {
            v.setBackgroundResource(android.R.drawable.ic_menu_save);
            v.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
            mFirstNameView.setEnabled(true);
            mFirstNameView.requestFocus(employee.getFirst_name().length());
        }
    }

    public void onEditSurnameE(View v) {
        if (v == null)
            v = findViewById(R.id.chkSurnameE);
        if (mSurnameView.isEnabled()) {
            String newValue = mSurnameView.getText().toString();
            if(newValue.trim().equals("") || newValue == null){
                mSurnameView.requestFocus();
                mSurnameView.setText("");
                mSurnameView.setError(getString(R.string.error_field_required));
            } else {
                v.setBackgroundResource(android.R.drawable.ic_menu_edit);
                mSurnameView.setEnabled(false);
                if (!employee.getSurname().equals(newValue)) {
                    employee.setSurname(newValue);
                    empUpdate = true;
                }
            }
        } else {
            v.setBackgroundResource(android.R.drawable.ic_menu_save);
            v.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
            mSurnameView.setEnabled(true);
            mSurnameView.requestFocus();
        }
    }

    public void onEditBirthdayE(View v) {
        if (v == null)
            v = findViewById(R.id.chkBirthdayE);
        if (mBirthDayView.isEnabled()) {
            Date newValue = util.convertStringToDate(mBirthDayView.getText().toString());
            if (newValue != null) {
                mBirthDayView.setEnabled(false);
                v.setBackgroundResource(android.R.drawable.ic_menu_edit);
                if (employee.getBirthday().compareTo(newValue) != 0) {
                    employee.setBirthday(newValue);
                    empUpdate = true;
                    values.put(empTable.getColBirthday(), util.convertDateToString(employee.getBirthday(), "MM/dd/yyyy"));
                }
            } else {
                mBirthDayView.setEnabled(true);
                v.setBackgroundResource(android.R.drawable.ic_menu_save);
                v.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                mBirthDayView.setError(getString(R.string.error_invalid_date));
                mBirthDayView.requestFocus();
                mBirthDayView.setText("");
            }
        } else {
            v.setBackgroundResource(android.R.drawable.ic_menu_save);
            v.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
            mBirthDayView.setEnabled(true);
            mBirthDayView.setTag(mBirthDayView.getKeyListener());
            mBirthDayView.setKeyListener(null);
            mBirthDayView.requestFocus();
        }
    }

    public void onEditAddressE(View v) {
        if (v == null)
            v = findViewById(R.id.chkAddressE1);
        String newValue = mAddressView.getText().toString().trim();
        String newValue1 = mAddressView1.getText().toString().trim();
        if (mAddressView.isEnabled() || mAddressView1.isEnabled()) {
            if(newValue.length() > 0) {
                v.setBackgroundResource(android.R.drawable.ic_menu_edit);
                mAddressView.setEnabled(false);
                mAddressView1.setEnabled(false);

                if (newValue.charAt(newValue.length() - 1) != ',') {
                    newValue += ",";
                }
                if (!employee.getAddress().equals(newValue + newValue1) ) {
                    employee.setAddress(newValue + newValue1);
                    empUpdate = true;
                    values.put(empTable.getColAddress(), newValue + newValue1);
                }
            } else {
                mAddressView.requestFocus();
                mAddressView.setError(getString(R.string.error_field_required));
            }
        } else {
            v.setBackgroundResource(android.R.drawable.ic_menu_save);
            v.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
            mAddressView.setEnabled(true);
            mAddressView1.setEnabled(true);
            if(newValue.length() == 0) {
                mAddressView.requestFocus();
            } else {
                mAddressView1.requestFocus();
            }
        }
    }

    public void onEditEmailE(View v) {
        if (v == null)
            v = findViewById(R.id.chkEmailE);
        if (mEmailView.isEnabled()) {
            String newValue = mEmailView.getText().toString();
            EmailValidator emailValidator = new EmailValidator();
            if (emailValidator.validate(newValue)) {
                v.setBackgroundResource(android.R.drawable.ic_menu_edit);
                mEmailView.setEnabled(false);
                if (!employee.getAddress().equals(newValue)) {
                    employee.setAddress(newValue);
                    empUpdate = true;
                    values.put(empTable.getColEmail(), newValue);
                }
            } else {
                v.setBackgroundResource(android.R.drawable.ic_menu_save);
                v.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                mEmailView.setError(getString(R.string.error_invalid_email));
                mEmailView.requestFocus();
            }
        } else {
            v.setBackgroundResource(android.R.drawable.ic_menu_save);
            v.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
            mEmailView.setEnabled(true);
            mEmailView.requestFocus();
        }
    }

    private void checkEditTexts() {
        if (mFirstNameView.isEnabled()) {
            onEditFirstNameE(null);
        }
        if (mSurnameView.isEnabled()) {
            onEditSurnameE(null);
        }
        if (mBirthDayView.isEnabled()) {
            onEditBirthdayE(null);
        }
        if (mAddressView.isEnabled()) {
            onEditAddressE(null);
        }
        if (mPhoneNoView.isEnabled()) {
            onEditPhoneE(null);
        }
        if (mEmailView.isEnabled()) {
            onEditEmailE(null);
        }
    }

    public void onUpdateEmp(View v) {
        AlertDialogManager alert = new AlertDialogManager();
        this.checkEditTexts();
        if (empUpdate) {
            if (myDb.updateEmployee(values, employee.getId())) {
                this.setEmpProfileData();
                alert.showAlertDialog(this, "Update Successfully..", "New data was added to your profile!", true);
                empUpdate = false;
                values.clear();
            } else {
                alert.showAlertDialog(this, "Error..", "Something went wrong. Please contact our support team for details!", false);
            }
        } else {
            alert.showAlertDialog(this, "No changes..", "No changes were made!", true);
        }

    }
}
