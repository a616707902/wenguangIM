package com.wenguang.chat.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.util.NetUtils;
import com.wenguang.chat.R;
import com.wenguang.chat.base.BaseActivity;
import com.wenguang.chat.common.Common;
import com.wenguang.chat.common.MyApplication;
import com.wenguang.chat.mvp.presenter.BasePresenter;
import com.wenguang.chat.mvp.presenter.EMCallPhonePresenter;
import com.wenguang.chat.mvp.view.EMCallPhoneView;

import butterknife.Bind;
import butterknife.OnClick;

public class EMCallPhoneActivity extends BaseActivity implements EMCallPhoneView {

    @Bind(R.id.headimg)
    ImageView headimg;
    ;
    @Bind(R.id.phone)
    TextView phone;
    @Bind(R.id.address)
    TextView address;
    @Bind(R.id.status)
    TextView status;
    @Bind(R.id.mianti)
    TextView mianti;
    @Bind(R.id.pu_call)
    TextView puCall;
    @Bind(R.id.quite)
    TextView quite;
    @Bind(R.id.keybox)
    TextView keybox;
    @Bind(R.id.kill)
    TextView kill;
    @Bind(R.id.cantact)
    TextView cantact;
    private String phonenum;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_emcall_phone;
    }

    @Override
    protected void initInjector() {
        phonenum = getIntent().getStringExtra("phonenum");
    }

    @Override
    protected void initEventAndData() {
        phone.setText(phonenum);
        ((EMCallPhonePresenter) mPresenter).getLocale(this, phonenum);
        setStatus(Common.CONTECTING);


    }




    @Override
    public void showLoadProgressDialog(String str) {

    }

    @Override
    public void dissDialog() {

    }

    @Override
    public BasePresenter getPresenter() {
        return new EMCallPhonePresenter();
    }


    @OnClick({R.id.mianti, R.id.pu_call, R.id.quite, R.id.keybox, R.id.kill, R.id.cantact})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mianti:
                break;
            case R.id.pu_call:
                break;
            case R.id.quite:
                break;
            case R.id.keybox:
                break;
            case R.id.kill:
                break;
            case R.id.cantact:
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getQueues().cancelAll("get");
    }

    @Override
    public void setStatus(int res) {
        switch (res) {
            case Common.CONTECTING:
                status.setText(R.string.status_connecting);
                break;
            case Common.CONTECTED:
                status.setText(R.string.status_connected);
                break;
        }


    }

    @Override
    public void setLocal(String res) {
        address.setText(res);
    }

}
