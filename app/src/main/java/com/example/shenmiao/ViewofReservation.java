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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

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

public class ViewofReservation extends AppCompatActivity {
    private static final String TAG = "";
    private String identifynumber,name,site,time;
    private TextView t1,t2,t3,t4,cancel;
    private ImageView quit;
    int type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewofreservation);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        Bundle bundle = getIntent().getExtras();
        identifynumber = bundle.getString("identifynumber");
        t1 = findViewById(R.id.viewre_t1);
        t2 = findViewById(R.id.viewre_t2);
        t3 = findViewById(R.id.viewre_t3);
        t4 = findViewById(R.id.viewre_t4);
        quit = findViewById(R.id.viewre_quxiao);
        cancel = findViewById(R.id.viewre_cancelre);
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(ViewofReservation.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Reservationtable", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                if (identifynumber.equals(cursor.getString(cursor.getColumnIndex("identifynumber"))))
                {
                    name = cursor.getString(cursor.getColumnIndex("name"));
                    site = cursor.getString(cursor.getColumnIndex("Site"));
                    time = cursor.getString(cursor.getColumnIndex("time"));
                    type = cursor.getInt(cursor.getColumnIndex("type"));
                    t1.setText(name);
                    t2.setText(time);
                    t3.setText(site);
                    if(type == 1)
                    {
                        t4.setText("第一剂");
                    }
                    else
                    {
                        t4.setText("第二剂");
                    }
                    break;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(!t1.getText().toString().equals("无"))
        {
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewofReservation.this);
                    builder.setTitle("提示信息");
                    builder.setMessage("是否确定取消预约");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            t1.setText("无");
                            t2.setText("无");
                            t3.setText("无");
                            t4.setText("无");
                            db.execSQL("DELETE FROM  Reservationtable WHERE identifynumber = '"+identifynumber+"';");
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.show();
                }
            });
        }
    }
}
