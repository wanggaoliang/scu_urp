package com.wangr.ro.ro.public_Class;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by wangr on 2018/3/5.
 */

public class qiangke {
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

}
