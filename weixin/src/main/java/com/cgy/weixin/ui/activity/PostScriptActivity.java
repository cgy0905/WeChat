package com.cgy.weixin.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.cgy.weixin.R;
import com.cgy.weixin.ui.base.BaseActivity;
import com.cgy.weixin.ui.presenter.PostScriptPresenter;
import com.cgy.weixin.ui.view.IPostScriptView;

import butterknife.Bind;

/**
 * Created by cgy
 * 2018/6/5  17:00
 * 附言界面
 */
public class PostScriptActivity extends BaseActivity<IPostScriptView, PostScriptPresenter> implements IPostScriptView{

    @Bind(R.id.btnToolbarSend)
    Button mBtnToolbarSend;
    @Bind(R.id.etMsg)
    EditText mEtMsg;
    @Bind(R.id.ibClear)
    ImageButton mIbClear;

    private String mUserId;

    @Override
    public void init() {
        mUserId = getIntent().getStringExtra("userId");
    }

    @Override
    public void initView() {
        mBtnToolbarSend.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(mUserId)) {
            finish();
        }
    }

    @Override
    public void initListener() {
        mIbClear.setOnClickListener(v -> mEtMsg.setText(""));
        mBtnToolbarSend.setOnClickListener(v -> mPresenter.addFriend(mUserId));
    }

    @Override
    protected PostScriptPresenter createPresenter() {
        return new PostScriptPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_postscript;
    }

    @Override
    public EditText getEtMsg() {
        return mEtMsg;
    }
}
