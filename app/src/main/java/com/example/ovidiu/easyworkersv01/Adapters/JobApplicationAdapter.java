package com.example.ovidiu.easyworkersv01.Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ovidiu.easyworkersv01.Entity.Job;
import com.example.ovidiu.easyworkersv01.Entity.JobApplication;
import com.example.ovidiu.easyworkersv01.R;
import com.example.ovidiu.easyworkersv01.Util.DatabaseManager;

import java.util.ArrayList;

/**
 * Created by Ovidiu on 12/12/2017.
 */


public class JobApplicationAdapter extends ArrayAdapter<JobApplication> {
    private Context ctx = null;
    private ArrayList<JobApplication> mDataSource;
    private DatabaseManager myDb;

    // override other abstract methods here
    public JobApplicationAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<JobApplication>
            jobs) {
        super(context, resource, jobs);
        ctx = context;
        mDataSource = jobs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_job_row, container, false);
        }

        JobApplication job = getItem(position);

        TextView jobNameView = ((TextView) convertView.findViewById(R.id.jobNameMainE));
        jobNameView.setText(job.getJob().getTitle());

        TextView compNameView = ((TextView) convertView.findViewById(R.id.compNameJob));
        compNameView.setText(job.getJob().getCompany().getName());

        TextView jobLocView = ((TextView) convertView.findViewById(R.id.compLocJobMainE));
        jobLocView.setText(job.getJob().getCompany().getAddress());

        return convertView;
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Nullable
    @Override
    public JobApplication getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}