package com.cgy.weixin.ui.presenter;

import com.cgy.weixin.db.model.Friend;
import com.cgy.weixin.ui.base.BaseActivity;
import com.cgy.weixin.ui.base.BasePresenter;
import com.cgy.weixin.ui.view.IContactsView;
import com.lqr.adapter.LQRHeaderAndFooterAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cgy
 * 2018/5/30  10:22
 */
public class ContactsPresenter extends BasePresenter<IContactsView>{

    private List<Friend> mData = new ArrayList<>();
    private LQRHeaderAndFooterAdapter mAdapter;



    public ContactsPresenter(BaseActivity context) {
        super(context);
    }

    public void loadContacts() {
        setAdapter();
        loadData();
    }

    private void loadData() {
        //Observable.just();
    }

    private void setAdapter() {

    }
}
