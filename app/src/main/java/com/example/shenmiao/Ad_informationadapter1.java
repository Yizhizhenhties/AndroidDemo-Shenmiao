package com.example.shenmiao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class Ad_informationadapter1 extends ArrayAdapter<Ad_information1> {

    private int resourseId;

    public Ad_informationadapter1(Context context, int textresourseId, List<Ad_information1> objects)
    {
        super(context, textresourseId, objects);
        resourseId = textresourseId;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        Ad_information1 information = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourseId,parent,
                    false);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView)view.findViewById(R.id.ad_item1_name);
            viewHolder.time = (TextView)view.findViewById(R.id.ad_item1_time);
            viewHolder.site = (TextView)view.findViewById(R.id.ad_item1_site);
            viewHolder.type = (TextView)view.findViewById(R.id.ad_item1_type);
            viewHolder.iden = (TextView)view.findViewById(R.id.ad_item1_identify);
            viewHolder.ph = (TextView)view.findViewById(R.id.ad_item1_phonenumber);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.name.setText(information.getName());
        viewHolder.time.setText(information.getTime());
        viewHolder.site.setText(information.getSite());
        viewHolder.iden.setText(information.getIden());
        viewHolder.ph.setText(information.getPh());
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
        TextView ph;
    }
}
