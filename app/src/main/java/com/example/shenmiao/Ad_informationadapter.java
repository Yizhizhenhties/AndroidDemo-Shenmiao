package com.example.shenmiao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class Ad_informationadapter  extends ArrayAdapter<Ad_information> {

    private int resourseId;

    public  Ad_informationadapter(Context context, int textresourseId, List<Ad_information> objects)
    {
        super(context, textresourseId, objects);
        resourseId = textresourseId;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        Ad_information information = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourseId,parent,
                    false);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView)view.findViewById(R.id.ad_item_name);
            viewHolder.time = (TextView)view.findViewById(R.id.ad_item_time);
            viewHolder.site = (TextView)view.findViewById(R.id.ad_item_site);
            viewHolder.type = (TextView)view.findViewById(R.id.ad_item_type);
            viewHolder.iden = (TextView)view.findViewById(R.id.ad_item_identify);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.name.setText(information.getName());
        viewHolder.time.setText(information.getTime());
        viewHolder.site.setText(information.getSite());
        viewHolder.iden.setText(information.getIden());
        if(information.getType() == 1)
        {
            viewHolder.type.setText("第一剂");
        }
        else{
            viewHolder.type.setText("第二剂");
        }
        return view;
    }

    class ViewHolder{
        TextView name;
        TextView time;
        TextView site;
        TextView type;
        TextView iden;
    }
}
