package com.wangr.ro.ro.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wangr.ro.ro.R;
import com.wangr.ro.ro.dataBaseApi.DataBaseAPI;
import com.wangr.ro.ro.public_Class.Lesson;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AgendaFragment extends Fragment {
    private DataBaseAPI dataBaseAPI;
    private List<Lesson> lessonList;

    public AgendaFragment() {
        DataBaseAPI dbp=new DataBaseAPI(getContext());
        List<Lesson> lessonList=new ArrayList<Lesson>();
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_agenda, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    private void getLessons() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
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
                    Document objectDoc = Jsoup.connect("http://zhjw.scu.edu.cn/xkAction.do?actionType=6")
                            .cookie("JSESSIONID", sessionId)
                            .get();
                    Pattern pattern = Pattern.compile("(\\d+)-(\\d{1,2})");
                    Pattern patterns = Pattern.compile("(\\d+)~(\\d{1,2})");
                    Elements kemus = objectDoc.select("tr.odd");

                    for(int i=0;i<kemus.size();i++) {
                        Elements kemu = kemus.get(i).select("td");
                        String name = kemu.get(2).text();
                        String teacher=kemu.get(7).text();
                        String section=kemu.get(13).text();
                        String local=kemu.get(14).text()+kemu.get(15).text();
                        String classroom=kemu.get(16).text();
                        String day=kemu.get(12).text();
                        String s=kemu.get(11).text();
                        Matcher matcher = pattern.matcher(s);
                        matcher.find();
                        Matcher matchesr = patterns.matcher(section);
                        matchesr.find();
                        if(s.equals("  双周"))
                            dataBaseAPI.replace(name,teacher,2,18,Integer.parseInt(matchesr.group(1)),Integer.parseInt(matchesr.group(2)),local,classroom,Integer.parseInt(day),2);
                        else if(s.equals("  单周"))
                            dataBaseAPI.replace(name,teacher,1,17,Integer.parseInt(matchesr.group(1)),Integer.parseInt(matchesr.group(2)),local,classroom,Integer.parseInt(day),1);
                        else
                            dataBaseAPI.replace(name,teacher,Integer.parseInt(matcher.group(1)),Integer.parseInt(matcher.group(2)),Integer.parseInt(matchesr.group(1)),Integer.parseInt(matchesr.group(2)),local,classroom,Integer.parseInt(day),0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
