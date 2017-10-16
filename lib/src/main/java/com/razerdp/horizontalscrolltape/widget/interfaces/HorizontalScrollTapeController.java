package com.razerdp.horizontalscrolltape.widget.interfaces;

import com.razerdp.horizontalscrolltape.widget.config.HorizontalScrollTapeConfig;

/**
 * Created by 大灯泡 on 2017/10/16.
 */

public interface HorizontalScrollTapeController {

    int getLineWidth();

    HorizontalScrollTapeConfig setLineWidth(int lineWidth);

    int getMiddleLineWidth();

    HorizontalScrollTapeConfig setMiddleLineWidth(int middleLineWidth);

    int getLineColor();

    HorizontalScrollTapeConfig setLineColor(int lineColor);

    int getMiddleLineColor();

    HorizontalScrollTapeConfig setMiddleLineColor(int middleLineColor);


    int getLineMargin();

    HorizontalScrollTapeConfig setLineMargin(int lineMargin);

    boolean isAlphaSide();

    HorizontalScrollTapeConfig setAlphaSide(boolean alphaSide);

    boolean isAutoAlign();

    HorizontalScrollTapeConfig setAutoAlign(boolean autoAlign);

    int getLineHeight();

    HorizontalScrollTapeConfig setLineHeight(int lineHeight);

    int getLongLineHeight();

    HorizontalScrollTapeConfig setLongLineHeight(int longLineHeight);

    int getMiddleLineHeight();

    HorizontalScrollTapeConfig setMiddleLineHeight(int middleLineHeight);

    OnScrollingListener getOnScrollingListener();

    HorizontalScrollTapeConfig setOnScrollingListener(OnScrollingListener listener);

    int getMaxValue();

    HorizontalScrollTapeConfig setMaxValue(int maxValue);

    int getMinValue();

    HorizontalScrollTapeConfig setMinValue(int minValue);

    int getTextSize();

    HorizontalScrollTapeConfig setTextSize(int textSize);

    int getTextColor();

    HorizontalScrollTapeConfig setTextColor(int color);

    int getTextTopMargin();

    HorizontalScrollTapeConfig setTextTopMargin(int topMargin);

    int getLongLineWidth();

    HorizontalScrollTapeConfig setLongLineWidth(int longLineWidth);


}
