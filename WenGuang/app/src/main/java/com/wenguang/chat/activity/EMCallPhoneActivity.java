package com.wenguang.chat.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    ImageView mHeadimg;
    @Bind(R.id.phone)
    TextView mPhone;
    @Bind(R.id.address)
    TextView mAddress;
    @Bind(R.id.status)
    TextView mStatus;
    @Bind(R.id.guaduan)
    TextView mGuaduan;
    @Bind(R.id.jieting)
    TextView mJieting;
    @Bind(R.id.comming_layout)
    LinearLayout mCommingLayout;
    @Bind(R.id.mianti)
    TextView mMianti;
    @Bind(R.id.pu_call)
    TextView mPuCall;
    @Bind(R.id.quite)
    TextView mQuite;
    @Bind(R.id.keybox)
    TextView mKeybox;
    @Bind(R.id.kill)
    TextView mKill;
    @Bind(R.id.cantact)
    TextView mCantact;
    @Bind(R.id.call_layout)
    LinearLayout mCallLayout;
    private String phonenum;
    private  boolean isComming=false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_emcall_phone;
    }

    @Override
    protected void initInjector() {
        phonenum = getIntent().getStringExtra("phonenum");
        isComming=getIntent().getBooleanExtra("isComingCall",false);

    }

    @Override
    protected void initEventAndData() {
        mPhone.setText(phonenum);
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



    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getQueues().cancelAll("get");
    }

    @Override
    public void setStatus(int res) {
        switch (res) {
            case Common.CONTECTING:
                mStatus.setText(R.string.status_connecting);
                break;
            case Common.CONTECTED:
                mStatus.setText(R.string.status_connected);
                break;
            case Common.ONTHELINE:
                mStatus.setText(R.string.on_the_line);
                break;
            case Common.KILL:
                mStatus.setText(R.string.guaduan);
                break;
            case Common.OTHERKILL:
                mStatus.setText(R.string.otherguaduan);
                break;
        }


    }

    @Override
    public void setLocal(String res) {
        mAddress.setText(res);
    }


    @OnClick({R.id.guaduan, R.id.jieting, R.id.mianti, R.id.pu_call, R.id.quite, R.id.keybox, R.id.kill, R.id.cantact})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.guaduan:
                break;
            case R.id.jieting:
                break;
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
}
