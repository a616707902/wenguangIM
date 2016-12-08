package com.wenguang.chat.mvp.model;

import android.content.Context;

import com.wenguang.chat.bean.PhoneLocal;
import com.wenguang.chat.event.CallBackBmob;

/**
* Created by MVPHelper on 2016/12/07
*/

public interface EMCallPhoneModel{
    void volleyPost(Context context,String phonenum,CallBackBmob<PhoneLocal> callBackBmob);
}