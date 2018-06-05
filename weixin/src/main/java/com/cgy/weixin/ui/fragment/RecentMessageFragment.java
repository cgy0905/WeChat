package com.cgy.weixin.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cgy.weixin.R;
import com.cgy.weixin.app.AppConst;
import com.cgy.weixin.manager.BroadcastManager;
import com.cgy.weixin.ui.activity.MainActivity;
import com.cgy.weixin.ui.base.BaseFragment;
import com.cgy.weixin.ui.presenter.RecentMessagePresenter;
import com.cgy.weixin.ui.view.IRecentMessageView;
import com.lqr.recyclerview.LQRRecyclerView;

import butterknife.Bind;

/**
 * Created by cgy
 * 2018/5/29  17:37
 * 最近会话列表界面
 */
public class RecentMessageFragment extends BaseFragment<IRecentMessageView, RecentMessagePresenter> implements IRecentMessageView{

    private boolean isFirst = true;

    @Bind(R.id.rvRecentMessage)
    LQRRecyclerView mRvRecentMessage;

    @Override
    public void init() {
        registerBR();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirst) {
            mPresenter.getConversations();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterBR();
    }

    private void registerBR() {
        BroadcastManager.getInstance(getActivity()).register(AppConst.UPDATE_CONVERSATIONS, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mPresenter.getConversations();
                isFirst = false;
            }
        });
    }

    private void unregisterBR() {
        BroadcastManager.getInstance(getActivity()).unregister(AppConst.UPDATE_CONVERSATIONS);
    }

    @Override
    protected RecentMessagePresenter createPresenter() {
        return new RecentMessagePresenter((MainActivity)getActivity());
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_recent_message;
    }

    @Override
    public LQRRecyclerView getRvRecentMessage() {
        return mRvRecentMessage;
    }
}
