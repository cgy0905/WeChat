package com.cgy.weixin.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.LinearLayout;

import com.cgy.weixin.R;
import com.cgy.weixin.app.AppConst;
import com.cgy.weixin.manager.BroadcastManager;
import com.cgy.weixin.ui.base.BaseActivity;
import com.cgy.weixin.ui.presenter.GroupListPresenter;
import com.cgy.weixin.ui.view.IGroupListView;
import com.lqr.recyclerview.LQRRecyclerView;

import butterknife.Bind;

public class GroupListActivity extends BaseActivity<IGroupListView, GroupListPresenter> implements IGroupListView{

    @Bind(R.id.llGroups)
    LinearLayout mLlGroups;
    @Bind(R.id.rvGroupList)
    LQRRecyclerView mRvGroupList;

    @Override
    public void init() {
        registerBR();
    }

    @Override
    public void initData() {
        mPresenter.loadGroups();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBR();
    }

    private void registerBR() {
        BroadcastManager.getInstance(this).register(AppConst.GROUP_LIST_UPDATE, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mPresenter.loadGroups();
            }
        });

    }

    private void unregisterBR() {
        BroadcastManager.getInstance(this).unregister(AppConst.GROUP_LIST_UPDATE);
    }

    @Override
    protected GroupListPresenter createPresenter() {
        return new GroupListPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_group_list;
    }

    @Override
    public LinearLayout getLlGroups() {
        return mLlGroups;
    }

    @Override
    public LQRRecyclerView getRvGroupList() {
        return mRvGroupList;
    }
}
