package com.wenguang.chat.mvp.presenter;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.wenguang.chat.common.UserManager;
import com.wenguang.chat.event.CallBackBmob;
import com.wenguang.chat.mvp.model.LoginModel;
import com.wenguang.chat.mvp.model.LoginModelImpl;
import com.wenguang.chat.mvp.view.LoginView;
import com.wenguang.chat.utils.MobileUtils;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;


/**
 * 作者：chenpan
 * 时间：2016/11/15 11:06
 * 邮箱：616707902@qq.com
 * 描述：
 */

public class LoginPresenter extends BasePresenter<LoginView> {
    LoginModel mLoginModel = new LoginModelImpl();

    public void login(final Context context, final String account, String passwed, boolean isverify) {
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(passwed)) {
            if (null != mView) {
                mView.editEmpty();
                return;
            }
        }
        if (null != mView) {
            mView.showLoadProgressDialog("登录中……");
        }
        if (isverify) {
            //SMSSDK.submitVerificationCode("+86", account, passwed);
            BmobSMS.verifySmsCode(account, passwed, new UpdateListener() {

                @Override
                public void done(BmobException ex) {
                    // TODO Auto-generated method stub
                    if (ex == null) {//短信验证码已验证成功
                        Log.i("bmob", "验证通过");
                        queryUserbyAccount(context, account);
                    } else {
                        if (mView != null) {
                            if (ex.getErrorCode() == 207) {
                                mView.showError("验证码错误！");
                            } else {
                                mView.showError(ex.getErrorCode() + ex.getLocalizedMessage());
                            }
                        }
                        Log.i("bmob", "验证失败：code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
                    }
                }
            });

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
            mLoginModel.sendVerifyCode(context, account, new CallBackBmob<Boolean>() {
                @Override
                public void succssCallBack(Boolean isok) {
                    if (isok) {
                        if (mView != null) {
                            mView.showError("已发送验证码");
                        }

                    } else {
                        if (mView != null) {
                            mView.showError("发送验证码失败");
                        }
                    }
                }

                @Override
                public void failed(String e) {
                    if (mView != null) {
                        mView.showError("发送验证码失败");
                    }
                }
            });
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
                    EMClient.getInstance().login(account, UserManager.getInstance().getUser().getPassword(), new EMCallBack() {//回调
                        @Override
                        public void onSuccess() {
                            EMClient.getInstance().groupManager().loadAllGroups();
                            EMClient.getInstance().chatManager().loadAllConversations();
                            Log.d("main", "登录聊天服务器成功！");
                            if (null != mView) {

                                mView.goHomeActivity();
                            }
                        }

                        @Override
                        public void onProgress(int progress, String status) {

                        }

                        @Override
                        public void onError(int code, String message) {
                            Log.d("main", "登录聊天服务器失败！");
                            mView.showError("登录聊天服务器失败！");
                        }
                    });

                } else {
                    addUser(context, account, account.substring(0, 4) + "wgtx");
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
    public void addUser(Context context, final String account, final String password) {
        mLoginModel.addData(account, password, new CallBackBmob<String>() {
            @Override
            public void succssCallBack(String jsonArray) {
                if (!TextUtils.isEmpty(jsonArray)) {
                    if (null != mView) {
                        EMClient.getInstance().login(account, password, new EMCallBack() {//回调
                            @Override
                            public void onSuccess() {
                                EMClient.getInstance().groupManager().loadAllGroups();
                                EMClient.getInstance().chatManager().loadAllConversations();
                                Log.d("main", "登录聊天服务器成功！");

                                mView.goHomeActivity();
                            }

                            @Override
                            public void onProgress(int progress, String status) {

                            }

                            @Override
                            public void onError(int code, String message) {
                                Log.d("main", "登录聊天服务器失败！");
                                mView.showError("登录聊天服务器失败！");
                            }
                        });
                    }
                } else {
                    if (null != mView) {
                        mView.showError("链接服务器错误！");
                    }
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

    /**
     * 查询数据库中判断登录
     *
     * @param context
     * @param account
     */
    public void queryUserbyUser(Context context, final String account, final String password) {
        mLoginModel.queryDataByUser(account, password, new CallBackBmob<Boolean>() {
            @Override
            public void succssCallBack(Boolean jsonArray) {
                if (null != mView && jsonArray) {
                    EMClient.getInstance().login(account, password, new EMCallBack() {//回调
                        @Override
                        public void onSuccess() {
                            EMClient.getInstance().groupManager().loadAllGroups();
                            EMClient.getInstance().chatManager().loadAllConversations();
                            Log.d("main", "登录聊天服务器成功！");
                            mView.goHomeActivity();
                        }

                        @Override
                        public void onProgress(int progress, String status) {

                        }

                        @Override
                        public void onError(int code, String message) {
                            Log.d("main", "登录聊天服务器失败！");
                            mView.showError("登录聊天服务器失败！");
                        }
                    });

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
