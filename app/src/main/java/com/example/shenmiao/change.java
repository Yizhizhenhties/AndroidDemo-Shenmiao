package com.example.shenmiao;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class change extends DialogFragment {
    private Window window;
    private View view;
    private Button button1,button2;
    private EditText editText;
    private OnGetContentListener myListener;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        myListener = (OnGetContentListener) activity;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //获取fragment的layout
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.change, container, false);
        button1 = view.findViewById(R.id.change_button1);
        button2 = view.findViewById(R.id.change_button2);
        editText = view.findViewById(R.id.change_password);
        editText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
                myListener.getContent(editText.getText().toString());
            }
        });
        return view;
    }
    @Override
    public void onStart() {
        // 下面这些设置必须在此方法(onStart())中才有效
        window = getDialog().getWindow();
        // 如果不设置这句代码, 那么弹框就会与四边都有一定的距离
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        // 如果不设置宽度,那么即使你在布局中设置宽度为 match_parent 也不会起作用
        params.width = getResources().getDisplayMetrics().widthPixels;
        window.setAttributes(params);
        getDialog().getWindow().getDecorView().setBackground(new ColorDrawable(Color.TRANSPARENT));
        super.onStart();
    }
}
