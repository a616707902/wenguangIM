package com.wenguang.chat.mvp.model;

import android.content.Context;

import com.wenguang.chat.event.CallBack;

/**
* Created by MVPHelper on 2016/11/16
*/

public interface ContactFragmentModel{
    void loadContacts(final Context context,final CallBack callBack);

}