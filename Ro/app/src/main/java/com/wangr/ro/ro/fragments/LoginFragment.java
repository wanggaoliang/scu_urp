package com.wangr.ro.ro.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wangr.ro.ro.R;
import com.wangr.ro.ro.dataBaseApi.mSharePerference;
import com.wangr.ro.ro.public_Class.qiangke;

public class LoginFragment extends Fragment {
    private Button login_btn;
    private TextView login_status;
    private EditText zjhEdit;
    private EditText mmEdit;
    private Handler handler;
    private Bundle args;
    private mSharePerference msp;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        login_btn = rootView.findViewById(R.id.login_btn);
        login_status = rootView.findViewById(R.id.login_status);
        zjhEdit = rootView.findViewById(R.id.zjh);
        mmEdit = rootView.findViewById(R.id.mm);
        handler = new MyHandler();
         args = new Bundle();
        msp=new mSharePerference(getActivity(),"save.himi");
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mmEdit.getText().toString().isEmpty() || zjhEdit.getText().toString().isEmpty()) {
                    login_status.setText("Lack of Imformation");
                } else {
                    Thread thread = new NetworkThread();
                    thread.start();
                }
            }
        });
        return rootView;
    }
    class NetworkThread extends Thread {
        @Override
        public void run() {
            String s;
            switch (qiangke.login(zjhEdit.getText().toString(), mmEdit.getText().toString())) {
                case 0:
                    s = "Login Successful";
                    msp.setAccount(zjhEdit.getText().toString());
                    msp.setPassword(mmEdit.getText().toString());
                    msp.setSessionid(qiangke.getSessionId());
                    args.putString("sessionid", qiangke.getSessionId() );
                    msp.setLoginState();
                    break;
                case 1:
                    s = "Imformation Error";
                    break;
                case -2:
                    s = "Internet Error";
                    break;
                default:
                    s = "Server Downtime";
            }

            Message msg = handler.obtainMessage();
            msg.obj = s;
            handler.sendMessage(msg);
        }
    }
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            String s = (String) msg.obj;
            login_status.setText(s);
            if (s.equals("Login Successful")) {
                Fragment grab=new GrabFragment();
                grab.setArguments(args);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container,grab)
                        .addToBackStack(null)
                        .commit();
            }

        }

    }
}


