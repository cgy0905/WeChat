package com.cgy.weixin.ui.fragment;

import com.cgy.weixin.R;
import com.cgy.weixin.app.AppConst;
import com.cgy.weixin.ui.activity.MainActivity;
import com.cgy.weixin.ui.activity.ScanActivity;
import com.cgy.weixin.ui.base.BaseFragment;
import com.cgy.weixin.ui.presenter.DiscoveryPresenter;
import com.cgy.weixin.ui.view.IDiscoveryView;
import com.lqr.optionitemview.OptionItemView;

import butterknife.Bind;

/**
 * Created by cgy
 * 2018/5/29  17:37
 * 发现界面
 */
public class DiscoveryFragment extends BaseFragment<IDiscoveryView, DiscoveryPresenter> implements IDiscoveryView{

    @Bind(R.id.oivScan)
    OptionItemView mOivScan;
    @Bind(R.id.oivShop)
    OptionItemView mOivShop;
    @Bind(R.id.oivGame)
    OptionItemView mOivGame;

    @Override
    public void initListener() {
        mOivScan.setOnClickListener(v -> ((MainActivity)getActivity()).jumpToActivity(ScanActivity.class));
        mOivShop.setOnClickListener(v -> ((MainActivity)getActivity()).jumpToWebViewActivity(AppConst.WeChatUrl.JD));
        mOivGame.setOnClickListener(v -> ((MainActivity)getActivity()).jumpToWebViewActivity(AppConst.WeChatUrl.GAME));
    }

    @Override
    protected DiscoveryPresenter createPresenter() {
        return new DiscoveryPresenter((MainActivity)getActivity());
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_discovery;
    }
}
