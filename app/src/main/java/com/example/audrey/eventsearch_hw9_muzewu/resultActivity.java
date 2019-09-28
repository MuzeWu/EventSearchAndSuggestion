package com.example.audrey.eventsearch_hw9_muzewu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class resultActivity extends AppCompatActivity implements CustomAdapter.ItemClickListener{
    public static final String EXTRA_MESSAGE = "com.example.android.audrey.eventsearch_hw9_muzewu.extra.MESSAGE";
    CustomAdapter adapter;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();

        try{
            String result = intent.getStringExtra(Tab1.EXTRA_MESSAGE);
            Log.i("result Activity", "onCreate: " + result);
            JSONObject responseObject = new JSONObject(result);
//            if(responseObject.hasOwnProperty("_embedded"))
            if(responseObject.has("_embedded")) {
                findViewById(R.id.noSearchResult).setVisibility(View.GONE);
                findViewById(R.id.recycler_view).setVisibility(View.VISIBLE);
                JSONArray array = responseObject.getJSONObject("_embedded").getJSONArray("events");
                ArrayList<String> eventNames = new ArrayList<>();
                ArrayList<String> eventVenues = new ArrayList<>();
                ArrayList<String> eventTimes = new ArrayList<>();
                ArrayList<String> eventCate = new ArrayList<>();
                ArrayList<String> eventID = new ArrayList<>();
                for(int i = 0; i < array.length(); i ++) {
                    JSONObject row = array.getJSONObject(i);
                    String eventName = row.getString("name");
                    String date = row.getJSONObject("dates").getJSONObject("start").getString("localDate");
                    if(row.getJSONObject("dates").getJSONObject("start").has("localTime")) {
                        date +=  " " + row.getJSONObject("dates").getJSONObject("start").getString("localTime");
                    }
                    String cate = "";
                    if(row.has("classifications")) {
                        cate = row.getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name");
                    }
                    String venue = row.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
                    String id = row.getString("id");
                    eventNames.add(eventName);
                    eventVenues.add(venue);
                    eventTimes.add(date);
                    eventCate.add(cate);
                    eventID.add(id);
                }
                RecyclerView recyclerView = findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager((this)));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                adapter = new CustomAdapter(this, eventNames, eventVenues, eventTimes, eventCate, eventID);
                adapter.setClickListener(this);
                recyclerView.setAdapter(adapter);
            }
            else {
                findViewById(R.id.noSearchResult).setVisibility(View.VISIBLE);
                findViewById(R.id.recycler_view).setVisibility(View.GONE);
            }
        }
        catch (Exception e) {
            Log.e("resultActivityError", "onCreate: " + e);
            e.printStackTrace();
        }
        Toolbar mToolbar = findViewById(R.id.toolbarR);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    @Override
    public void onItemClick(View view, int position) {
        //Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        try{
            final Intent intent = new Intent(this,detailsActivity.class);
            String selectedID = adapter.getItem(position);
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
    @Override
    public void onResume(){
        super.onResume();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager((this)));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }
}

