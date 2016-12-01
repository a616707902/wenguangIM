package com.wenguang.chat.mvp.presenter;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;

import com.wenguang.chat.event.CallBackBmob;
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

    public void login(Context context, final String account, String passwed, boolean isverify) {
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(passwed)) {
            if (null != mView) {
                mView.editEmpty();
                return;
            }
        }
        if (isverify) {
            SMSSDK.submitVerificationCode("+86", account, passwed);

        } else {
            queryUserbyUser(context, account, passwed);

        }


    }

    /**
     * 发送验证码
     *
     * @param context
     * @param account
     */
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

    /**
     * 查询数据库中是否有此账号
     *
     * @param context
     * @param account
     */
    public void queryUserbyAccount(final Context context, final String account) {
        mLoginModel.queryData(account, new CallBackBmob<Boolean>() {
            @Override
            public void succssCallBack(Boolean jsonArray) {
                if (jsonArray) {
                    if (null != mView) {
                        mView.goHomeActivity();
                    }
                } else {
                    addUser(context, account, "");
                }
            }

            @Override
            public void failed(String e) {
                mView.showError(e);
            }
        });
    }

    /**
     * 增加一条数据
     *
     * @param context
     * @param account
     * @param password
     */
    public void addUser(Context context, String account, String password) {
        mLoginModel.addData(account, password, new CallBackBmob<String>() {
            @Override
            public void succssCallBack(String jsonArray) {
                if (!TextUtils.isEmpty(jsonArray)) {
                    if (null != mView) {
                        mView.goHomeActivity();
                    }
                }else{
                    if (null!=mView){
                        mView.showError("链接服务器错误！");
                    }
                }
            }

            @Override
            public void failed(String e) {
                if (null!=mView){
                    mView.showError(e);
                }
            }
        });
    }

    /**
     * 查询数据库中判断登录
     *
     * @param context
     * @param account
     */
    public void queryUserbyUser(Context context, String account, String password) {
        mLoginModel.queryDataByUser(account, password, new CallBackBmob<Boolean>() {
            @Override
            public void succssCallBack(Boolean jsonArray) {
                if (null != mView && jsonArray) {
                    mView.goHomeActivity();
                }
            }

            @Override
            public void failed(String e) {
                if (null != mView) {
                    mView.showError(e);
                }
            }
        });
    }
}
