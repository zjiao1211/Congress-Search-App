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

public class AdapterBills extends BaseAdapter {
    private Context context = null;
    private LayoutInflater inflater;
    List<BillsData> data= Collections.emptyList();
    BillsData current;

    public AdapterBills(Context context, List<BillsData> data){
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
    public BillsData getItem(int position) {
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
            ll = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.bill_cell, null);
        }

        current = data.get(position);

        TextView bill_id = (TextView) ll.findViewById(R.id.bill_Id);
        bill_id.setText(current.getBill_id().toUpperCase());


        TextView bill_title = (TextView) ll.findViewById(R.id.bill_Title);
        if(current.getShort_title().equals("N.A.")) {
            bill_title.setText(current.getLong_title());
        } else {
            bill_title.setText(current.getShort_title());
        }

        TextView bill_date = (TextView) ll.findViewById(R.id.bill_Date);
        String date = current. getIntroduced_on();
        date = changeDateFormat("yyyy-MM-dd", "MMM dd, yyyy", date);
        bill_date.setText(date);


        return ll;
    }
    private String changeDateFormat(String oldFormat, String newFormat, String dateString) {
        String result = "";

        SimpleDateFormat formatterOld = new SimpleDateFormat(oldFormat, Locale.getDefault());
        SimpleDateFormat formatterNew = new SimpleDateFormat(newFormat, Locale.getDefault());
        Date date = null;
        try {
            date = formatterOld.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            result = formatterNew.format(date);
        }
        return result;
    }

}
