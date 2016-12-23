package com.wenguang.chat.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.wenguang.chat.utils.common.SortModel;

import java.util.List;

/**
 * 作者：chenpan
 * 时间：2016/11/15 16:33
 * 邮箱：616707902@qq.com
 * 描述：
 */

public class Common {



    private static LayoutInflater inflater;

    public static View inflate(Context context, int res) {
        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }
        return inflater.inflate(res, null);


    }

    public static String USER_ACCOUNT = "";
    public static final int REQUECT_CODE_MISSCALL = 1;
    public static final int REQUECT_CODE_CONTACT = 2;
    public static final int REQUECT_CALL_PHONE = 3;
    public static final int REQUECT_SEND_MESSAGE = 4;
    public static final int REQUECT_READ_EXTERNAL_STORAGE = 5;
    public static final int REQUECT_CAMERA = 6;
    public static final int REQUECT_CODE_SMS =7 ;
    /**
     * 正在连接
     */
    public static final int CONTECTING = 1;
    /**
     * 连接成功
     */
    public static final int CONTECTED = 2;
    /**
     * 通话中
     */
    public static final int ONTHELINE = 3;
    /**
     * 挂断
     */
    public static final int KILL = 4;
    /**
     * 对方已挂断
     */
    public static final int OTHERKILL = 5;
    /**
     * 已拒绝
     */
    public static final int CONFUSE = 6;
    public static final String LOCAL_FOLDER_NAME="local_folder_name";//跳转到相册页的文件夹名称
    /** 请求相册 */
    public static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0;
    /** 请求相机 */
    public static final int REQUEST_CODE_GETIMAGE_BYCAMERA = 1;
    /** 请求裁剪 */
    public static final int REQUEST_CODE_GETIMAGE_BYCROP = 2;
    public  static List<SortModel> SEND_LIST=null;
    public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
}
