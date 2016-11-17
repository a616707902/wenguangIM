package com.wenguang.chat.mvp.model;

import android.content.Context;

/**
* Created by MVPHelper on 2016/11/15
*/

public interface LoginModel{

void sendVerifyCode(Context context,String phone);
}