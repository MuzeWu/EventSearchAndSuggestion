package com.example.audrey.eventsearch_hw9_muzewu;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class detailVenueFragment extends Fragment {
    View myView;
    private MapView myMapView;
    private GoogleMap googleMap;

    public detailVenueFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_detail_venue, container, false);

        TextView venueText = myView.findViewById(R.id.venueText);
        final TextView addressTitle = myView.findViewById(R.id.addressTitle);
        final TextView addressText = myView.findViewById(R.id.addressText);
        TextView cityTitle = myView.findViewById(R.id.cityTitle);
        TextView cityText = myView.findViewById(R.id.cityText);
        TextView phoneTitle = myView.findViewById(R.id.phoneTitle);
        TextView phoneText = myView.findViewById(R.id.phoneText);
        TextView hoursTitle = myView.findViewById(R.id.hoursTitle);
        TextView hoursText = myView.findViewById(R.id.hoursText);
        TextView gRuleTitle = myView.findViewById(R.id.gRuleTitle);
        TextView gRuleText = myView.findViewById(R.id.gRuleText);
        TextView cRuleTitle = myView.findViewById(R.id.cRuleTitle);
        TextView cRuleText = myView.findViewById(R.id.cRuleText);
        final double lan;
        final double lng;
        myMapView = myView.findViewById(R.id.mapView);
        myMapView.onCreate(savedInstanceState);
        myMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ini map error", "onCreateView: ", e);
        }

        myMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
//                googleMap.setMyLocationEnabled(true);
                //To add marker
                LatLng venueLoc = new LatLng(34, -118);
                googleMap.addMarker(new MarkerOptions().position(venueLoc));
                // For zooming functionality
                CameraPosition cameraPosition = new CameraPosition.Builder().target(venueLoc).zoom(14).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        try {
            String venueName = detailsActivity.details.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
            venueText.setText(venueName);
            Log.i("venue name", "onCreateView: " + venueName);
            JSONObject responseVenue = detailsActivity.details.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0);
            if(responseVenue.has("address") && responseVenue.getJSONObject("address").has("line1")) {
                addressTitle.setText(R.string.address);
                addressText.setText(responseVenue.getJSONObject("address").getString("line1"));
            }
            cityTitle.setText(R.string.city);
            cityText.setText(responseVenue.getJSONObject("city").getString("name"));
            if(responseVenue.has("boxOfficeInfo")) {
                if(responseVenue.getJSONObject("boxOfficeInfo").has("phoneNumberDetail")) {
                    phoneTitle.setText(R.string.phone);
                    phoneText.setText(responseVenue.getJSONObject("boxOfficeInfo").getString("phoneNumberDetail"));
                }
                if(responseVenue.getJSONObject("boxOfficeInfo").has("openHoursDetail")) {
                    hoursTitle.setText(R.string.hours);
                    hoursText.setText(responseVenue.getJSONObject("boxOfficeInfo").getString("openHoursDetail"));
                }
            }
            if(responseVenue.has("generalInfo")) {
                if(responseVenue.getJSONObject("generalInfo").has("generalRule")) {
                    gRuleTitle.setText(R.string.gRule);
                    gRuleText.setText(responseVenue.getJSONObject("generalInfo").getString("generalRule"));

                }
                if(responseVenue.getJSONObject("generalInfo").has("childRule")) {
                    cRuleTitle.setText(R.string.cRule);
                    cRuleText.setText(responseVenue.getJSONObject("generalInfo").getString("childRule"));
                }
            }
            lan = responseVenue.getJSONObject("location").getDouble("latitude");
            lng = responseVenue.getJSONObject("location").getDouble("longitude");
            myMapView = myView.findViewById(R.id.mapView);
            myMapView.onCreate(savedInstanceState);
            myMapView.onResume();
            try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("ini map error", "onCreateView: ", e);
            }

            myMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap mMap) {
                    googleMap = mMap;
//                googleMap.setMyLocationEnabled(true);
                    //To add marker
                    Log.i("lantitude", "onMapReady: " + lan);
                    LatLng venueLoc = new LatLng(lan, lng);
                    googleMap.addMarker(new MarkerOptions().position(venueLoc));
                    // For zooming functionality
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(venueLoc).zoom(14).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            });
        }
        catch (Exception e) {
            Log.e("venue error", "onCreateView: ", e);
        }
        return myView;
    }
    @Override
    public void onResume() {
        super.onResume();
        myMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        myMapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        myMapView.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        myMapView.onLowMemory();
    }

}
