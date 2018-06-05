package com.cgy.weixin.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import com.cgy.weixin.R;
import com.cgy.weixin.app.AppConst;
import com.cgy.weixin.manager.BroadcastManager;
import com.cgy.weixin.ui.base.BaseActivity;
import com.cgy.weixin.ui.presenter.MyInfoPresenter;
import com.cgy.weixin.ui.view.IMyInfoView;
import com.lqr.imagepicker.ImagePicker;
import com.lqr.imagepicker.bean.ImageItem;
import com.lqr.imagepicker.ui.ImageGridActivity;
import com.lqr.optionitemview.OptionItemView;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;

import butterknife.Bind;

public class MyInfoActivity extends BaseActivity<IMyInfoView, MyInfoPresenter> implements IMyInfoView {

    public static final int REQUEST_IMAGE_PICKER = 1000;
    @Bind(R.id.ivHeader)
    ImageView mIvHeader;
    @Bind(R.id.llHeader)
    AutoLinearLayout mLlHeader;

    @Bind(R.id.oivName)
    OptionItemView mOivName;
    @Bind(R.id.oivAccount)
    OptionItemView mOivAccount;
    @Bind(R.id.oivQRCodeCard)
    OptionItemView mOivQRCodeCard;

    @Override
    public void init() {
        super.init();
        registerBR();
    }

    @Override
    public void initData() {
        mPresenter.loadUserInfo();
    }

    @Override
    public void initListener() {
        mIvHeader.setOnClickListener(v -> {
            Intent intent = new Intent(MyInfoActivity.this, ShowBigImageActivity.class);
            intent.putExtra("url", mPresenter.mUserInfo.getPortraitUri().toString());
            jumpToActivity(intent);
        });

        mLlHeader.setOnClickListener(v -> {
            Intent intent = new Intent(this, ImageGridActivity.class);
            startActivityForResult(intent, REQUEST_IMAGE_PICKER);
        });
        mOivQRCodeCard.setOnClickListener(v -> jumpToActivity(QRCodeCardActivity.class));
        mOivName.setOnClickListener(v -> jumpToActivity(ChangeMyNameActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBR();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE_PICKER:
                if (requestCode == ImagePicker.RESULT_CODE_ITEMS) {
                    if (data != null) {
                        ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                        if (images != null && images.size() > 0) {
                            ImageItem imageItem = images.get(0);
                            mPresenter.setPortrait(imageItem);

                        }
                    }
                }
                break;
        }
    }

    private void registerBR() {
        BroadcastManager.getInstance(this).register(AppConst.CHANGE_INFO_FOR_CHANGE_NAME, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mPresenter.loadUserInfo();
            }
        });
    }

    private void unregisterBR() {
        BroadcastManager.getInstance(this).unregister(AppConst.CHANGE_INFO_FOR_CHANGE_NAME);
    }

    @Override
    protected MyInfoPresenter createPresenter() {
        return new MyInfoPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_my_info;
    }

    @Override
    public ImageView getIvHeader() {
        return mIvHeader;
    }

    @Override
    public OptionItemView getOivName() {
        return mOivName;
    }

    @Override
    public OptionItemView getOivAccount() {
        return mOivAccount;
    }

}
