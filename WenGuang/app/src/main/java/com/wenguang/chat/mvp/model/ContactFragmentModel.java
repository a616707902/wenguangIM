package com.wenguang.chat.mvp.model;

import android.content.Context;

import com.wenguang.chat.event.CallBack;
import com.wenguang.chat.utils.common.SortModel;

import java.util.List;

/**
* Created by MVPHelper on 2016/11/16
*/

public interface ContactFragmentModel{
    void loadContacts(final Context context,final CallBack callBack);
   void search(String str, List<SortModel> mAllContactsList,final CallBack callBack);


}