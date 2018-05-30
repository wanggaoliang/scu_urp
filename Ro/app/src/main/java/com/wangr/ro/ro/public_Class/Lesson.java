package com.wangr.ro.ro.public_Class;

/**
 * Created by wangr on 2018/1/31.
         */

public class Lesson {
    private String name;
    private String teacher;
    private String local;
    private String site;
    private String startsection;
    private String oversection;
    private String day;
    public Lesson(String name,String teacher,String local,String site,String startsection,String oversection,String day) {
        this.local=local;
        this.teacher=teacher;
        this.name = name;
        this.site=site;
        this.startsection=startsection;
        this.oversection=oversection;
        this.day=day;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getStartsection() {
        return startsection;
    }

    public void setStartsection(String startsection) {
        this.startsection = startsection;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getOversection() {
        return oversection;
    }

    public void setOversection(String oversection) {
        this.oversection = oversection;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
