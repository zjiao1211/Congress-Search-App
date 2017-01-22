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
public class Bills extends Fragment {

    public final static String EXTRA_MESSAGE = "com.example.congresssearch.ViewDetails_B";
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private View view;
    private TabLayout tabLayout;
    private ListView b_listview;
    private AdapterBills bAdapter;
    private List<BillsData> bill_active;
    private List<BillsData> bill_new = new ArrayList<>();

    public Bills() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_bills, container, false);
        getActivity().setTitle("Bills");

        tabLayout = (TabLayout) view.findViewById(R.id.b_tab_layout);
        tabLayout.setSelectedTabIndicatorHeight(0);

        TabLayout.Tab tab1 = tabLayout.newTab().setText("Active Bills");
        tabLayout.addTab(tab1);

        TabLayout.Tab tab2 = tabLayout.newTab().setText("New Bills");
        tabLayout.addTab(tab2);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0) {
                    // Setup and Handover data to listview (initialize to by state)
                    b_listview = (ListView)view.findViewById(R.id.b_listview);
                    bAdapter = new AdapterBills(getActivity(), bill_active);
                    b_listview.setAdapter(bAdapter);
                    setListener(bill_active);
                } else if(tab.getPosition() == 1) {
                    b_listview = (ListView)view.findViewById(R.id.b_listview);
                    bAdapter = new AdapterBills(getActivity(), bill_new);
                    b_listview.setAdapter(bAdapter);
                    setListener(bill_new);
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


    private class AsyncFetch extends AsyncTask<String, String, String[]> {
        ProgressDialog pdLoading = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        protected String[] doInBackground(String... params) {
            String url1 = "http://zjiao.cy8pm7xxjw.us-west-1.elasticbeanstalk.com/?apikey=ff7bc" +
                    "0029eb24bb392f848926acde51c&database=bills&per_page=50&type=active";
            String url2 = "http://zjiao.cy8pm7xxjw.us-west-1.elasticbeanstalk.com/?apikey=ff7bc" +
                    "0029eb24bb392f848926acde51c&database=bills&per_page=50&type=new";
            String[] result = new String[2];


            result[0] = getJsonData(url1);
            result[1] = getJsonData(url2);
            return result;
        }

        private String getJsonData(String des) {
            HttpURLConnection conn;
            URL url = null;
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL(des);
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


        protected void onPostExecute(String[] result) {

            //this method will be running on UI thread




            pdLoading.dismiss();
            List<List<BillsData>> bill = new ArrayList<>();

            pdLoading.dismiss();
            try {
                for(int k = 0; k < 2; k++) {
                    JSONObject response = new JSONObject(result[k]);
                    JSONArray jArray = response.optJSONArray("results");
                    List<BillsData> bill_List = new ArrayList<>();
                    // Extract data from json and store into ArrayList as class objects
                    for(int i = 0;i < jArray.length();i++){

                        JSONObject json_data = jArray.getJSONObject(i);
                        BillsData info = new BillsData();


                        info.setBill_id(json_data.getString("bill_id"));
                        if(json_data.has("short_title") && !json_data.isNull("short_title")) {
                            info.setShort_title(json_data.getString("short_title"));
                        } else {
                            info.setShort_title("N.A.");
                        }
                        if(json_data.has("official_title") && !json_data.isNull("official_title")) {
                            info.setLong_title(json_data.getString("official_title"));
                        } else {
                            info.setLong_title("N.A.");
                        }
                        info.setIntroduced_on(json_data.getString("introduced_on"));

                        info.setBill_type(json_data.getString("bill_type"));

                        JSONObject sponsorInfo = (JSONObject) json_data.get("sponsor");
                        info.setSponsor(sponsorInfo.getString("title") + ". "
                                + sponsorInfo.get("last_name") + ", " + sponsorInfo.get("first_name"));

                        JSONObject historyInfo = (JSONObject) json_data.get("history");
                        if(historyInfo.getString("active").equals("true")) {
                            info.setStatus("Active");
                        } else {
                            info.setStatus("New");
                        }

                        if(json_data.getString("chamber").equals("house")) {
                            info.setChamber("House");
                        } else {
                            info.setChamber("Senate");
                        }

                        JSONObject urlInfo = (JSONObject) json_data.get("urls");
                        info.setCongress_url(urlInfo.getString("congress"));

                        if(json_data.has("version_status") && !json_data.isNull("version_status")) {
                            info.setVersion_status(json_data.getString("version_status"));
                        } else {
                            info.setVersion_status("N.A.");
                        }

                        if(json_data.has("bill_url") && !json_data.isNull("bill_url")) {
                            info.setBill_url(json_data.getString("bill_url"));
                        } else {
                            info.setBill_url("N.A.");
                        }

                        bill_List.add(info);
                    }
                    bill.add(bill_List);
                }
                bill_active = new ArrayList<>(bill.get(0));
                bill_new = new ArrayList<>(bill.get(1));



                //Sort data by state_name

                Comparator<BillsData> Intro_Order = new Comparator<BillsData>() {
                    public int compare(BillsData object1, BillsData object2) {
                        int res = String.CASE_INSENSITIVE_ORDER.compare(object1.getIntroduced_on(), object2.getIntroduced_on());
                        return res * -1;
                    }
                };

                Collections.sort(bill_active, Intro_Order);
                Collections.sort(bill_new, Intro_Order);





                // Setup and Handover data to listview (initialize to by state)
                b_listview = (ListView)view.findViewById(R.id.b_listview);
                bAdapter = new AdapterBills(getActivity(), bill_active);
                b_listview.setAdapter(bAdapter);
                setListener(bill_active);


            } catch (JSONException e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }
    private void setListener(final List<BillsData> bill) {
        b_listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                BillsData info = bill.get(position);
                Intent intent = new Intent(getActivity(), ViewDetails_B.class);
                intent.putExtra(EXTRA_MESSAGE, info);
                startActivity(intent);
            }
        });
    }


}
