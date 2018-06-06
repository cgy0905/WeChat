package com.cgy.weixin.ui.presenter;

import com.cgy.weixin.R;
import com.cgy.weixin.api.ApiRetrofit;
import com.cgy.weixin.ui.base.BaseActivity;
import com.cgy.weixin.ui.base.BasePresenter;
import com.cgy.weixin.ui.view.IPostScriptView;
import com.cgy.weixin.utils.LogUtils;
import com.cgy.weixin.utils.UIUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cgy
 * 2018/6/5  17:41
 */
public class PostScriptPresenter extends BasePresenter<IPostScriptView>{

    public PostScriptPresenter(BaseActivity context) {
        super(context);
    }

    public void addFriend(String userId) {
        String msg = getView().getEtMsg().getText().toString().trim();
        ApiRetrofit.getInstance().sendFriendInvitation(userId, msg)
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(friendRelationshipResponse -> {
                   if (friendRelationshipResponse.getCode() == 200) {
                       UIUtils.showToast(UIUtils.getString(R.string.request_sent_success));
                       mContext.finish();
                   } else {
                       UIUtils.showToast(UIUtils.getString(R.string.request_sent_fail));
                   }
                }, this::loadError);

    }

    private void loadError(Throwable throwable) {
        LogUtils.sf(throwable.getLocalizedMessage());
        UIUtils.showToast(throwable.getLocalizedMessage());
    }
}
