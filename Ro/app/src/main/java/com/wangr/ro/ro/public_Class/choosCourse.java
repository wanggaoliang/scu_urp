package com.wangr.ro.ro.public_Class;


import android.os.Handler;
import android.os.Message;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangr on 2018/3/20.
 */

public class choosCourse {
    private String zjh;
    private String mm;
    private String sessionId;
    private List<Map<String,String>> lessonlist;

    public  choosCourse(){
        lessonlist=new ArrayList<Map<String, String>>();
    }
    public  choosCourse(String zjh,String mm){
        this.zjh=zjh;
        this.mm=mm;
        lessonlist=new ArrayList<Map<String, String>>();

    }


    public  String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setTlessonmap(String kch,String cxkxh){
        Map<String, String> tlessonmap = new HashMap<String, String>();
        tlessonmap.put("kch",kch);
        tlessonmap.put("preActionType","2");
        tlessonmap.put("actionType","5");
        tlessonmap.put("kcm","");
        tlessonmap.put("cxkxh",cxkxh);
        tlessonmap.put("pageNumber","-2");
        tlessonmap.put("kkxsjc","");
        tlessonmap.put("skjc","");
        tlessonmap.put("skjs","");
        tlessonmap.put("skxq","");
        lessonlist.add(tlessonmap);
    }
    public String verifyCourse(Map<String,String> tlessonmap) {
            try {
                Document origin1 = Jsoup.connect("http://zhjw.scu.edu.cn/xkAction.do?actionType=1")
                        .header("Referer", "http://zhjw.scu.edu.cn/login.jsp")
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                        .cookie("JSESSIONID", sessionId)
                        .get();
                Document origin2 = Jsoup.connect("http://zhjw.scu.edu.cn/xkAction.do?/xkAction.do?actionType=5&pageNumber=-1&cx=ori")
                        .header("Referer", "http://zhjw.scu.edu.cn/login.jsp")
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                        .cookie("JSESSIONID", sessionId)
                        .get();
                Document origin3 = Jsoup.connect("http://zhjw.scu.edu.cn/xkAction.do")
                        .data(tlessonmap)
                        .header("Referer", "http://zhjw.scu.edu.cn/login.jsp")
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                        .cookie("JSESSIONID", sessionId)
                        .post();
                Document origin4 = Jsoup.connect("http://zhjw.scu.edu.cn/xkAction.do")
                        .data("kcId", tlessonmap.get("kch")+"_"+tlessonmap.get("cxkxh"))
                        .data("preActionType", "5")
                        .data("actionType", "9")
                        .header("Referer", "http://zhjw.scu.edu.cn/login.jsp")
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                        .cookie("JSESSIONID", sessionId)
                        .post();
                Elements tis = origin4.select("strong");
                return tis.get(0).text();
            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
        }


    public int getLessonListSize(){
        return lessonlist.size();
    }
    public Map<String, String> getLesson(int i){
        return lessonlist.get(i);
    }





    public  int login() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("zjh", zjh);
        map.put("mm", mm);

        try {
            Connection.Response response = Jsoup.connect("http://zhjw.scu.edu.cn/loginAction.do")
                    .header("Referer","http://zhjw.scu.edu.cn/login.jsp")
                    .header( "User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
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

}
