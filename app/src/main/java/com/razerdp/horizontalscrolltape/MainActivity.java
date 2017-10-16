package com.razerdp.horizontalscrolltape;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.razerdp.horizontalscrolltape.widget.HorizontalScrollTape;

public class MainActivity extends AppCompatActivity {
    HorizontalScrollTape mScrollTape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mScrollTape= (HorizontalScrollTape) findViewById(R.id.scroll);
    }
}
