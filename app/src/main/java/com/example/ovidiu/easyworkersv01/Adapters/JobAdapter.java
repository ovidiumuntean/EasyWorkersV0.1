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
import com.example.ovidiu.easyworkersv01.R;

import java.util.ArrayList;

/**
 * Created by Ovidiu on 11/12/2017.
 */

public class JobAdapter extends ArrayAdapter<Job> {
    private Context ctx = null;
    private ArrayList<Job> mDataSource;

    // override other abstract methods here
    public JobAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<Job>
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

        Job job = getItem(position);

        TextView jobNameView = ((TextView) convertView.findViewById(R.id.jobNameMainE));
        jobNameView.setText(job.getTitle());

        TextView compNameView = ((TextView) convertView.findViewById(R.id.compNameJob));
        compNameView.setText(job.getCompany().getName());

        TextView jobLocView = ((TextView) convertView.findViewById(R.id.compLocJobMainE));
        jobLocView.setText(job.getCompany().getAddress());

        return convertView;
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Nullable
    @Override
    public Job getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
