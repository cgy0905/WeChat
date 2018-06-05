package com.cgy.weixin.ui.activity;

import com.cgy.weixin.ui.base.BaseFragmentActivity;
import com.cgy.weixin.ui.presenter.SessionPresenter;
import com.cgy.weixin.ui.view.ISessionView;

import io.rong.imlib.model.Conversation;

/**
 * Created by cgy
 * 2018/6/5  9:43
 */
public class SessionActivity extends BaseFragmentActivity<ISessionView, SessionPresenter> implements ISessionView{

    public static final int REQUEST_IMAGE_PICKER = 1000;
    public static final int REQUEST_TAKE_PHONE = 1001;
    public static final int REQUEST_MY_LOCATION = 1002;

    public static final int SESSION_TYPE_PRIVATE = 1;
    public static final int SESSION_TYPE_GROUP = 2;

    private String mSessionId = "";
    private boolean mIsFirst = false;
    private Conversation.ConversationType mConversationType = Conversation.ConversationType.PRIVATE;


    @Override
    protected SessionPresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return 0;
    }
}
