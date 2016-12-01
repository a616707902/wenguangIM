package com.wenguang.chat.common;

import android.app.Application;

import cn.bmob.v3.Bmob;
import cn.smssdk.SMSSDK;

/**
 * 作者：chenpan
 * 时间：2016/11/14 20:15
 * 邮箱：616707902@qq.com
 * 描述：
 */

public class MyApplication extends Application {


    private static MyApplication mMyApplication;


    @Override
    public void onCreate() {

        super.onCreate();
        mMyApplication = this;
        SMSSDK.initSDK(this, "1913670399fc0", "b315b8802256aa230d1f855624ef15ff");
        Bmob.initialize(this, "4bdac565f3790ee4edac17b048261edc");
        //  initFrescoConfig();
    }

    public static MyApplication getApplication() {
        return mMyApplication;
    }


}
