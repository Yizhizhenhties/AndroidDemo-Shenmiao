package com.example.shenmiao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Adminva extends AppCompatActivity {
    private TextView t;
    private String final_date = "所有",final_site = "所有";
    private String TAG = "";
    private Button b;
    private List<Ad_information> informationList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_vaccination);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        t = findViewById(R.id.ad_va_time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date(System.currentTimeMillis());
        Spinner spinner = (Spinner) findViewById(R.id.ad_va_spin);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] languages = getResources().getStringArray(R.array.languages);
                final_site = languages[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar endDate = Calendar.getInstance();
                Calendar startDate = Calendar.getInstance();
                startDate.setTime(date);
                endDate.setTime(date);
                endDate.add(Calendar.MONTH, 1);
                TimePickerView pvCustomTime = new TimePickerBuilder(Adminva.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date d, View v) {//选中事件回调
                        t.setText(simpleDateFormat.format(d));
                        final_date = t.getText().toString();
                    }
                })
                        .setRangDate(startDate, endDate)
                        .setTitleText("预约时间")
                        .setContentTextSize(20)
                        .build();
                pvCustomTime.show();
            }
        });

        init_information();
        Ad_informationadapter adadapter = new Ad_informationadapter(this,R.layout.information_item,informationList);
        ListView listView = (ListView) findViewById(R.id.ad_va_list);
        listView.setAdapter(adadapter);

        b = findViewById(R.id.ad_va_queding);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                informationList.clear();
                if(final_site.equals("所有")&& !final_date.equals("所有")) {
                    init_information_1(final_date);
                }
                else if(!final_site.equals("所有")&& final_date.equals("所有")) {
                    init_information_2(final_site);
                }
                else if(!final_site.equals("所有")&& !final_date.equals("所有")) {
                    init_information_3(final_site,final_date);
                }
                else
                {
                    init_information();
                }
                adadapter.notifyDataSetChanged();
            }
        });
    }
    private void init_information(){
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Vaccinationtable", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String name  = cursor.getString(cursor.getColumnIndex("name"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String site = cursor.getString(cursor.getColumnIndex("Site"));
                String iden = cursor.getString(cursor.getColumnIndex("identifynumber"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                Ad_information item = new Ad_information(name,time,site,iden,type);
                Log.d(TAG, "init_information: 11111111111"+name);
                informationList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
    private void init_information_1(String t){
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Vaccinationtable", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                if(t.equals(cursor.getString(cursor.getColumnIndex("time")))) {
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String time = cursor.getString(cursor.getColumnIndex("time"));
                    String site = cursor.getString(cursor.getColumnIndex("Site"));
                    String iden = cursor.getString(cursor.getColumnIndex("identifynumber"));
                    int type = cursor.getInt(cursor.getColumnIndex("type"));
                    Ad_information item = new Ad_information(name, time, site, iden, type);
                    Log.d(TAG, "init_information: 11111111111" + name);
                    informationList.add(item);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
    private void init_information_2(String s){
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Vaccinationtable", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                if(s.equals(cursor.getString(cursor.getColumnIndex("Site")))) {
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String time = cursor.getString(cursor.getColumnIndex("time"));
                    String site = cursor.getString(cursor.getColumnIndex("Site"));
                    String iden = cursor.getString(cursor.getColumnIndex("identifynumber"));
                    int type = cursor.getInt(cursor.getColumnIndex("type"));
                    Ad_information item = new Ad_information(name, time, site, iden, type);
                    Log.d(TAG, "init_information: 11111111111" + name);
                    informationList.add(item);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
    private void init_information_3(String s,String t){
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Vaccinationtable", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                if(s.equals(cursor.getString(cursor.getColumnIndex("Site")))
                        && t.equals(cursor.getString(cursor.getColumnIndex("time")))) {
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String time = cursor.getString(cursor.getColumnIndex("time"));
                    String site = cursor.getString(cursor.getColumnIndex("Site"));
                    String iden = cursor.getString(cursor.getColumnIndex("identifynumber"));
                    int type = cursor.getInt(cursor.getColumnIndex("type"));
                    Ad_information item = new Ad_information(name, time, site, iden, type);
                    Log.d(TAG, "init_information: 11111111111" + name);
                    informationList.add(item);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
