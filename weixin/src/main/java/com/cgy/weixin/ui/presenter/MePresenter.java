package com.cgy.weixin.ui.presenter;

import android.net.Uri;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.cgy.weixin.R;
import com.cgy.weixin.api.ApiRetrofit;
import com.cgy.weixin.db.DBManager;
import com.cgy.weixin.db.model.Friend;
import com.cgy.weixin.model.cache.UserCache;
import com.cgy.weixin.model.response.GetUserInfoByIdResponse;
import com.cgy.weixin.ui.base.BaseActivity;
import com.cgy.weixin.ui.base.BasePresenter;
import com.cgy.weixin.ui.view.IMeView;
import com.cgy.weixin.utils.LogUtils;
import com.cgy.weixin.utils.UIUtils;

import io.rong.imlib.model.UserInfo;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cgy
 * 2018/6/5  10:42
 */
public class MePresenter extends BasePresenter<IMeView>{

    private UserInfo mUserInfo;
    private boolean isFirst = true;

    public MePresenter(BaseActivity context) {
        super(context);
    }

    public void loadUserInfo() {
        mUserInfo = DBManager.getInstance().getUserInfo(UserCache.getId());
        if (mUserInfo == null || isFirst) {
            isFirst = false;
            ApiRetrofit.getInstance().getUserInfoById(UserCache.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getUserInfoByIdResponse -> {
                        if (getUserInfoByIdResponse != null && getUserInfoByIdResponse.getCode() == 200) {
                            GetUserInfoByIdResponse.ResultEntity result = getUserInfoByIdResponse.getResult();

                            mUserInfo = new UserInfo(UserCache.getId(), result.getNickname(), Uri.parse(result.getPortraitUri()));
                            if (TextUtils.isEmpty(mUserInfo.getPortraitUri().toString())) {
                                mUserInfo.setPortraitUri(Uri.parse(DBManager.getInstance().getPortraitUri(mUserInfo)));
                            }

                            DBManager.getInstance().saveOrUpdateFriend(new Friend(mUserInfo.getUserId(), mUserInfo.getName(), mUserInfo.getPortraitUri().toString()));
                            fillView();
                        }
                    }, this::loadError);
        } else {
            fillView();
        }

    }

    private void loadError(Throwable throwable) {
        LogUtils.sf(throwable.getLocalizedMessage());
        UIUtils.showToast(throwable.getLocalizedMessage());
    }

    private void fillView() {
        if (mUserInfo != null) {
            Glide.with(mContext).load(mUserInfo.getPortraitUri()).centerCrop().into(getView().getIvHeader());
            getView().getTvAccount().setText(UIUtils.getString(R.string.my_chat_account, mUserInfo.getUserId()));
            getView().getTvName().setText(mUserInfo.getName());
        }
    }

    public void refreshUserInfo() {
        UserInfo userInfo = DBManager.getInstance().getUserInfo(UserCache.getId());
        if (userInfo == null) {
            loadUserInfo();
        } else {
            mUserInfo = userInfo;
        }
    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }
}
