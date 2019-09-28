package com.example.audrey.eventsearch_hw9_muzewu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;


import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class detailArtistFragment extends Fragment {
    View myView;
    ArrayList<String> img1Urls = new ArrayList<>();
    ArrayList<String> img2Urls = new ArrayList<>();

    imgAdapter myAdapter1;
    imgAdapter myAdapter2;

//    int number = 0;

    public detailArtistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_detail_artist, container, false);
//        number ++;
//        Log.i("count artist", "onCreateView: " + number);
        TextView header1 = myView.findViewById(R.id.header1);
        final TextView artist1Name = myView.findViewById(R.id.artist1Name);
        final TextView fl1Text = myView.findViewById(R.id.fl1Text);
        final TextView pop1Text = myView.findViewById(R.id.pop1Text);
        final TextView check1Text = myView.findViewById(R.id.check1Text);

        TextView header2 = myView.findViewById(R.id.header2);
        final TextView artist2Name = myView.findViewById(R.id.artist2Name);
        final TextView fl2Text = myView.findViewById(R.id.fl2Text);
        final TextView pop2Text = myView.findViewById(R.id.pop2Text);
        final TextView check2Text = myView.findViewById(R.id.check2Text);
        JSONObject response = detailsActivity.details;
        try{
            final String artist1 = response.getJSONObject("_embedded").getJSONArray("attractions").getJSONObject(0).getString("name");
            String cate = response.getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name");
            header1.setText(artist1);
            final String imgUrl1 = "http://nodejsforhw8-muzewu.appspot.com/imgSearch?img=" + artist1;
            RequestQueue queue = Volley.newRequestQueue(myView.getContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, imgUrl1,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject imgObject = new JSONObject(response);
                                JSONArray imgArr = imgObject.getJSONArray("items");
                                for(int i = 0; i < imgArr.length(); i ++) {
                                    img1Urls.add(imgArr.getJSONObject(i).getString("link"));
                                }
                                myAdapter1 = new imgAdapter(myView.getContext(), img1Urls);
                                RecyclerView recyclerView = myView.findViewById(R.id.img1_recycler_view);
                                recyclerView.setLayoutManager(new LinearLayoutManager((myView.getContext())));
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(myAdapter1);
                            }
                            catch (Exception e) {
                                Log.e("img response errors", "onResponse: " + e);
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("img Error", "on response:",error);
                }
            });
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
            if(cate.equals("Music")) {
                myView.findViewById(R.id.table1).setVisibility(myView.VISIBLE);
                String spotifyURL1 = "http://nodejsforhw8-muzewu.appspot.com/spotify?artist=" + artist1;
//                RequestQueue queueSpotify = Volley.newRequestQueue(myView.getContext());
                StringRequest stringRequestS = new StringRequest(Request.Method.GET, spotifyURL1,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String responseS) {
                                try{
                                    Log.i("spotify response", "onResponse: " + responseS);
                                    JSONObject responseObject = new JSONObject(responseS);
                                    artist1Name.setText(artist1);
                                    JSONObject artist1Obj = responseObject.getJSONObject("artists").getJSONArray("items").getJSONObject(0);
                                    fl1Text.setText(artist1Obj.getJSONObject("followers").getString("total"));
                                    pop1Text.setText(artist1Obj.getString("popularity"));
                                    check1Text.setClickable(true);
                                    check1Text.setMovementMethod(LinkMovementMethod.getInstance());
                                    String check1 = artist1Obj.getJSONObject("external_urls").getString("spotify");
                                    String spotifyText1 = "<a href='" + check1 + " ' target='_blank'> Spotify </a >";
                                    check1Text.setText(Html.fromHtml(spotifyText1));
                                }
                                catch (Exception e) {
                                    Log.e("errors", "onResponse: " + e);
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("spotifyError", "on response:",error);
                    }
                });
                // Add the request to the RequestQueue.
                queue.add(stringRequestS);
            }
            if(response.getJSONObject("_embedded").getJSONArray("attractions").length() > 1) {
                final String artist2 = response.getJSONObject("_embedded").getJSONArray("attractions").getJSONObject(1).getString("name");
                header2.setText(artist2);
//                Log.i("2nd name", "onCreateView: " + artist2);
                final String imgUrl2 = "http://nodejsforhw8-muzewu.appspot.com/imgSearch?img=" + artist2;
                queue = Volley.newRequestQueue(myView.getContext());
                StringRequest imgRequest2 = new StringRequest(Request.Method.GET, imgUrl2,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONObject imgObject = new JSONObject(response);
                                    JSONArray imgArr = imgObject.getJSONArray("items");
                                    for(int i = 0; i < imgArr.length(); i ++) {
                                        img2Urls.add(imgArr.getJSONObject(i).getString("link"));
                                    }
                                    myAdapter2 = new imgAdapter(myView.getContext(), img2Urls);
                                    RecyclerView recyclerView = myView.findViewById(R.id.img2_recycler_view);
                                    recyclerView.setLayoutManager(new LinearLayoutManager((myView.getContext())));
                                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                                    recyclerView.setAdapter(myAdapter2);
                                }
                                catch (Exception e) {
                                    Log.e("img response errors", "onResponse: " + e);
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("img Error", "on response:",error);
                    }
                });
                // Add the request to the RequestQueue.
                queue.add(imgRequest2);
                if(cate.equals("Music")) {
                    myView.findViewById(R.id.table2).setVisibility(myView.VISIBLE);
                    String spotifyURL2 = "http://nodejsforhw8-muzewu.appspot.com/spotify?artist=" + artist2;
                    queue = Volley.newRequestQueue(myView.getContext());
                    StringRequest spotifyRequest2 = new StringRequest(Request.Method.GET, spotifyURL2,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String responseS) {
                                    try{
                                        JSONObject responseObject = new JSONObject(responseS);
                                        artist2Name.setText(artist2);
                                        JSONObject artistObj = responseObject.getJSONObject("artists").getJSONArray("items").getJSONObject(0);
                                        fl2Text.setText(artistObj.getJSONObject("followers").getString("total"));
                                        pop2Text.setText(artistObj.getString("popularity"));
                                        check2Text.setClickable(true);
                                        check2Text.setMovementMethod(LinkMovementMethod.getInstance());
                                        String check2 = artistObj.getJSONObject("external_urls").getString("spotify");
                                        String spotifyText2 = "<a href='" + check2 + " ' target='_blank'> Spotify </a >";
                                        check2Text.setText(Html.fromHtml(spotifyText2));
                                    }
                                    catch (Exception e) {
                                        Log.e("spotify2 errors", "onResponse: " + e);
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("spotify2Error", "on response:",error);
                        }
                    });
                    // Add the request to the RequestQueue.
                    queue.add(spotifyRequest2);
                }
            }
        }
        catch (Exception e) {}
        return myView;
    }
}

