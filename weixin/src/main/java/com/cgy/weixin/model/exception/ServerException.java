package com.cgy.weixin.model.exception;

import com.cgy.weixin.R;
import com.cgy.weixin.utils.UIUtils;

/**
 * Created by cgy
 * 2018/5/28  18:24
 * 服务器异常
 */
public class ServerException extends Exception{

    public ServerException(int errorCode) {
        this(UIUtils.getString(R.string.error_code) + errorCode);
    }
    public ServerException(String message) {
        super(message);
    }
}
