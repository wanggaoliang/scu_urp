package com.wangr.ro.ro.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.wangr.ro.ro.R;
import com.wangr.ro.ro.public_Class.CircleBean;
import com.wangr.ro.ro.public_Class.Point;
import com.wangr.ro.ro.public_Class.MyTimerTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.animation.ValueAnimator.INFINITE;

/**
 * Created by wangr on 2018/2/10.
 */

public class MyView extends View {
    private List<Point> mPointsList;
    private int mViewWidth;
    private int waveMaxHeihgt;
    private int line=500;
    private int colors[] = new int[2];
    private float positions[] = new float[2];
    private float mWaveWidth;
    private float mWaveHeight;
    private float mLeftSide;
    public  float SPEED = 3.7f;
    public float heightDivider=8f;
    private float widthDivider;
    private boolean isMeasured=false;
    private float mMoveLen;
    private float startmovLen;
    private Paint mPaint;
    private Path mWavePath;
    private Shader shader;

    public MyView(Context context) {
        super(context);
        init();
    }
    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaveView);
        SPEED = typedArray.getFloat(R.styleable.WaveView_Speed,3.7f);
        heightDivider=typedArray.getFloat(R.styleable.WaveView_HeightDivider,8f);
        startmovLen=typedArray.getFloat(R.styleable.WaveView_Initial_offset,0f);
        widthDivider=typedArray.getFloat(R.styleable.WaveView_widthDivider,1f);
        colors[0]=typedArray.getColor(R.styleable.WaveView_startcolor,0xaaFF7E37);
        colors[1]=typedArray.getColor(R.styleable.WaveView_overcolor,0xaaFF7E37);
        typedArray.recycle();
        mMoveLen+=startmovLen;
        init();
    }
    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    public ValueAnimator startAnimation() {

        ValueAnimator startAnimator = ValueAnimator.ofFloat(0,1);
        startAnimator.setDuration(500);
        startAnimator.setRepeatCount(INFINITE);
        startAnimator.setInterpolator(new LinearInterpolator());
        startAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                System.out.println(mLeftSide);
                mMoveLen += SPEED;
                // 水位上升
                mLeftSide += SPEED;
                // 波形平移
                for (int i = 0; i < mPointsList.size(); i++)
                {
                    mPointsList.get(i).setX(mPointsList.get(i).getX() + SPEED);

                }
                if (mMoveLen >= mWaveWidth)
                {
                    // 波形平移超过一个完整波形后复位
                    mMoveLen = 0;
                    resetPoints();
                }
                invalidate();
            }
        });
        return startAnimator;
    }

    public void openAnimation(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        startAnimation().start();
    }

    /**
     * 所有点的x坐标都还原到初始状态，也就是一个周期前的状态
     */
    private void resetPoints()
    {
        mLeftSide = -mWaveWidth;
        for (int i = 0; i < mPointsList.size(); i++)
        {
            mPointsList.get(i).setX(i * mWaveWidth / 4 - mWaveWidth);
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure( widthMeasureSpec,  heightMeasureSpec);
        if (!isMeasured)
        {
            isMeasured = true;
            waveMaxHeihgt = getMeasuredHeight();
            mViewWidth = getMeasuredWidth();
            // 根据View宽度计算波形峰值
            mWaveHeight = mViewWidth / heightDivider;
            // 波长等于四倍View宽度也就是View中只能看到四分之一个波形，这样可以使起伏更明显
            mWaveWidth = mViewWidth/widthDivider ;
            // 左边隐藏的距离预留一个波形
            mLeftSide = -mWaveWidth;
            // 这里计算在可见的View宽度中能容纳几个波形，注意n上取整
            int n = (int) Math.round(mViewWidth / mWaveWidth + 0.5);
            // n个波形需要4n+1个点，但是我们要预留一个波形在左边隐藏区域，所以需要4n+5个点
            for (int i = 0; i < (4 * n + 5); i++)
            {
                // 从P0开始初始化到P4n+4，总共4n+5个点
                float x = i * mWaveWidth / 4 - mWaveWidth+startmovLen;
                float y = 0;
                switch (i % 4)
                {
                    case 0:
                    case 2:
                        // 零点位于水位线上
                        y = line;
                        break;
                    case 1:
                        // 往下波动的控制点
                        y = line+ mWaveHeight;
                        break;
                    case 3:
                        // 往上波动的控制点
                        y = line - mWaveHeight;
                        break;
                }
                mPointsList.add(new Point(x, y));
            }
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        mWavePath.reset();
        int i = 0;
        mWavePath.moveTo(mPointsList.get(0).getX(), mPointsList.get(0).getY());
        for (; i < mPointsList.size() - 2; i = i + 2)
        {
            mWavePath.quadTo(mPointsList.get(i + 1).getX(),
                    mPointsList.get(i + 1).getY(), mPointsList.get(i + 2)
                            .getX(), mPointsList.get(i + 2).getY());
        }
        mWavePath.lineTo(mPointsList.get(i).getX(), 0);
        mWavePath.lineTo(mLeftSide, 0);
        mWavePath.close();

        // mPaint的Style是FILL，会填充整个Path区域
        canvas.drawPath(mWavePath, mPaint);


    }

    private void init()
    {
        positions[0]=0;
        positions[1]=1;
        mPointsList = new ArrayList<Point>();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        shader = new LinearGradient(0, line, 1020, line, colors, positions, Shader.TileMode.MIRROR);
        mPaint.setShader(shader);
        mWavePath = new Path();
    }





}
