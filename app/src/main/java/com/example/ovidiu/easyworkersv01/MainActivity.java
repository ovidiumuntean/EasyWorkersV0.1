package com.example.ovidiu.easyworkersv01;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private boolean empBool = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent loginIntent = new Intent(this, EmployeeLogin.class);
            startActivity(loginIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void onCompany(View v){
        TextView compEmpQuestion = (TextView) findViewById(R.id.textView4);
        if (empBool){
            compEmpQuestion.setText("Are you an employee?");
            empBool = false;
        } else {
            compEmpQuestion.setText("Are you a company?");
            empBool = true;
        }

    }

    public void onLogin(View v) {
        if (empBool) {
            Intent empLogIntent = new Intent(this, EmployeeLogin.class);
            startActivity(empLogIntent);
        } else {
            //TODO: Add you code for open the company intent log in
        }
    }

    public void onRegister(View v){
        if(empBool) {
            Intent empRegIntent = new Intent(this, EmployeeRegister.class);
            startActivity(empRegIntent);
        } else {
            //TODO: ADD YOUR INTENT FOR COMP REGISTER
        }
    }

    public void link(View v){
        Intent companyRegIntent = new Intent(this, CompanyReg.class);
        startActivity(companyRegIntent);
    }
}
