package com.wenguang.chat.mvp.presenter;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;

import com.wenguang.chat.mvp.model.LoginModel;
import com.wenguang.chat.mvp.model.LoginModelImpl;
import com.wenguang.chat.mvp.view.LoginView;
import com.wenguang.chat.utils.MobileUtils;

import cn.smssdk.SMSSDK;

/**
 * 作者：chenpan
 * 时间：2016/11/15 11:06
 * 邮箱：616707902@qq.com
 * 描述：
 */

public class LoginPresenter extends BasePresenter<LoginView> {
    LoginModel mLoginModel = new LoginModelImpl();

    public void login(Context context, String account, String passwed, boolean isverify) {
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(passwed)) {
            if (null != mView) {
                mView.editEmpty();
                return;
            }
        }
        if (isverify) {
            SMSSDK.submitVerificationCode("+86", account, passwed);
        }else{
            if (null != mView) {
                mView.goHomeActivity();
            }
        }


    }

    public void sendVerity(Context context, String account) {
        if (TextUtils.isEmpty(account)) {
            if (null != mView) {
                mView.editEmpty();
                return;
            }
        }
        if (MobileUtils.isMobileNo(account)) {
            new TimeCount(60 * 1000, 1000).start();
            mLoginModel.sendVerifyCode(context, account);
        } else {
            if (mView != null) {
                mView.isMobNumber();
            }
        }


    }

    /***
     * 获取验证码倒计时
     */
    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (mView != null) {
                mView.verifyBtnText(false, millisUntilFinished / 1000 + "秒后可重发");
            }
        }

        @Override
        public void onFinish() {
            if (mView != null) {
                mView.verifyBtnText(true, "重新获取");
            }
        }
    }
}
