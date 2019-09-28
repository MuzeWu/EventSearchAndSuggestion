package com.example.audrey.eventsearch_hw9_muzewu;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Tab1 extends Fragment {
    private Spinner spinnerCate, spinnerUnit;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    private AutoSuggestAdapter autoSuggestAdapter;
    private boolean fromHere;
    public static final String EXTRA_MESSAGE = "com.example.android.audrey.eventsearch_hw9_muzewu.extra.MESSAGE";



    View myView;

    public Tab1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_tab1, container, false);
        return myView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        final Button buttonSearch = view.findViewById(R.id.searchButton);
        final Button buttonClear = view.findViewById(R.id.clearButton);
        final TextView location = getView().findViewById(R.id.locationText);
        final RadioButton here = getView().findViewById(R.id.radio_here);
        final RadioButton other = getView().findViewById(R.id.radio_other);




        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                //boolean checked = ((RadioButton) myView).isChecked();
                switch(checkedId) {
                    case R.id.radio_here:
                        location.setEnabled(false);
                        getView().findViewById(R.id.warning2).setVisibility(View.GONE);
                        break;
                    case R.id.radio_other:
                        location.setEnabled(true);
                        buttonSearch.setEnabled(true);
                        break;
                }
            }
        });
        spinnerCate = (Spinner) view.findViewById(R.id.spinnerCate);
        spinnerUnit = (Spinner) view.findViewById(R.id.spinnerUnit);
        final AppCompatAutoCompleteTextView autoCompleteTextView =
                view.findViewById(R.id.auto_complete_edit_text);
        //final TextView selectedText = findViewById(R.id.selected_item);
        //Setting up the adapter for AutoSuggest
        autoSuggestAdapter = new AutoSuggestAdapter(view.getContext(),
                android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(autoSuggestAdapter);
        autoCompleteTextView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        //selectedText.setText(autoSuggestAdapter.getObject(position));
                    }
                });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
                if(!autoCompleteTextView.getText().equals("")){
                    buttonSearch.setEnabled(true);
                    getView().findViewById(R.id.warning1).setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompleteTextView.getText())) {
                        getKeywordCall(autoCompleteTextView.getText().toString());
                    }
                }
                return false;
            }
        });


       location.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(!other.isChecked() || (other.isChecked()&&!location.getText().equals(""))){
                    buttonSearch.setEnabled(true);
                    getView().findViewById(R.id.warning2).setVisibility(View.GONE);
                }
            }
        });

       buttonClear.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
               autoCompleteTextView.setText("");
               spinnerCate.setSelection(0);
               spinnerUnit.setSelection(0);
               location.setText("");
               TextView disText = getView().findViewById(R.id.disText);
               disText.setText("");
               disText.setHint("10");
               here.setSelected(true);
               other.setSelected(false);
           }
       });
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Intent intent = new Intent(v.getContext(), resultActivity.class);

                String keyword = autoCompleteTextView.getText().toString();
                //keyword = URLEncoder.encode(keyword, "UTF-8");
                String category = spinnerCate.getSelectedItem().toString();
                TextView disText = getView().findViewById(R.id.disText);
                String distance = disText.getText().toString();
                String unit = spinnerUnit.getSelectedItem().toString();
                String address = location.getText().toString();
                if(here.isChecked()) {
                    fromHere = true;
                }
                else {
                    fromHere = false;
                }
                if(keyword.equals("") || (!fromHere && address.equals(""))) {
                    buttonSearch.setEnabled(false);
                    if((keyword.equals(""))){
                        final TextView warning1 = getView().findViewById(R.id.warning1);
                        warning1.setVisibility(View.VISIBLE);
                    }
                    if((!fromHere && address.equals(""))){
                        final  TextView warning2 = getView().findViewById(R.id.warning2);
                        warning2.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    final TextView warning1 = getView().findViewById(R.id.warning1);
                    warning1.setVisibility(View.GONE);
                    final  TextView warning2 = getView().findViewById(R.id.warning2);
                    warning2.setVisibility(View.GONE);
                    buttonSearch.setEnabled(true);
                    Log.i("from", "onClick: " + fromHere);
                    if(fromHere) {
                        String searchUrl = "http://nodejsforhw8-muzewu.appspot.com/search?keyword=" + keyword + "&category=" + category + "&distance=" + distance + "&locLan=34.0522&locLon=-118.244" + "&unit=" + unit;
                        Log.i("here url", "onClick: " + searchUrl);
                        RequestQueue queue = Volley.newRequestQueue(getView().getContext());
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, searchUrl,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try{
                                            //Log.i("searchResult", "onResponse: " + response);
                                            //JSONObject responseObject = new JSONObject(response);
                                            intent.putExtra(EXTRA_MESSAGE, response);
                                            Log.i("tab1 response", "onResponse: ");
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
                                //Log.e("searchError", "on response:",error);
                            }
                        });

// Add the request to the RequestQueue.
                        queue.add(stringRequest);
                    }
                    else {
                        Log.i("from other", "onClick: ");
                        String searchUrl = "http://nodejsforhw8-muzewu.appspot.com/search?keyword=" + keyword + "&category=" + category + "&distance=" + distance + "&location=" + address + "&unit=" + unit;
                        Log.i("searchUrl", "onClick: " + searchUrl);
                        RequestQueue queue = Volley.newRequestQueue(getView().getContext());
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, searchUrl,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try{
                                            //Log.i("searchResult", "onResponse: " + response);
                                            //JSONObject responseObject = new JSONObject(response);
                                            intent.putExtra(EXTRA_MESSAGE, response);
                                            Log.i("tab1 response", "onResponse: ");
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
                                //Log.e("searchError", "on response:",error);
                            }
                        });
// Add the request to the RequestQueue.
                        queue.add(stringRequest);
                    }
                }
            }
        });
    }

    private void getKeywordCall(String text) {
        ApiCall.getKeyword(getView().getContext(), text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                List<String> stringList = new ArrayList<>();
                try {
                    //Log.i("response", "onResponse: " + response);
                    JSONObject responseObject = new JSONObject(response);
                    JSONArray array = responseObject.getJSONObject("_embedded").getJSONArray("attractions");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
                        stringList.add(row.getString("name"));
                    }
                } catch (Exception e) {
                    Log.e("errors", "onResponse: " + e);
                    e.printStackTrace();
                }
                //IMPORTANT: set data here and notify
                autoSuggestAdapter.setData(stringList);
                autoSuggestAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("errors:", "onResponse:" + error);
            }
        });
    }

}
