package com.example.shenmiao;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.shenmiao.PopupWindow.CommonPopupWindow;
import com.example.shenmiao.PopupWindow.CommonUtil;
import com.example.shenmiao.utils.FileStorage;
import com.example.shenmiao.utils.SPUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class Fragment3 extends Fragment{
    private static final String TAG = "";
    private View view;
    private int if_admin;
    private String name,head;
    private String identifynumber;
    private TextView textView,textView1,textView2;
    private LinearLayout linearLayout;
    private ImageView qrcode,rightarrow;

    private ImageView mIcon;

    public Fragment3(Bundle bundle) {
        name = bundle.getString("name");
        if_admin = bundle.getInt("if_admin");
        identifynumber = bundle.getString("identifynumber");
        head = bundle.getString("head");
    }
    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //获取fragment的layout
        view = inflater.inflate(R.layout.myfragment3, container, false);
        textView = view.findViewById(R.id.name);
        textView.setText(name);
        textView1 = view.findViewById(R.id.identifynumber);
        textView1.setText("身份证号:"+identifynumber.substring(0,3)+"************"+identifynumber.substring(15,18));
        textView2 = view.findViewById(R.id.if_admin);

        if(if_admin == 1)
        {
            textView2.setBackgroundResource(R.drawable.textview_border1);
            textView2.setTextColor(Color.parseColor("#FF0000"));
        }
        linearLayout = (LinearLayout)view.findViewById(R.id.settinglayout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logoutorchange logoutorchange=new Logoutorchange();
                logoutorchange.show(getFragmentManager(),"");
            }
        });
        qrcode = (ImageView)view.findViewById(R.id.qrcode);
        qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QRCode qrCode = new QRCode(name);
                qrCode.show(getFragmentManager(),"");
            }
        });
        rightarrow = (ImageView)view.findViewById(R.id.rightarrow);
        rightarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QRCode qrCode = new QRCode(name);
                qrCode.show(getFragmentManager(),"");
            }
        });


        mIcon = view.findViewById(R.id.icon1);
        if(head.equals("p1"))
        {
            mIcon.setImageResource(R.mipmap.p1);
        }
        else
        {
            loadCircleImage(getContext(), head, mIcon);
        }
        mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Head.class);
                Bundle bundle = new Bundle();
                bundle.putString("identifynumber",identifynumber);
                bundle.putString("head",head);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CART_BROADCAST");
        BroadcastReceiver mItemViewListClickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                String msg = intent.getStringExtra("data");
                if("refresh".equals(msg)){
                    refresh();
                }
            }
        };
        broadcastManager.registerReceiver(mItemViewListClickReceiver, intentFilter);

        return view;
    }


    public static void loadCircleImage(Context context, String path, ImageView imageView) {
        // RequestOptions  扩展glide  自定义加载方式
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .circleCrop()//设置圆形
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(path).apply(options).into(imageView);
    }

    private void refresh()
    {
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(getActivity());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Informationtable", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                if (identifynumber.equals(cursor.getString(cursor.getColumnIndex("identifynumber")))) {
                    head = cursor.getString(cursor.getColumnIndex("head"));
                    break;
                }
            } while (cursor.moveToNext());
        }
        if(head.equals("p1"))
        {
            mIcon.setImageResource(R.mipmap.p1);
        }
        else
        {
            loadCircleImage(getContext(),head,mIcon);
        }
    }

}
