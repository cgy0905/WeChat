package com.cgy.weixin.ui.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgy.weixin.R;
import com.cgy.weixin.ui.base.BaseActivity;
import com.cgy.weixin.ui.presenter.SearchUserPresenter;
import com.cgy.weixin.ui.view.ISearchUserView;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import butterknife.Bind;

/**
 * Created by cgy
 * 2018/6/5  14:32
 * 搜索用户界面
 */
public class SearchUserActivity extends BaseActivity<ISearchUserView, SearchUserPresenter> implements ISearchUserView {

    @Bind(R.id.etSearchContent)
    EditText mEtSearchContent;
    @Bind(R.id.llToolbarSearch)
    AutoLinearLayout mLlToolbarSearch;
    @Bind(R.id.rlNoResultTip)
    AutoRelativeLayout mRlNoResultTip;
    @Bind(R.id.tvMsg)
    TextView mTvMsg;
    @Bind(R.id.llSearch)
    AutoLinearLayout mLlSearch;


    @Override
    public void initView() {
        mToolbarTitle.setVisibility(View.GONE);
        mLlToolbarSearch.setVisibility(View.VISIBLE);
    }

    @Override
    public void initListener() {
        mEtSearchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String content = mEtSearchContent.getText().toString().trim();
                mRlNoResultTip.setVisibility(View.GONE);
                if (content.length() > 0) {
                    mLlSearch.setVisibility(View.VISIBLE);
                    mTvMsg.setText(content);
                } else {
                    mLlSearch.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mLlSearch.setOnClickListener(v -> mPresenter.searchUser());
    }

    @Override
    protected SearchUserPresenter createPresenter() {
        return new SearchUserPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_search_user;
    }


    @Override
    public EditText getEtSearchContent() {
        return mEtSearchContent;
    }

    @Override
    public RelativeLayout getRlNoResultTip() {
        return mRlNoResultTip;
    }

    @Override
    public LinearLayout getLlSearch() {
        return mLlSearch;
    }
}
