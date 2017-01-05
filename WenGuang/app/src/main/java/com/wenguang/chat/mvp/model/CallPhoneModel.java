package com.wenguang.chat.mvp.model;

import android.content.Context;

import com.wenguang.chat.event.CallBackBmob;

/**
* Created by MVPHelper on 2016/11/28
*/

public interface CallPhoneModel{
    void callPhone(Context callPhoneActivity, String phoneNum);
    void queryData(String account, final CallBackBmob callBackBmob);

}