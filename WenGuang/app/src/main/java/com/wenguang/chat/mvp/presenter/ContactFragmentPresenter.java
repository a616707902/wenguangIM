package com.wenguang.chat.mvp.presenter;

import android.content.Context;

import com.wenguang.chat.event.CallBack;
import com.wenguang.chat.event.CallBackBmob;
import com.wenguang.chat.mvp.model.CallPhoneModel;
import com.wenguang.chat.mvp.model.CallPhoneModelImpl;
import com.wenguang.chat.mvp.model.ContactFragmentModel;
import com.wenguang.chat.mvp.model.ContactFragmentModelImpl;
import com.wenguang.chat.mvp.view.ContactFragmentView;
import com.wenguang.chat.utils.common.SortModel;

import java.util.List;

/**
 * 作者：chenpan
 * 时间：2016/11/16 15:55
 * 邮箱：616707902@qq.com
 * 描述：
 */

public class ContactFragmentPresenter extends BasePresenter<ContactFragmentView>{
    CallPhoneModel callPhone=new CallPhoneModelImpl();
    ContactFragmentModel mContactFragmentModel=new ContactFragmentModelImpl();
    public void  getContactList(Context context){
        mContactFragmentModel.loadContacts(context, new CallBack() {
            @Override
            public void getContactlist(List<SortModel> models) {
                if (null != mView&&models!=null) {
                    mView.setAdapter(models);
                    mView.setList(models);
                }
            }
        });
    }
    public void serchContact(Context context,String content,List<SortModel> models ){
        mContactFragmentModel.search(content, models, new CallBack() {
            @Override
            public void getContactlist(List<SortModel> models) {
                if (null != mView&&models!=null) {
                    mView.setAdapter(models);
                }
            }
        });

    }

    /**
     * 拨打电话
     * @param callPhoneActivity
     * @param phoneNum
     */
    public void callPhone(Context callPhoneActivity, String phoneNum) {
        //  boolean isphone=MobileUtils.isMobileNo(phoneNum);
        //  if (isphone){
        callPhone.callPhone(callPhoneActivity,phoneNum);
//        }else {
//            if (null!=mView)
//            {
//                mView.showMessage(callPhoneActivity.getResources().getString(R.string.notmobnumber));
//            }
//        }
    }

    public void queryAccount(final Context context, final String callPhoneNum) {
        mContactFragmentModel.queryData(callPhoneNum, new CallBackBmob<Boolean>() {
            @Override
            public void succssCallBack(Boolean jsonArray) {
                if (jsonArray) {
                    if (null != mView) {
                        mView.showDialog(callPhoneNum, null);
                    }
                } else {
                    if (null != mView) {
                        mView.showDialog(callPhoneNum, "该号码不支持拨打免费电话");
                    }
                }
            }

            @Override
            public void failed(String e) {
                if (null != mView) {
                    mView.showDialog(callPhoneNum, "该号码不支持拨打免费电话");
                }
            }
        });
    }
}
