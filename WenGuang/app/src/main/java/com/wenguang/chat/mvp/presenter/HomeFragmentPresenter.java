package com.wenguang.chat.mvp.presenter;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;

import com.wenguang.chat.bean.RecordEntity;
import com.wenguang.chat.mvp.model.HomeFragmentModelImpl;
import com.wenguang.chat.mvp.view.HomeFragmentView;

import java.util.List;

/**
 * 作者：chenpan
 * 时间：2016/11/16 15:54
 * 邮箱：616707902@qq.com
 * 描述：
 */

public class HomeFragmentPresenter extends BasePresenter<HomeFragmentView> {
    private Context mContext;
    private HomeFragmentModelImpl mHomeModel;

    public HomeFragmentPresenter(Context context) {
        mContext = context;
        mHomeModel = new HomeFragmentModelImpl(mContext, new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                HomeFragmentPresenter.this.getSMSCount();
            }
        });
    }

    /**
     * 注册获取短信
     */
    public void rigsterContentObserver() {
        mHomeModel.registerObserver();
    }

    /**
     * 获取未读短信数量
     */

    public void getSMSCount() {
        int count = mHomeModel.getNewSmsCount() + mHomeModel.getNewMmsCount();
        if (null != mView) {
            mView.setSMSCount(count);
        }
    }

    /**
     * 获取未接来电数量
     */
    public void getMissCallCount() {
        int count = mHomeModel.readMissCall();
        if (null != mView) {
            mView.setMissedCalls(count);
        }
    }
    /**
     * 获取通话记录
     */
    public void getCallRecords() {
        List<RecordEntity> recordEntities= mHomeModel.getRecord();
        if (null != mView) {
            mView.setCallRecordList(recordEntities);
        }
    }

}
