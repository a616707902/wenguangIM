package com.wenguang.chat.fragment;


import android.Manifest;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.wenguang.chat.R;
import com.wenguang.chat.adapter.MessageAdapter;
import com.wenguang.chat.base.BaseFragment;
import com.wenguang.chat.bean.GetMessageInfo;
import com.wenguang.chat.bean.MessageInfo;
import com.wenguang.chat.common.Common;
import com.wenguang.chat.mvp.presenter.BasePresenter;
import com.wenguang.chat.mvp.presenter.MessageFragmentPresenter;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.util.List;

import butterknife.Bind;

public class MessageFragment extends BaseFragment {


    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.list_view)
    ListView mListView;
    List<MessageInfo> mInfoList;
    MessageAdapter adapter;

    @Override
    protected int getlayoutId() {
        return R.layout.message_fragment;
    }

    @Override
    protected void initInjector() {
//        mInfoList = new ArrayList<MessageInfo>();
//        adapter = new MessageAdapter(mActivity, mInfoList);
//        mListView.setAdapter(adapter);

    }

    @Override
    protected void initEventAndData() {
//        <uses-permission android:name="android.permission.RECEIVE_SMS" >
//        </uses-permission>
//        <uses-permission android:name="android.permission.READ_SMS" >
//        </uses-permission>
        MPermissions.requestPermissions(this, Common.REQUECT_CODE_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS);

    }

    @Override
    protected void lazyLoadData() {

    }

    @Override
    public BasePresenter getPresenter() {
        return new MessageFragmentPresenter();
    }

    @PermissionGrant(Common.REQUECT_CODE_SMS)
    public void requestSMSSuccess() {
        //  ((CallPhonePresenter) mPresenter).getContactList(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                GetMessageInfo getMessageInfo = new GetMessageInfo(mActivity);
                mInfoList = getMessageInfo.getSmsInfos();
                mHandler.sendEmptyMessage(1);
            }
        }).start();
        // adapter.updateListView(getMessageInfo.getSmsInfos());

    }

    @PermissionDenied(Common.REQUECT_CODE_SMS)
    public void requestSMSFailed() {
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter=new MessageAdapter(mActivity,mInfoList);
            mListView.setAdapter(adapter);
        }
    };
}
