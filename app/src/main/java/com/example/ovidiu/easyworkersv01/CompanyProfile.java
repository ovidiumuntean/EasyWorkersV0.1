package com.example.ovidiu.easyworkersv01;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ovidiu.easyworkersv01.Entity.Company;
import com.example.ovidiu.easyworkersv01.Entity.Employee;
import com.example.ovidiu.easyworkersv01.Tables.CompanyTable;
import com.example.ovidiu.easyworkersv01.Tables.EmployeeTable;
import com.example.ovidiu.easyworkersv01.Util.AlertDialogManager;
import com.example.ovidiu.easyworkersv01.Util.DatabaseManager;
import com.example.ovidiu.easyworkersv01.Util.EmailValidator;
import com.example.ovidiu.easyworkersv01.Util.SessionManager;
import com.example.ovidiu.easyworkersv01.Util.UsefullyFunctions;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class CompanyProfile extends AppCompatActivity {

    private Company company;

    private EditText mConfPasswordView;
    private EditText mNameView;
    private EditText mRegView;
    private EditText mPhoneNoView;
    private EditText mEmailView;
    private EditText mAddressView;
    private EditText mIdView;
    private boolean compUpdate = false;

    // used for util functions
    private UsefullyFunctions util;

    // used for update the emp in database in one go
    private ContentValues values;
    private CompanyTable compTable;
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
        setContentView(R.layout.activity_company_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //imageViewLoad = (CircleImageView) findViewById(R.id.profile_image);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                intent = new Intent(Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//                startActivityForResult(intent, IMG_RESULT);

            }

        });

        // Session Manager
        session = new SessionManager(getApplicationContext());
        //Database Manager
        myDb = new DatabaseManager(this, null, null, 1);
        compTable = new CompanyTable();
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
                    int dbRes = myDb.addEmpPicture(company.getId(), image);
                    if(dbRes > 0){
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

    private void setEmpProfileData() {
        // Get user Details from DB
        company= myDb.searchCompanyByEmail(session.getUserDetails().get(SessionManager.KEY_EMAIL));
        if (company != null && session.isLoggedIn()) {
            this.setTitle(company.getName());
        } else {
            session.logoutUser();
            finish();
        }
        // GETTING THE EDIT TEXT FROM VIEW
        mIdView = (EditText) findViewById(R.id.idProfC);
        mNameView = (EditText) findViewById(R.id.NameProfC);
        mPhoneNoView = (EditText) findViewById(R.id.phoneNoProfC);
        mEmailView = (EditText) findViewById(R.id.emailProfC);
        mRegView = (EditText) findViewById(R.id.RegNumProfC);
        mAddressView = (EditText) findViewById(R.id.addressProfC);


        //SETTING THE EDIT TEXTS CONTENT
        mIdView.setText(String.valueOf(company.getId()));
        mNameView.setText(company.getName());
        mRegView.setText(company.getRegNum());
        if (!company.getAddress().equals("")) {
            mAddressView.setText(company.getAddress());
        }
        mPhoneNoView.setText(company.getPhoneNum());
        mEmailView.setText(company.getEmail());

    }

    public void onEditPhoneC(View v) {
        if (mPhoneNoView.isEnabled()) {
            mPhoneNoView.setEnabled(false);
            String newPhone = mPhoneNoView.getText().toString();
            if (!company.getPhoneNum().equals(newPhone)) {
                company.setPhoneNum(newPhone);
                compUpdate = true;
                values.put(compTable.getColPhoneNo(), newPhone);
            }
        } else {
            mPhoneNoView.setEnabled(true);
            mPhoneNoView.requestFocus();
        }
    }

    public void onEditNameC(View v) {
        if (v == null)
            v = findViewById(R.id.chkName);
        if (mNameView.isEnabled()) {
            String newName = mNameView.getText().toString();
            if(newName.trim().equals("") || newName == null){
                mNameView.requestFocus();
                mNameView.setText("");
                mNameView.setError(getString(R.string.error_field_required));
            } else {
                mNameView.setEnabled(false);
                v.setBackgroundResource(android.R.drawable.ic_menu_edit);
                if (!company.getName().equals(newName)) {
                    company.setName(newName);
                    compUpdate = true;
                    values.put(compTable.getColName(), newName);
                }
            }
        } else {
            v.setBackgroundResource(android.R.drawable.ic_menu_save);
            v.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
            mNameView.setEnabled(true);
            mNameView.requestFocus(company.getName().length());
        }
    }

    public void onEditRegNoC(View v) {
        if (v == null)
            v = findViewById(R.id.regnumState);
        if (mRegView.isEnabled()) {
            String newName = mRegView.getText().toString();
            if(newName.trim().equals("") || newName == null){
                mRegView.requestFocus();
                mRegView.setText("");
                mRegView.setError(getString(R.string.error_field_required));
            } else {
                mRegView.setEnabled(false);
                v.setBackgroundResource(android.R.drawable.ic_menu_edit);
                if (!company.getName().equals(newName)) {
                    company.setName(newName);
                    compUpdate = true;
                    values.put(compTable.getColRegnum(), newName);
                }
            }
        } else {
            v.setBackgroundResource(android.R.drawable.ic_menu_save);
            v.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
            mRegView.setEnabled(true);
            mRegView.requestFocus(company.getRegNum().length());
        }
    }

    public void onEditAddressC(View v) {
        if (v == null)
            v = findViewById(R.id.chkAddress);
        if (mAddressView.isEnabled()) {
            v.setBackgroundResource(android.R.drawable.ic_menu_edit);
            mAddressView.setEnabled(false);
            String newValue = mAddressView.getText().toString();
            if (!company.getAddress().equals(newValue)) {
                company.setAddress(newValue);
                compUpdate = true;
                values.put(compTable.getColAddress(), newValue);
            }
        } else {
            v.setBackgroundResource(android.R.drawable.ic_menu_save);
            v.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
            mAddressView.setEnabled(true);
            mAddressView.requestFocus();
        }
    }

    public void onEditEmailC(View v) {
        if (v == null)
            v = findViewById(R.id.chkEmail);
        if (mEmailView.isEnabled()) {
            String newValue = mEmailView.getText().toString();
            EmailValidator emailValidator = new EmailValidator();
            if (emailValidator.validate(newValue)) {
                v.setBackgroundResource(android.R.drawable.ic_menu_edit);
                mEmailView.setEnabled(false);
                if (!company.getAddress().equals(newValue)) {
                    company.setAddress(newValue);
                    compUpdate = true;
                    values.put(compTable.getColEmail(), newValue);
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

    public void onUpdateEmp(View v) {
        AlertDialogManager alert = new AlertDialogManager();
        if (compUpdate) {
            if (myDb.updateCompany(values, company.getId())) {
                this.setEmpProfileData();
                alert.showAlertDialog(this, "Update Successfully..", "New data was added to your profile!", true);
                compUpdate = false;
                values.clear();
            } else {
                alert.showAlertDialog(this, "Error..", "Something went wrong. Please contact our support team for details!", false);
            }
        } else {
            alert.showAlertDialog(this, "No changes..", "No changes were made!", true);
        }

    }
}
