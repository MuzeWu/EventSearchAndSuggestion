package com.example.audrey.eventsearch_hw9_muzewu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class artistAdapter extends RecyclerView.Adapter<artistAdapter.ViewHolder>{
    private List<String> nameData;
    private List<String> flData;
    private List<String> popData;
    private List<String> urlData;
    private LayoutInflater mInflater;
//    private ItemClickListener mClickListener;

    artistAdapter(Context context, List<String> names, List<String> fl, List<String> pop, List<String> url){
        this.mInflater = LayoutInflater.from(context);
        this.nameData = names;
        this.flData = fl;
        this.popData = pop;
        this.urlData = url;
//        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.artist_holder_layout, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String names = nameData.get(position);
        holder.name.setText(names);
        holder.header.setText(names);
        holder.followers.setText(flData.get(position));
        holder.popularity.setText(popData.get(position));
        holder.spotify.setClickable(true);
        holder.spotify.setMovementMethod(LinkMovementMethod.getInstance());
        String url = "<a href='" + urlData.get(position) + " ' target='_blank'> Spotify </a >";
        holder.spotify.setText(Html.fromHtml(url));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return nameData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView header;
        TextView followers;
        TextView popularity;
        TextView spotify;
        RecyclerView imgRecyc;


        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.artist1Name);
            header = itemView.findViewById(R.id.artist1);
            followers = itemView.findViewById(R.id.fl1Text);
            popularity = itemView.findViewById(R.id.pop1Text);
            spotify = itemView.findViewById(R.id.check1Text);
            imgRecyc = itemView.findViewById(R.id.img_recycler_view);
        }
    }


}