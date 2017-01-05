package com.wenguang.chat.fragment;


import android.Manifest;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.wenguang.chat.R;
import com.wenguang.chat.adapter.MessageCursorAdapter;
import com.wenguang.chat.base.BaseFragment;
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
    //MessageAdapter adapter;

    private CursorAdapter mAdapter;
    private MyAsyncQueryHandler asyncQueryHandler;
    private String[] projection = new String[] { "sms.thread_id AS _id",
            "sms.body AS snippet", "groups.msg_count AS msg_count",
            "sms.address AS address", "sms.date AS date" };


    @Override
    protected int getlayoutId() {
        return R.layout.message_fragment;
    }

    @Override
    protected void initInjector() {
//        mInfoList = new ArrayList<MessageInfo>();
//        adapter = new MessageAdapter(mActivity, mInfoList);
//        mListView.setAdapter(adapter);
        mAdapter = new MessageCursorAdapter(mActivity, null);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        MPermissions.requestPermissions(this, Common.REQUECT_CODE_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS);
    }

    @Override
    protected void initEventAndData() {
//        <uses-permission android:name="android.permission.RECEIVE_SMS" >
//        </uses-permission>
//        <uses-permission android:name="android.permission.READ_SMS" >
//        </uses-permission>
       // MPermissions.requestPermissions(this, Common.REQUECT_CODE_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS);

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
        asyncQueryHandler = new MyAsyncQueryHandler(mActivity.getContentResolver());
        asyncQueryHandler.startQuery(0, null,
                Uri.parse("content://sms/conversations/"), projection, null,
                null, " date desc");
        //  ((CallPhonePresenter) mPresenter).getContactList(this);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                GetMessageInfo getMessageInfo = new GetMessageInfo(mActivity);
//                mInfoList = getMessageInfo.getSmsInfos();
//                mHandler.sendEmptyMessage(1);
//            }
//        }).start();
        // adapter.updateListView(getMessageInfo.getSmsInfos());

    }

    @PermissionDenied(Common.REQUECT_CODE_SMS)
    public void requestSMSFailed() {
    }
//
//    Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            adapter=new MessageAdapter(mActivity,mInfoList);
//            mListView.setAdapter(adapter);
//        }
//    };

    class MyAsyncQueryHandler extends AsyncQueryHandler {

        public MyAsyncQueryHandler(ContentResolver cr) {
            super(cr);
            // TODO Auto-generated constructor stub
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            super.onQueryComplete(token, cookie, cursor);
            mAdapter.changeCursor(cursor);
        }
    }
}
