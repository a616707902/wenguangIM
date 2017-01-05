package com.wenguang.chat.mvp.view;

/**
* Created by MVPHelper on 2016/12/07
*/

public interface EMCallPhoneView extends BaseView{
    
    /**
     * 设置状态
     * @param res
     */
    void  setStatus(int res);

    /**
     * 设置归属地
     * @param res
     */
    void  setLocal(String res);

    void showMessage(String str);
    void showName(String str);

}