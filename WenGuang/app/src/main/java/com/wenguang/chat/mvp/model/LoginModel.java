package com.wenguang.chat.mvp.model;

import android.content.Context;

import com.wenguang.chat.event.CallBackBmob;

/**
* Created by MVPHelper on 2016/11/15
*/

public interface LoginModel{

    void sendVerifyCode(Context context, String phone, final CallBackBmob callBackBmob);
    void queryData(String account, final CallBackBmob callBackBmob);
     void addData(String account ,String password, final CallBackBmob callBackBmob);
    void queryDataByUser(String account, String password,final CallBackBmob callBackBmob);
}