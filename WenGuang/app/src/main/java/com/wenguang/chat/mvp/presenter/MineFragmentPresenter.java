package com.wenguang.chat.mvp.presenter;

import android.content.Context;

import com.wenguang.chat.bean.User;
import com.wenguang.chat.event.CallBackBmob;
import com.wenguang.chat.mvp.model.MineFragmentModel;
import com.wenguang.chat.mvp.model.MineFragmentModelImpl;
import com.wenguang.chat.mvp.view.MineFragmentView;

/**
 * 作者：chenpan
 * 时间：2016/11/16 15:56
 * 邮箱：616707902@qq.com
 * 描述：
 */

public class MineFragmentPresenter extends BasePresenter<MineFragmentView> {
    MineFragmentModel mineFragmentModel = new MineFragmentModelImpl();

    /**
     * 根据账号查询用户信息
     *
     * @param context
     * @param account
     */
    public void getUserMessage(Context context, String account) {
        mineFragmentModel.getUserByAccount(account, new CallBackBmob<User>() {
            @Override
            public void succssCallBack(User jsonArray) {
                if (null != mView) {
                    mView.setIdCard(jsonArray.getIdcard());
                }
            }

            @Override
            public void failed(String e) {
                if (null != mView) {
                    mView.showMessage("数据加载失败");
                }
            }
        });
    }

    public void upDataUserMessage(Context context, String name, String sign, String idcard, final String picpath) {
        mineFragmentModel.upDataUserMessageByAccount( name, sign, idcard,picpath, new CallBackBmob<String>() {
            @Override
            public void succssCallBack(String jsonArray) {

                if (null != mView) {
                    mView.showMessage(jsonArray);
                }
            }

            @Override
            public void failed(String e) {
                if (null != mView) {
                    mView.showMessage("操作失败");
                }
            }
        });
    }
}
