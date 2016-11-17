package com.wenguang.chat.mvp.view;

/**
* Created by MVPHelper on 2016/11/16
*/

public interface HomeFragmentView extends BaseView{
    /**
     * 设置未读短信
     * @param count
     */
    void  setSMSCount(int count);

    /**
     * 设置未接来电
     * @param count
     */
    void setMissedCalls(int count);
}