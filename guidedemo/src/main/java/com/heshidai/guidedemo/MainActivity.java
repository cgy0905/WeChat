package com.heshidai.guidedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {


    private int images[] = new int[] {R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3, R.drawable.guide_4, R.drawable.guide_5};
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addGuidePage();
    }

    private void addGuidePage() {
        boolean flag = SPUtils.getInstance(this).getBoolean(Constant.IS_First_OPEN, false);
        if (flag) {
            return;
        }
        GuideUtils.getInstance().initGuide(this,images[i]);
        if (i < images.length) {
            i++;
        } else if (i == images.length -1) {
            SPUtils.getInstance(this).putBoolean(Constant.IS_First_OPEN, true);
            return;

        }
    }
}
