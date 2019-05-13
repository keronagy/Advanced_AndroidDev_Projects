package com.example.deadlock.assignment3;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class ListViewAdapter extends ArrayAdapter {
    private Context context;
    int mResource;
    public ListViewAdapter(@NonNull Context context, int resource, ArrayList<EventClass> Events) {
        super(context, resource, Events);

        this.context=context;
        this.mResource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        EventClass event = (EventClass) getItem(position);
        String name = event.getName();
        String date = event.getDate();


        LayoutInflater layoutInflater = (LayoutInflater.from(context));
        convertView = layoutInflater.inflate(mResource,parent,false);

        TextView v1 = (TextView) convertView.findViewById(R.id.tv1);
        TextView v2 = (TextView) convertView.findViewById(R.id.tv2);


        v1.setText(name);
        v2.setText(date);
        return convertView;
    }
}
