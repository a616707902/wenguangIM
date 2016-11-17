package com.wenguang.chat.mvp.model;


import android.content.Context;

import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;

/**
* Created by MVPHelper on 2016/11/15
*/

public class LoginModelImpl implements LoginModel{

    @Override
    public void sendVerifyCode(Context context, String phone) {
        SMSSDK.getVerificationCode("+86", phone.trim(), new OnSendMessageHandler() {
            @Override
            public boolean onSendMessage(String s, String s1) {
                return false;
            }
        });
    }
}