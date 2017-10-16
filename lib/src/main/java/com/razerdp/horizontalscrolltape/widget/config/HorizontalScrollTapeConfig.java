package com.razerdp.horizontalscrolltape.widget.config;

import android.graphics.Color;

import com.razerdp.horizontalscrolltape.widget.interfaces.HorizontalScrollTapeController;
import com.razerdp.horizontalscrolltape.widget.interfaces.OnScrollingListener;

import java.io.Serializable;

/**
 * Created by 大灯泡 on 2017/10/16.
 * <p>
 * 配置
 */

public class HorizontalScrollTapeConfig implements Serializable, HorizontalScrollTapeController {
    //size
    private int lineWidth;
    private int longLineWidth;
    private int lineHeight;
    private int longLineHeight;
    private int middleLineWidth;
    private int middleLineHeight;
    private int textSize;
    //color
    private int lineColor;
    private int middleLineColor;
    private int textColor;
    //option
    private int lineMargin;//间隔
    private boolean alphaSide;
    private boolean autoAlign = true;//自动对齐
    private int minValue;
    private int maxValue;
    private int textTopMargin;

    private OnScrollingListener mOnScrollingListener;

    public HorizontalScrollTapeConfig() {
        setDefaultValue();
    }

    public void setDefaultValue() {
        lineWidth = 4;
        longLineWidth = 6;
        lineHeight = 40;
        middleLineWidth = 8;
        middleLineHeight = longLineHeight = 80;

        textSize = 24;

        lineColor = Color.rgb(220, 220, 220);
        middleLineColor = Color.rgb(76, 187, 117);
        textColor = Color.BLACK;

        lineMargin = 15;
        textTopMargin = 40;
        alphaSide = true;
        autoAlign = true;
    }


    @Override
    public int getLineWidth() {
        return lineWidth;
    }

    @Override
    public HorizontalScrollTapeConfig setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    @Override
    public int getMiddleLineWidth() {
        return middleLineWidth;
    }

    @Override
    public HorizontalScrollTapeConfig setMiddleLineWidth(int middleLineWidth) {
        this.middleLineWidth = middleLineWidth;
        return this;
    }

    @Override
    public int getLineColor() {
        return lineColor;
    }

    @Override
    public HorizontalScrollTapeConfig setLineColor(int lineColor) {
        this.lineColor = lineColor;
        return this;
    }

    @Override
    public int getMiddleLineColor() {
        return middleLineColor;
    }

    @Override
    public HorizontalScrollTapeConfig setMiddleLineColor(int middleLineColor) {
        this.middleLineColor = middleLineColor;
        return this;
    }

    @Override
    public int getLineMargin() {
        return lineMargin;
    }

    @Override
    public HorizontalScrollTapeConfig setLineMargin(int lineMargin) {
        this.lineMargin = lineMargin;
        return this;
    }

    @Override
    public boolean isAlphaSide() {
        return alphaSide;
    }

    @Override
    public HorizontalScrollTapeConfig setAlphaSide(boolean alphaSide) {
        this.alphaSide = alphaSide;
        return this;
    }

    public boolean isAutoAlign() {
        return autoAlign;
    }

    public HorizontalScrollTapeConfig setAutoAlign(boolean autoAlign) {
        this.autoAlign = autoAlign;
        return this;
    }

    @Override
    public int getLineHeight() {
        return lineHeight;
    }

    @Override
    public HorizontalScrollTapeConfig setLineHeight(int lineHeight) {
        this.lineHeight = lineHeight;
        return this;
    }

    @Override
    public int getLongLineHeight() {
        return longLineHeight;
    }

    @Override
    public HorizontalScrollTapeConfig setLongLineHeight(int longLineHeight) {
        this.longLineHeight = longLineHeight;
        return this;
    }

    @Override
    public int getMiddleLineHeight() {
        return middleLineHeight;
    }

    @Override
    public HorizontalScrollTapeConfig setMiddleLineHeight(int middleLineHeight) {
        this.middleLineHeight = middleLineHeight;
        return this;
    }

    @Override
    public OnScrollingListener getOnScrollingListener() {
        return mOnScrollingListener;
    }

    @Override
    public HorizontalScrollTapeConfig setOnScrollingListener(OnScrollingListener listener) {
        this.mOnScrollingListener = listener;
        return this;
    }

    @Override
    public int getMaxValue() {
        return maxValue;
    }

    @Override
    public HorizontalScrollTapeConfig setMaxValue(int maxValue) {
        this.maxValue = maxValue;
        return this;
    }

    @Override
    public int getMinValue() {
        return minValue;
    }

    @Override
    public HorizontalScrollTapeConfig setMinValue(int minValue) {
        this.minValue = minValue;
        return this;
    }

    @Override
    public int getTextSize() {
        return textSize;
    }

    @Override
    public HorizontalScrollTapeConfig setTextSize(int textSize) {
        this.textSize = textSize;
        return this;
    }

    @Override
    public int getTextColor() {
        return textColor;
    }

    @Override
    public HorizontalScrollTapeConfig setTextColor(int color) {
        this.textColor = color;
        return this;
    }

    @Override
    public int getTextTopMargin() {
        return textTopMargin;
    }

    @Override
    public HorizontalScrollTapeConfig setTextTopMargin(int topMargin) {
        this.textTopMargin = topMargin;
        return this;
    }

    @Override
    public int getLongLineWidth() {
        return longLineWidth;
    }

    @Override
    public HorizontalScrollTapeConfig setLongLineWidth(int longLineWidth) {
        this.longLineWidth = longLineWidth;
        return this;
    }
}
