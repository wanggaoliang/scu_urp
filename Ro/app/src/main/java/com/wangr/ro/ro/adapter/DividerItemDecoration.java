package com.wangr.ro.ro.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wangr.ro.ro.R;

/**
 * Created by wangr on 2018/2/28.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private int mHeight = 5;
    private  Paint mPaint;
// 在构造函数里进行绘制的初始化，如画笔属性设置等
    public DividerItemDecoration(Context context) {
        // 轴点画笔(红色)
            mPaint = new Paint();
            mPaint.setColor(Color.GRAY);
            mPaint.setStrokeWidth((float) 5.0);
            mPaint.setAntiAlias(true);
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        if (position != 0) {
            //第一个item预留空间
            outRect.top = mHeight;
        }
    }
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        final int left = parent.getLeft();
        final int right = parent.getRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View childView = parent.getChildAt(i);
            final int bottom = childView.getTop();
            final int top = bottom - mHeight;
            c.drawRect(left, top, right, bottom, mPaint);
        }
    }
}
