package com.example.congresssearch;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.congresssearch.Committees.EXTRA_MESSAGE;

public class ViewDetails_C extends AppCompatActivity {
    private CommitteesData info;
    public static final String PREFS_NAME = "CongressSearch";
    public static final String FAVORITES = "Committees_Favor";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details__c);
        getSupportActionBar().setIcon(R.drawable.left);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        info = (CommitteesData)intent.getSerializableExtra(EXTRA_MESSAGE);

        TextView committee_Id = (TextView)findViewById(R.id.C_ID);
        committee_Id.setText(info.getCommittee_id().toUpperCase());

        TextView name = (TextView)findViewById(R.id.C_Name);
        name.setText(info.getCommittee_name());

        ImageView partyImage = (ImageView) findViewById(R.id.C_Image);
        // load image into imageview using Picasso
        if(info.getChamber().equals("House")) {
            Picasso.with(this).load(R.drawable.h).resize(90, 105).into(partyImage);
        } else if (info.getChamber().equals("Senate")) {
            Picasso.with(this).load(R.drawable.s).resize(90, 105).into(partyImage);
        } else {
            Picasso.with(this).load(R.drawable.s).resize(90, 105).into(partyImage);
        }

        TextView chamber = (TextView)findViewById(R.id.C_Chamber);
        chamber.setText(info.getChamber());

        TextView parent = (TextView)findViewById(R.id.C_Parent);
        parent.setText(info.getParent());

        TextView phone = (TextView)findViewById(R.id.C_Phone);
        phone.setText(info.getPhone().toUpperCase());

        TextView office = (TextView)findViewById(R.id.C_Office);
        office.setText(info.getOffice().toUpperCase());



        //Configure Favorite Button for B
        final Button favorite = (Button) findViewById(R.id.favorite);

        if (!checkFavoriteItem(info)) {
            favorite.setBackgroundResource(R.drawable.star_empty);
            favorite.setTag("empty");
        } else {
            favorite.setBackgroundResource(R.drawable.star_filled);
            favorite.setTag("filled");
        }

        favorite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String tag = favorite.getTag().toString();
                if (tag.equalsIgnoreCase("empty")) {
                    favorite.setBackgroundResource(R.drawable.star_filled);
                    favorite.setTag("filled");
                    addFavorite(info);
                } else {
                    favorite.setBackgroundResource(R.drawable.star_empty);
                    favorite.setTag("empty");
                    removeFavorite(info);
                }

            }
        });




    }


    /*Checks whether a particular product exists in SharedPreferences*/
    public boolean checkFavoriteItem(CommitteesData checkInfo) {
        boolean check = false;
        List<CommitteesData> favorites = getFavorites();
        if (favorites != null) {
            for (CommitteesData info : favorites) {
                if (info.getCommittee_id().equals(checkInfo.getCommittee_id())) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }


    private void saveFavorites(List<CommitteesData> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.commit();
    }

    private void addFavorite(CommitteesData info) {
        List<CommitteesData> favorites = getFavorites();
        if (favorites == null)
            favorites = new ArrayList<>();
        /*
        int index = -1;
        for(int i = 0; i < favorites.size(); i++) {
            if(favorites.get(i).getBioguide_id().equals(info.getBioguide_id())) {
                index = i;
            }
        }
        if(index == -1) {

        }
        */
        favorites.add(info);
        saveFavorites(favorites);
    }

    //You cannot to remove info directly because it is an object, favorites may not know to delete whom.
    //So we need to add a for loop to remove the item with the same bioguide_id.
    private void removeFavorite(CommitteesData info) {
        ArrayList<CommitteesData> favorites = getFavorites();
        if (favorites != null) {
            for (CommitteesData temp : favorites) {
                if(temp.getCommittee_id().equals(info.getCommittee_id())) {
                    favorites.remove(temp);
                    break;
                }
            }
            saveFavorites(favorites);
        }
    }

    private ArrayList<CommitteesData> getFavorites() {
        SharedPreferences settings;
        List<CommitteesData> favorites;

        settings = PreferenceManager.getDefaultSharedPreferences(this);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            CommitteesData[] favoriteItems = gson.fromJson(jsonFavorites,
                    CommitteesData[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<>(favorites);
        } else
            return null;

        return (ArrayList<CommitteesData>) favorites;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}
