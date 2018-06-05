package com.cgy.weixin.ui.view;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by cgy
 * 2018/6/5  14:34
 */
public interface ISearchUserView {

    EditText getEtSearchContent();

    RelativeLayout getRlNoResultTip();

    LinearLayout getLlSearch();
}
