package com.cgy.weixin.ui.activity;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cgy.weixin.R;
import com.cgy.weixin.app.AppConst;
import com.cgy.weixin.db.DBManager;
import com.cgy.weixin.db.model.GroupMember;
import com.cgy.weixin.model.cache.UserCache;
import com.cgy.weixin.ui.base.BaseActivity;
import com.cgy.weixin.ui.base.BasePresenter;
import com.cgy.weixin.utils.LogUtils;
import com.cgy.weixin.utils.UIUtils;
import com.lqr.ninegridimageview.LQRNineGridImageView;
import com.lqr.ninegridimageview.LQRNineGridImageViewAdapter;

import java.util.List;

import butterknife.Bind;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.rong.imlib.model.UserInfo;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cgy
 * 2018/6/4  11:32
 */
public class QRCodeCardActivity extends BaseActivity{

    private UserInfo mUserInfo;
    private String mGroupId;

    @Bind(R.id.ivHeader)
    ImageView mIvHeader;
    @Bind(R.id.ngiv)
    LQRNineGridImageView mNgiv;
    @Bind(R.id.tvName)
    TextView mTvName;
    @Bind(R.id.ivCard)
    ImageView mIvCard;
    @Bind(R.id.tvTip)
    TextView mTvTip;

    @Override
    public void init() {
        mGroupId = getIntent().getStringExtra("groupId");
    }

    @Override
    public void initView() {
        mTvTip.setText(UIUtils.getString(R.string.gy_code_card_tip));
    }

    @Override
    public void initData() {
        if (TextUtils.isEmpty(mGroupId)) {
            mUserInfo = DBManager.getInstance().getUserInfo(UserCache.getId());
            if (mUserInfo != null) {
                Glide.with(this).load(mUserInfo.getPortraitUri()).centerCrop().into(mIvHeader);
                mTvName.setText(mUserInfo.getName());
                setQRCode(AppConst.QrCodeCommon.ADD + mUserInfo.getUserId());
            }
        } else {
          mNgiv.setVisibility(View.VISIBLE);
          mIvHeader.setVisibility(View.GONE);
            Observable.just(DBManager.getInstance().getGroupsById(mGroupId))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(groups -> {
                       if (groups == null)
                           return;
                       mTvName.setText(groups.getName());
                    });
            mNgiv.setAdapter(new LQRNineGridImageViewAdapter<GroupMember>() {
                @Override
                protected void onDisplayImage(Context context, ImageView imageView, GroupMember groupMember) {
                    Glide.with(context).load(groupMember.getPortraitUri()).centerCrop().into(imageView);
                }
            });
            List<GroupMember> groupMembers = DBManager.getInstance().getGroupMembers(mGroupId);
            mNgiv.setImagesData(groupMembers);
            setQRCode(AppConst.QrCodeCommon.JOIN + mGroupId);
            mTvTip.setVisibility(View.GONE);
        }
    }

    private void setQRCode(String content) {
        Observable.just(QRCodeEncoder.syncEncodeQRCode(content, UIUtils.dip2Px(100)))
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bitmap -> mIvCard.setImageBitmap(bitmap), this::loadQRCardError);
    }

    private void loadQRCardError(Throwable throwable) {
        LogUtils.sf(throwable.getLocalizedMessage());
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_qr_code_card;
    }
}
