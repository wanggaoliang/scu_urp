package com.wangr.ro.ro.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wangr.ro.ro.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wangr on 2018/2/28.
 */

public class MyAdapter extends RecyclerView.Adapter {
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, Object>> listItem;

    public MyAdapter(Context context, ArrayList<HashMap<String, Object>> listItem) {
        inflater = LayoutInflater.from(context);
        this.listItem = listItem;
    }
    //定义Viewholder
    class Viewholder extends RecyclerView.ViewHolder {
        private TextView name,local_site,teacher,section;

        public Viewholder(View root) {
            super(root);
            name = (TextView) root.findViewById(R.id.name);
            local_site = (TextView) root.findViewById(R.id.local_site);
            section = (TextView) root.findViewById(R.id.section);
            teacher = (TextView) root.findViewById(R.id.teacher);
        }

        public TextView getName() { return name; }
        public TextView getLocal_site() { return local_site; }

        public TextView getTeacher() {
            return teacher;
        }

        public TextView getSection() {
            return section;
        }
    }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new Viewholder(inflater.inflate(R.layout.list_cell, null));
    }
    //在这里把ViewHolder绑定Item的布局

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Viewholder vh = (Viewholder) holder; // 绑定数据到ViewHolder里面
        vh.name.setText((String) listItem.get(position).get("name"));
        vh.local_site.setText((String) listItem.get(position).get("local_site"));
        vh.teacher.setText((String) listItem.get(position).get("teacher"));
        vh.section.setText((String) listItem.get(position).get("section"));
    }
    //返回Item数目

        @Override
        public int getItemCount() {
        return listItem.size();
    }
}

