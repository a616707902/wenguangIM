package com.wenguang.chat.mvp.model;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;

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
}