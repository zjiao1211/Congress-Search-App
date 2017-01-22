package com.example.congresssearch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.congresssearch.Legislators.EXTRA_MESSAGE;

public class ViewDetails_L extends AppCompatActivity {
    private ImageView photo;
    private ImageView partyImage;
    private TextView party;
    private TextView l_name;
    private TextView email;
    private TextView chamber;
    private TextView phone;
    private TextView start_term;
    private TextView end_term;
    private ProgressBar term;
    private TextView office;
    private TextView state;
    private TextView fax;
    private TextView birthday;
    private String email_address;
    private Button facebook;
    private Button twitter;
    private Button website;
    private LegislatorsData info;
    public static final String PREFS_NAME = "CongressSearch";
    public static final String FAVORITES = "Legislators_Favor";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details__l);
        getSupportActionBar().setIcon(R.drawable.left);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        info = (LegislatorsData)intent.getSerializableExtra(EXTRA_MESSAGE);


        photo = (ImageView) findViewById(R.id.photo);
        Picasso.with(this).load("https://theunitedstates.io/images/congress/original/"
                + info.getBioguide_id() + ".jpg")
                .resize(180, 200)
                .centerCrop()
                .into(photo);

        partyImage = (ImageView) findViewById(R.id.partyImage);
        party = (TextView)findViewById(R.id.party);
        if(info.getParty().equals("R")) {
            Picasso.with(this).load(R.drawable.r).into(partyImage);
            party.setText("Republican");
        } else if(info.getParty().equals("D")) {
            Picasso.with(this).load(R.drawable.d).into(partyImage);
            party.setText("Democrat");
        } else {
            Picasso.with(this).load("http://independentamericanparty.org/wp-content/themes/v/images/logo-american-heritage-academy.png")
                    .into(partyImage);
            party.setText("Libertarian");
        }


        l_name = (TextView)findViewById(R.id.L_Name);
        l_name.setText(info.getTitle() + ". " + info.getLast_name() + ", " + info.getFirst_name());

        email = (TextView)findViewById(R.id.L_Email);
        email_address = info.getEmail();
        email.setText(email_address);
        email.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + email_address));
                try {
                    startActivity(emailIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ViewDetails_L.this, "No email clients installed.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        chamber = (TextView)findViewById(R.id.L_Chamber);
        if(info.getChamber().equals("senate")) {
            chamber.setText("Senate");
        } else if(info.getChamber().equals("house")) {
            chamber.setText("House");
        }

        phone = (TextView)findViewById(R.id.L_Contact);
        phone.setText(info.getPhone());

        start_term = (TextView)findViewById(R.id.L_Start);
        end_term = (TextView)findViewById(R.id.L_End);

        String start = info.getStart_term();
        start = changeDateFormat("yyyy-MM-dd", "MMM dd, yyyy", start);
        start_term.setText(start);
        String end = info.getEnd_term();
        end = changeDateFormat("yyyy-MM-dd", "MMM dd, yyyy", end);
        end_term.setText(end);


        term = (ProgressBar) findViewById(R.id.L_Term);

        try {
            Date startTime = new SimpleDateFormat("yyyy-MM-dd").parse(info.getStart_term());
            Date endTime = new SimpleDateFormat("yyyy-MM-dd").parse(info.getEnd_term());
            Date currentTime = new Date();
            long diff1 = currentTime.getTime() - startTime.getTime();
            long diff2 = endTime.getTime() - startTime.getTime();
            int percentage = (int) Math.round(100.0 * Math.abs(diff1)
                    / Math.abs(diff2));
            term.setProgress(percentage);
            String strProgress = String.valueOf(percentage) + "%";
            TextView percent = (TextView) findViewById(R.id.progress_horizontal);
            percent.setText(strProgress);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        office = (TextView)findViewById(R.id.L_Office);
        office.setText(info.getOffice());

        state = (TextView)findViewById(R.id.L_State);
        state.setText(info.getState());

        fax = (TextView)findViewById(R.id.L_Fax);
        fax.setText(info.getFax());

        birthday = (TextView)findViewById(R.id.L_Birthday);
        String birth = info.getBirthday();
        birth = changeDateFormat("yyyy-MM-dd", "MMM dd, yyyy", birth);
        birthday.setText(birth);

        facebook = (Button) findViewById(R.id.facebook);
        facebook.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                if(info.getFacebook().equals("N.A.")) {
                    Toast.makeText(ViewDetails_L.this, "facebook not available", Toast.LENGTH_SHORT).show();
                } else {
                    facebookIntent.setData(Uri.parse("https://www.facebook.com/" + info.getFacebook()));
                    startActivity(facebookIntent);
                }

            }
        });

        twitter = (Button) findViewById(R.id.twitter);
        twitter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent twitterIntent = new Intent(Intent.ACTION_VIEW);
                if(info.getTwitter().equals("N.A.")) {
                    Toast.makeText(ViewDetails_L.this, "twitter not available", Toast.LENGTH_SHORT).show();
                } else {
                    twitterIntent.setData(Uri.parse("https://www.twitter.com/" + info.getTwitter()));
                    startActivity(twitterIntent);
                }

            }
        });

        website = (Button) findViewById(R.id.website);
        website.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW);
                if(info.getWebsite().equals("N.A.")) {
                    Toast.makeText(ViewDetails_L.this, "website not available", Toast.LENGTH_SHORT).show();
                } else {
                    websiteIntent.setData(Uri.parse(info.getWebsite()));
                    startActivity(websiteIntent);
                }

            }
        });


        //Configure Favorite Button for L
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
    public boolean checkFavoriteItem(LegislatorsData checkInfo) {
        boolean check = false;
        List<LegislatorsData> favorites = getFavorites();
        if (favorites != null) {
            for (LegislatorsData info : favorites) {
                if (info.getBioguide_id().equals(checkInfo.getBioguide_id())) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }


    private void saveFavorites(List<LegislatorsData> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.commit();
    }

    private void addFavorite(LegislatorsData info) {
        List<LegislatorsData> favorites = getFavorites();
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
    private void removeFavorite(LegislatorsData info) {
        ArrayList<LegislatorsData> favorites = getFavorites();
        if (favorites != null) {
            for (LegislatorsData temp : favorites) {
                if(temp.getBioguide_id().equals(info.getBioguide_id())) {
                    favorites.remove(temp);
                    break;
                }
            }
            saveFavorites(favorites);
        }
    }

    private ArrayList<LegislatorsData> getFavorites() {
        SharedPreferences settings;
        List<LegislatorsData> favorites;

        settings = PreferenceManager.getDefaultSharedPreferences(this);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            LegislatorsData[] favoriteItems = gson.fromJson(jsonFavorites,
                    LegislatorsData[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<>(favorites);
        } else
            return null;

        return (ArrayList<LegislatorsData>) favorites;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

}
