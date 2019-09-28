package com.example.audrey.eventsearch_hw9_muzewu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class imgAdapter extends RecyclerView.Adapter<imgAdapter.ViewHolder> {
    private List<String> imgData;
    private LayoutInflater mInflater;

    imgAdapter(Context context, List<String> links){
        this.mInflater = LayoutInflater.from(context);
        this.imgData = links;
    }

    @Override
    public imgAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.img_holder_layout, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(imgAdapter.ViewHolder holder, int position) {
        String link = imgData.get(position);
        Picasso.with(holder.itemView.getContext()).load(link).into(holder.img);

    }

    @Override
    public int getItemCount() {
        return imgData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.artistImage);
        }
    }
}
