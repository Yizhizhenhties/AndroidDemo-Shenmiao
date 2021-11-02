package com.example.shenmiao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ViewofVaccination extends AppCompatActivity {
    private static final String TAG = "";
    private String identifynumber,name1,site1,time1,name2,site2,time2;
    private TextView t1,t2,t3,t4,t5,t6;
    private ImageView quit;
    int type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewofvaccination);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        Bundle bundle = getIntent().getExtras();
        identifynumber = bundle.getString("identifynumber");
        t1 = findViewById(R.id.viewva_t1);
        t2 = findViewById(R.id.viewva_t2);
        t3 = findViewById(R.id.viewva_t3);
        t4 = findViewById(R.id.viewva_t4);
        t5 = findViewById(R.id.viewva_t5);
        t6 = findViewById(R.id.viewva_t6);
        quit = findViewById(R.id.viewva_quxiao);
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(ViewofVaccination.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Vaccinationtable", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                if (identifynumber.equals(cursor.getString(cursor.getColumnIndex("identifynumber"))))
                {
                    type = cursor.getInt(cursor.getColumnIndex("type"));
                    if(type == 1)
                    {
                        name1 = cursor.getString(cursor.getColumnIndex("name"));
                        site1 = cursor.getString(cursor.getColumnIndex("Site"));
                        time1 = cursor.getString(cursor.getColumnIndex("time"));
                        t1.setText(name1);
                        t2.setText(time1);
                        t3.setText(site1);
                    }
                    else
                    {
                        name2 = cursor.getString(cursor.getColumnIndex("name"));
                        site2 = cursor.getString(cursor.getColumnIndex("Site"));
                        time2 = cursor.getString(cursor.getColumnIndex("time"));
                        t4.setText(name2);
                        t5.setText(time2);
                        t6.setText(site2);
                    }
                }
            } while (cursor.moveToNext());
        }
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
