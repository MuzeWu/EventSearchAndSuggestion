package com.example.audrey.eventsearch_hw9_muzewu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class detailsActivity extends AppCompatActivity {

    public static JSONObject details;
    private String date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        String response = intent.getStringExtra(resultActivity.EXTRA_MESSAGE);
        try {
            details = new JSONObject(response);
            final String name = details.getString("name");
            TextView detailName = findViewById(R.id.detailName);

            if(name.length() > 20) {
                int i = 20;
                while (name.charAt(i) != ' ') {
                    i --;
                }
                String subName = name.substring(0,i);
                subName += "...";
                detailName.setText(subName);
            }
            else {
                detailName.setText(name);
            }
            final String venue = details.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
            date = details.getJSONObject("dates").getJSONObject("start").getString("localDate");
            if(details.getJSONObject("dates").getJSONObject("start").has("localTime")) {
                date = details.getJSONObject("dates").getJSONObject("start").getString("localDate") + " " +  details.getJSONObject("dates").getJSONObject("start").getString("localTime");
            }
            final String date = details.getJSONObject("dates").getJSONObject("start").getString("localDate") + " " +  details.getJSONObject("dates").getJSONObject("start").getString("localTime");
            final String seg = details.getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name");
            final String website = details.getString("url");
            final String id = details.getString("id");

            ImageView twitter = findViewById(R.id.twitter);
            twitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
//                Toast.makeText(v.getContext(), "Item " + position + " is clicked.", Toast.LENGTH_SHORT).show();
                    String twitterUrl = "https://twitter.com/intent/tweet?text=Check out " + name + " located at " + venue + ". Website: " + website;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterUrl));
                    v.getContext().startActivity(browserIntent);
                }
            });

            final ImageButton favButtonD = findViewById(R.id.favButtonD);
            final Set<String> storage = MainActivity.sharedpreferences.getStringSet("savedEvents", new HashSet<String>());
            Log.i("detail storage", "onCreate: " + storage);
            final JSONObject itemObj = new JSONObject();
            try{
                itemObj.put("name", name);
                itemObj.put("venue", venue);
                itemObj.put("date", date);
                itemObj.put("segment", seg);
                itemObj.put("id", id);
            }
            catch (Exception e) {
                Log.e("itemObj err", "",e);
            }
            Log.i("itemObj", "onCreate: " + itemObj);

            if(storage.contains(itemObj.toString())) {
                favButtonD.setImageResource(R.drawable.heart_fill_red);
            }
            else {
                Log.i("no contains", "onCreate: ");
            }

            favButtonD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if(storage.contains(itemObj.toString())) {
                        storage.remove(itemObj.toString());
                        favButtonD.setImageResource(R.drawable.heart_fill_white);
                        Toast.makeText(v.getContext(), name + " was removed from favorites ", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        storage.add(itemObj.toString());
                        favButtonD.setImageResource(R.drawable.heart_fill_red);
                        Toast.makeText(v.getContext(), name + " was added to favorites ", Toast.LENGTH_SHORT).show();
                    }
                    SharedPreferences.Editor editor = MainActivity.sharedpreferences.edit();
                    editor.putStringSet("savedEvents", storage);
                    editor.apply();
                }
            });
        }
        catch (Exception e) {

        }
        ViewPager viewPager = (ViewPager) findViewById(R.id.pagerD);
        detailViewPagerAdapter myPagerAdapter = new detailViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayoutD);
        tabLayout.setupWithViewPager(viewPager);
        final int[] ICONS = new int[]{
                R.drawable.info,
                R.drawable.artist,
                R.drawable.venue,
                R.drawable.upc
        };

        tabLayout.getTabAt(0).setIcon(ICONS[0]);
        tabLayout.getTabAt(1).setIcon(ICONS[1]);
        tabLayout.getTabAt(2).setIcon(ICONS[2]);
        tabLayout.getTabAt(3).setIcon(ICONS[3]);

        Toolbar mToolbar = findViewById(R.id.toolbarD);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
