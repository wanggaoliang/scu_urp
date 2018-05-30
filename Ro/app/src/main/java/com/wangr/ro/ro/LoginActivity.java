package com.wangr.ro.ro;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.wangr.ro.ro.dataBaseApi.mSharePerference;
import com.wangr.ro.ro.public_Class.Lesson;
import com.wangr.ro.ro.public_Class.qiangke;
import java.lang.reflect.Field;

public class LoginActivity extends AppCompatActivity {
    private Button login_btn;
    private TextView login_status;
    private EditText zjhEdit;
    private EditText mmEdit;
    private Handler handler;
    private mSharePerference msp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    |View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_login);
        msp=new mSharePerference(LoginActivity.this,"save.himi");
        login_btn=findViewById(R.id.login_btn);
        login_status=findViewById(R.id.login_status);
        zjhEdit=findViewById(R.id.zjh);
        mmEdit=findViewById(R.id.mm);
        handler=new MyHandler();
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mmEdit.getText().toString().isEmpty()||zjhEdit.getText().toString().isEmpty()){
                    login_status.setText("Lack of Imformation");
                }
                else{
                    Thread thread=new NetworkThread();
                    thread.start();
                }
            }
        });
    }
    class NetworkThread extends Thread{
        @Override
        public void run() {
            String s;
            switch (qiangke.login(zjhEdit.getText().toString(),mmEdit.getText().toString())){
                case 0:
                    s="Login Successful";
                    msp.setAccount(zjhEdit.getText().toString());
                    msp.setPassword(mmEdit.getText().toString());
                    msp.setSessionid(qiangke.getSessionId());
                    msp.setLoginState();
                    break;
                case 1:
                    s="Imformation Error";
                    break;
                case -2:
                    s="Internet Error";
                    break;
                default:
                    s="Server Downtime";
            }

            Message msg = handler.obtainMessage();
            msg.obj = s;
            handler.sendMessage(msg);
        }
    }
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            String s =(String)msg.obj;
            login_status.setText(s);
            if(s.equals("Login Successful")){
                Intent intent = new Intent(LoginActivity.this, LessonActivity.class);
                startActivity(intent);
                }

            }

    }

}
