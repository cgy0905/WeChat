package com.cgy.weixin.ui.view;

import android.widget.EditText;

import com.lqr.recyclerview.LQRRecyclerView;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by cgy
 * 2018/6/5  10:14
 */
public interface ISessionView {

    BGARefreshLayout getRefreshLayout();

    LQRRecyclerView getRvMsg();

    EditText getEtContent();
}
