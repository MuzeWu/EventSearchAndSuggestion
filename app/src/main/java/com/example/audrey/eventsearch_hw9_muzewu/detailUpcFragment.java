package com.example.audrey.eventsearch_hw9_muzewu;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class detailUpcFragment extends Fragment implements upcAdapter.ItemClickListener{
    View myView;
    upcAdapter myAdapter;

    public detailUpcFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_detail_upc, container, false);

        final ArrayList<String> eventNames = new ArrayList<>();
        final ArrayList<String> artists = new ArrayList<>();
        final ArrayList<String> time = new ArrayList<>();
        final ArrayList<String> types = new ArrayList<>();
        final ArrayList<String> urls = new ArrayList<>();

        final Spinner spinnerFilter = myView.findViewById(R.id.spinnerfilter);
        ArrayAdapter<CharSequence> myArrAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.sortFilter, android.R.layout.simple_spinner_item);
        myArrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(myArrAdapter);

        final Spinner spinnerOrder = myView.findViewById(R.id.spinnerSortOrder);
        ArrayAdapter<CharSequence> adapterOrder = ArrayAdapter.createFromResource(getContext(),
                R.array.sortOrder, android.R.layout.simple_spinner_item);
        adapterOrder.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrder.setAdapter(adapterOrder);
        spinnerOrder.setEnabled(false);

        JSONObject response = detailsActivity.details;
        try{
            String venueName = response.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
            String venueURL = "http://nodejsforhw8-muzewu.appspot.com/venueID?name=" + venueName;
            RequestQueue queue = Volley.newRequestQueue(myView.getContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, venueURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String responseVenues) {
                            try{
                                JSONObject responseObject = new JSONObject(responseVenues);
                                String venueID = responseObject.getJSONObject("resultsPage").getJSONObject("results").getJSONArray("venue").getJSONObject(0).getString("id");
                                String upcURL = "http://nodejsforhw8-muzewu.appspot.com/getEvents?id=" + venueID;
                                RequestQueue queue = Volley.newRequestQueue(myView.getContext());
                                StringRequest stringRequest = new StringRequest(Request.Method.GET, upcURL,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String responseEvents) {
                                                try{
                                                    JSONObject responseObject = new JSONObject(responseEvents);
                                                    if(responseObject.getJSONObject("resultsPage").getString("totalEntries").equals("0")) {
                                                        myView.findViewById(R.id.noUpc).setVisibility(View.VISIBLE);
                                                        myView.findViewById(R.id.upc_recycler_view).setVisibility(View.GONE);
                                                    }
                                                    else {
                                                        myView.findViewById(R.id.noUpc).setVisibility(View.GONE);
                                                        myView.findViewById(R.id.upc_recycler_view).setVisibility(View.VISIBLE);
                                                        JSONArray eventsArr = responseObject.getJSONObject("resultsPage").getJSONObject("results").getJSONArray("event");
                                                        for(int i = 0; i < Math.min(eventsArr.length(), 5) ; i ++) {
                                                            eventNames.add(eventsArr.getJSONObject(i).getString("displayName"));
                                                            artists.add(eventsArr.getJSONObject(i).getJSONArray("performance").getJSONObject(0).getString("displayName"));
                                                            String oldDates = eventsArr.getJSONObject(i).getJSONObject("start").getString("date"); ;
                                                            SimpleDateFormat month_date = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
                                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                                            Date date = sdf.parse(oldDates);
                                                            String formattedDate = month_date.format(date) + " " + eventsArr.getJSONObject(i).getJSONObject("start").getString("time");
                                                            time.add(formattedDate);

                                                            types.add(eventsArr.getJSONObject(i).getString("type"));
                                                            urls.add(eventsArr.getJSONObject(i).getString("uri"));
                                                        }

                                                        final ArrayList<upcEvents> eventsData = new ArrayList<>();
                                                        final ArrayList<upcEvents> defaultData = new ArrayList<>();
                                                        for(int i =0; i < eventNames.size(); i ++) {
                                                            eventsData.add(new upcEvents(eventNames.get(i), artists.get(i), time.get(i), types.get(i),urls.get(i)));
                                                            defaultData.add(new upcEvents(eventNames.get(i), artists.get(i), time.get(i), types.get(i),urls.get(i)));
                                                        }

                                                        myAdapter = new upcAdapter(myView.getContext(), eventsData);
                                                        final RecyclerView recyclerView = myView.findViewById(R.id.upc_recycler_view);
                                                        recyclerView.setLayoutManager(new LinearLayoutManager((myView.getContext())));
                                                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                                                        recyclerView.setAdapter(myAdapter);

                                                        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                            @Override
                                                            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                                                // your code here
                                                                String typeText = spinnerFilter.getSelectedItem().toString();

                                                                //do the sorting
                                                                if(typeText.equals("Default")){
                                                                    //go back to the original
                                                                    myAdapter = new upcAdapter(myView.getContext(), defaultData);
                                                                    recyclerView.setAdapter(myAdapter);
                                                                    spinnerOrder.setEnabled(false);
                                                                }else {
                                                                    spinnerOrder.setEnabled(true);
                                                                    if (typeText.equals("Event Name")) {
                                                                        if (spinnerOrder.getSelectedItem().toString().equals("Ascending")) {
                                                                            Collections.sort(eventsData, new SortbyEventNameASC());
                                                                        } else {
                                                                            Collections.sort(eventsData, new SortbyEventNameDES());
                                                                        }
                                                                    }
                                                                    if (typeText.equals("Time")) {
                                                                        if (spinnerOrder.getSelectedItem().toString().equals("Ascending")) {
                                                                            Collections.sort(eventsData, new SortbyTimeASC());
                                                                        } else {
                                                                            Collections.sort(eventsData, new SortbyTimeDES());
                                                                        }
                                                                    }
                                                                    if (typeText.equals("Artist")) {
                                                                        if (spinnerOrder.getSelectedItem().toString().equals("Ascending")) {
                                                                            Collections.sort(eventsData, new SortbyArtistASC());
                                                                        } else {
                                                                            Collections.sort(eventsData, new SortbyArtistDES());
                                                                        }
                                                                    }
                                                                    if (typeText.equals("Type")) {
                                                                        if (spinnerOrder.getSelectedItem().toString().equals("Ascending")) {
                                                                            Collections.sort(eventsData, new SortbyTypeASC());
                                                                        } else {
                                                                            Collections.sort(eventsData, new SortbyTypeDES());
                                                                        }
                                                                    }
                                                                    Log.i("else", "in else ");
                                                                    myAdapter = new upcAdapter(myView.getContext(), eventsData);
                                                                    recyclerView.setAdapter(myAdapter);
                                                                }

                                                            }
                                                            @Override
                                                            public void onNothingSelected(AdapterView<?> parentView) {

                                                            }
                                                        });

                                                        spinnerOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                            @Override
                                                            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                                                String filter = spinnerFilter.getSelectedItem().toString();
                                                                String orderBy = spinnerOrder.getSelectedItem().toString();
                                                                if(orderBy.equals("Ascending")){
                                                                    if(filter.equals("Event Name")){
                                                                        Collections.sort(eventsData, new SortbyEventNameASC());
                                                                    }
                                                                    if(filter.equals("Time")){
                                                                        Collections.sort(eventsData, new SortbyTimeASC());
                                                                    }
                                                                    if(filter.equals("Artist")){
                                                                        Collections.sort(eventsData, new SortbyArtistASC());
                                                                    }
                                                                    if(filter.equals("Type")){
                                                                        Collections.sort(eventsData, new SortbyTypeASC());
                                                                    }
                                                                    myAdapter = new upcAdapter(myView.getContext(), eventsData);
                                                                    recyclerView.setAdapter(myAdapter);
                                                                }
                                                                if(orderBy.equals("Descending")){
                                                                    if(filter.equals("Event Name")){
                                                                        Collections.sort(eventsData, new SortbyEventNameDES());
                                                                    }
                                                                    if(filter.equals("Time")){
                                                                        Collections.sort(eventsData, new SortbyTimeDES());
                                                                    }
                                                                    if(filter.equals("Artist")){
                                                                        Collections.sort(eventsData, new SortbyArtistDES());
                                                                    }
                                                                    if(filter.equals("Type")){
                                                                        Collections.sort(eventsData, new SortbyTypeDES());
                                                                    }
                                                                    myAdapter = new upcAdapter(myView.getContext(), eventsData);
                                                                    recyclerView.setAdapter(myAdapter);
                                                                }
                                                            }

                                                            @Override
                                                            public void onNothingSelected(AdapterView<?> parentView) {

                                                            }
                                                        });
                                                    }

                                                }
                                                catch (Exception e) {
                                                    Log.e("upc response errors", "onResponse: " + e);
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("upcError2", "on response:",error);
                                    }
                                });
                                queue.add(stringRequest);
                            }
                            catch (Exception e) {
                                Log.e("venueID response error", "onResponse: " + e);
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("upcError1", "on response:",error);
                }
            });
            queue.add(stringRequest);
        }
        catch (Exception e) {
            Log.e("venue name errors", "onCreateView: ", e);
        }
        return myView;
    }
    @Override
    public void onItemClick(View view, int position) {
        Log.i("clicked!", "onItemClick: ");
        Toast.makeText(getActivity(), "You clicked " + myAdapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}

