package com.wenguang.chat.mvp.model;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.provider.Telephony;
import android.support.v4.content.ContextCompat;

/**
* Created by MVPHelper on 2016/11/16
*/

public class HomeFragmentModelImpl {


    private ContentObserver newMmsContentObserver ;
    private  Context mContext;
    public HomeFragmentModelImpl(Context context,ContentObserver newMmsContentObserver) {
        this.newMmsContentObserver = newMmsContentObserver;
        mContext=context;
    }

    /**
     * 注册
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public  void registerObserver() {
        unregisterObserver();
        mContext.getContentResolver().registerContentObserver(Uri.parse("content://sms"), true,
                newMmsContentObserver);
        mContext.getContentResolver().registerContentObserver(Telephony.MmsSms.CONTENT_URI, true,
                newMmsContentObserver);
    }
    public synchronized void unregisterObserver() {
        try {
            if (newMmsContentObserver != null) {
                mContext.getContentResolver().unregisterContentObserver(newMmsContentObserver);
            }
            if (newMmsContentObserver != null) {
                mContext.getContentResolver().unregisterContentObserver(newMmsContentObserver);
            }
        } catch (Exception e) {
        }
    }

    /**获取未读信数量
     *
     * @return
     */
    public int getNewSmsCount() {
        int result = 0;
        Cursor csr = mContext.getContentResolver().query(Uri.parse("content://sms"), null,
                "type = 1 and read = 0", null, null);
        if (csr != null) {
            result = csr.getCount();
            csr.close();
        }
        return result;
    }
    /**
     *  获取未读彩信数量：
      */

    public int getNewMmsCount() {
        int result = 0;
        Cursor csr = mContext.getContentResolver().query(Uri.parse("content://mms/inbox"),
                null, "read = 0", null, null);
        if (csr != null) {
            result = csr.getCount();
            csr.close();
        }
        return result;
    }

    /**
     * 获取未接来电
     * @return
     */
    public  int readMissCall() {
        int result = 0;

//        Cursor cursor = mContext.getContentResolver().query(CallLog.Calls.CONTENT_URI, new String[] {
//                CallLog.Calls.TYPE
//        }, " type=? and new=?", new String[] {
//                CallLog.Calls.MISSED_TYPE + "", "1"
//        }, "date desc");
        Cursor cursor=null;
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {

        }else{
             cursor = mContext.getContentResolver().query(CallLog.Calls.CONTENT_URI,
            new String[]{CallLog.Calls.TYPE}, " type=?",
                    new String[]{CallLog.Calls.MISSED_TYPE + ""}, "date desc");
        }

            if (cursor != null) {
                result = cursor.getCount();
                cursor.close();
            }
        return result;
    }
}