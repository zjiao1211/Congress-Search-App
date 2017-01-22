package com.example.congresssearch;



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
public class Legislators extends Fragment {

    public final static String EXTRA_MESSAGE = "com.example.congresssearch.ViewDetails_L";
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private View view;
    private TabLayout tabLayout;
    private ListView l_listview;
    private AdapterLegislators lAdapter;
    private List<LegislatorsData> data_state;
    private List<LegislatorsData> data_house = new ArrayList<>();
    private List<LegislatorsData> data_senate = new ArrayList<>();
    private LinkedHashMap<String, Integer> stateIndex;
    private LinkedHashMap<String, Integer> houseIndex;
    private LinkedHashMap<String, Integer> senateIndex;
    public Legislators() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_legislators, container, false);
        getActivity().setTitle("Legislators");



        tabLayout = (TabLayout) view.findViewById(R.id.l_tab_layout);
        tabLayout.setSelectedTabIndicatorHeight(0);

        TabLayout.Tab tab1 = tabLayout.newTab().setText("BY STATES");
        tabLayout.addTab(tab1);

        TabLayout.Tab tab2 = tabLayout.newTab().setText("HOUSE");
        tabLayout.addTab(tab2);

        TabLayout.Tab tab3 = tabLayout.newTab().setText("SENATE");
        tabLayout.addTab(tab3);


        // Set OnSelectedEvent

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        if(tab.getPosition() == 0) {
                            // Setup and Handover data to listview (initialize to by state)
                            l_listview = (ListView)view.findViewById(R.id.l_listview);
                            lAdapter = new AdapterLegislators(getActivity(), data_state);
                            l_listview.setAdapter(lAdapter);
                            setListener(data_state);
                            getStateIndex(data_state);
                            displayIndex(stateIndex);
                        } else if(tab.getPosition() == 1) {
                            // Setup and Handover data to listview (initialize to by state)
                            l_listview = (ListView)view.findViewById(R.id.l_listview);
                            lAdapter = new AdapterLegislators(getActivity(), data_house);
                            l_listview.setAdapter(lAdapter);
                            setListener(data_house);
                            getHouseNameIndex(data_house);
                            displayIndex(houseIndex);

                        } else if(tab.getPosition() == 2) {
                            // Setup and Handover data to listview (initialize to by state)
                            l_listview = (ListView)view.findViewById(R.id.l_listview);
                            lAdapter = new AdapterLegislators(getActivity(), data_senate);
                            l_listview.setAdapter(lAdapter);
                            setListener(data_senate);
                            getSenateNameIndex(data_senate);
                            displayIndex(senateIndex);

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
                 url = new URL("http://zjiao.cy8pm7xxjw.us-west-1.elasticbeanstalk.com/?apikey=ff7bc0029eb24bb392f848926acde51c&database=legislators&per_page=all");

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
            List<LegislatorsData> data = new ArrayList<>();

            pdLoading.dismiss();
            try {

                JSONObject response = new JSONObject(result);
                JSONArray jArray = response.optJSONArray("results");
                // Extract data from json and store into ArrayList as class objects
                for(int i = 0;i < jArray.length();i++){

                    JSONObject json_data = jArray.getJSONObject(i);
                    LegislatorsData info = new LegislatorsData();


                    info.setBioguide_id(json_data.getString("bioguide_id"));
                    info.setParty(json_data.getString("party"));
                    info.setState_name(json_data.getString("state_name"));

                    if(json_data.has("district") && !json_data.isNull("district")) {
                        info.setDistrict(json_data.getInt("district"));
                    } else {
                        info.setDistrict(-1);
                    }
                    info.setTitle(json_data.getString("title"));
                    info.setFirst_name(json_data.getString("first_name"));
                    info.setLast_name(json_data.getString("last_name"));
                    info.setChamber(json_data.getString("chamber"));
                    info.setEmail(json_data.getString("oc_email"));
                    info.setPhone(json_data.getString("phone"));
                    info.setStart_term(json_data.getString("term_start"));
                    info.setEnd_term(json_data.getString("term_end"));
                    info.setState(json_data.getString("state"));
                    if(json_data.has("office") && !json_data.isNull("office")) {
                        info.setOffice(json_data.getString("office"));
                    } else {
                        info.setOffice("N.A.");
                    }

                    if(json_data.has("fax") && !json_data.isNull("fax")) {
                        info.setFax(json_data.getString("fax"));
                    } else {
                        info.setFax("N.A.");
                    }

                    if(json_data.has("birthday") && !json_data.isNull("birthday")) {
                        info.setBirthday(json_data.getString("birthday"));
                    } else {
                        info.setBirthday("N.A.");
                    }

                    if(json_data.has("facebook_id") && !json_data.isNull("facebook_id")) {
                        info.setFacebook(json_data.getString("facebook_id"));
                    } else {
                        info.setFacebook("N.A.");
                    }
                    if(json_data.has("twitter_id") && !json_data.isNull("twitter_id")) {
                        info.setTwitter(json_data.getString("twitter_id"));
                    } else {
                        info.setTwitter("N.A.");
                    }
                    if(json_data.has("website") && !json_data.isNull("website")) {
                        info.setWebsite(json_data.getString("website"));
                    } else {
                        info.setWebsite("N.A.");
                    }
                    if(info.getChamber().equals("house")) {
                        data_house.add(info);
                    } else if (info.getChamber().equals("senate")) {
                        data_senate.add(info);
                    }
                    data.add(info);
                }


                //Sort data by state_name
                data_state=new ArrayList<>(data);
                Comparator<LegislatorsData> State_Order = new Comparator<LegislatorsData>() {
                    public int compare(LegislatorsData object1, LegislatorsData object2) {
                        int res = String.CASE_INSENSITIVE_ORDER.compare(object1.getState_name(), object2.getState_name());
                        return res;
                    }
                };
                Collections.sort(data_state, State_Order);

                //Filter chamber house and Sort data by last_name
                Collections.sort(data_house, LastName_Order);

                //Filter chamber senate and Sort data by last_name
                Collections.sort(data_senate, LastName_Order);




                // Setup and Handover data to listview (initialize to by state)
                l_listview = (ListView)view.findViewById(R.id.l_listview);
                lAdapter = new AdapterLegislators(getActivity(), data_state);
                l_listview.setAdapter(lAdapter);
                setListener(data_state);
                getStateIndex(data_state);
                displayIndex(stateIndex);


            } catch (JSONException e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }
    private void setListener(final List<LegislatorsData> data) {
        l_listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                LegislatorsData info = data.get(position);
                Intent intent = new Intent(getActivity(), ViewDetails_L.class);
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
    private void getStateIndex(List<LegislatorsData> data_state) {
        stateIndex = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < data_state.size(); i++) {
            String state = data_state.get(i).getState_name();
            String index = state.substring(0, 1);

            if (stateIndex.get(index) == null)
                stateIndex.put(index, i);
        }
    }

    private void getHouseNameIndex(List<LegislatorsData> data_house) {
        houseIndex = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < data_house.size(); i++) {
            String state = data_house.get(i).getLast_name();
            String index = state.substring(0, 1);

            if (houseIndex.get(index) == null)
                houseIndex.put(index, i);
        }
    }

    private void getSenateNameIndex(List<LegislatorsData> data_senate) {
        senateIndex = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < data_senate.size(); i++) {
            String state = data_senate.get(i).getLast_name();
            String index = state.substring(0, 1);

            if (senateIndex.get(index) == null)
                senateIndex.put(index, i);
        }
    }


    private void displayIndex(final LinkedHashMap<String, Integer> Indexes) {
        LinearLayout indexLayout = (LinearLayout) view.findViewById(R.id.side_index);
        indexLayout.removeAllViews();
        TextView textView;
        List<String> indexList = new ArrayList<String>(Indexes.keySet());

        for (String index : indexList) {
            textView = (TextView) getLayoutInflater(Bundle.EMPTY).inflate(
                    R.layout.side_index_item, null);
            textView.setText(index);
            textView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    TextView selectedIndex = (TextView) view;
                    l_listview.setSelection(Indexes.get(selectedIndex.getText()));
                }
            });
            indexLayout.addView(textView);

        }
    }




}


