package com.cgy.weixin.ui.presenter;

import com.bumptech.glide.Glide;
import com.cgy.weixin.R;
import com.cgy.weixin.api.ApiRetrofit;
import com.cgy.weixin.app.AppConst;
import com.cgy.weixin.db.DBManager;
import com.cgy.weixin.db.model.Friend;
import com.cgy.weixin.manager.BroadcastManager;
import com.cgy.weixin.model.cache.UserCache;
import com.cgy.weixin.model.response.QiNiuTokenResponse;
import com.cgy.weixin.ui.base.BaseActivity;
import com.cgy.weixin.ui.base.BasePresenter;
import com.cgy.weixin.ui.view.IMyInfoView;
import com.cgy.weixin.utils.LogUtils;
import com.cgy.weixin.utils.UIUtils;
import com.lqr.imagepicker.bean.ImageItem;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;

import io.rong.imlib.model.UserInfo;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cgy
 * 2018/6/4  10:40
 */
public class MyInfoPresenter extends BasePresenter<IMyInfoView>{

    public UserInfo mUserInfo;
    public UploadManager mUploadManager;

    public MyInfoPresenter(BaseActivity context) {
        super(context);
    }

    public void loadUserInfo() {
        mUserInfo = DBManager.getInstance().getUserInfo(UserCache.getId());
        if (mUserInfo != null) {
            Glide.with(mContext).load(mUserInfo.getPortraitUri()).centerCrop().into(getView().getIvHeader());
            getView().getOivName().setRightText(mUserInfo.getName());
            getView().getOivAccount().setRightText(mUserInfo.getUserId());
        }

    }

    public void setPortrait(ImageItem imageItem) {
        mContext.showWaitingDialog(UIUtils.getString(R.string.please_wait));
        //上传头像
        ApiRetrofit.getInstance().getQiNiuToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(qiNiuTokenResponse -> {
                    if (qiNiuTokenResponse != null && qiNiuTokenResponse.getCode() == 200) {
                        if (mUploadManager == null)
                            mUploadManager = new UploadManager();
                        File imageFile = new File(imageItem.path);
                        QiNiuTokenResponse.ResultEntity result = qiNiuTokenResponse.getResult();
                        String domain = result.getDomain();
                        String token = result.getToken();
                        //上传到七牛
                        mUploadManager.put(imageFile, null, token, (String s, ResponseInfo responseInfo, JSONObject jsonObject) -> {
                            if (responseInfo.isOK()) {
                                String key = jsonObject.optString("key");
                                String imageUrl = "http://" + domain + "/" + key;
                                //修改自己服务器头像数据
                                ApiRetrofit.getInstance().setPortrait(imageUrl)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(setPortraitResponse -> {
                                           if (setPortraitResponse != null && setPortraitResponse.getCode() == 200) {
                                               Friend friend = DBManager.getInstance().getFriendById(UserCache.getId());
                                               if (friend != null) {
                                                   friend.setPortraitUri(imageUrl);
                                                   DBManager.getInstance().saveOrUpdateFriend(friend);
                                                   DBManager.getInstance().updateGroupMemberPortraitUri(UserCache.getId(), imageUrl);
                                                   Glide.with(mContext).load(friend.getPortraitUri()).centerCrop().into(getView().getIvHeader());
                                                   BroadcastManager.getInstance(mContext).sendBroadcast(AppConst.CHANGE_INFO_FOR_ME);
                                                   BroadcastManager.getInstance(mContext).sendBroadcast(AppConst.UPDATE_CONVERSATIONS);
                                                   BroadcastManager.getInstance(mContext).sendBroadcast(AppConst.UPDATE_GROUP);
                                                   UIUtils.showToast(UIUtils.getString(R.string.set_success));
                                               }
                                               mContext.hideWaitingDialog();
                                           } else {
                                               uploadError(null);
                                           }
                                        }, this::uploadError);

                            } else {
                                uploadError(null);
                            }
                        }, null);
                    } else {
                        uploadError(null);
                    }
                }, this::uploadError);
    }



    private void uploadError(Throwable throwable) {
        if (throwable != null)
            LogUtils.sf(throwable.getLocalizedMessage());
        mContext.hideWaitingDialog();
        UIUtils.showToast(UIUtils.getString(R.string.set_fail));
    }
}
