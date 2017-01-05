package com.wenguang.chat.mvp.model;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.wenguang.chat.bean.PhoneLocal;
import com.wenguang.chat.event.CallBackBmob;
import com.wenguang.chat.network.VolleyInterface;
import com.wenguang.chat.network.VolleyRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MVPHelper on 2016/12/07
 */

public class EMCallPhoneModelImpl implements EMCallPhoneModel {
    String url = "http://apis.juhe.cn/mobile/get";
    String juheAPIKEY = "e59b086b3ef990049b3056d6a5d5e342";

    @Override
    public void volleyPost(Context context, String phonenum, final CallBackBmob<PhoneLocal> callBackBmob) {


        VolleyInterface volleyInterface = new VolleyInterface(context) {
            @Override
            public void onMySuccess(String result) {
                Gson gson = new Gson();
                PhoneLocal phoneLocal = gson.fromJson(result, PhoneLocal.class);
                if ("200".equals(phoneLocal.getResultcode())) {
                    callBackBmob.succssCallBack(phoneLocal);
                } else {
                    callBackBmob.failed("未知");

                }

            }

            @Override
            public void onMyError(VolleyError result) {
                callBackBmob.failed("未知");
            }
        };
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phonenum);
        map.put("key", juheAPIKEY);
        VolleyRequest.RequestPostString(context, url, "get", map, volleyInterface);
    }

    public void getContactName(Context context, String number, final CallBackBmob<String> callBackBmob) {
        if (TextUtils.isEmpty(number)) {
            callBackBmob.succssCallBack("");
        }

        final ContentResolver resolver = context.getContentResolver();

        Uri lookupUri = null;
        String[] projection = new String[]{ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.DISPLAY_NAME};
        Cursor cursor = null;
        try {
            lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
            cursor = resolver.query(lookupUri, projection, null, null, null);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                lookupUri = Uri.withAppendedPath(android.provider.Contacts.Phones.CONTENT_FILTER_URL,
                        Uri.encode(number));
                cursor = resolver.query(lookupUri, projection, null, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String ret = null;
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            ret = cursor.getString(1);
        }

        cursor.close();
        callBackBmob.succssCallBack(ret);
    }

}