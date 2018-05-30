package com.wangr.ro.ro.fragments;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.wangr.ro.ro.R;
import com.wangr.ro.ro.service.GrabService;


public class GrabFragment extends Fragment {
  private EditText edit_kch;
  private EditText edit_cxkxh;
  private TextView info_dis;
  private Handler handler;
  private GrabService.MyBinder binder;
  private MyReceiver receiver=null;
  private ServiceConnection serviceConnection;
  private String sessionid;
  private boolean islogin=false;
    // TODO: Rename and change types of parameters


    public GrabFragment() {
        // Required empty public constructor
      serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service){
          binder =(GrabService.MyBinder)service;
          binder.setSessionId(sessionid);
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
      };
    }


  public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      Bundle bundle=intent.getExtras();
      String s=bundle.getString("response");
      System.out.println(s);
      info_dis.setText(s);
    }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_grab, container, false);
        if (getArguments() != null) {
            sessionid = getArguments().getString("sessionid");
            System.out.println(sessionid);
        }
        Intent intent = new Intent(getActivity(),GrabService.class);
        getActivity().bindService(intent, serviceConnection,  Context.BIND_AUTO_CREATE);
        info_dis=rootView.findViewById(R.id.info_dis);
        edit_kch=rootView.findViewById(R.id.edit_kch);
        edit_cxkxh=rootView.findViewById(R.id.eidt_cxkxh);
        receiver=new MyReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.wangr.ro.ro.service");
        getActivity().registerReceiver(receiver,filter);
        rootView.findViewById(R.id.btn_verfy).setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
          binder.setLesson(edit_kch.getText().toString(),edit_cxkxh.getText().toString());

            }
      });
      rootView.findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
          binder.startToGrab();
        }
      });
        return rootView;
    }

}
