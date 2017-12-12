package com.example.ovidiu.easyworkersv01;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import com.example.ovidiu.easyworkersv01.Adapters.MyAdapter;
import com.example.ovidiu.easyworkersv01.Entity.Company;
import com.example.ovidiu.easyworkersv01.Entity.Job;
import com.example.ovidiu.easyworkersv01.Tables.JobTable;
import com.example.ovidiu.easyworkersv01.Util.DatabaseManager;
import java.util.ArrayList;

public class PostedJobsTabs extends Fragment {

    private static final String TAG = "Tab1Fragment";
    private static final int REQUEST_READ_CONTACTS = 0;
    private MyAdapter custtomCursorAdapter;
    private DatabaseManager myDb;
    private ArrayList<Job> mJobList;
    private JobTable jobTable;
    private Button btnTEST;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_posted_jobs_tabs,container,false);
        myDb = new DatabaseManager(this.getContext(), null, null, 1);
        ListView listView = (ListView) view.findViewById(R.id.listView);
        Company company = myDb.searchCompanyByEmail(EmployeeLogin.email);

        mJobList = myDb.getJob(company);

        custtomCursorAdapter = new MyAdapter(getContext(), R.layout.list3col, mJobList);
        listView.setAdapter(custtomCursorAdapter);



        return view;
    }
}