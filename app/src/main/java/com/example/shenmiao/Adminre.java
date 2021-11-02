package com.example.shenmiao;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.contrarywind.listener.OnItemSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Adminre extends AppCompatActivity {
    private TextView t;
    private String final_date = "所有",final_site = "所有";
    private String choice = "取消预约";
    private String TAG = "";
    private Button b;
    private List<Ad_information1> informationList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_reservation);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        t = findViewById(R.id.ad_re_time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date(System.currentTimeMillis());
        Spinner spinner = (Spinner) findViewById(R.id.ad_re_spin);
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
                TimePickerView pvCustomTime = new TimePickerBuilder(Adminre.this, new OnTimeSelectListener() {
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
        Ad_informationadapter1 adadapter = new Ad_informationadapter1(this,R.layout.information_item1,informationList);
        ListView listView = (ListView) findViewById(R.id.ad_re_list);
        listView.setAdapter(adadapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Ad_information1 information = informationList.get(position);

                final String[] item = new String[]{"取消预约", "接种完成"};
                AlertDialog dia = new AlertDialog.Builder(view.getContext())
                .setTitle("操作如下")
                .setSingleChoiceItems(item, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choice = item[which];
                    }
                }).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(choice.equals("取消预约"))
                                {
                                    String id = information.getIden();
                                    MyDatabaseHelper dbHelper = new MyDatabaseHelper(Adminre.this);
                                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                                    db.execSQL("DELETE FROM  Reservationtable WHERE identifynumber = '"+id+"';");
                                    informationList.remove(position);
                                    adadapter.notifyDataSetChanged();
                                }
                                if(choice.equals("接种完成"))
                                {
                                    String id = information.getIden();
                                    MyDatabaseHelper dbHelper = new MyDatabaseHelper(Adminre.this);
                                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                                    Cursor cursor = db.query("Reservationtable", null, null, null, null, null, null);
                                    String name = "",time ="",site = "";
                                    int type = 1;
                                    if (cursor.moveToFirst()) {
                                        do {
                                            if (id.equals(cursor.getString(cursor.getColumnIndex("identifynumber"))))
                                            {
                                                name = cursor.getString(cursor.getColumnIndex("name"));
                                                time = cursor.getString(cursor.getColumnIndex("time"));
                                                site = cursor.getString(cursor.getColumnIndex("Site"));
                                                type = cursor.getInt(cursor.getColumnIndex("type"));
                                                break;
                                            }
                                        } while (cursor.moveToNext());
                                    }
                                    ContentValues values = new ContentValues();
                                    values.put("name", name);
                                    values.put("identifynumber", id);
                                    values.put("time",time);
                                    values.put("Site",site);
                                    values.put("type",type);
                                    db.insert("Vaccinationtable", null, values);
                                    values.clear();
                                    db.execSQL("DELETE FROM  Reservationtable WHERE identifynumber = '"+id+"';");
                                    informationList.remove(position);
                                    adadapter.notifyDataSetChanged();
                                }
                            }
                        }).setNegativeButton("取消", null).create();
                dia.show();
                return true;
            }
        });

        b = findViewById(R.id.ad_re_queding);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: 1111111111111111111+"+final_date);
                Log.d(TAG, "onClick: 1111111111111111111+"+final_site);
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
        Cursor cursor = db.query("Reservationtable", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String name  = cursor.getString(cursor.getColumnIndex("name"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String site = cursor.getString(cursor.getColumnIndex("Site"));
                String iden = cursor.getString(cursor.getColumnIndex("identifynumber"));
                String ph = cursor.getString(cursor.getColumnIndex("phonenumber"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                Ad_information1 item = new Ad_information1(name,time,site,iden,ph,type);
                informationList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
    private void init_information_1(String t){
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Reservationtable", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                if(t.equals(cursor.getString(cursor.getColumnIndex("time")))) {
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String time = cursor.getString(cursor.getColumnIndex("time"));
                    String site = cursor.getString(cursor.getColumnIndex("Site"));
                    String iden = cursor.getString(cursor.getColumnIndex("identifynumber"));
                    String ph = cursor.getString(cursor.getColumnIndex("phonenumber"));
                    int type = cursor.getInt(cursor.getColumnIndex("type"));
                    Ad_information1 item = new Ad_information1(name, time, site, iden,ph, type);
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
        Cursor cursor = db.query("Reservationtable", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                if(s.equals(cursor.getString(cursor.getColumnIndex("Site")))) {
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String time = cursor.getString(cursor.getColumnIndex("time"));
                    String site = cursor.getString(cursor.getColumnIndex("Site"));
                    String iden = cursor.getString(cursor.getColumnIndex("identifynumber"));
                    String ph = cursor.getString(cursor.getColumnIndex("phonenumber"));
                    int type = cursor.getInt(cursor.getColumnIndex("type"));
                    Ad_information1 item = new Ad_information1(name, time, site, iden,ph, type);
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
        Cursor cursor = db.query("Reservationtable", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                if(s.equals(cursor.getString(cursor.getColumnIndex("Site")))
                && t.equals(cursor.getString(cursor.getColumnIndex("time")))) {
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String time = cursor.getString(cursor.getColumnIndex("time"));
                    String site = cursor.getString(cursor.getColumnIndex("Site"));
                    String ph = cursor.getString(cursor.getColumnIndex("phonenumber"));
                    String iden = cursor.getString(cursor.getColumnIndex("identifynumber"));
                    int type = cursor.getInt(cursor.getColumnIndex("type"));
                    Ad_information1 item = new Ad_information1(name, time, site, iden,ph, type);
                    Log.d(TAG, "init_information: 11111111111" + name);
                    informationList.add(item);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
