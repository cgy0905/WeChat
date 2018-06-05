package com.cgy.weixin.ui.activity;

import com.cgy.weixin.R;
import com.cgy.weixin.ui.base.BaseActivity;
import com.cgy.weixin.ui.base.BasePresenter;

/**
 * Created by cgy
 * 2018/6/4  15:52
 */
public class AboutActivity extends BaseActivity{
    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_about;
    }
}
