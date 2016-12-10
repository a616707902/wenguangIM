package com.wenguang.chat.mvp.view;

import com.wenguang.chat.utils.common.SortModel;

import java.util.List;

/**
* Created by MVPHelper on 2016/11/28
*/

public interface CallPhoneView extends BaseView{
    void  setAdapter(List<SortModel> models);

    void setList(List<SortModel> models);
    void showMessage(String str);
    void showDialog(String phone,String str);
}