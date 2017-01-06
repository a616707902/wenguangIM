package com.wenguang.chat.event;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import com.wenguang.chat.common.Common;
import com.wenguang.chat.common.MyApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private final String TAG = "CrashHandler";
    private Context mContext;
    private static CrashHandler mHandler;
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private CrashHandler(Context context,
                         Thread.UncaughtExceptionHandler defaultHandler) {
        mContext = context;
        mDefaultHandler = defaultHandler;
    }

    public static CrashHandler getInstance(Context context,
                                           Thread.UncaughtExceptionHandler defaultHandler) {
        if (mHandler == null)
            mHandler = new CrashHandler(context, defaultHandler);

        return mHandler;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (ex != null) {
          //  FileUtil.writeToFile(ex.getMessage(), Common.PHONE_PATH + "/Error/" + getCurrentDate() + ".txt");
            saveCrashInfo(collectDeviceInfo(mContext), ex);
        }
        restartApplication();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
        System.exit(0);
        mDefaultHandler.uncaughtException(thread, ex);

    }

    /**
     * @param
     * @return
     * @throws NullPointerException
     * @功能描述：传说中的重启自身
     */
    private void restartApplication() {
        final Intent intent = MyApplication.getApplication().getPackageManager().getLaunchIntentForPackage(MyApplication.getApplication().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.getApplication().startActivity(intent);
    }

    private static void saveCrashInfo(Map<String, String> deviceInfos, Throwable ex) {

        StringBuffer crashLog = new StringBuffer();

        for (Map.Entry<String, String> entry : deviceInfos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            crashLog.append(key + "=" + value + "\n");
        }

        Writer writer = null;
        PrintWriter printWriter = null;
        try {

            writer = new StringWriter();
            printWriter = new PrintWriter(writer);

            ex.printStackTrace(printWriter);

            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            crashLog.append(writer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer == null) {
                try {
                    writer.close();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (printWriter == null) {
                try {
                    printWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // 鎶婇敊璇俊鎭啓鍏ユ枃浠?
        FileOutputStream fos = null;
        try {

            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {

                // 鐢熸垚鏍圭洰褰?
                File saveFolder = new File(
                        Common.SAVEFOLDER);
                saveFolder.mkdirs();
                // 鍐欏叆鏂囦欢
                String fileName = "CrashError"+getCurrentDate() +".txt";
                fos = new FileOutputStream(new File(saveFolder, fileName));
                fos.write(crashLog.toString().getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                    fos = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Map<String, String> collectDeviceInfo(Context ctx) {

        Map<String, String> infos = new HashMap<String, String>();

        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null"
                        : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                //Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return infos;
    }

    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return sdf.format(new Date(System.currentTimeMillis()));
    }
}