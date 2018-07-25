package com.cgy.weixin.ui.view;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lqr.recyclerview.LQRRecyclerView;

/**
 * Created by cgy
 * 2018/6/11  15:45
 */
public interface ICreateGroupView {

    Button getBtnToolbarSend();

    LQRRecyclerView getRvContacts();

    LQRRecyclerView getRvSelectedContacts();

    EditText getEtKey();

    View getHeaderView();
}
