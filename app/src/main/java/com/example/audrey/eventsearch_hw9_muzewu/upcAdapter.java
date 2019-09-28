package com.example.audrey.eventsearch_hw9_muzewu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class upcAdapter extends RecyclerView.Adapter<upcAdapter.ViewHolder>{
    private List<upcEvents> eventsData;
    private List<String> nameData;
    private List<String> artistData;
    private List<String> timeData;
    private List<String> typeData;
    private List<String> urlData;
    private LayoutInflater mInflater;
    private upcAdapter.ItemClickListener mClickListener;


    upcAdapter(Context context, List<upcEvents> data){
        this.mInflater = LayoutInflater.from(context);
        this.eventsData = data;
    }

    @Override
    public upcAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.upc_holder_layout, parent, false);
        return new upcAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(upcAdapter.ViewHolder holder, final int position) {
        String names = eventsData.get(position).eventName;
        holder.name.setText(names);
        holder.artist.setText(eventsData.get(position).eventArtist);
        holder.time.setText(eventsData.get(position).timeString);
        holder.type.setText(eventsData.get(position).eventType);
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                Toast.makeText(v.getContext(), "Item " + position + " is clicked.", Toast.LENGTH_SHORT).show();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(eventsData.get(position).eventUrl));
                v.getContext().startActivity(browserIntent);
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return eventsData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView artist;
        TextView time;
        TextView type;


        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.upcEventName);
            artist = itemView.findViewById(R.id.upcArtist);
            time = itemView.findViewById(R.id.upcTime);
            type = itemView.findViewById(R.id.upcType);
//            itemView.setOnClickListener(this);
        }
//        @Override
//        public void onClick(View view) {
//            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
//        }
    }
    // convenience method for getting data at click position
    String getItem(int id) {
        return nameData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(upcAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
