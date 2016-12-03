package com.wenguang.chat.mvp.model;

import com.wenguang.chat.bean.User;
import com.wenguang.chat.event.CallBackBmob;

/**
 * Created by MVPHelper on 2016/11/16
 */

public interface MineFragmentModel {
    void getUserByAccount(String account, final CallBackBmob<User> callBackBmob);

    void upDataUserMessageByAccount( String name, String sign, String idcard, CallBackBmob<String> call);
}