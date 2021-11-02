package com.example.shenmiao;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.shenmiao.PopupWindow.CommonPopupWindow;
import com.example.shenmiao.PopupWindow.CommonUtil;
import com.example.shenmiao.utils.FileStorage;
import com.example.shenmiao.utils.SPUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class Fragment2 extends Fragment {
    private View view;
    private int if_admin;
    private String name;
    private String phonenumber;
    private String identifynumber;
    private LinearLayout yuyue,view_yuyue,view_jiezhong,admin_enter;
    private Bundle bun;

    public Fragment2(Bundle bundle) {
        if_admin = bundle.getInt("if_admin");
        name = bundle.getString("name");
        phonenumber = bundle.getString("phonenumber");
        identifynumber = bundle.getString("identifynumber");
        bun = bundle;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //获取fragment的layout
        view = inflater.inflate(R.layout.myfragment2, container, false);
        yuyue = (LinearLayout)view.findViewById(R.id.yuyue);
        yuyue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Reservation.class);
                Bundle bundle = new Bundle();
                bundle.putString("identifynumber",identifynumber);
                bundle.putString("name",name);
                bundle.putString("phonenumber",phonenumber);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        view_yuyue = (LinearLayout) view.findViewById(R.id.view_yuyue);
        view_yuyue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ViewofReservation.class);
                Bundle bundle = new Bundle();
                bundle.putString("identifynumber",identifynumber);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        view_jiezhong = (LinearLayout)view.findViewById(R.id.view_jiezhong);
        view_jiezhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ViewofVaccination.class);
                Bundle bundle = new Bundle();
                bundle.putString("identifynumber",identifynumber);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        admin_enter = (LinearLayout)view.findViewById(R.id.admin_enter);
        admin_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(if_admin == 0)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("提示信息");
                    builder.setMessage("您无权进入管理员入口");
                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.show();
                }
                else
                {
                    admin_enter enter=new admin_enter();
                    enter.show(getFragmentManager(),"");
                }
            }
        });
        return view;
    }


}