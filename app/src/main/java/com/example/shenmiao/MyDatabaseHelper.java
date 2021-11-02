package com.example.shenmiao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by PCDNKH on 2020/11/20.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public MyDatabaseHelper(Context context){
        super(context,"Zhenh.db",null,2);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table Informationtable ("
                + "id integer primary key autoincrement, "
                + "identifynumber String, "
                + "password String,"
                + "name String, "
                + "if_admin integer,"
                + "phonenumber String,"
                + "head String)"
        );
        db.execSQL("create table Reservationtable ("
                + "id integer primary key autoincrement, "
                + "name String, "
                + "identifynumber String,"
                + "time String, "
                + "phonenumber String,"
                + "Site String,"
                + "type integer)");
        db.execSQL("create table Vaccinationtable ("
                + "id integer primary key autoincrement, "
                + "name String, "
                + "identifynumber String,"
                + "time String, "
                + "Site String, "
                + "type integer)");

        db.execSQL("insert into Informationtable values(1,'445121200001010000','111111','小一',1,'15976390000','p1')");
        db.execSQL("insert into Informationtable values(2,'445121200001010001','111111','小二',0,'15976390001','p1')");
        db.execSQL("insert into Informationtable values(3,'445121200001010002','111111','小三',0,'15976390002','p1')");
        db.execSQL("insert into Informationtable values(4,'445121200001010003','111111','小四',0,'15976390003','p1')");
        db.execSQL("insert into Informationtable values(5,'445121200001010004','111111','小五',0,'15976390004','p1')");
        db.execSQL("insert into Informationtable values(6,'445121200001010005','111111','小六',0,'15976390005','p1')");
        db.execSQL("insert into Informationtable values(7,'445121200001010006','111111','小七',0,'15976390006','p1')");
        db.execSQL("insert into Informationtable values(8,'445121200001010007','111111','小八',0,'15976390007','p1')");
        db.execSQL("insert into Informationtable values(9,'445121200001010008','111111','小九',0,'15976390008','p1')");
        db.execSQL("insert into Informationtable values(10,'445121200001010009','111111','小十',0,'15976390009','p1')");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    }
}
