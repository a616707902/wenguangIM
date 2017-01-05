package com.wenguang.chat.activity;

import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.wenguang.chat.R;
import com.wenguang.chat.adapter.ConversationAdapter;
import com.wenguang.chat.base.BaseActivity;
import com.wenguang.chat.bean.MessageInfo;
import com.wenguang.chat.mvp.presenter.BasePresenter;
import com.wenguang.chat.mvp.presenter.ConversationPresenter;
import com.wenguang.chat.mvp.view.ConversationView;

import java.util.ArrayList;

import butterknife.Bind;

public class MessageConvertionActivity extends BaseActivity implements ConversationView {

    @Bind(R.id.mconverListView)
    ListView mMconverListView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.title)
    TextView mTitle;
    private String thread_id;
    private String phone;
    private ConversationAdapter conAdapter;
    private ArrayList<MessageInfo> mDatas;
    private String[] projection = new String[]{"_id", "address", "person",
            "body", "type", "date"};

//    private String[] projection = new String[] { "sms.thread_id AS _id", "sms.address AS address",
//            "sms.body AS snippet", "type",
//            "sms.date AS date" };
    private String[] contactProjection = new String[]{ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.DISPLAY_NAME};
    private Uri uri = Uri.parse("content://sms");

    // String[] projection = new String[] {"_id"};

    private mySmsContentObserver observer = new mySmsContentObserver(new Handler());
    class mySmsContentObserver extends ContentObserver {

        public mySmsContentObserver(Handler handler) {
            super(handler);
            // TODO Auto-generated constructor stub
        }
        @Override
        public void onChange(boolean selfChange) {
            // TODO Auto-generated method stub
            super.onChange(selfChange);
            initEventAndData();
        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_convertion;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }




    @Override
    protected void initInjector() {
        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.drawable.icon_back);

        Intent intent = getIntent();
        if (intent != null) {
            thread_id = intent.getStringExtra("thread_id");
            phone = intent.getStringExtra("phone");
        }
        mTitle.setText(phone);
        setSupportActionBar(mToolbar);
        getContentResolver().registerContentObserver(uri, true, observer);

    }

    @Override
    protected void initEventAndData() {
        //data--数据库中获取!!!!thread
        //select projection[] from sms where thread_id = thread_id order by date desc
        Cursor threadDetailCursor = getContentResolver().query(uri, projection, "thread_id="+thread_id, null, "date desc");
      //  Cursor threadDetailCursor = getContentResolver().("select _id ,address ,person,body,date,type from sms where thread_id = "+thread_id);
        praseThreadDetailCursor(threadDetailCursor);
        threadDetailCursor.close();
        //适配器
        conAdapter = new ConversationAdapter(this, mDatas);
        //设置适配器
        mMconverListView.setAdapter(conAdapter);

    }

    private void praseThreadDetailCursor(Cursor threadDetailCursor) {
        mDatas = new ArrayList<MessageInfo>();
        if (threadDetailCursor != null && threadDetailCursor.getCount() > 0) {
            while (threadDetailCursor.moveToNext()) {
                String address = threadDetailCursor.getString(threadDetailCursor.getColumnIndex("address"));
                String body = threadDetailCursor.getString(threadDetailCursor.getColumnIndex("body"));
                String type = threadDetailCursor.getString(threadDetailCursor.getColumnIndex("type"));
                String date = threadDetailCursor.getString(threadDetailCursor.getColumnIndex("date"));

                //type為1是对方的item，为2是自己的itme
                //通过电话获取联系人的姓名
                String contactName = null;
                Uri contactNameUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(address));
                Cursor contactNameCursor = getContentResolver().query(contactNameUri, contactProjection, null, null, null);
                if (contactNameCursor != null && contactNameCursor.getCount() != 0 && contactNameCursor.moveToFirst()) {
                    contactName = contactNameCursor.getString(1);
                } else {
                    contactName = address;
                }
                contactNameCursor.close();
                if (type.equals("1")) {
                    //对方
                    MessageInfo heiBean = null;
                    if (contactName != null) {
                        heiBean = new MessageInfo(contactName, date, body, R.layout.receive_item);
                    } else {
                        heiBean = new MessageInfo(address, date, body, R.layout.receive_item);
                    }
                    mDatas.add(heiBean);
                } else {
                    //自己
                    MessageInfo mBean = new MessageInfo(null, date, body, R.layout.send_item);
                    mDatas.add(mBean);

                }
            }

        }else{
            threadDetailCursor=getContentResolver().query(uri, projection, "address=?", new String[]{phone}, "date desc");
            if (threadDetailCursor != null && threadDetailCursor.getCount() > 0) {
                while (threadDetailCursor.moveToNext()) {
                    String address = threadDetailCursor.getString(threadDetailCursor.getColumnIndex("address"));
                    String body = threadDetailCursor.getString(threadDetailCursor.getColumnIndex("body"));
                    String type = threadDetailCursor.getString(threadDetailCursor.getColumnIndex("type"));
                    String date = threadDetailCursor.getString(threadDetailCursor.getColumnIndex("date"));

                    //type為1是对方的item，为2是自己的itme
                    //通过电话获取联系人的姓名
                    String contactName = null;
                    Uri contactNameUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(address));
                    Cursor contactNameCursor = getContentResolver().query(contactNameUri, contactProjection, null, null, null);
                    if (contactNameCursor != null && contactNameCursor.getCount() != 0 && contactNameCursor.moveToFirst()) {
                        contactName = contactNameCursor.getString(1);
                    } else {
                        contactName = address;
                    }
                    contactNameCursor.close();
                    if (type.equals("1")) {
                        //对方
                        MessageInfo heiBean = null;
                        if (contactName != null) {
                            heiBean = new MessageInfo(contactName, date, body, R.layout.receive_item);
                        } else {
                            heiBean = new MessageInfo(address, date, body, R.layout.receive_item);
                        }
                        mDatas.add(heiBean);
                    } else {
                        //自己
                        MessageInfo mBean = new MessageInfo(null, date, body, R.layout.send_item);
                        mDatas.add(mBean);

                    }
                }

            }
        }
    }

    @Override
    public BasePresenter getPresenter() {
        return new ConversationPresenter();
    }

    @Override
    public void showLoadProgressDialog(String str) {

    }

    @Override
    public void dissDialog() {

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
