package com.example.xianglijin.testviewpager;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private AdapterCycle mAdapter;
    private ViewPager mVp;
    private List<Drawable> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVp = (ViewPager) findViewById(R.id.top_vp);
        list = new ArrayList<>();
        list.add(getResources().getDrawable(R.drawable.bg_guide_page1));
        list.add(getResources().getDrawable(R.drawable.bg_guide_page2));
        list.add(getResources().getDrawable(R.drawable.bg_guide_page3));
        mAdapter = new AdapterCycle(this, mVp, list);
        mVp.setAdapter(mAdapter);
    }
}
