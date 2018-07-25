package com.cgy.weixin.ui.activity;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgy.weixin.R;
import com.cgy.weixin.model.cache.UserCache;
import com.cgy.weixin.ui.base.BaseActivity;
import com.cgy.weixin.ui.presenter.AddFriendPresenter;
import com.cgy.weixin.ui.view.IAddFriendView;
import com.cgy.weixin.utils.UIUtils;

import butterknife.Bind;

/**
 * Created by cgy
 * 2018/5/29  18:14
 * 添加好友界面
 */
public class AddFriendActivity extends BaseActivity<IAddFriendView, AddFriendPresenter> implements IAddFriendView{

    @Bind(R.id.llSearchUser)
    LinearLayout mLlSearchUser;
    @Bind(R.id.tvAccount)
    TextView mTvAccount;


    @Override
    public void initView() {
        setToolbarTitle(UIUtils.getString(R.string.add_friend));
        mTvAccount.setText(UserCache.getId() + "");
    }

    @Override
    public void initListener() {
        mLlSearchUser.setOnClickListener(v -> jumpToActivity(SearchUserActivity.class));
    }

    @Override
    protected AddFriendPresenter createPresenter() {
        return new AddFriendPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_add_friend;
    }
}
