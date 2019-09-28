package com.example.audrey.eventsearch_hw9_muzewu;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>{
    private List<String> nameData;
    private List<String> venueData;
    private List<String> dateData;
    private List<String> segData;
    private List<String> idData;
    private Set<String> saveItem = new HashSet<>();
    private LayoutInflater mInflater;


    private ItemClickListener mClickListener;

    CustomAdapter(Context context, List<String> names, List<String> venues, List<String> date, List<String> segment, List<String> id){
        this.mInflater = LayoutInflater.from(context);
        this.nameData = names;
        this.venueData = venues;
        this.dateData = date;
        this.segData = segment;
        this.idData = id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.custom_recyclerview_layout, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String names = nameData.get(position);
        saveItem =  MainActivity.sharedpreferences.getStringSet("savedEvents", new HashSet<String>());
        holder.eventName.setText(names);
        holder.eventVenue.setText(venueData.get(position));
        holder.eventTime.setText(dateData.get(position));
        switch (segData.get(position)) {
            case "Music":
                holder.eventIcon.setImageResource(R.drawable.music_icon);
                break;
            case "Sports":
                holder.eventIcon.setImageResource(R.drawable.sport_icon);
                break;
            case "Arts & Theatre":
                holder.eventIcon.setImageResource(R.drawable.art_icon);
                break;
            case "Film":
                holder.eventIcon.setImageResource(R.drawable.film_icon);
                break;
                default:
                    holder.eventIcon.setImageResource(R.drawable.miscellaneous_icon);
        }
        final JSONObject itemObject = new JSONObject();
        try{
            itemObject.put("name", nameData.get(position));
            itemObject.put("venue", venueData.get(position));
            itemObject.put("date", dateData.get(position));
            itemObject.put("segment", segData.get(position));
            itemObject.put("id", idData.get(position));
        }
        catch (Exception e) {
            Log.e("itemObj err", "", e);
        }
        Log.i("adapter storage", "onBindViewHolder: " + saveItem);
        if(saveItem.contains(itemObject.toString())) {
            Log.i("contains item", "onBindViewHolder: " + nameData.get(position));
            holder.favButton.setImageResource(R.drawable.heart_fill_red);
        }
        holder.favButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(saveItem.contains(itemObject.toString())) {
                    Log.i("contains", "onClick: " + nameData.get(position));
                    holder.favButton.setImageResource(R.drawable.heart_outline_black);
                    saveItem.remove(itemObject.toString());
                    Log.i("after removing", "onClick: " + saveItem);
                    Toast.makeText(holder.itemView.getContext(), nameData.get(position) + " was removed from favorites ", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.i("contains", "onClick: " + idData.get(position));
                    holder.favButton.setImageResource(R.drawable.heart_fill_red);
                    saveItem.add(itemObject.toString());
                    Toast.makeText(holder.itemView.getContext(), nameData.get(position) + " was added to favorites ", Toast.LENGTH_SHORT).show();
                }
                SharedPreferences.Editor editor = MainActivity.sharedpreferences.edit();
                editor.putStringSet("savedEvents", saveItem);
                editor.apply();
            }
        });
//        notifyDataSetChanged();
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return nameData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView eventName;
        TextView eventVenue;
        TextView eventTime;
        ImageView eventIcon;
        ImageButton favButton;

        ViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventName);
            eventVenue = itemView.findViewById(R.id.eventVenue);
            eventTime = itemView.findViewById(R.id.eventTime);
            eventIcon = itemView.findViewById(R.id.icons);
            favButton = itemView.findViewById(R.id.favButtonR);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return idData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}