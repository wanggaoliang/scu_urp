package com.wangr.ro.ro.dataBaseApi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by wangr on 2018/2/2.
 */

public class mSQLiteOpenHelper extends SQLiteOpenHelper {

    public  mSQLiteOpenHelper (Context context) {

        super(context, "schedule.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        System.out.println("数据库oncreate");

        db.execSQL("create table syllabus ( name varchar(50) primary key,teacher varchar(40),startweek integer,overweek integer,startsection integer,oversection integer,local varchar(50),site varchar(50),day integer,isonce integer)");
        db.execSQL("create table grade ( name varchar(50) primary key,xuefen integer,xingzhi varchar(10),grade integer)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("正价");
    }
}
