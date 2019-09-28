package com.example.audrey.eventsearch_hw9_muzewu;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class Tab2 extends Fragment implements favAdapter.ItemClickListener{
    View myView;
    private Set<String> storage =  new HashSet<>();
    public static final String EXTRA_MESSAGE = "com.example.android.audrey.eventsearch_hw9_muzewu.extra.MESSAGE";

    favAdapter myAdapter;

    public Tab2() {
        // Required empty public constructor
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        final ArrayList<String> names = new ArrayList<>();
        final ArrayList<String> venues = new ArrayList<>();
        final ArrayList<String> dates = new ArrayList<>();
        final ArrayList<String> segs = new ArrayList<>();
        final ArrayList<String> ids = new ArrayList<>();
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            storage = MainActivity.sharedpreferences.getStringSet("savedEvents", storage);
            Log.i("storage", "onCreateView: " + storage);
            if(storage.size() == 0) {
                myView.findViewById(R.id.noFavText).setVisibility(myView.VISIBLE);
                myView.findViewById(R.id.fav_recycler_view).setVisibility(View.GONE);
            }
            else {
                try{
                    myView.findViewById(R.id.noFavText).setVisibility(View.GONE);
                    myView.findViewById(R.id.fav_recycler_view).setVisibility(View.VISIBLE);

                    Log.i("entered else", "setUserVisibleHint: ");
                    JSONArray favArr = new JSONArray(storage.toString());
                    for(int i = 0; i < favArr.length(); i ++) {
                        names.add(favArr.getJSONObject(i).getString("name"));
                        venues.add(favArr.getJSONObject(i).getString("venue"));
                        dates.add(favArr.getJSONObject(i).getString("date"));
                        segs.add(favArr.getJSONObject(i).getString("segment"));
                        ids.add(favArr.getJSONObject(i).getString("id"));
                    }
                    Log.i("names", "onCreateView: " + names);
                    myAdapter = new favAdapter(myView.getContext(), this, names, venues, dates, segs, ids);
                    myAdapter.setClickListener(this);
//                    myAdapter.notifyDataSetChanged();
                    RecyclerView recyclerView = myView.findViewById(R.id.fav_recycler_view);
                    recyclerView.setLayoutManager(new LinearLayoutManager((myView.getContext())));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(myAdapter);
                }
                catch (Exception e) {
                    Log.e("tab2", "", e);
                }
                Log.i("tab2", "onViewCreated: " + storage);
            }
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_tab2, container, false);
//        MainActivity.sharedpreferences.edit().clear().apply();
        return myView;
    }



    @Override
    public void onItemClick(View view, int position) {
        //Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        try{
            final Intent intent = new Intent(view.getContext(), detailsActivity.class);
            Log.i("entered click", "onItemClick: ");
            String selectedID = myAdapter.getItem(position);
            String detailURL = "http://nodejsforhw8-muzewu.appspot.com/getDetails?id=" + selectedID;
            RequestQueue queue = Volley.newRequestQueue(view.getContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, detailURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                //Log.i("detailResult", "onResponse: " + response);
                                //JSONObject responseObject = new JSONObject(response);
                                intent.putExtra(EXTRA_MESSAGE, response);
                                startActivity(intent);
                            }
                            catch (Exception e) {
                                Log.e("errors", "onResponse: " + e);
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("searchError", "on response:",error);
                }
            });
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }
        catch (Exception e) {
            Log.e("result error", "onItemClick: ", e);
        }
    }
}
