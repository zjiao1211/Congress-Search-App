package com.example.congresssearch;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
/**
 * A simple {@link Fragment} subclass.
 */
public class Committees extends Fragment {

    public final static String EXTRA_MESSAGE = "com.example.congresssearch.ViewDetails_C";
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private View view;
    private TabLayout tabLayout;
    private ListView c_listview;
    private AdapterCommittees cAdapter;
    private List<CommitteesData> committee_house = new ArrayList<>();
    private List<CommitteesData> committee_senate = new ArrayList<>();
    private List<CommitteesData> committee_joint = new ArrayList<>();

    public Committees() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_committees, container, false);
        getActivity().setTitle("Committees");



        tabLayout = (TabLayout) view.findViewById(R.id.c_tab_layout);
        tabLayout.setSelectedTabIndicatorHeight(0);

        TabLayout.Tab tab1 = tabLayout.newTab().setText("HOUSE");
        tabLayout.addTab(tab1);

        TabLayout.Tab tab2 = tabLayout.newTab().setText("SENATE");
        tabLayout.addTab(tab2);

        TabLayout.Tab tab3 = tabLayout.newTab().setText("JOINT");
        tabLayout.addTab(tab3);



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0) {
                    // Setup and Handover data to listview (initialize to by state)
                    c_listview = (ListView)view.findViewById(R.id.c_listview);
                    cAdapter = new AdapterCommittees(getActivity(), committee_house);
                    c_listview.setAdapter(cAdapter);
                    setListener(committee_house);

                } else if(tab.getPosition() == 1) {
                    // Setup and Handover data to listview (initialize to by state)
                    c_listview = (ListView)view.findViewById(R.id.c_listview);
                    cAdapter = new AdapterCommittees(getActivity(), committee_senate);
                    c_listview.setAdapter(cAdapter);
                    setListener(committee_senate);

                } else if(tab.getPosition() == 2) {
                    // Setup and Handover data to listview (initialize to by state)
                    c_listview = (ListView)view.findViewById(R.id.c_listview);
                    cAdapter = new AdapterCommittees(getActivity(), committee_joint);
                    c_listview.setAdapter(cAdapter);
                    setListener(committee_joint);
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });



        new AsyncFetch().execute();

        return view;
    }
    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(getActivity());
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL("http://zjiao.cy8pm7xxjw.us-west-1.elasticbeanstalk.com/?apikey=ff7bc0029eb24bb392f848926acde51c&database=committees&per_page=all");

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }


                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {
                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();
            try {

                JSONObject response = new JSONObject(result);
                JSONArray jArray = response.optJSONArray("results");
                // Extract data from json and store into ArrayList as class objects
                for(int i = 0;i < jArray.length();i++){

                    JSONObject json_data = jArray.getJSONObject(i);
                    CommitteesData info = new CommitteesData();


                    info.setCommittee_id(json_data.getString("committee_id"));
                    info.setCommittee_name(json_data.getString("name"));

                    if(json_data.getString("chamber").equals("house")) {
                        info.setChamber("House");
                    } else if (json_data.getString("chamber").equals("senate")) {
                        info.setChamber("Senate");
                    } else {
                        info.setChamber("Joint");
                    }

                    if(json_data.has("parent_committee_id") && !json_data.isNull("parent_committee_id")) {
                        info.setParent(json_data.getString("parent_committee_id"));
                    } else {
                        info.setParent("N.A.");
                    }

                    if(json_data.has("phone") && !json_data.isNull("phone")) {
                        info.setPhone(json_data.getString("phone"));
                    } else {
                        info.setPhone("N.A.");
                    }

                    if(json_data.has("office") && !json_data.isNull("office")) {
                        info.setOffice(json_data.getString("office"));
                    } else {
                        info.setOffice("N.A.");
                    }

                    if(info.getChamber().equals("House")) {
                        committee_house.add(info);
                    } else if (info.getChamber().equals("Senate")) {
                        committee_senate.add(info);
                    } else if (info.getChamber().equals("Joint")) {
                        committee_joint.add(info);
                    }
                }


                Comparator<CommitteesData> Committee_Name = new Comparator<CommitteesData>() {
                    public int compare(CommitteesData object1, CommitteesData object2) {
                        int res = String.CASE_INSENSITIVE_ORDER.compare(object1.getCommittee_name(), object2.getCommittee_name());
                        return res;
                    }
                };

                Collections.sort(committee_house, Committee_Name);

                Collections.sort(committee_senate, Committee_Name);

                Collections.sort(committee_joint, Committee_Name);



                // Setup and Handover data to listview (initialize to by state)
                c_listview = (ListView)view.findViewById(R.id.c_listview);
                cAdapter = new AdapterCommittees(getActivity(), committee_house);
                c_listview.setAdapter(cAdapter);
                setListener(committee_house);


            } catch (JSONException e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }


    private void setListener(final List<CommitteesData> data) {
        c_listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                CommitteesData info = data.get(position);
                Intent intent = new Intent(getActivity(), ViewDetails_C.class);
                intent.putExtra(EXTRA_MESSAGE, info);
                startActivity(intent);
            }
        });


    }


    public Comparator<LegislatorsData> LastName_Order = new Comparator<LegislatorsData>() {
        public int compare(LegislatorsData object1, LegislatorsData object2) {
            int res = String.CASE_INSENSITIVE_ORDER.compare(object1.getLast_name(), object2.getLast_name());
            return res;
        }
    };

}