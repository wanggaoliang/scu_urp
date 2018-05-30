package com.wangr.ro.ro.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.wangr.ro.ro.public_Class.choosCourse;

public class GrabService extends Service {
    public GrabService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new MyBinder();
    }
    public class MyBinder extends Binder {
        public choosCourse cco;
        public MyBinder(){
            cco=new choosCourse();
        }
        public void setSessionId(String sessionId){
            cco.setSessionId(sessionId);
        }
        public void setLesson(String kch,String cxkxh){
            cco.setTlessonmap(kch,cxkxh);
        }
        public void startToGrab() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        for(int i=0;i<cco.getLessonListSize();i++){
                            String s=cco.verifyCourse(cco.getLesson(i));
                            Intent intent = new Intent();
                            intent.setAction("com.wangr.ro.ro.service");
                            intent.putExtra("response", s);
                            sendBroadcast(intent);
                        }
                    }
                }
            }).start();
        }
    }
}
