package com.wenguang.chat.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.wenguang.chat.R;
import com.wenguang.chat.common.Common;
import com.wenguang.chat.common.UserManager;
import com.wenguang.chat.event.CallBackBmob;
import com.wenguang.chat.mvp.model.LoginModel;
import com.wenguang.chat.mvp.model.LoginModelImpl;
import com.wenguang.chat.utils.StatusBarUtil;
import com.wenguang.chat.utils.ToastUtils;


/**
 *
 */
public class SplashActivity extends Activity {

    LoginModel mLoginModel = new LoginModelImpl();
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {

        }
    };

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        StatusBarUtil.setTransparent(this);

        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (UserManager.getInstance().isLogin()) {
                        queryUserbyUser(SplashActivity.this,UserManager.getInstance().getUser().getAccount(),UserManager.getInstance().getUser().getPassword());
                } else {
                    Intent intent = new Intent(SplashActivity.this,
                            LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);
    }

    @Override
    public void onBackPressed() {
    }

    /**
     * 查询数据库中判断登录
     *
     * @param context
     * @param account
     */
    public void queryUserbyUser(Context context, final String account, String password) {
        mLoginModel.queryDataByUser(account, password, new CallBackBmob<Boolean>() {
            @Override
            public void succssCallBack(Boolean jsonArray) {
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                Common.USER_ACCOUNT = account;
                startActivity(intent);
                finish();
            }

            @Override
            public void failed(String e) {
                ToastUtils.showToast(SplashActivity.this, e);
            }
        });
    }
}
