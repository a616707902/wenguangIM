package com.wenguang.chat.mvp.presenter;

import android.content.Context;

import com.wenguang.chat.adapter.ReContactAdapter;
import com.wenguang.chat.common.Common;
import com.wenguang.chat.mvp.model.SendMessageModel;
import com.wenguang.chat.mvp.model.SendMessageModelImpl;
import com.wenguang.chat.mvp.view.RecommendView;
import com.wenguang.chat.mvp.view.SendMessageView;
import com.wenguang.chat.utils.common.SortModel;

/**
 * 作者：陈攀
 * 邮件：616707902@qq.com
 * 创建时间：2016/12/3.
 * 描述:
 */
public class SendMessagePresenter extends BasePresenter<SendMessageView> {
    SendMessageModel sendMessageModel = new SendMessageModelImpl();

    public void sendMessage(Context context, String msg) {
        if (Common.SEND_LIST != null&&Common.SEND_LIST.size()>0) {
            for (SortModel sort : Common.SEND_LIST
                    ) {
                sendMessageModel.sendMessage(sort.getNumber(), msg);
            }
            if (null != mView) {
                mView.showMessage("发送成功");
                mView.back();
            }
        } else {
            if (null != mView) {
                mView.showMessage("未选取联系人");
            }
        }

    }
}
