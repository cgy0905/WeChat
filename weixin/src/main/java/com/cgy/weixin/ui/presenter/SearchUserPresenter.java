package com.cgy.weixin.ui.presenter;

import android.text.TextUtils;

import com.cgy.weixin.R;
import com.cgy.weixin.ui.base.BaseActivity;
import com.cgy.weixin.ui.base.BasePresenter;
import com.cgy.weixin.ui.view.ISearchUserView;
import com.cgy.weixin.utils.RegularUtils;
import com.cgy.weixin.utils.UIUtils;

/**
 * Created by cgy
 * 2018/6/5  14:34
 */
public class SearchUserPresenter extends BasePresenter<ISearchUserView>{
    public SearchUserPresenter(BaseActivity context) {
        super(context);
    }

    public void searchUser() {
        String content = getView().getEtSearchContent().getText().toString().trim();

        if (TextUtils.isEmpty(content)) {
            UIUtils.showToast(UIUtils.getString(R.string.content_no_empty));
            return;
        }

        mContext.showWaitingDialog(UIUtils.getString(R.string.please_wait));
        if (RegularUtils.isMobile(content)) {

        }
    }
}
