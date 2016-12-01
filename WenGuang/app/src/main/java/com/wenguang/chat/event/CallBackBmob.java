package com.wenguang.chat.event;

/**
 * 作者：chenpan
 * 时间：2016/11/30 11:12
 * 邮箱：616707902@qq.com
 * 描述：
 */

public interface CallBackBmob<T> {

    void succssCallBack(T jsonArray);
    void failed(String e);
}
