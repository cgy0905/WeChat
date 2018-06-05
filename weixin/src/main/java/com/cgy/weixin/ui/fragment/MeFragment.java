package com.cgy.weixin.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cgy.weixin.R;
import com.cgy.weixin.app.AppConst;
import com.cgy.weixin.manager.BroadcastManager;
import com.cgy.weixin.ui.activity.MainActivity;
import com.cgy.weixin.ui.activity.MyInfoActivity;
import com.cgy.weixin.ui.activity.SettingActivity;
import com.cgy.weixin.ui.base.BaseFragment;
import com.cgy.weixin.ui.presenter.MePresenter;
import com.cgy.weixin.ui.view.IMeView;
import com.cgy.weixin.utils.LogUtils;
import com.cgy.weixin.utils.UIUtils;
import com.cgy.weixin.widget.CustomDialog;
import com.lqr.optionitemview.OptionItemView;

import butterknife.Bind;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.rong.imlib.model.UserInfo;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cgy
 * 2018/5/29  17:37
 */
public class MeFragment extends BaseFragment<IMeView, MePresenter> implements IMeView{

    @Bind(R.id.llMyInfo)
    LinearLayout mLlMyInfo;
    @Bind(R.id.ivHeader)
    ImageView mIvHeader;
    @Bind(R.id.tvName)
    TextView mTvName;
    @Bind(R.id.tvAccount)
    TextView mTvAccount;
    @Bind(R.id.ivGYCordCard)
    ImageView mIvGYCordCard;

    @Bind(R.id.oivAlbum)
    OptionItemView mOivAlbum;
    @Bind(R.id.oivCollection)
    OptionItemView mOivCollection;
    @Bind(R.id.oivWallet)
    OptionItemView mOivWallet;
    @Bind(R.id.oivCardPacket)
    OptionItemView mOivCardPacket;

    @Bind(R.id.oivSetting)
    OptionItemView mOivSetting;

    private CustomDialog mGyCardDialog;


    @Override
    public void init() {
        registerBR();
    }

    @Override
    public void initData() {
        mPresenter.loadUserInfo();
    }

    @Override
    public void initView(View rootView) {
        mIvGYCordCard.setOnClickListener(v -> showGYCard());
        mOivAlbum.setOnClickListener(v -> ((MainActivity)getActivity()).jumpToWebViewActivity(AppConst.WeChatUrl.MY_JIAN_SHU));
        mOivCollection.setOnClickListener(v -> ((MainActivity)getActivity()).jumpToWebViewActivity(AppConst.WeChatUrl.MY_CSDN));
        mOivWallet.setOnClickListener(v -> ((MainActivity)getActivity()).jumpToWebViewActivity(AppConst.WeChatUrl.MY_OSCHINA));
        mOivCardPacket.setOnClickListener(v -> ((MainActivity)getActivity()).jumpToWebViewActivity(AppConst.WeChatUrl.MY_GITHUB));
    }

    @Override
    public void initListener() {
        mLlMyInfo.setOnClickListener(v -> ((MainActivity)getActivity()).jumpToActivityAndClearTop(MyInfoActivity.class));
        mOivSetting.setOnClickListener(v -> ((MainActivity)getActivity()).jumpToActivityAndClearTop(SettingActivity.class));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterBR();
    }

    private void showGYCard() {
        if (mGyCardDialog == null) {
            View gyCardView = View.inflate(getActivity(), R.layout.include_gycode_card, null);
            ImageView ivHeader = (ImageView) gyCardView.findViewById(R.id.ivHeader);
            TextView tvName = (TextView) gyCardView.findViewById(R.id.tvName);
            ImageView ivCard = (ImageView) gyCardView.findViewById(R.id.ivCard);
            TextView tvTip = (TextView) gyCardView.findViewById(R.id.tvTip);
            tvTip.setText(UIUtils.getString(R.string.gy_code_card_tip));

            UserInfo userInfo = mPresenter.getUserInfo();
            if (userInfo != null) {
                Glide.with(getActivity()).load(userInfo.getPortraitUri()).centerCrop().into(ivHeader);
                tvName.setText(userInfo.getName());
                Observable.just(QRCodeEncoder.syncEncodeQRCode(AppConst.QrCodeCommon.ADD + userInfo.getUserId(), UIUtils.dip2Px(100)))
                        .observeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(bitmap -> ivCard.setImageBitmap(bitmap), this::loadGYCardError);
            }
            mGyCardDialog = new CustomDialog(getActivity(), 300, 400, gyCardView, R.style.MyDialog);
        }
        mGyCardDialog.show();
    }

    private void loadGYCardError(Throwable throwable) {
        LogUtils.sf(throwable.getLocalizedMessage());
    }

    private void registerBR() {
        BroadcastManager.getInstance(getActivity()).register(AppConst.CHANGE_INFO_FOR_ME, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mPresenter.loadUserInfo();
            }
        });
    }
    private void unregisterBR() {
        BroadcastManager.getInstance(getActivity()).unregister(AppConst.CHANGE_INFO_FOR_ME);
    }

    @Override
    protected MePresenter createPresenter() {
        return new MePresenter((MainActivity)getActivity());
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_me;
    }

    @Override
    public ImageView getIvHeader() {
        return mIvHeader;
    }

    @Override
    public TextView getTvName() {
        return mTvName;
    }

    @Override
    public TextView getTvAccount() {
        return mTvAccount;
    }
}
