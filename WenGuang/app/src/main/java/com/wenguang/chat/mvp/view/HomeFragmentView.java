package com.wenguang.chat.mvp.view;

import com.wenguang.chat.bean.RecordEntity;

import java.util.List;

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

    /**
     * 将获取到的通话记录设置到界面上
     * @param callRecordList
     */
   void setCallRecordList(List<RecordEntity> callRecordList);
}