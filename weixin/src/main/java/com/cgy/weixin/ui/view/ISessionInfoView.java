package com.cgy.weixin.ui.view;

import android.widget.Button;

import com.kyleduo.switchbutton.SwitchButton;
import com.lqr.optionitemview.OptionItemView;
import com.lqr.recyclerview.LQRRecyclerView;

/**
 * Created by cgy
 * 2018/6/12  11:02
 */
public interface ISessionInfoView {

    LQRRecyclerView getRvMember();

    OptionItemView getOivGroupName();

    OptionItemView getOivNickNameInGroup();

    SwitchButton getSbToTop();

    Button getBtnQuit();
}
