package com.wenguang.chat.common;

import android.app.ActivityManager;
import android.app.Application;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.wenguang.chat.service.CallReceiver;

import java.util.Iterator;
import java.util.List;

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
    public static RequestQueue queues;
    private CallReceiver callReceiver;

    @Override
    public void onCreate() {

        super.onCreate();
        mMyApplication = this;
        queues = Volley.newRequestQueue(getApplicationContext());
        SMSSDK.initSDK(this, "1913670399fc0", "b315b8802256aa230d1f855624ef15ff");
        Bmob.initialize(this, "4bdac565f3790ee4edac17b048261edc");
        UserManager.getInstance().init(this);
        initEMSDK();
        registerCall();
        //  initFrescoConfig();
    }

    private void registerCall() {
        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
        if(callReceiver == null){
            callReceiver = new CallReceiver();
        }

        //register incoming call receiver
        registerReceiver(callReceiver, callFilter);
    }

    public static MyApplication getApplication() {
        return mMyApplication;
    }

    public static RequestQueue getQueues() {
        return queues;
    }

    private void initEMSDK() {
        EMOptions options = new EMOptions();
// 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
// 如果APP启用了远程的service，此application:onCreate会被调用2次
// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
// 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null || !processAppName.equalsIgnoreCase(this.getPackageName())) {
            Log.e("em", "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
//初始化
        EMClient.getInstance().init(mMyApplication, options);
//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
}
