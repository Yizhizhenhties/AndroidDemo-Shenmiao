package com.example.shenmiao;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsViewHolder extends RecyclerView.ViewHolder {
    public NewsViewHolder(@NonNull View itemView) {
        super(itemView);
        desc = itemView.findViewById(R.id.news_desc);
        autor = itemView.findViewById(R.id.news_autor);
        img = itemView.findViewById(R.id.news_image);
    }
    public TextView desc;
    public TextView autor;
    public ImageView img;
}