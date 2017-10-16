package com.razerdp.horizontalscrolltape.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.razerdp.horizontalscrolltape.widget.config.HorizontalScrollTapeConfig;
import com.razerdp.horizontalscrolltape.widget.interfaces.HorizontalScrollTapeController;
import com.razerdp.horizontalscrolltape.widget.interfaces.OnScrollingListener;

/**
 * Created by 大灯泡 on 2017/10/16.
 * <p>
 * 水平滑动卷尺
 */

public class HorizontalScrollTape extends View implements HorizontalScrollTapeController {
    private static final String TAG = "HorizontalScrollTape";

    private HorizontalScrollTapeConfig mConfig;

    private Paint middleLinePaint;
    private Paint linePaint;
    private Paint textPaint;
    private Rect textBounds = new Rect();

    private float lastTouchPos;
    private float scrollDistance;//总滑动量

    private boolean hasConfigChanged;
    private boolean isScrolling;

    private Scroller mScroller;
    private GestureDetector mGestureDetector;


    public HorizontalScrollTape(Context context) {
        this(context, null);
    }

    public HorizontalScrollTape(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalScrollTape(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        if (mConfig == null) mConfig = new HorizontalScrollTapeConfig();
        mScroller = new Scroller(context);
        mGestureDetector = new GestureDetector(context, mSimpleOnGestureListener);
        mGestureDetector.setIsLongpressEnabled(false);
        initPaint();
        applyConfig();
    }

    private void initPaint() {
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);

        middleLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        middleLinePaint.setStyle(Paint.Style.STROKE);
        middleLinePaint.setStrokeCap(Paint.Cap.ROUND);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }


