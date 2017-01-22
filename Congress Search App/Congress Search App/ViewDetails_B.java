package com.example.congresssearch;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.congresssearch.Bills.EXTRA_MESSAGE;

public class ViewDetails_B extends AppCompatActivity {
    private BillsData info;
    public static final String PREFS_NAME = "CongressSearch";
    public static final String FAVORITES = "Bills_Favor";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details__b);
        getSupportActionBar().setIcon(R.drawable.left);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        info = (BillsData)intent.getSerializableExtra(EXTRA_MESSAGE);





        TextView bill_id = (TextView)findViewById(R.id.B_ID);
        bill_id.setText(info.getBill_id().toUpperCase());


        TextView title = (TextView)findViewById(R.id.B_Title);
        if(info.getShort_title().equals("N.A.")) {
            title.setText(info.getLong_title());
        } else {
            title.setText(info.getShort_title());
        }

        TextView type = (TextView)findViewById(R.id.B_Type);
        type.setText(info.getBill_type().toUpperCase());

        TextView sponsor = (TextView)findViewById(R.id.B_Sponsor);
        sponsor.setText(info.getSponsor());

        TextView chamber = (TextView)findViewById(R.id.B_Chamber);
        chamber.setText(info.getChamber());

        TextView status = (TextView)findViewById(R.id.B_Status);
        status.setText(info.getStatus());

        TextView intro = (TextView)findViewById(R.id.B_Intro);
        String date = info.getIntroduced_on();
        date = changeDateFormat("yyyy-MM-dd", "MMM dd, yyyy", date);
        intro.setText(date);

        TextView congressURL = (TextView)findViewById(R.id.B_CURL);
        congressURL.setText(info.getCongress_url());

        TextView version = (TextView)findViewById(R.id.B_Version);
        version.setText(info.getVersion_status());

        TextView bill_url = (TextView)findViewById(R.id.B_Bill_URL);
        bill_url.setText(info.getBill_url());


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
    public boolean checkFavoriteItem(BillsData checkInfo) {
        boolean check = false;
        List<BillsData> favorites = getFavorites();
        if (favorites != null) {
            for (BillsData info : favorites) {
                if (info.getBill_id().equals(checkInfo.getBill_id())) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }


    private void saveFavorites(List<BillsData> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.commit();
    }

    private void addFavorite(BillsData info) {
        List<BillsData> favorites = getFavorites();
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
    private void removeFavorite(BillsData info) {
        ArrayList<BillsData> favorites = getFavorites();
        if (favorites != null) {
            for (BillsData temp : favorites) {
                if(temp.getBill_id().equals(info.getBill_id())) {
                    favorites.remove(temp);
                    break;
                }
            }
            saveFavorites(favorites);
        }
    }

    private ArrayList<BillsData> getFavorites() {
        SharedPreferences settings;
        List<BillsData> favorites;

        settings = PreferenceManager.getDefaultSharedPreferences(this);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            BillsData[] favoriteItems = gson.fromJson(jsonFavorites,
                    BillsData[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<>(favorites);
        } else
            return null;

        return (ArrayList<BillsData>) favorites;
    }







    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
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
