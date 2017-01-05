package com.wenguang.chat.mvp.model;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.wenguang.chat.bean.User;
import com.wenguang.chat.event.CallBackBmob;

import org.json.JSONArray;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by MVPHelper on 2016/11/28
 */

public class CallPhoneModelImpl implements CallPhoneModel {

    @Override
    public void callPhone(Context callPhoneActivity, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        if (ContextCompat.checkSelfPermission(callPhoneActivity,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

        } else {
            callPhoneActivity.startActivity(intent);
        }

    }

    /**
     * 查询数据
     */
    public void queryData(String account, final CallBackBmob callBackBmob) {
        BmobQuery query = new BmobQuery("wenguangUser");
        query.addWhereEqualTo("account", account);
        //v3.5.0版本提供`findObjectsByTable`方法查询自定义表名的数据
        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray jsonArray, BmobException e) {
                if (e == null) {
                    Log.i("bmob", "查询成功：" + jsonArray.toString());
                    System.out.print(jsonArray);
                    if (jsonArray != null && jsonArray.length() > 0) {

                        Gson gson = new Gson();
                        User[] users = gson.fromJson(jsonArray.toString(), User[].class);
                        if (users != null) {
                            callBackBmob.succssCallBack(true);
                          //  UserManager.getInstance().saveUser(users[0]);
                        } else {
                            callBackBmob.succssCallBack(false);
                        }
                    } else {
                        callBackBmob.succssCallBack(false);
                        //  callBackBmob.failed(e.getMessage() );
                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                    callBackBmob.succssCallBack(false);
                }
            }
        });
    }
}