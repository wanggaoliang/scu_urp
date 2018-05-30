package com.wangr.ro.ro.widget;


import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.wangr.ro.ro.R;
import com.wangr.ro.ro.public_Class.BezierUtil;
import com.wangr.ro.ro.public_Class.CircleBean;
import com.wangr.ro.ro.public_Class.MyTimerTask;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * Created by wangr on 2018/2/28.
 */
public class BubbleView extends View {

    private int colors[] = new int[2];
    private float positions[] = new float[2];
    final float[] pos = new float[2];
    final float[] tan = new float[2];
    private  float floatSpeed=0.002f;
    private float floatT=0;
    private float t=0;
    private int amplitude = 20;//振幅
    private int height;
    private int width;
    private boolean ismeasured=false;
    private boolean isstart=false;
    private MyTimerTask mTask;
    private Timer timer;
    private Paint paint;
    private List<CircleBean> mCircleBeen = new ArrayList<>();
    private List<PathMeasure> pathMeasures = new ArrayList<>();
    private AnimatorSet animatorSet;
    private View centerImg;


    public OnBubbleAnimationListener onBubbleAnimationListener;

    public BubbleView(Context context) {
        super(context);
        init();
    }

    public BubbleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BubbleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public View getCenterImg() {
        return centerImg;
    }
    public void setCenterImg(View centerImg) {
        this.centerImg = centerImg;
    }


    public List<CircleBean> getCircleBeen() {
        return mCircleBeen;
    }


    public void setOnBubbleAnimationListener(OnBubbleAnimationListener onBubbleAnimationListener) {
        this.onBubbleAnimationListener = onBubbleAnimationListener;
    }

    public abstract static class OnBubbleAnimationListener {

        public abstract void onCompletedAnimationListener();

    }


    /**
     * 初始化
     */
    private void init() {
        timer=new Timer();
        animatorSet = new AnimatorSet();
        initPaint();
        initShader();
    }

