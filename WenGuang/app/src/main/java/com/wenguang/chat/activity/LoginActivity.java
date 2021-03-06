package com.wenguang.chat.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wenguang.chat.R;
import com.wenguang.chat.base.BaseActivity;
import com.wenguang.chat.common.Common;
import com.wenguang.chat.mvp.presenter.BasePresenter;
import com.wenguang.chat.mvp.presenter.LoginPresenter;
import com.wenguang.chat.mvp.view.LoginView;
import com.wenguang.chat.utils.ClickUtil;
import com.wenguang.chat.utils.ToastUtils;
import com.wenguang.chat.widget.LoadProgressDialog;

import butterknife.Bind;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginView {


    @Bind(R.id.login_account)
    EditText mLoginAccount;
    @Bind(R.id.login_passwed)
    EditText mLoginPasswed;
    @Bind(R.id.login_auth)
    Button mLoginAuth;
    @Bind(R.id.login_login)
    Button mLoginLogin;
    @Bind(R.id.login_other)
    TextView mLoginOther;

    private String account;
    private String passwed;
  //  EventHandler mEventHandler;

    private LoadProgressDialog progressDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initInjector() {
     // MObSMS();
        bmobSMS();
    }

    /**
     * bmob短信验证
     */
    private void bmobSMS() {
       // BmobSMS.initialize(this, "4bdac565f3790ee4edac17b048261edc", new MySMSCodeListener());

    }
//    class MySMSCodeListener implements SMSCodeListener {
//
//        @Override
//        public void onReceive(String content) {
//            if(mLoginPasswed != null){
//                mLoginPasswed.setText(content);
//            }
//        }
//
//    }
    /**
     * mob短信验证
     */
//    private void MObSMS() {
//        mEventHandler = new EventHandler() {
//
//            @Override
//            public void afterEvent(int event, int result, final Object data) {
//
//                if (result == SMSSDK.RESULT_COMPLETE) {
//                    //回调完成
//                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
//                        //提交验证码成功
////                       goHomeActivity();
//                        ((LoginPresenter) mPresenter).queryUserbyAccount(LoginActivity.this, account);
//                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
//
//                        showError("已发送验证码");
//
//                        //获取验证码成功
//                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
//                        //返回支持发送验证码的国家列表
//                    }
//                } else {
//                    ((Throwable) data).printStackTrace();
//                    try {
//                        Throwable throwable = (Throwable) data;
//                        throwable.printStackTrace();
//                        JSONObject object = new JSONObject(throwable.getMessage());
//                        String des = object.optString("detail");//错误描述
//                        int status = object.optInt("status");//错误代码
//                        if (status > 0 && !TextUtils.isEmpty(des)) {
//                            showError(des);
//                            return;
//                        }
//                    } catch (Exception e) {
//                        //do something
//                        showError(((Throwable) data).toString());
//                    }
//
//                }
//            }
//        };
//        SMSSDK.registerEventHandler(mEventHandler); //注册短信回调
//    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    public BasePresenter getPresenter() {
        return new LoginPresenter();
    }


    @OnClick({R.id.login_auth, R.id.login_login, R.id.login_other})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_auth:
                account = mLoginAccount.getText().toString().trim();
                ((LoginPresenter) mPresenter).sendVerity(this, account);
                break;
            case R.id.login_login:
                if (!ClickUtil.isFastDoubleClick()) {
                    account = mLoginAccount.getText().toString().trim();
                    passwed = mLoginPasswed.getText().toString().trim();

                    ((LoginPresenter) mPresenter).login(this, account, passwed, mLoginAuth.getVisibility() == View.VISIBLE);
                }
                break;
            case R.id.login_other:
                if (mLoginAuth.getVisibility() == View.VISIBLE) {
                    mLoginAuth.setVisibility(View.GONE);
                    mLoginOther.setText(R.string.forget_verifylogin);
                } else {
                    mLoginAuth.setVisibility(View.VISIBLE);
                    mLoginOther.setText(R.string.passwed_login);
                }
                break;
        }
    }

    @Override
    public void editError() {
        ToastUtils.showToast(this, R.string.accountandpasswederror);
    }

    @Override
    public void editEmpty() {
        ToastUtils.showToast(this, R.string.accountandpasswedempty);
    }

    @Override
    public void verifyBtnText(boolean isClick, String text) {
        mLoginAuth.setClickable(isClick);
        mLoginAuth.setText(text);
    }

    @Override
    public void isMobNumber() {
        ToastUtils.showToast(this, R.string.notmobnumber);
    }

    @Override
    public void goHomeActivity() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dissDialog();
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                Common.USER_ACCOUNT = account;
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void showError(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dissDialog();
                ToastUtils.showToast(LoginActivity.this, msg);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      //  SMSSDK.unregisterEventHandler(mEventHandler);
    }


    @Override
    public void showLoadProgressDialog(String str) {
        progressDialog = LoadProgressDialog.getInstance(this);
        progressDialog.setMessage(str);
        progressDialog.show();
    }

    @Override
    public void dissDialog() {
        if (progressDialog != null)
            progressDialog.dismissWithSuccess();
    }
}
