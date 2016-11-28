package com.wenguang.chat.mvp.presenter;

import android.content.Context;

import com.wenguang.chat.event.CallBack;
import com.wenguang.chat.mvp.model.ContactFragmentModel;
import com.wenguang.chat.mvp.model.ContactFragmentModelImpl;
import com.wenguang.chat.mvp.view.ContactFragmentView;
import com.wenguang.chat.utils.common.SortModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 作者：chenpan
 * 时间：2016/11/16 15:55
 * 邮箱：616707902@qq.com
 * 描述：
 */

public class ContactFragmentPresenter extends BasePresenter<ContactFragmentView>{

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
}
