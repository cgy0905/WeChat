package com.cgy.weixin.ui.view;

import android.widget.LinearLayout;

import com.lqr.recyclerview.LQRRecyclerView;

/**
 * Created by cgy
 * 2018/6/8  15:08
 */
public interface INewFriendView {

    LinearLayout getLlNoNewFriend();

    LinearLayout getLlHasNewFriend();

    LQRRecyclerView getRvNewFriend();
}
