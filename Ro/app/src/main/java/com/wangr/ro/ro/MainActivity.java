package com.wangr.ro.ro;



import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.wangr.ro.ro.dataBaseApi.mSharePerference;
import com.wangr.ro.ro.widget.WaitAni;



public class MainActivity extends AppCompatActivity {

    private MyHandler handler;
    private WaitAni waitAni;
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
        setContentView(R.layout.activity_main);
        waitAni=findViewById(R.id.waitani);
        waitAni.openAnimation();
        handler=new MyHandler();

        Thread thread=new NetworkThread();
        thread.start();
    }

    class NetworkThread extends Thread {
        @Override
        public void run() {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        Message msg = handler.obtainMessage();
        handler.sendMessage(msg);
    }
        }
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
                Intent intent = new Intent(MainActivity.this,GrabActivity.class);
                startActivity(intent);
        }
    }
}
