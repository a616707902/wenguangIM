package com.wenguang.chat.mvp.view;

/**
* Created by MVPHelper on 2016/11/15
*/

public interface LoginView extends BaseView{
    /**
     * 输入的内容错误
     */
    void  editError();

    /**
     * 还未输入
     */
    void  editEmpty();

    /**
     * 设置获取验证码按钮状态
     * @param isClick
     * @param text
     */
    void  verifyBtnText(boolean isClick,String text);

    /**
     * 不是手机号码
     */
    void isMobNumber();

    /**
     * 跳转界面
     */
    void  goHomeActivity();
    /**
     *
     */
    void showError(String msg);
}