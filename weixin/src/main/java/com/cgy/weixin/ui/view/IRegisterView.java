package com.cgy.weixin.ui.view;

import android.widget.Button;
import android.widget.EditText;

/**
 * Created by cgy
 * 2018/5/29  9:18
 */
public interface IRegisterView {

    EditText getEtNickName();

    EditText getEtPhone();

    EditText getEtPwd();

    EditText getEtVerifyCode();

    Button getBtnSendCode();

}
