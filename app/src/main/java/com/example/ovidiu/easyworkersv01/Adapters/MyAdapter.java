package com.example.ovidiu.easyworkersv01.Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ovidiu.easyworkersv01.Entity.Job;
import com.example.ovidiu.easyworkersv01.R;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by anamali on 11/12/2017.
 */

public class MyAdapter extends ArrayAdapter<Job> {
    private Context ctx = null;

    public MyAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<Job> jobs){
        super(context, resource, jobs);
        ctx = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list3col, container, false);
        }
        ((TextView) convertView.findViewById(R.id.jobTitle))
                .setText(getItem(position).getTitle());
        ((TextView) convertView.findViewById(R.id.jobDescription))
                .setText(getItem(position).getDescription());
        ((TextView) convertView.findViewById(R.id.jobCategory))
                .setText(getItem(position).getCategory());;
        ((TextView) convertView.findViewById(R.id.jobExperience))
                .setText(String.valueOf(getItem(position).getExperience()));
        return convertView;
    }
    }