    private void applyConfig() {
        linePaint.setStrokeWidth(mConfig.getLineWidth());
        linePaint.setColor(mConfig.getLineColor());
        linePaint.setDither(true);

        middleLinePaint.setStrokeWidth(mConfig.getMiddleLineWidth());
        middleLinePaint.setColor(mConfig.getMiddleLineColor());
        middleLinePaint.setDither(true);


        textPaint.setTextSize(mConfig.getTextSize());
        textPaint.setColor(mConfig.getTextColor());
        textPaint.setDither(true);


        hasConfigChanged = false;

    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            float curX = mScroller.getCurrX();
            float distanceX = lastTouchPos - curX;
            Log.i(TAG, "curx = " + curX + "  distance = " + distanceX);
            onScrollingEvent(curX, distanceX);
        } else {
            isScrolling = false;
            callScrollStop();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    private void onScrollingEvent(float curX, float distanceX) {
        scrollDistance += distanceX;
        postInvalidate();
        lastTouchPos = curX;
    }

    private GestureDetector.SimpleOnGestureListener mSimpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (!mScroller.isFinished() || mScroller.computeScrollOffset()) {
                mScroller.abortAnimation();
                isScrolling = false;
                callScrollStop();
            }
            lastTouchPos = e1.getX();
            if (distanceX != 0) {
                if (!isScrolling) {
                    isScrolling = true;
                    callScrollStart();
                }
                onScrollingEvent(e2.getX(), distanceX);
            }
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            lastTouchPos = e1.getX();
            mScroller.fling((int) e1.getX(),
                            0,
                            (int) (velocityX),
                            0,
                            Integer.MIN_VALUE,
                            Integer.MAX_VALUE,
                            0,
                            0);
            return true;
        }
    };


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (hasConfigChanged) {
            applyConfig();
        }

        final int canvasWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        final int canvasHeight = getHeight() - getPaddingTop() - getPaddingBottom();

        //计算可以show出来的线的数量
        int showLineCount = canvasWidth / (mConfig.getLineWidth() + mConfig.getLineMargin());
        //计算移动了多少格
        int deltaX = (int) (scrollDistance / (float) (mConfig.getLineWidth() + mConfig.getLineMargin()));

        //顶部的线
        canvas.drawLine(0, 0, canvasWidth, 0, linePaint);
        //画线（边缘可设置透明渐变）
        /**@see HorizontalScrollTapeConfig#alphaSide*/
        for (int i = 0; i < showLineCount; i++) {
            boolean isLong = (i + deltaX) % 10 == 0;
            int alpha = calculateAlpha(showLineCount, i);
            linePaint.setStrokeWidth(isLong ? mConfig.getLongLineWidth() : mConfig.getLineWidth());
            linePaint.setAlpha(alpha);
            textPaint.setAlpha(alpha);
            float left = (mConfig.getLineWidth() + mConfig.getLineMargin()) * i;
            float top = 0;
            float bottom = top + (isLong ? mConfig.getLongLineHeight() : mConfig.getLineHeight());
            while (bottom > canvasHeight) {
                bottom = bottom / 2;
            }
            canvas.drawLine(left, top, left, bottom, linePaint);
            if (isLong) {
                String num = String.valueOf((mConfig.getMinValue() + (deltaX + i) / 10));
                textPaint.getTextBounds(num, 0, num.length(), textBounds);
                canvas.drawText(num, left, bottom + mConfig.getTextTopMargin(), textPaint);
            }
        }
        //中间的指针
        canvas.drawLine(canvasWidth / 2, 0, canvasWidth / 2, mConfig.getMiddleLineHeight(), middleLinePaint);
    }

    private int calculateAlpha(int showLineCount, int i) {
        //透明渐变区域取左右25%
        int alphaRange = (int) (showLineCount * 0.25);
        int alpha;
        if (i < alphaRange) {
            //左边25%
            alpha = mConfig.isAlphaSide() ? (int) ((i * 1.0f / alphaRange * 1.0f) * 255) : 255;
        } else if (i > (showLineCount - alphaRange)) {
            //右边25%
            alpha = mConfig.isAlphaSide() ? (int) (((showLineCount - i) * 1.0f / alphaRange * 1.0f) * 255) : 255;
        } else {
            //中间50%
            alpha = 255;
        }
        return alpha;
    }


    //-----------------------------------------call back-----------------------------------------
    void callScrollStart() {
        if (mConfig.getOnScrollingListener() != null) {
            mConfig.getOnScrollingListener().onStart();
        }
    }

    void callScrolling(float value) {
        if (mConfig.getOnScrollingListener() != null) {
            mConfig.getOnScrollingListener().onScrolling(value);
        }
    }

    void callScrollStop() {
        if (mConfig.getOnScrollingListener() != null) {
            mConfig.getOnScrollingListener().onStop();
        }
    }

    //-----------------------------------------getter/setter-----------------------------------------


    @Override
    public int getLineWidth() {
        return mConfig.getLineWidth();
    }

    @Override
    public HorizontalScrollTapeConfig setLineWidth(int lineWidth) {
        hasConfigChanged = true;
        return mConfig.setLineWidth(lineWidth);
    }

    @Override
    public int getMiddleLineWidth() {
        return mConfig.getMiddleLineWidth();
    }

    @Override
    public HorizontalScrollTapeConfig setMiddleLineWidth(int middleLineWidth) {
        hasConfigChanged = true;
        return mConfig.setMiddleLineWidth(middleLineWidth);
    }

    @Override
    public int getLineColor() {
        return mConfig.getLineColor();
    }

    @Override
    public HorizontalScrollTapeConfig setLineColor(int lineColor) {
        hasConfigChanged = true;
        return mConfig.setLineColor(lineColor);
    }

    @Override
    public int getMiddleLineColor() {
        return mConfig.getMiddleLineColor();
    }

    @Override
    public HorizontalScrollTapeConfig setMiddleLineColor(int middleLineColor) {
        hasConfigChanged = true;
        return mConfig.setMiddleLineColor(middleLineColor);
    }

    @Override
    public int getLineMargin() {
        return mConfig.getLineMargin();
    }

    @Override
    public HorizontalScrollTapeConfig setLineMargin(int lineMargin) {
        hasConfigChanged = true;
        return mConfig.setLineMargin(lineMargin);
    }

    @Override
    public boolean isAlphaSide() {
        return mConfig.isAlphaSide();
    }

    @Override
    public HorizontalScrollTapeConfig setAlphaSide(boolean alphaSide) {
        hasConfigChanged = true;
        return mConfig.setAlphaSide(alphaSide);
    }

    @Override
    public boolean isAutoAlign() {
        return mConfig.isAutoAlign();
    }

    @Override
    public HorizontalScrollTapeConfig setAutoAlign(boolean autoAlign) {
        hasConfigChanged = true;
        return mConfig.setAutoAlign(autoAlign);
    }

    @Override
    public int getLineHeight() {
        return mConfig.getLineHeight();
    }

    @Override
    public HorizontalScrollTapeConfig setLineHeight(int lineHeight) {
        hasConfigChanged = true;
        return mConfig.setLineHeight(lineHeight);
    }

    @Override
    public int getLongLineHeight() {
        return mConfig.getLongLineHeight();
    }

    @Override
    public HorizontalScrollTapeConfig setLongLineHeight(int longLineHeight) {
        hasConfigChanged = true;
        return mConfig.setLongLineHeight(longLineHeight);
    }

    @Override
    public int getMiddleLineHeight() {
        return mConfig.getMiddleLineHeight();
    }

    @Override
    public HorizontalScrollTapeConfig setMiddleLineHeight(int middleLineHeight) {
        hasConfigChanged = true;
        return mConfig.setMiddleLineHeight(middleLineHeight);
    }

    @Override
    public OnScrollingListener getOnScrollingListener() {
        return mConfig.getOnScrollingListener();
    }

    @Override
    public HorizontalScrollTapeConfig setOnScrollingListener(OnScrollingListener listener) {
        return mConfig.setOnScrollingListener(listener);
    }

    @Override
    public int getMaxValue() {
        return mConfig.getMaxValue();
    }

    @Override
    public HorizontalScrollTapeConfig setMaxValue(int maxValue) {
        return mConfig.setMaxValue(maxValue);
    }

    @Override
    public int getMinValue() {
        return mConfig.getMinValue();
    }

    @Override
    public HorizontalScrollTapeConfig setMinValue(int minValue) {
        return mConfig.setMinValue(minValue);
    }

    @Override
    public int getTextSize() {
        return mConfig.getTextSize();
    }

    @Override
    public HorizontalScrollTapeConfig setTextSize(int textSize) {
        hasConfigChanged = true;
        return mConfig.setTextSize(textSize);
    }

    @Override
    public int getTextColor() {
        return mConfig.getTextColor();
    }

    @Override
    public HorizontalScrollTapeConfig setTextColor(int color) {
        hasConfigChanged = true;
        return mConfig.setTextColor(color);
    }

    @Override
    public int getTextTopMargin() {
        return mConfig.getTextTopMargin();
    }

    @Override
    public HorizontalScrollTapeConfig setTextTopMargin(int topMargin) {
        hasConfigChanged = true;
        return mConfig.setTextTopMargin(topMargin);
    }

    @Override
    public int getLongLineWidth() {
        return mConfig.getLongLineWidth();
    }

    @Override
    public HorizontalScrollTapeConfig setLongLineWidth(int longLineWidth) {
        hasConfigChanged = true;
        return mConfig.setLongLineWidth(longLineWidth);
    }

    public void setConfig(@NonNull HorizontalScrollTapeConfig config) {
        if (config == null) return;
        this.mConfig = config;
        hasConfigChanged = true;
    }

    @NonNull
    public HorizontalScrollTapeConfig getConfig() {
        return mConfig;
    }


}
