package com.wenguang.chat.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.wenguang.chat.R;

import java.util.List;

public class AppStatusService extends Service {
    private static final String TAG = "AppStatusService";
    private ActivityManager activityManager;
    private String packageName;
    private boolean isStop = false;
   
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
   
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        packageName = this.getPackageName();
        System.out.println("启动服务");

        new Thread() {
            public void run() {
                try {
                    while (!isStop) {
                        Thread.sleep(1000);
                        
                        if (isAppOnForeground()) {
                            hideNotification();
                            Log.v(TAG, "前台运行");
                        } else {
                            Log.v(TAG, "后台运行");
                            showNotification();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        
        return START_STICKY;
    }
   
   
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the device
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) return false;
        
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        
        return false;
    }
   
    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("终止服务");
        isStop = true;
    }

    public static void start(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, AppStatusService.class);
        context.startService(intent);
    }
    // 隐藏Notification
    public void hideNotification(){
        // 启动后删除之前我们定义的通知
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }

    // 显示Notification
    public void showNotification() {
        // 创建一个NotificationManager的引用
        NotificationManager notificationManager = (
                NotificationManager)getSystemService(
                        android.content.Context.NOTIFICATION_SERVICE);

        android.support.v4.app.NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this)
             /*设置small icon*/
                .setSmallIcon(R.mipmap.icon_appicon)
            /*设置title  大标题*/
                .setContentTitle("文广通讯")
            /*设置详细文本  小标题*/
                .setContentText("用心为你的每一分钟")
                .setWhen(System.currentTimeMillis())
                .setOngoing(true);

//        // 定义Notification的各种属性
//        Notification notification = new Notification(
//                R.mipmap.icon_appicon,"文广通讯",
//                System.currentTimeMillis());
//        // 将此通知放到通知栏的"Ongoing"即"正在运行"组中
//        notification.flags |= Notification.FLAG_ONGOING_EVENT;
//        // 点击后自动清除Notification
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
//        notification.defaults = Notification.DEFAULT_LIGHTS;
//        notification.ledARGB = Color.BLUE;
//        notification.ledOnMS = 5000;
//
//        // 设置通知的事件消息
//        CharSequence contentTitle = "阅读器显示信息"; // 通知栏标题
//        CharSequence contentText = "推送信息显示，请查看……"; // 通知栏内容
//
//        Intent notificationIntent = new Intent(AppManager.context,AppManager.context.getClass());
//        notificationIntent.setAction(Intent.ACTION_MAIN);
//        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//        PendingIntent contentIntent = PendingIntent.getActivity(
//                AppManager.context, 0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
//        notification.setLatestEventInfo(
//                AppManager.context, contentTitle, contentText, contentIntent);
        // 把Notification传递给NotificationManager
        Notification notification = notifyBuilder.build();
        startForeground(1, notification); // 设置为前台服务避免kill，Android4.3及以上需要设置id为0时通知栏才不显示该通知；
       // notificationManager.notify(0, notification);
    }
}