package com.cgy.weixin.ui.view;

import android.view.View;
import android.widget.TextView;

import com.lqr.recyclerview.LQRRecyclerView;

/**
 * Created by cgy
 * 2018/5/30  10:20
 */
public interface IContactsView {

    View getHeaderView();

    LQRRecyclerView getRvContacts();

    TextView getFooterView();
}
