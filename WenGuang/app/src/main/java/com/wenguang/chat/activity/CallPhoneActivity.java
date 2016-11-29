package com.wenguang.chat.activity;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.wenguang.chat.R;
import com.wenguang.chat.base.BaseActivity;
import com.wenguang.chat.mvp.presenter.BasePresenter;
import com.wenguang.chat.mvp.presenter.CallPhonePresenter;
import com.wenguang.chat.mvp.view.CallPhoneView;

import butterknife.Bind;
import butterknife.OnClick;

public class CallPhoneActivity extends BaseActivity implements CallPhoneView {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.contact_lv)
    ListView mContactLv;
    @Bind(R.id.call_phone_num)
    EditText mCallPhoneNum;
    @Bind(R.id.one)
    Button mOne;
    @Bind(R.id.two)
    Button mTwo;
    @Bind(R.id.three)
    Button mThree;
    @Bind(R.id.four)
    Button mFour;
    @Bind(R.id.five)
    Button mFive;
    @Bind(R.id.six)
    Button mSix;
    @Bind(R.id.seven)
    Button mSeven;
    @Bind(R.id.eight)
    Button mEight;
    @Bind(R.id.nine)
    Button mNine;
    @Bind(R.id.zero)
    Button mZero;
    @Bind(R.id.main_btn)
    ImageView mMainBtn;
    @Bind(R.id.call_phone)
    ImageView mCallPhone;
    @Bind(R.id.delete)
    ImageView mDelete;
    @Bind(R.id.keyboard)
    LinearLayout mKeyboard;
    private InputMethodManager mIM;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_call_phone;
    }

    @Override
    protected void initInjector() {
        mIM = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mCallPhoneNum.setInputType(InputType.TYPE_NULL);

    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    public BasePresenter getPresenter() {
        return new CallPhonePresenter();
    }


    @OnClick({R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine, R.id.zero, R.id.main_btn, R.id.call_phone, R.id.delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.one:
                break;
            case R.id.two:
                break;
            case R.id.three:
                break;
            case R.id.four:
                break;
            case R.id.five:
                break;
            case R.id.six:
                break;
            case R.id.seven:
                break;
            case R.id.eight:
                break;
            case R.id.nine:
                break;
            case R.id.zero:
                break;
            case R.id.main_btn:
                break;
            case R.id.call_phone:
                break;
            case R.id.delete:
                break;
        }
    }



    @OnClick(R.id.call_phone_num)
    public void onClick() {
    }
}
