package com.razerdp.horizontalscrolltape.widget.interfaces;

/**
 * Created by 大灯泡 on 2017/10/16.
 * <p>
 * 回调
 */

public interface OnScrollingListener {
    void onStart();

    void onScrolling(float curVal);

    void onStop();
}
