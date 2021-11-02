package com.example.shenmiao;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Reservation extends AppCompatActivity {
    private static final String TAG = "";
    private RadioGroup radioGroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private String name,phonenumber,identifynumber;
    private TextView n,p,i,site,time;
    private Button quxiao,queding;
    int jici = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        Bundle bundle=getIntent().getExtras();
        phonenumber = bundle.getString("phonenumber");
        identifynumber = bundle.getString("identifynumber");
        name = bundle.getString("name");
        quxiao = findViewById(R.id.re_quxiao);
        queding = findViewById(R.id.re_queding);
        radioButton1 = findViewById(R.id.jici_1);
        radioButton2 = findViewById(R.id.jici_2);
        radioGroup = findViewById(R.id.jici);
        site = findViewById(R.id.re_site);
        time = findViewById(R.id.re_time);
        n = findViewById(R.id.re_name);
        i = findViewById(R.id.re_identifynumber);
        p = findViewById(R.id.re_phonenumber);
        n.setText(name);
        i.setText(identifynumber.substring(0,3)+"************"+identifynumber.substring(15,18));
        p.setText(phonenumber.substring(0,3)+"****"+phonenumber.substring(7,11));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.jici_1:
                        if (radioButton1.isChecked()) {
                            jici = 1;
                        }
                        break;
                    case R.id.jici_2:
                        if (radioButton2.isChecked()) {
                            jici = 2;
                        }
                        break;
                }
            }
        });
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> t3 = new ArrayList<>();
                t3.add("站点1");t3.add("站点2");t3.add("站点3");

                OptionsPickerView pvOptions = new OptionsPickerBuilder(Reservation.this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                        //返回的分别是三个级别的选中位置
                        String tx = t3.get(options1);
                        site.setText(tx);
                    }
                }).setTitleText("站点选择")
                        .setContentTextSize(20)
                        .build();
                pvOptions.setPicker(t3);
                pvOptions.show();
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar endDate = Calendar.getInstance();
                Calendar startDate = Calendar.getInstance();
                startDate.setTime(date);
                endDate.setTime(date);
                endDate.add(Calendar.MONTH, 1);
                TimePickerView pvCustomTime = new TimePickerBuilder(Reservation.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date d, View v) {//选中事件回调
                        time.setText(simpleDateFormat.format(d));
                    }
                })
                        .setRangDate(startDate, endDate)
                        .setTitleText("预约时间")
                        .setContentTextSize(20)
                        .build();
                pvCustomTime.show();
            }
        });
        queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(time.getText().toString().equals("无") || site.getText().toString().equals("无"))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Reservation.this);
                    builder.setTitle("提示信息");
                    builder.setMessage("预约失败，未填写预约时间或预约站点");
                    builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.show();
                }
                else {
                    MyDatabaseHelper dbHelper = new MyDatabaseHelper(Reservation.this);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    Cursor cursor = db.query("Reservationtable", null, null, null, null, null, null);
                    int flag = 0,flag1 = 0;
                    if (cursor.moveToFirst()) {
                        do {
                            if (identifynumber.equals(cursor.getString(cursor.getColumnIndex("identifynumber"))))
                            {
                                flag = 1;
                                break;
                            }
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                    Cursor cursor1 = db.query("Vaccinationtable", null, null, null, null, null, null);
                    if (cursor1.moveToFirst()) {
                        do {
                            if (identifynumber.equals(cursor1.getString(cursor1.getColumnIndex("identifynumber")))
                                    && jici == cursor1.getInt(cursor1.getColumnIndex("type")))
                            {
                                flag = 1;
                                break;
                            }
                        } while (cursor1.moveToNext());
                    }
                    cursor1.close();
                    if(jici == 2)
                    {
                        Cursor cursor2 = db.query("Vaccinationtable", null, null, null, null, null, null);
                        if (cursor2.moveToFirst()) {
                            do {
                                if (identifynumber.equals(cursor2.getString(cursor2.getColumnIndex("identifynumber")))
                                        && cursor2.getInt(cursor2.getColumnIndex("type")) == 1)
                                {
                                    flag1 = 1;
                                    break;
                                }
                            } while (cursor2.moveToNext());
                        }
                        cursor2.close();
                        if(flag1 == 0)
                        {
                            flag = 1;
                        }
                    }
                    if(flag == 1)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Reservation.this);
                        builder.setTitle("提示信息");
                        builder.setMessage("预约失败，请检查您的预约信息（是否已有预约信息或剂次信息出错）");
                        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder.show();
                    }
                    else{
                        ContentValues values = new ContentValues();
                        values.put("name", name);
                        values.put("identifynumber", identifynumber);
                        values.put("time",time.getText().toString());
                        values.put("phonenumber",phonenumber);
                        values.put("Site",site.getText().toString());
                        values.put("type",jici);
                        db.insert("Reservationtable", null, values);
                        values.clear();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Reservation.this);
                        builder.setTitle("提示信息");
                        builder.setMessage("预约成功");
                        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                        builder.show();
                    }
                }
            }
        });


    }

}
