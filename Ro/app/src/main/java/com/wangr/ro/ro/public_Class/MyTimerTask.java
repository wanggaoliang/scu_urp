package com.wangr.ro.ro.public_Class;

import android.os.Handler;

import java.util.TimerTask;

/**
 * Created by wangr on 2018/2/11.
 */

public class MyTimerTask extends TimerTask
{
    Handler handler;

    public MyTimerTask(Handler handler)
    {
        this.handler = handler;
    }

    @Override
    public void run()
    {
        handler.sendMessage(handler.obtainMessage());
    }

}