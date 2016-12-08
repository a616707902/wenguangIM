package com.wenguang.chat.mvp.presenter;

import android.content.Context;

import com.wenguang.chat.bean.PhoneLocal;
import com.wenguang.chat.event.CallBackBmob;
import com.wenguang.chat.mvp.model.EMCallPhoneModel;
import com.wenguang.chat.mvp.model.EMCallPhoneModelImpl;
import com.wenguang.chat.mvp.view.EMCallPhoneView;

/**
 * 作者：陈攀
 * 邮件：616707902@qq.com
 * 创建时间：2016/12/7.
 * 描述:
 */
public class EMCallPhonePresenter extends BasePresenter<EMCallPhoneView> {
    EMCallPhoneModel mEMCallPhoneModel = new EMCallPhoneModelImpl();

    public void getLocale(Context context, String phonenum) {
        mEMCallPhoneModel.volleyPost(context, phonenum, new CallBackBmob<PhoneLocal>() {
            @Override
            public void succssCallBack(PhoneLocal jsonArray) {

            }

            @Override
            public void failed(String e) {

            }
        });
    }
}
