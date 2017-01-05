package com.wenguang.chat.mvp.presenter;

import android.content.Context;

import com.wenguang.chat.R;
import com.wenguang.chat.bean.PhoneLocal;
import com.wenguang.chat.event.CallBackBmob;
import com.wenguang.chat.mvp.model.CallPhoneModel;
import com.wenguang.chat.mvp.model.CallPhoneModelImpl;
import com.wenguang.chat.mvp.model.EMCallPhoneModel;
import com.wenguang.chat.mvp.model.EMCallPhoneModelImpl;
import com.wenguang.chat.mvp.view.EMCallPhoneView;
import com.wenguang.chat.utils.MobileUtils;

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
                if (null != mView) {
                    mView.setLocal(jsonArray.getResult().getProvince() + "·"+jsonArray.getResult().getCity());
                }
            }

            @Override
            public void failed(String e) {
                if (null != mView) {
                    mView.setLocal(e);
                }
            }
        });
    }
    /**
     * 拨打电话
     * @param callPhoneActivity
     * @param phoneNum
     */
    CallPhoneModel callPhone=new CallPhoneModelImpl();
    public void callPhone(Context callPhoneActivity, String phoneNum) {
        boolean isphone= MobileUtils.isMobileNo(phoneNum);
        if (isphone){
            callPhone.callPhone(callPhoneActivity,phoneNum);
        }else {
            if (null!=mView)
            {
                mView.showMessage(callPhoneActivity.getResources().getString(R.string.notmobnumber));
            }
        }
    }

    public void searchName(Context callPhoneActivity, String phoneNum) {
        boolean isphone= MobileUtils.isMobileNo(phoneNum);
        if (isphone){
            mEMCallPhoneModel.getContactName(callPhoneActivity, phoneNum, new CallBackBmob<String>() {
                @Override
                public void succssCallBack(String jsonArray) {
                    if (null!=mView)
                    {
                        mView.showName(jsonArray);
                    }
                }

                @Override
                public void failed(String e) {

                }
            });
        }else {
            if (null!=mView)
            {
                mView.showMessage(callPhoneActivity.getResources().getString(R.string.notmobnumber));
            }
        }
    }
}
