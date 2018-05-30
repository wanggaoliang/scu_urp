package com.wangr.ro.ro.dataBaseApi;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by wangr on 2018/3/7.
 */

public class mSharePerference {
    private SharedPreferences sp;

    public mSharePerference(Context context,String dict){
        sp = context.getSharedPreferences(dict, Context.MODE_PRIVATE);

    }

    public void setLoginState() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin", true);
        editor.commit();
    }

    public boolean getLoginState() {
        boolean b = sp.getBoolean("isLogin", false);
        return b;
        }


    public void setAccount(String account){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("account", account);
        editor.commit();
    }
    public String getAccount(){
        String a=sp.getString("account","");
        return a;
    }


    public void setPassword(String password){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("password", password);
        editor.commit();
    }
    public String getPassword(){
        String p=sp.getString("password","");
        return p;
    }



    public String getSessionid(){
        String p=sp.getString("sessionid","");
        return p;
    }
    public void setSessionid(String sessionid){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("sessionid", sessionid);
        editor.commit();
    }

    }

