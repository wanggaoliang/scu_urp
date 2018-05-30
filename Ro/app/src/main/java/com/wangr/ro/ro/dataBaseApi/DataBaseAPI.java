package com.wangr.ro.ro.dataBaseApi;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wangr.ro.ro.public_Class.Lesson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangr on 2018/2/2.
 */

public class DataBaseAPI {
    private mSQLiteOpenHelper helper;
    public DataBaseAPI(Context context) {
        helper = new mSQLiteOpenHelper(context);
    }
    public long replace(String name,String teacher,int startweek,int overweek,int startsection,int oversection,String local,String site,int day,int isonce){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put("name", name);
        values.put("teacher",teacher);
        values.put("startweek",startweek );
        values.put("overweek",overweek);
        values.put("startsection",startsection);
        values.put("oversection",oversection);
        values.put("local",local);
        values.put("site",site);
        values.put("day",day);
        values.put("isonce",isonce);
        long result = db.replace("syllabus", null, values); //组拼sql语句实现的.带返回值
        db.close(); //释放资源
        return result; //添加到哪一行，-1添加失败
    }
    public long replace(String name,String xuefen,String xingzhi,String grade){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put("name", name);
        values.put("xuefen",xuefen );
        values.put("xingzhi",xingzhi);
        values.put("grade",grade);
        long result = db.replace("grade", null, values); //组拼sql语句实现的.带返回值
        db.close(); //释放资源
        return result; //添加到哪一行，-1添加失败
    }
    public long add(String name,int startweek,int overweek,int startsection,int oversection,String local,String site,int day,int isonce){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put("name", name);
        values.put("startweek",startweek );
        values.put("overweek",overweek);
        values.put("startsection",startsection);
        values.put("oversection",oversection);
        values.put("local",local);
        values.put("site",site);
        values.put("day",day);
        values.put("isonce",isonce);
        long result = db.insert("syllabus", null, values); //组拼sql语句实现的.带返回值
        db.close(); //释放资源
        return result; //添加到哪一行，-1添加失败
    }
    public long add(String name,String xuefen,String xingzhi,String grade){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put("name", name);
        values.put("xuefen",xuefen );
        values.put("xingzhi",xingzhi);
        values.put("grade",grade);
        long result = db.insert("grade", null, values); //组拼sql语句实现的.带返回值
        db.close(); //释放资源
        return result; //添加到哪一行，-1添加失败
    }
    public List<Lesson> findAll(String args[]){

        List<Lesson> lessons =new ArrayList<Lesson>();

        SQLiteDatabase  db = helper.getReadableDatabase();

        //Cursor cursor = db.rawQuery("select name, sex from student", null);

        Cursor cursor =  db.query("syllabus", new String[]{"name","teacher","startsection","oversection","local","site","day"},"day=? or 1=1",args, null, null, null);

        while(cursor.moveToNext()){
            String name = cursor.getString(0);
            String teacher = cursor.getString(1);
            String startsection = cursor.getString(2);
            String oversection = cursor.getString(3);
            String local= cursor.getString(4);
            String site = cursor.getString(5);
            String day=cursor.getString(6);
           Lesson lesson = new Lesson(name,teacher,local,site,startsection,oversection,day);
            lessons.add(lesson);
        }
        cursor.close();
        db.close();
        return lessons;

    }

}