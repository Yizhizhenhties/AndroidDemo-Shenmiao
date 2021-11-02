package com.example.shenmiao;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

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
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends FragmentActivity implements OnGetContentListener{
    private ViewPager mViewPager;
    private RadioGroup mTabRadioGroup;
    private List<Fragment> mFragments;
    private FragmentPagerAdapter mAdapter;
    private Bundle bundle;
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bundle=getIntent().getExtras();
        if(savedInstanceState!=null)
        {
            bundle = savedInstanceState;
        }
        initView(bundle);
    }
    private void initView(Bundle bundle) {
        // find view
        mViewPager = findViewById(R.id.fragment_vp);
        mTabRadioGroup = findViewById(R.id.tabs_rg);
        // init fragment
        mFragments = new ArrayList<>(3);
        mFragments.add(new Fragment1());
        mFragments.add(new Fragment2(bundle));
        mFragments.add(new Fragment3(bundle));
        // init view pager
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mAdapter);
        // register listener
        mViewPager.addOnPageChangeListener(mPageChangeListener);
        mTabRadioGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewPager.removeOnPageChangeListener(mPageChangeListener);
    }
    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }
        @Override
        public void onPageSelected(int position) {
            RadioButton radioButton = (RadioButton) mTabRadioGroup.getChildAt(position);
            radioButton.setChecked(true);
        }
        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };
    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            for (int i = 0; i < group.getChildCount(); i++) {
                if (group.getChildAt(i).getId() == checkedId) {
                    mViewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    };
    static class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mList;

        public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.mList = list;
        }
        @Override
        public Fragment getItem(int position) {
            return this.mList == null ? null : this.mList.get(position);
        }
        @Override
        public int getCount() {
            return this.mList == null ? 0 : this.mList.size();
        }
    }
    @Override
    public void getContent(String Content) {
        if(Content.length() < 6)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示信息");
            builder.setMessage("修改密码失败，密码不能少于6个字符");
            builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.show();
        }
        else if(Content.length() > 12)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示信息");
            builder.setMessage("修改密码失败，密码不能大于12个字符");
            builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.show();
        }
        else {
            MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);;
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Bundle bundle=getIntent().getExtras();
            String psw = "";
            String id = bundle.getString("identifynumber");
            Cursor cursor = db.query("Informationtable", null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    if (id.equals(cursor.getString(cursor.getColumnIndex("identifynumber")))) {
                        psw = cursor.getString(cursor.getColumnIndex("password"));
                        break;
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
            if (Content.equals(psw)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示信息");
                builder.setMessage("修改密码失败，密码不能与原密码相同");
                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
            }
            else {
                ContentValues values = new ContentValues();
                values.put("password", Content);
                db.update("Informationtable", values, "identifynumber = ?", new String[]{id});
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示信息");
                builder.setMessage("账号密码更改成功");
                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
            }
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState = bundle;
    }

}
