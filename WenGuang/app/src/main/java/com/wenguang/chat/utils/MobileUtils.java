package com.wenguang.chat.utils;

import android.text.TextUtils;

/**
 * 作者：chenpan
 * 时间：2016/10/18 17:01
 * 邮箱：616707902@qq.com
 * 描述：手机帮助类
 */

public class MobileUtils {
    /**
     * 判断字符串是不是手机号码的正则表达式
     * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
     * 联通：130、131、132、152、1 55、156、185、186 电信：133、153、180、189、（1349卫通）
     * 总结起来就是第一位必定为1，第二位必定为3或5或7或8或4，其他位置的可以为0-9
     */
    public static boolean isMobileNo(String mobiles) {
        String telRegex = "[1][34578]\\d{9}";
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        } else {
            return mobiles.matches(telRegex);
        }
    }
}