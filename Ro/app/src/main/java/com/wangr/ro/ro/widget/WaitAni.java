package com.wangr.ro.ro.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by wangr on 2018/3/8.
 */

public class WaitAni extends View {
    private int viewWidth;
    private int viewHeight;
    private int offset=60;
    private int height;
    private int height1;
    private int width;
    private int width1;
    private double radiussq;
    private boolean isMeasured;
    private Paint circlePaint;
    private Paint linePaint;
    private AnimatorSet animatorSet;

    public WaitAni(Context context) {
        super(context);
        init();
    }
    public WaitAni(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public WaitAni(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public int getheight() {
        return height;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isMeasured) {
            isMeasured = true;
            viewHeight = getMeasuredHeight();
            viewWidth = getMeasuredWidth();
            height=viewHeight/3;
            width1=viewWidth/2+2*offset;
            height1=viewHeight/2;
            radiussq=(2*offset)*(2*offset)+(viewHeight/4-viewHeight/2)*(viewHeight/4-viewHeight/2);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        circlePaint.setColor(0xff63dff6);
        canvas.drawLine(viewWidth/2f,viewHeight/4f,width,height,linePaint);
        canvas.drawCircle(width,height,30,circlePaint);
        circlePaint.setColor(0xffff0000);
        canvas.drawLine(viewWidth/2f,viewHeight/4f,viewWidth/2-offset,viewHeight/2,linePaint);
        canvas.drawCircle(viewWidth/2-offset,viewHeight/2,30,circlePaint);
        circlePaint.setColor(0xffebd669);
        canvas.drawLine(viewWidth/2f,viewHeight/4f,viewWidth/2,viewHeight/2,linePaint);
        canvas.drawCircle(viewWidth/2,viewHeight/2,30,circlePaint);
        circlePaint.setColor(0xffff9852);
        canvas.drawLine(viewWidth/2f,viewHeight/4f,viewWidth/2+offset,viewHeight/2,linePaint);
        canvas.drawCircle(viewWidth/2+offset,viewHeight/2,30,circlePaint);
        circlePaint.setColor(0xff63dff6);
        canvas.drawLine(viewWidth/2f,viewHeight/4f,width1,height1,linePaint);
        canvas.drawCircle(width1,height1,30,circlePaint);

    }
    private void init(){
        circlePaint=new Paint();
        circlePaint.setAntiAlias(true);
        linePaint=new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.GRAY);
        animatorSet=new AnimatorSet();
    }
    public ValueAnimator inLeftAnimation() {

        ValueAnimator startAnimator = ValueAnimator.ofFloat(0,1);
        startAnimator.setDuration(500);
        startAnimator.setInterpolator(new AccelerateInterpolator());
        startAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float a=(Float) valueAnimator.getAnimatedValue()*(viewHeight/2-viewHeight/3);
                height = (int)a+viewHeight/3;
                width=viewWidth/2-(int)Math.sqrt(radiussq-(a+viewHeight/12)*(a+viewHeight/12));
                invalidate();
            }
        });
        return startAnimator;
    }
    public ValueAnimator outLeftAnimation() {

        ValueAnimator startAnimator = ValueAnimator.ofFloat(1,0);
        startAnimator.setDuration(500);
        startAnimator.setInterpolator(new DecelerateInterpolator());
        startAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float a=(Float) valueAnimator.getAnimatedValue()*(viewHeight/2-viewHeight/3);
                height = (int)a+viewHeight/3;
                width=viewWidth/2-(int)Math.sqrt(radiussq-(a+viewHeight/12)*(a+viewHeight/12));
                invalidate();
            }
        });
        return startAnimator;
    }
    public ValueAnimator inRightAnimation() {

        ValueAnimator startAnimator = ValueAnimator.ofFloat(0,1);
        startAnimator.setInterpolator(new AccelerateInterpolator());
        startAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float a=(Float) valueAnimator.getAnimatedValue()*(viewHeight/2-viewHeight/3);
                height1 = (int)a+viewHeight/3;
                width1=viewWidth/2+(int)Math.sqrt(radiussq-(a+viewHeight/12)*(a+viewHeight/12));
                invalidate();
            }
        });
        return startAnimator;
    }
    public ValueAnimator outRightAnimation() {

        ValueAnimator startAnimator = ValueAnimator.ofFloat(1,0);
        startAnimator.setInterpolator(new DecelerateInterpolator());
        startAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float a=(Float) valueAnimator.getAnimatedValue()*(viewHeight/2-viewHeight/3);
                height1 = (int)a+viewHeight/3;
                width1=viewWidth/2+(int)Math.sqrt(radiussq-(a+viewHeight/12)*(a+viewHeight/12));
                invalidate();
            }
        });
        return startAnimator;
    }
    public void openAnimation() {
            List<Animator> l=new ArrayList<Animator>();
            l.add(inLeftAnimation());
            l.add(outRightAnimation());
            l.add(inRightAnimation());
            l.add(outLeftAnimation());
            l.add(inLeftAnimation());
            l.add(outRightAnimation());
            l.add(inRightAnimation());
            l.add(outLeftAnimation());
            l.add(inLeftAnimation());
            animatorSet.playSequentially(l);
            animatorSet.start();


    }
}
