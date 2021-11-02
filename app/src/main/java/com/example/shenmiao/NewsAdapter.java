package com.example.shenmiao;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<News> newsList;
    private Context context;

    public NewsAdapter(Context context, List<News> newsList){
        this.newsList = newsList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_item, viewGroup, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        NewsViewHolder holder = (NewsViewHolder) viewHolder;
        final News one_new = newsList.get(i);
        holder.desc.setText(one_new.getDesc());
        holder.autor.setText(one_new.getAutor());
        Glide.with(context)
                .load(one_new.getImage())
                .into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                News news = newsList.get(i);
                Intent intent = new Intent(context,newview.class);
                Bundle bundle = new Bundle();
                bundle.putString("name",news.getNewsUrl());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

}