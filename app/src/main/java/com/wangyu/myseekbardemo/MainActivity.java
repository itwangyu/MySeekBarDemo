package com.wangyu.myseekbardemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MySeekBar3 mSeekbar = (MySeekBar3) findViewById(R.id.myseekbar);
        mSeekbar.setProgress(9);
    }
}
