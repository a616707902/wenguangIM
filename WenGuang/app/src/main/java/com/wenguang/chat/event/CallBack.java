package com.wenguang.chat.event;

import com.wenguang.chat.utils.common.SortModel;

import java.util.List;

/**
 * 作者：chenpan
 * 时间：2016/11/23 12:42
 * 邮箱：616707902@qq.com
 * 描述：
 */

public interface CallBack {
    void getContactlist(List<SortModel> models);
}
