package com.cgy.weixin.ui.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgy.weixin.R;
import com.cgy.weixin.ui.base.BaseActivity;
import com.cgy.weixin.ui.presenter.NewFriendPresenter;
import com.cgy.weixin.ui.view.INewFriendView;
import com.cgy.weixin.utils.UIUtils;
import com.lqr.recyclerview.LQRRecyclerView;

import butterknife.Bind;

public class NewFriendActivity extends BaseActivity<INewFriendView, NewFriendPresenter> implements INewFriendView {

    @Bind(R.id.llToolbarAddFriend)
    LinearLayout mLlToolbarAddFriend;
    @Bind(R.id.tvToolbarAddFriend)
    TextView mTvToolbarAddFriend;

    @Bind(R.id.llNoNewFriend)
    LinearLayout mLlNoNewFriend;
    @Bind(R.id.llHasNewFriend)
    LinearLayout mLlHasNewFriend;
    @Bind(R.id.rvNewFriend)
    LQRRecyclerView mRvNewFriend;

    @Override
    public void initView() {
        mLlToolbarAddFriend.setVisibility(View.VISIBLE);
        setToolbarTitle(UIUtils.getString(R.string.new_friend));
    }

    @Override
    public void initData() {
        mPresenter.loadNewFriendData();
    }

    @Override
    public void initListener() {
        mTvToolbarAddFriend.setOnClickListener(v -> jumpToActivity(AddFriendActivity.class));
    }

    @Override
    protected NewFriendPresenter createPresenter() {
        return new NewFriendPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_new_friend;
    }

    @Override
    public LinearLayout getLlNoNewFriend() {
        return mLlNoNewFriend;
    }

    @Override
    public LinearLayout getLlHasNewFriend() {
        return mLlHasNewFriend;
    }

    @Override
    public LQRRecyclerView getRvNewFriend() {
        return mRvNewFriend;
    }
}
