package com.wenguang.chat.common;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.wenguang.chat.R;
import com.wenguang.chat.event.CrashHandler;
import com.wenguang.chat.service.CallReceiver;
import com.wenguang.chat.utils.LocalImageHelper;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.Bmob;

/**
 * 作者：chenpan
 * 时间：2016/11/14 20:15
 * 邮箱：616707902@qq.com
 * 描述：
 */

public class MyApplication extends Application {

    public static final String IMAGE_LOADER_CACHE_PATH = "/wenguang/Images/";
    private static MyApplication mMyApplication;
    public static RequestQueue queues;
    private CallReceiver callReceiver;
    private Display display;
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    @Override
    public void onCreate() {

        super.onCreate();
        mMyApplication = this;
        queues = Volley.newRequestQueue(getApplicationContext());
      //  SMSSDK.initSDK(this, "1913670399fc0", "b315b8802256aa230d1f855624ef15ff");
        Bmob.initialize(this, "4bdac565f3790ee4edac17b048261edc");
        UserManager.getInstance().init(this);
        initEMSDK();
        registerCall();
        init();
        //  initFrescoConfig();
    }

    /**
     * 初始化
     */
    private void init() {
        initImageLoader(getApplicationContext());
        //本地图片辅助类初始化
        LocalImageHelper.init(this);
        if (display == null) {
            WindowManager windowManager = (WindowManager)
                    getSystemService(Context.WINDOW_SERVICE);
            display = windowManager.getDefaultDisplay();
        }
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(CrashHandler.getInstance(
                this, mDefaultHandler));
    }

    public  void initImageLoader(Context context) {
        File cacheDir = null;
        if (!TextUtils.isEmpty(IMAGE_LOADER_CACHE_PATH)) {
            cacheDir = StorageUtils.getOwnCacheDirectory(context, IMAGE_LOADER_CACHE_PATH);
        } else {
            cacheDir = StorageUtils.getCacheDirectory(context);
        }
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY);
        config.denyCacheImageMultipleSizesInMemory();
        config.memoryCacheSize((int) Runtime.getRuntime().maxMemory() / 4);
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(100 * 1024 * 1024); // 100 MiB
        config.diskCacheExtraOptions(720, 1280, null);
        config.diskCache(new UnlimitedDiskCache(cacheDir));
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        //修改连接超时时间5秒，下载超时时间5秒
        config.imageDownloader(new BaseImageDownloader(mMyApplication, 5 * 1000, 5 * 1000));
        //		config.writeDebugLogs(); // Remove for release app
        // Initialize ImageLoader with configuration.
        config.memoryCacheSizePercentage(14);
        config.memoryCacheExtraOptions(720, 1280);
        config.memoryCache(new WeakMemoryCache());

        config.threadPoolSize(3);
        config.threadPriority(Thread.NORM_PRIORITY - 2);

        config.defaultDisplayImageOptions(getDisplayOptions());
        ImageLoader.getInstance().init(config.build());
    }
    public DisplayImageOptions getDisplayOptions() {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.color.default_image_background)
                .showImageForEmptyUri(R.color.default_image_background)
                .showImageOnFail(R.color.default_image_background)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }
    public String getCachePath() {
        File cacheDir;
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir = getExternalCacheDir();
        else
            cacheDir = getCacheDir();
        if (!cacheDir.exists())
            cacheDir.mkdirs();
        return cacheDir.getAbsolutePath();
    }

    private void registerCall() {
        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
        if (callReceiver == null) {
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

    /**
     * @return
     * @Description： 获取当前屏幕1/4宽度
     */
    public int getQuarterWidth() {
        return display.getWidth() / 4;
    }
}
