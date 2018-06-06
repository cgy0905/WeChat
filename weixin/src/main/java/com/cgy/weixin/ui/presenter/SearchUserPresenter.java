package com.cgy.weixin.ui.presenter;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.cgy.weixin.R;
import com.cgy.weixin.api.ApiRetrofit;
import com.cgy.weixin.app.AppConst;
import com.cgy.weixin.model.response.GetUserInfoByIdResponse;
import com.cgy.weixin.model.response.GetUserInfoByPhoneResponse;
import com.cgy.weixin.ui.activity.UserInfoActivity;
import com.cgy.weixin.ui.base.BaseActivity;
import com.cgy.weixin.ui.base.BasePresenter;
import com.cgy.weixin.ui.view.ISearchUserView;
import com.cgy.weixin.utils.LogUtils;
import com.cgy.weixin.utils.RegularUtils;
import com.cgy.weixin.utils.UIUtils;

import io.rong.imlib.model.UserInfo;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cgy
 * 2018/6/5  14:34
 */
public class SearchUserPresenter extends BasePresenter<ISearchUserView>{
    public SearchUserPresenter(BaseActivity context) {
        super(context);
    }

    public void searchUser() {
        String content = getView().getEtSearchContent().getText().toString().trim();

        if (TextUtils.isEmpty(content)) {
            UIUtils.showToast(UIUtils.getString(R.string.content_no_empty));
            return;
        }

        mContext.showWaitingDialog(UIUtils.getString(R.string.please_wait));
        if (RegularUtils.isMobile(content)) {
            ApiRetrofit.getInstance().getUserInfoFromPhone(AppConst.REGION, content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getUserInfoByPhoneResponse -> {
                        mContext.hideWaitingDialog();
                        if (getUserInfoByPhoneResponse.getCode() == 200) {
                            GetUserInfoByPhoneResponse.ResultEntity result = getUserInfoByPhoneResponse.getResult();
                            UserInfo userInfo = new UserInfo(result.getId(), result.getNickname(), Uri.parse(result.getPortraitUri()));
                            Intent intent = new Intent(mContext, UserInfoActivity.class);
                            intent.putExtra("userInfo", userInfo);
                            mContext.jumpToActivity(intent);
                        } else {
                            getView().getRlNoResultTip().setVisibility(View.VISIBLE);
                            getView().getLlSearch().setVisibility(View.GONE);
                        }
                    }, this::loadError);
        } else {
            ApiRetrofit.getInstance().getUserInfoById(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getUserInfoByIdResponse -> {
                        mContext.hideWaitingDialog();
                        if (getUserInfoByIdResponse.getCode() == 200) {
                            GetUserInfoByIdResponse.ResultEntity result = getUserInfoByIdResponse.getResult();
                            UserInfo userInfo = new UserInfo(result.getId(), result.getNickname(), Uri.parse(result.getPortraitUri()));
                            Intent intent = new Intent(mContext, UserInfoActivity.class);
                            intent.putExtra("userInfo", userInfo);
                            mContext.jumpToActivity(intent);
                        } else {
                            getView().getRlNoResultTip().setVisibility(View.VISIBLE);
                            getView().getLlSearch().setVisibility(View.GONE);
                        }
                    }, this::loadError);
        }
    }

    private void loadError(Throwable throwable) {
        mContext.hideWaitingDialog();
        LogUtils.sf(throwable.getLocalizedMessage());
    }
}
