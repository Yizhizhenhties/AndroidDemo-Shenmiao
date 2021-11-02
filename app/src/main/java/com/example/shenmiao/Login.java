package com.example.shenmiao;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import at.markushi.ui.CircleButton;

public class Login extends AppCompatActivity {
    private EditText accountEdit;
    private EditText passwordEdit;
    private CircleButton login;
    private MyDatabaseHelper dbHelper;
    private static final String TAG = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        accountEdit = (EditText) findViewById(R.id.account);
        passwordEdit = (EditText) findViewById(R.id.password);
        passwordEdit.setInputType(0x81);
        login = (CircleButton) findViewById(R.id.login_button);
        dbHelper = new MyDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                Cursor cursor = db.query("Informationtable", null, null, null, null, null, null);
                String rightpassword = "";
                String name = "";
                String phonenumber = "";
                String head = "";
                int if_admin = 0;
                if (cursor.moveToFirst()) {
                    do {
                        if (account.equals(cursor.getString(cursor.getColumnIndex("identifynumber")))) {
                            rightpassword = cursor.getString(cursor.getColumnIndex("password"));
                            name = cursor.getString(cursor.getColumnIndex("name"));
                            phonenumber = cursor.getString(cursor.getColumnIndex("phonenumber"));
                            if_admin = cursor.getInt(cursor.getColumnIndex("if_admin"));
                            head = cursor.getString(cursor.getColumnIndex("head"));
                            break;
                        }
                    } while (cursor.moveToNext());
                }
                cursor.close();
                if (password.equals(rightpassword)) {
                    Intent intent = new Intent(Login.this,MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("identifynumber",account);
                    bundle.putString("password",password);
                    bundle.putString("name",name);
                    bundle.putInt("if_admin",if_admin);
                    bundle.putString("phonenumber",phonenumber);
                    bundle.putString("head",head);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                    builder.setTitle("提示信息");
                    builder.setMessage("登陆失败，账号错误或密码错误");
                    builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.show();
                    accountEdit.setText("");
                    passwordEdit.setText("");
                }
            }
        });

    }
}