    /**
     * 初始化圆渐变色的LinearGradient的colors和positions
     */
    private void initShader() {
        colors[0] = ContextCompat.getColor(getContext(),R.color.circle_start);
        colors[1] = ContextCompat.getColor(getContext(), R.color.circle_end);
        positions[0] = 0;
        positions[1] = 1;
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(60);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!ismeasured)
        {
            ismeasured = true;
            height = getMeasuredHeight();
            width = getMeasuredWidth();
            default_Circlebean();
            addPath();
        }


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCircleBeen != null) {
            for (CircleBean circleBean : getCircleBeen()) {
                paint.setShader(getColorShader(circleBean));
                paint.setAlpha(circleBean.getAlpha());
                canvas.drawCircle(circleBean.getP().x, circleBean.getP().y-170, circleBean.getRadius(), paint);

            }
        }
    }

    /**
     * 获取圆的渐变色
     *
     * @param circleBean 传入每个圆的bean对象
     * @return 返回Shader
     */
    private Shader getColorShader(CircleBean circleBean) {
        float x0 = circleBean.getP().x - circleBean.getRadius();
        float y0 = circleBean.getP().y;

        float x1 = circleBean.getP().x + circleBean.getRadius();
        float y1 = circleBean.getP().y;

        Shader shader = new LinearGradient(x0, y0, x1, y1, colors, positions, Shader.TileMode.MIRROR);
        return shader;
    }


    /**
     * 开启动画
     */
    public void openAnimation() {
        if(!isstart){
            animatorSet.play(inAnimation());
            animatorSet.start();
            isstart=true;
        }
        start();

    }

    /**
     * 关闭动画
     */
    public void stopAnimation() {
        timer.cancel();
        AnimatorSet outanimator=new AnimatorSet();
        outanimator.play(outAnimation());
        outanimator.start();
    }

    /**
     * 启动动画
     *
     * @return 返回ValueAnimator对象
     */
    public ValueAnimator inAnimation() {

        ValueAnimator startAnimator = ValueAnimator.ofFloat(0, 1);
        startAnimator.setDuration(800);
        startAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        startAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                t = (Float) valueAnimator.getAnimatedValue();
                for (int i = 0; i < mCircleBeen.size(); i++) {
                    CircleBean c = mCircleBeen.get(i);
                    PointF pointF = BezierUtil.CalculateBezierPointForQuadratic(t, c.getP0(), c.getP1(), c.getP2());
                    mCircleBeen.get(i).setP(pointF);

                    c.setAlpha((int) (t * 100));
                    if (t > 0.5) {
                        setCenterViewAlpha(t);
                    } else {
                        setCenterViewAlpha(0);
                    }
                }

                invalidate();
            }
        });
        return startAnimator;
    }


    Handler updateHandler = new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {

            if(t>=1) {
                floatT += floatSpeed;
                if (floatT >= 1 || floatT <= 0) {
                    floatSpeed = -floatSpeed;
                }
                for (int i = 0; i < mCircleBeen.size(); i++) {
                    pathMeasures.get(i).getPosTan(pathMeasures.get(i).getLength() * floatT, pos, tan);
                    mCircleBeen.get(i).setP(new PointF(pos[0], pos[1]));
                }
                if (floatT > 0.3) {
                    setCenterViewAlpha((float) ((1 - floatT) + 0.3));
                }
            }
            invalidate();

        }
    };
    private void addPath(){
        for (int i = 0; i < mCircleBeen.size(); i++) {
            Path path = new Path();
            if (i % 2 == 0) {
                path.addCircle(mCircleBeen.get(i).getP2().x - amplitude, mCircleBeen.get(i).getP2().y, amplitude, Path.Direction.CCW);
            } else {
                path.addCircle(mCircleBeen.get(i).getP2().x - amplitude, mCircleBeen.get(i).getP2().y, amplitude, Path.Direction.CW);
            }
            PathMeasure pathMeasure = new PathMeasure();
            pathMeasure.setPath(path, true);
            pathMeasures.add(pathMeasure);
        }
    }
    private void start()
    {
        if (mTask != null)
        {
            mTask.cancel();
            mTask = null;
        }
        mTask = new MyTimerTask(updateHandler);
        timer.schedule(mTask, 0, 10);
    }
    /**
     * 结束动画
     *
     * @return 返回ValueAnimator对象
     */
    private ValueAnimator outAnimation() {
        ValueAnimator outAnimator = ValueAnimator.ofFloat(0, 1);
        outAnimator.setDuration(600);
        outAnimator.setInterpolator(new DecelerateInterpolator());
        outAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setCenterViewAlpha(0);
                float t = (Float) valueAnimator.getAnimatedValue();
                for (int i = 0; i < mCircleBeen.size(); i++) {
                    CircleBean c = mCircleBeen.get(i);
                    PointF pointF = BezierUtil.CalculateBezierPointForQuadratic(t, c.getP2(), c.getP3(), c.getP4());
                    mCircleBeen.get(i).setP(pointF);
                    c.setAlpha((int) ((1 - t) * 100));
                }
                invalidate();
                if (1==t){
                    if (onBubbleAnimationListener!=null){
                        onBubbleAnimationListener.onCompletedAnimationListener();
                    }
                }
            }
        });
        return outAnimator;
    }

    private void setCenterViewAlpha(float alpha) {
        if (getCenterImg()!=null){
            getCenterImg().setAlpha(alpha);
        }
    }

    /**
     * 设置上下左右浮动的振幅
     *
     * @param amplitude 振幅值
     * @return 返回
     */
    private BubbleView setAmplitude(int amplitude) {
        this.amplitude = amplitude;
        return this;
    }
    private void default_Circlebean(){
        int centerX = width / 2;
        int centerY = height / 2;


        CircleBean circleBean = new CircleBean(
                new PointF((float) (-width / 5.1), (float) (height / 1.5)),
                new PointF(centerX - 30, height * 2 / 3),
                new PointF((float) (width / 2.4), (float) (height / 3.4)),
                new PointF(width / 6, centerY - 120),
                new PointF((float) (width / 7.2), -height / 128),
                (float) (width / 14.4), 60);
        CircleBean circleBean2 = new CircleBean(
                new PointF(-width / 4, (float) (height / 1.3)),
                new PointF(centerX - 20, height * 3 / 5),
                new PointF((float) (width / 2.1), (float) (height / 2.5)),
                new PointF(width / 3, centerY - 10),
                new PointF(width / 4, (float) (-height / 5.3)),
                width / 4, 60);
        CircleBean circleBean3 = new CircleBean(
                new PointF(-width / 12, (float) (height / 1.1)),
                new PointF(centerX - 100, height * 2 / 3),
                new PointF((float) (width / 3.4), height / 2),
                new PointF(0, centerY + 100),
                new PointF(0, 0),
                width / 24, 60);

        CircleBean circleBean4 = new CircleBean(
                new PointF(-width / 9, (float) (height / 0.9)),
                new PointF(centerX, height * 3 / 4),
                new PointF((float) (width / 2.1), (float) (height / 2.3)),
                new PointF(width / 2, centerY),
                new PointF((float) (width / 1.5), (float) (-height / 5.6)),
                width / 4, 60);

        CircleBean circleBean5 = new CircleBean(
                new PointF((float) (width / 1.4), (float) (height / 0.9)),
                new PointF(centerX, height * 3 / 4),
                new PointF(width / 2, (float) (height / 2.37)),
                new PointF(width * 10 / 13, centerY - 20),
                new PointF(width / 2, (float) (-height / 7.1)),
                width / 4, 60);
        CircleBean circleBean6 = new CircleBean(
                new PointF((float) (width / 0.8), height),
                new PointF(centerX + 20, height * 2 / 3),
                new PointF((float) (width / 1.9), (float) (height / 2.3)),
                new PointF(width * 11 / 14, centerY + 10),

                new PointF((float) (width / 1.1), (float) (-height / 6.4)),
                (float) (width / 4), 60);
        CircleBean circleBean7 = new CircleBean(
                new PointF((float) (width / 0.9), (float) (height / 1.2)),
                new PointF(centerX + 20, height * 4 / 7),
                new PointF((float) (width / 1.6), (float) (height / 1.9)),
                new PointF(width, centerY + 10),

                new PointF(width, 0),
                (float) (width / 9.6), 60);

        mCircleBeen.add(circleBean);
        mCircleBeen.add(circleBean2);
        mCircleBeen.add(circleBean3);
        mCircleBeen.add(circleBean4);
        mCircleBeen.add(circleBean5);
        mCircleBeen.add(circleBean6);
        mCircleBeen.add(circleBean7);

    }
}