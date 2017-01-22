package com.example.congresssearch;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Favorites extends Fragment {
    public final static String EXTRA_MESSAGE_L = "com.example.congresssearch.ViewDetails_L";
    public final static String EXTRA_MESSAGE_B = "com.example.congresssearch.ViewDetails_B";
    public final static String EXTRA_MESSAGE_C = "com.example.congresssearch.ViewDetails_C";
    private View view;
    private TabLayout tabLayout;
    private ListView f_listview;

    private List<LegislatorsData> fav_l = new ArrayList<>();
    private List<BillsData> fav_b = new ArrayList<>();
    private List<CommitteesData> fav_c = new ArrayList<>();
    private LinkedHashMap<String, Integer> favoriteIndex;
    private ListAdapter fAdapter;
    public static final String PREFS_NAME = "CongressSearch";
    public static final String FAVORITES_L = "Legislators_Favor";
    public static final String FAVORITES_B = "Bills_Favor";
    public static final String FAVORITES_C = "Committees_Favor";


    public Favorites() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_favorites, container, false);
        getActivity().setTitle("Favorites");

        tabLayout = (TabLayout) view.findViewById(R.id.f_tab_layout);
        tabLayout.setSelectedTabIndicatorHeight(0);

        TabLayout.Tab tab1 = tabLayout.newTab().setText("LEGISLATORS");
        tabLayout.addTab(tab1);

        TabLayout.Tab tab2 = tabLayout.newTab().setText("BILLS");
        tabLayout.addTab(tab2);

        TabLayout.Tab tab3 = tabLayout.newTab().setText("COMMITTEES");
        tabLayout.addTab(tab3);

        // Setup and Handover data to listview (initialize to by state)
        f_listview = (ListView)view.findViewById(R.id.f_listview);
        fav_l = getFavorites_L();
        Collections.sort(fav_l, LastName_Order);
        fAdapter = new AdapterLegislators(getActivity(), fav_l);
        f_listview.setAdapter(fAdapter);
        setListenerL(fav_l);

        getNameIndex(fav_l);
        displayIndex(favoriteIndex);


        // Set OnSelectedEvent
        final LinearLayout ll = (LinearLayout)view.findViewById(R.id.side_index_f);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0) {
                    // Setup and Handover data to listview (initialize to by state)
                    f_listview = (ListView)view.findViewById(R.id.f_listview);
                    fav_l = getFavorites_L();
                    Collections.sort(fav_l, LastName_Order);
                    fAdapter = new AdapterLegislators(getActivity(), fav_l);
                    f_listview.setAdapter(fAdapter);
                    setListenerL(fav_l);

                    getNameIndex(fav_l);
                    displayIndex(favoriteIndex);

                    ll.setVisibility(View.VISIBLE);
                }
                else if(tab.getPosition() == 1) {
                    // Setup and Handover data to listview (initialize to by state)
                    f_listview = (ListView)view.findViewById(R.id.f_listview);
                    fav_b = getFavorites_B();
                    Collections.sort(fav_b, ReverseDate_Order);
                    fAdapter = new AdapterBills(getActivity(), fav_b);
                    f_listview.setAdapter(fAdapter);
                    setListenerB(fav_b);

                    //hide the index bar
                    ll.setVisibility(View.GONE);
                }
                else if(tab.getPosition() == 2) {
                    // Setup and Handover data to listview (initialize to by state)
                    f_listview = (ListView)view.findViewById(R.id.f_listview);
                    fav_c = getFavorites_C();
                    Collections.sort(fav_c, CommitteesName_Order);
                    fAdapter = new AdapterCommittees(getActivity(), fav_c);
                    f_listview.setAdapter(fAdapter);
                    setListenerC(fav_c);

                    //hide the index bar
                    ll.setVisibility(View.GONE);

                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        return view;
    }
    private ArrayList<LegislatorsData> getFavorites_L() {
        SharedPreferences settings;
        List<LegislatorsData> favorites;

        settings =  PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (settings.contains(FAVORITES_L)) {
            String jsonFavorites = settings.getString(FAVORITES_L, null);
            Gson gson = new Gson();
            LegislatorsData[] favoriteItems = gson.fromJson(jsonFavorites,
                    LegislatorsData[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<>(favorites);
        } else
            return null;

        return (ArrayList<LegislatorsData>) favorites;
    }
    public Comparator<LegislatorsData> LastName_Order = new Comparator<LegislatorsData>() {
        public int compare(LegislatorsData object1, LegislatorsData object2) {
            int res = String.CASE_INSENSITIVE_ORDER.compare(object1.getLast_name(), object2.getLast_name());
            return res;
        }
    };

    private void setListenerL(final List<LegislatorsData> data) {
        f_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                LegislatorsData info = data.get(position);
                Intent intent = new Intent(getActivity(), ViewDetails_L.class);
                intent.putExtra(EXTRA_MESSAGE_L, info);
                //Learn how to use ForResult to update Former Activity(by rewriting onActivityResult Method)
                startActivityForResult(intent, 999);
            }
        });
    }


    private ArrayList<BillsData> getFavorites_B() {
        SharedPreferences settings;
        List<BillsData> favorites;

        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (settings.contains(FAVORITES_B)) {
            String jsonFavorites = settings.getString(FAVORITES_B, null);
            Gson gson = new Gson();
            BillsData[] favoriteItems = gson.fromJson(jsonFavorites,
                    BillsData[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<>(favorites);
        } else
            return null;

        return (ArrayList<BillsData>) favorites;
    }

    Comparator<BillsData> ReverseDate_Order = new Comparator<BillsData>() {
        public int compare(BillsData object1, BillsData object2) {
            int res = String.CASE_INSENSITIVE_ORDER.compare(object1.getIntroduced_on(), object2.getIntroduced_on());
            return res * -1;
        }
    };


    private void setListenerB(final List<BillsData> data) {
        f_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                BillsData info = data.get(position);
                Intent intent = new Intent(getActivity(), ViewDetails_B.class);
                intent.putExtra(EXTRA_MESSAGE_B, info);
                //Learn how to use ForResult to update Former Activity(by rewriting onActivityResult Method)
                startActivityForResult(intent, 888);
            }
        });
    }

    private ArrayList<CommitteesData> getFavorites_C() {
        SharedPreferences settings;
        List<CommitteesData> favorites;

        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (settings.contains(FAVORITES_C)) {
            String jsonFavorites = settings.getString(FAVORITES_C, null);
            Gson gson = new Gson();
            CommitteesData[] favoriteItems = gson.fromJson(jsonFavorites,
                    CommitteesData[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<>(favorites);
        } else
            return null;

        return (ArrayList<CommitteesData>) favorites;
    }

    Comparator<CommitteesData> CommitteesName_Order = new Comparator<CommitteesData>() {
        public int compare(CommitteesData object1, CommitteesData object2) {
            int res = String.CASE_INSENSITIVE_ORDER.compare(object1.getCommittee_name(), object2.getCommittee_name());
            return res;
        }
    };

    private void setListenerC(final List<CommitteesData> data) {
        f_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                CommitteesData info = data.get(position);
                Intent intent = new Intent(getActivity(), ViewDetails_C.class);
                intent.putExtra(EXTRA_MESSAGE_C, info);
                //Learn how to use ForResult to update Former Activity(by rewriting onActivityResult Method)
                startActivityForResult(intent, 777);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == 999) {
            f_listview = (ListView)view.findViewById(R.id.f_listview);
            fav_l = getFavorites_L();
            Collections.sort(fav_l, LastName_Order);
            fAdapter = new AdapterLegislators(getActivity(), fav_l);
            f_listview.setAdapter(fAdapter);
            setListenerL(fav_l);
            getNameIndex(fav_l);
            displayIndex(favoriteIndex);
        } else if (requestCode == 888) {
            f_listview = (ListView)view.findViewById(R.id.f_listview);
            fav_b = getFavorites_B();
            Collections.sort(fav_b, ReverseDate_Order);
            fAdapter = new AdapterBills(getActivity(), fav_b);
            f_listview.setAdapter(fAdapter);
            setListenerB(fav_b);
        } else if (requestCode == 777) {
            f_listview = (ListView)view.findViewById(R.id.f_listview);
            fav_c = getFavorites_C();
            Collections.sort(fav_c, CommitteesName_Order);
            fAdapter = new AdapterCommittees(getActivity(), fav_c);
            f_listview.setAdapter(fAdapter);
            setListenerC(fav_c);
        }
    }







    private void getNameIndex(List<LegislatorsData> data_house) {
        favoriteIndex = new LinkedHashMap<>();
        for (int i = 0; i < data_house.size(); i++) {
            String state = data_house.get(i).getLast_name();
            String index = state.substring(0, 1);

            if (favoriteIndex.get(index) == null)
                favoriteIndex.put(index, i);
        }
    }

    //rewrite side_index_f  side_index_item
    private void displayIndex(final LinkedHashMap<String, Integer> Indexes) {
        LinearLayout indexLayout = (LinearLayout) view.findViewById(R.id.side_index_f);
        indexLayout.removeAllViews();
        TextView textView;
        List<String> indexList = new ArrayList<>(Indexes.keySet());

        for (String index : indexList) {
            textView = (TextView) getLayoutInflater(Bundle.EMPTY).inflate(
                    R.layout.side_index_item, null);
            textView.setText(index);
            textView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    TextView selectedIndex = (TextView) view;
                    f_listview.setSelection(Indexes.get(selectedIndex.getText()));
                }
            });
            indexLayout.addView(textView);

        }
    }

}
