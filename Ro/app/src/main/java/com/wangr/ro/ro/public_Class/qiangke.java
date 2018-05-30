package com.wangr.ro.ro.public_Class;

import android.content.Context;
import android.os.Message;

import com.wangr.ro.ro.dataBaseApi.DataBaseAPI;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangr on 2018/3/5.
 */

public class qiangke {
    private String zjh;
    private static String sessionId;

    public static String getSessionId() {
        return sessionId;
    }

    public static void setSessionId(String sessionId) {
        qiangke.sessionId = sessionId;
    }
    public  static int login(String zjh,String mm) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("zjh", zjh);
        map.put("mm", mm);

        try {
            Connection.Response response = Jsoup.connect("http://zhjw.scu.edu.cn/loginAction.do")
                    .data(map)
                    .method(Connection.Method.POST)
                    .timeout(2000)
                    .execute();
            Document doc1 = response.parse();
            String conclution=doc1.select("title").text();
            if(conclution.equals("四川大学本科教务系统 - 登录")){
                return 1;
            }
            else if(conclution.equals("学分制综合教务")){
                sessionId = response.cookie("JSESSIONID");
                return 0;
            }
            else{
                return -1;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return -2;
        }

    }
    public static void getGrade(final Context context){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DataBaseAPI dataBaseAPI=new DataBaseAPI(context);
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("zjh", "2016141462309");
                    map.put("mm", "4992700wr");
                    Connection.Response response = Jsoup.connect("http://zhjw.scu.edu.cn/loginAction.do")
                            .data(map)
                            .method(Connection.Method.POST)
                            .timeout(20000)
                            .execute();
                    Document doc1 = response.parse();
                    String sessionId = response.cookie("JSESSIONID");
                    Document objectDoc = Jsoup.connect("http://202.115.47.141/gradeLnAllAction.do?type=ln&oper=qbinfo&lnxndm=2017-2018%D1%A7%C4%EA%C7%EF(%C1%BD%D1%A7%C6%DA)#qb_2017-2018%E5%AD%A6%E5%B9%B4%E7%A7%8B(%E4%B8%A4%E5%AD%A6%E6%9C%9F)")
                            .cookie("JSESSIONID", sessionId)
                            .get();
                    Elements kemus = objectDoc.select("tr.odd");
                    for(int i=0;i<kemus.size();i++){
                        Elements tis = kemus.get(i).select("td");
                        String title=tis.get(2).text();
                        String xuefen=tis.get(4).text();
                        String xingzhi=tis.get(5).text();
                        String grade=tis.get(6).text();
                        dataBaseAPI.replace(title,xuefen,xingzhi,grade);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
