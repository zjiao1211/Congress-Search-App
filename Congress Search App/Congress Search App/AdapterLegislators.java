package com.example.congresssearch;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.Collections;
import java.util.List;

/**
 * Created by ZHEJIAO on 11/25/16.
 */

public class AdapterLegislators extends BaseAdapter {
    private Context context = null;
    private LayoutInflater inflater;
    List<LegislatorsData> data= Collections.emptyList();
    LegislatorsData current;

    public AdapterLegislators(Context context, List<LegislatorsData> data){
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
    public LegislatorsData getItem(int position) {
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
            ll = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.legislator_cell, null);
        }

        current = data.get(position);

        ImageView photo = (ImageView)ll.findViewById(R.id.photo);
        TextView name = (TextView) ll.findViewById(R.id.name);
        TextView attribute = (TextView) ll.findViewById(R.id.attribute);

        name.setText(current.getLast_name() + ", " + current.getFirst_name());
        if(current.getDistrict() != -1) {
            attribute.setText("(" + current.getParty() + ")" + current.getState_name() + " - " + "District " + current.getDistrict());
        } else {
            attribute.setText("(" + current.getParty() + ")" + current.getState_name() + " - " + "N.A.");
        }

        // load image into imageview using Picasso
        Picasso.with(context).load("https://theunitedstates.io/images/congress/original/"
                + current.getBioguide_id() + ".jpg")
                .resize(90, 105)
                .centerCrop()
                .into(photo);

        return ll;
    }

}
