package com.cgy.weixin.ui.presenter;

import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cgy.weixin.R;
import com.cgy.weixin.api.ApiRetrofit;
import com.cgy.weixin.app.AppConst;
import com.cgy.weixin.db.DBManager;
import com.cgy.weixin.db.model.Friend;
import com.cgy.weixin.manager.BroadcastManager;
import com.cgy.weixin.model.cache.UserCache;
import com.cgy.weixin.model.exception.ServerException;
import com.cgy.weixin.model.response.AgreeFriendsResponse;
import com.cgy.weixin.model.response.GetUserInfoByIdResponse;
import com.cgy.weixin.model.response.UserRelationshipResponse;
import com.cgy.weixin.ui.base.BaseActivity;
import com.cgy.weixin.ui.base.BasePresenter;
import com.cgy.weixin.ui.view.INewFriendView;
import com.cgy.weixin.utils.LogUtils;
import com.cgy.weixin.utils.NetUtils;
import com.cgy.weixin.utils.UIUtils;
import com.lqr.adapter.LQRAdapterForRecyclerView;
import com.lqr.adapter.LQRViewHolderForRecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.model.UserInfo;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by cgy
 * 2018/6/8  15:09
 */
public class NewFriendPresenter extends BasePresenter<INewFriendView>{

    private List<UserRelationshipResponse.ResultEntity> mData = new ArrayList<>();
    private LQRAdapterForRecyclerView<UserRelationshipResponse.ResultEntity> mAdapter;

    public NewFriendPresenter(BaseActivity context) {
        super(context);
    }

    public void loadNewFriendData() {
        if (!NetUtils.isNetworkAvailable(mContext)) {
            UIUtils.showToast(UIUtils.getString(R.string.please_check_net));
            return;
        }

        loadData();
        setAdapter();
    }

    private void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new LQRAdapterForRecyclerView<UserRelationshipResponse.ResultEntity>(mContext, mData, R.layout.item_new_friends) {
                @Override
                public void convert(LQRViewHolderForRecyclerView helper, UserRelationshipResponse.ResultEntity item, int position) {
                    ImageView ivHeader = helper.getView(R.id.ivHeader);
                    helper.setText(R.id.tvName, item.getUser().getNickname())
                            .setText(R.id.tvMsg, item.getMessage());

                    if (item.getStatus() == 20) {//已经是好友
                        helper.setViewVisibility(R.id.tvAdded, View.VISIBLE)
                                .setViewVisibility(R.id.tvWait, View.GONE)
                                .setViewVisibility(R.id.btnAck, View.GONE);
                    } else if (item.getStatus() == 11) {//别人发来的添加好友请求
                        helper.setViewVisibility(R.id.tvAdded, View.GONE)
                                .setViewVisibility(R.id.tvWait, View.GONE)
                                .setViewVisibility(R.id.btnAck, View.VISIBLE);

                    } else if (item.getStatus() == 10) {//我发起的添加好友请求
                        helper.setViewVisibility(R.id.tvAdded, View.GONE)
                                .setViewVisibility(R.id.tvWait, View.VISIBLE)
                                .setViewVisibility(R.id.btnAck, View.GONE);
                    }

                    String portraitUri = item.getUser().getPortraitUri();
                    if (TextUtils.isEmpty(portraitUri)) {
                        portraitUri = DBManager.getInstance().getPortraitUri(item.getUser().getNickname(), item.getUser().getId());
                    }
                    Glide.with(mContext).load(portraitUri).centerCrop().into(ivHeader);
                    helper.getView(R.id.btnAck).setOnClickListener(v -> agreeFriends(item.getUser().getId(), helper));
                }
            };
        }
        getView().getRvNewFriend().setAdapter(mAdapter);
    }

    private void agreeFriends(String friendId, LQRViewHolderForRecyclerView helper) {
        if (!NetUtils.isNetworkAvailable(mContext)){
            UIUtils.showToast(UIUtils.getString(R.string.please_check_net));
        }
        ApiRetrofit.getInstance().agreeFriends(friendId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap((Func1<AgreeFriendsResponse, Observable<GetUserInfoByIdResponse>>) agreeFriendsResponse -> {
                    if (agreeFriendsResponse != null && agreeFriendsResponse.getCode() == 200) {
                        helper.setViewVisibility(R.id.tvAdded, View.VISIBLE)
                                .setViewVisibility(R.id.btnAck, View.GONE);
                        return ApiRetrofit.getInstance().getUserInfoById(friendId);
                    }
                    return Observable.error(new ServerException(UIUtils.getString(R.string.agree_friend_fail)));
                })
        .subscribe(getUserInfoByIdResponse -> {
            if (getUserInfoByIdResponse != null && getUserInfoByIdResponse.getCode() == 200) {
                GetUserInfoByIdResponse.ResultEntity result = getUserInfoByIdResponse.getResult();
                UserInfo userInfo = new UserInfo(UserCache.getId(), result.getNickname(), Uri.parse(result.getPortraitUri()));
                if (TextUtils.isEmpty(userInfo.getPortraitUri().toString())) {
                    userInfo.setPortraitUri(Uri.parse(DBManager.getInstance().getPortraitUri(userInfo)));
                }
                Friend friend = new Friend(userInfo.getUserId(), userInfo.getName(), userInfo.getPortraitUri().toString());
                DBManager.getInstance().saveOrUpdateFriend(friend);
                UIUtils.postTaskDelay(() -> {
                    BroadcastManager.getInstance(UIUtils.getContext()).sendBroadcast(AppConst.UPDATE_FRIEND);
                    BroadcastManager.getInstance(UIUtils.getContext()).sendBroadcast(AppConst.UPDATE_CONVERSATIONS);
                }, 1000);
            }
        }, this::loadError);
    }

    private void loadData() {
        ApiRetrofit.getInstance().getAllUserRelationship()
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userRelationshipResponse -> {
                   if (userRelationshipResponse.getCode() == 200) {
                       List<UserRelationshipResponse.ResultEntity> result = userRelationshipResponse.getResult();

                       if (result != null && result.size() > 0) {
                           for (int i = 0; i < result.size(); i++) {
                               UserRelationshipResponse.ResultEntity entity = result.get(i);
                               if (entity.getStatus() == 10) {//是我发起的添加好友请求
                                   result.remove(entity);
                                   i--;
                               }
                           }
                       }

                       if (result != null && result.size() > 0) {
                           getView().getLlHasNewFriend().setVisibility(View.VISIBLE);
                           mData.clear();
                           mData.addAll(result);
                           if (mAdapter != null)
                               mAdapter.notifyDataSetChangedWrapper();
                       } else {
                           getView().getLlNoNewFriend().setVisibility(View.VISIBLE);
                       }
                   } else {
                       Observable.error(new ServerException(UIUtils.getString(R.string.load_error)));
                   }
                }, this::loadError);

    }

    private void loadError(Throwable throwable) {
        LogUtils.sf(throwable.getLocalizedMessage());
        UIUtils.showToast(throwable.getLocalizedMessage());
    }
}
