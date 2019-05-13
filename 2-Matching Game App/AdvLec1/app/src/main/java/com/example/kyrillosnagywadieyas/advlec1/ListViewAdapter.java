package com.example.kyrillosnagywadieyas.advlec1;

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
    public ListViewAdapter(@NonNull Context context, int resource, ArrayList<User> Users) {
        super(context, resource, Users);

        this.context=context;
        this.mResource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User user = (User) getItem(position);
        String name = user.getName();
        String time = user.getTimeStamp();
        int score = user.getScore();
        int id = position+1;

        LayoutInflater layoutInflater = (LayoutInflater.from(context));
        convertView = layoutInflater.inflate(mResource,parent,false);

        TextView v1 = (TextView) convertView.findViewById(R.id.tv1);
        TextView v2 = (TextView) convertView.findViewById(R.id.tv2);
        TextView v3 = (TextView) convertView.findViewById(R.id.tv3);
        TextView v4 = (TextView) convertView.findViewById(R.id.tv4);

        v1.setText(name);
        v2.setText(score+"");
        v3.setText(time);
        v4.setText(id+"");

        return convertView;
    }
}
