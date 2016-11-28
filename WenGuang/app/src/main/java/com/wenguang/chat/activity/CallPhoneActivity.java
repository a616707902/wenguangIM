package com.wenguang.chat.activity;

import com.wenguang.chat.base.BaseActivity;
import com.wenguang.chat.mvp.presenter.BasePresenter;
import com.wenguang.chat.mvp.view.CallPhoneView;

public class CallPhoneActivity extends BaseActivity implements CallPhoneView{


    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }
}
