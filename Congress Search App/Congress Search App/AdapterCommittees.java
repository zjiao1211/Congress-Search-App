package com.example.congresssearch;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ZHEJIAO on 11/27/16.
 */

public class AdapterCommittees extends BaseAdapter {
    private Context context = null;
    private LayoutInflater inflater;
    List<CommitteesData> data= Collections.emptyList();
    CommitteesData current;

    public AdapterCommittees (Context context, List<CommitteesData> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    private Context getContext() {
        return context;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public CommitteesData getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout ll = null;
        if(convertView != null) {
            ll = (LinearLayout) convertView;
        } else {
            ll = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.committee_cell, null);
        }

        current = data.get(position);

        TextView committee_Id = (TextView) ll.findViewById(R.id.committee_Id);
        committee_Id.setText(current.getCommittee_id().toUpperCase());


        TextView committee_Name = (TextView) ll.findViewById(R.id.committee_Name);
        committee_Name.setText(current.getCommittee_name());

        TextView committee_Chamber = (TextView) ll.findViewById(R.id.committee_Chamber);
        committee_Chamber.setText(current.getChamber());

        return ll;
    }
}
