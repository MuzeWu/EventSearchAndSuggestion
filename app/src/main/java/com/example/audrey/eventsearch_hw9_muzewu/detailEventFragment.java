package com.example.audrey.eventsearch_hw9_muzewu;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class detailEventFragment extends Fragment {
    View myView;

    public detailEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_detail_event, container, false);
        TextView artistTitle = myView.findViewById(R.id.artistTitle);
        TextView artistText = myView.findViewById(R.id.artistText);
        TextView venueText = myView.findViewById(R.id.venueText);
        TextView timeTitle = myView.findViewById(R.id.timeTitle);
        TextView timeText = myView.findViewById(R.id.timeText);
        TextView cateTitle = myView.findViewById(R.id.cateTitle);
        TextView cateText = myView.findViewById(R.id.cateText);
        TextView pRTitle = myView.findViewById(R.id.pRTitle);
        TextView pRText = myView.findViewById(R.id.pRText);
        TextView statusTitle = myView.findViewById(R.id.statusTitle);
        TextView statusText = myView.findViewById(R.id.statusText);
        TextView buyTitle = myView.findViewById(R.id.buyTitle);
        TextView buyText = myView.findViewById(R.id.buyText);
        TextView seatMapTitle = myView.findViewById(R.id.seatMapTitle);
        TextView seatMapText = myView.findViewById(R.id.seatMapText);
        try{
            JSONObject response = detailsActivity.details;
            if(response.getJSONObject("_embedded").has("attractions")) {
                artistTitle.setText(R.string.artist);
                JSONArray artistArr = response.getJSONObject("_embedded").getJSONArray("attractions");
                String artists = artistArr.getJSONObject(0).getString("name");
                for(int i = 1; i < artistArr.length(); i ++) {
                    artists += " | " + artistArr.getJSONObject(i).getString("name");
                }
                artistText.setText(artists);
            }
            String venue = response.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
            venueText.setText(venue);
            String oldDate = response.getJSONObject("dates").getJSONObject("start").getString("localDate");
            SimpleDateFormat month_date = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(oldDate);
            String formattedDate = month_date.format(date);
            if(response.getJSONObject("dates").getJSONObject("start").has("localTime")) {
                formattedDate = month_date.format(date) + " " + response.getJSONObject("dates").getJSONObject("start").getString("localTime");
            }
            timeText.setText(formattedDate);
            timeTitle.setText(R.string.time);
            String cate;
            if(response.has("classifications")) {
                if(response.getJSONArray("classifications").getJSONObject(0).getJSONObject("genre").getString("name").equals("Undefined")) {
                    cate = response.getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name");
                }
                else {
                    cate = response.getJSONArray("classifications").getJSONObject(0).getJSONObject("genre").getString("name")
                            +  " | " + response.getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name");
                }
                cateText.setText(cate);
                cateTitle.setText(R.string.cate);
            }
            if(response.has("priceRanges")) {
                String price = "$";
                if(response.getJSONArray("priceRanges").getJSONObject(0).has("min")){
                    price += response.getJSONArray("priceRanges").getJSONObject(0).getString("min") + " ~ $"
                            + response.getJSONArray("priceRanges").getJSONObject(0).getString("max");
                }
                pRText.setText(price);
                pRTitle.setText(R.string.pR);
            }
            statusTitle.setText(R.string.status);
            statusText.setText(response.getJSONObject("dates").getJSONObject("status").getString("code"));
            buyTitle.setText(R.string.buy);
            buyText.setClickable(true);
            buyText.setMovementMethod(LinkMovementMethod.getInstance());
            String buyUrl = response.getString("url");
            String ticketText = "<a href='" + buyUrl + " ' target='_blank'> Ticket Master </a >";
            buyText.setText(Html.fromHtml(ticketText));
            if(response.has("seatMap")) {
                seatMapTitle.setText(R.string.sM);
                seatMapText.setClickable(true);
                seatMapText.setMovementMethod(LinkMovementMethod.getInstance());
                String sMUrl = response.getJSONObject("seatmap").getString("staticUrl");
                String sMText = "<a href='" + sMUrl + " ' target='_blank'> View Here </a >";
                seatMapText.setText(Html.fromHtml(sMText));
            }
        }
        catch (Exception e) {

        }

        return myView;
    }

}
