package com.wenguang.chat.mvp.presenter;

import android.content.Context;

import com.wenguang.chat.event.CallBack;
import com.wenguang.chat.mvp.model.ContactFragmentModel;
import com.wenguang.chat.mvp.model.ContactFragmentModelImpl;
import com.wenguang.chat.mvp.view.MineFragmentView;
import com.wenguang.chat.mvp.view.RecommendView;
import com.wenguang.chat.utils.common.SortModel;

import java.util.List;

/**
 * 作者：陈攀
 * 邮件：616707902@qq.com
 * 创建时间：2016/12/3.
 * 描述:
 */
public class RecommendPresenter extends BasePresenter<RecommendView>{

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
