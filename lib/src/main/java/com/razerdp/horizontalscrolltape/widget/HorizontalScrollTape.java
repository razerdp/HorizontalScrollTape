package com.razerdp.horizontalscrolltape.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.razerdp.horizontalscrolltape.widget.config.HorizontalScrollTapeConfig;

/**
 * Created by 大灯泡 on 2017/10/16.
 * <p>
 * 水平滑动卷尺
 */

public class HorizontalScrollTape extends View {
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
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastTouchPos = x;
                break;
            case MotionEvent.ACTION_MOVE:
                float xDistance = lastTouchPos - x;
                if (xDistance != 0) {
                    if (!isScrolling) {
                        isScrolling = true;
                        callScrollStart();
                    }
                    onScrollingEvent(x, xDistance);
                }
                break;
            case MotionEvent.ACTION_UP:
                isScrolling = false;
                callScrollStop();
                break;
        }
        return true;
    }

    private void onScrollingEvent(float curX, float distanceX) {
        scrollDistance += distanceX;
        postInvalidate();
        lastTouchPos = curX;
    }


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
        Log.i(TAG, "" + deltaX);

        //顶部的线
        canvas.drawLine(0, 0, canvasWidth, 0, linePaint);
        //画线（边缘可能有透明渐变）
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

}
