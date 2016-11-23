package com.wenguang.chat.mvp.view;

import com.wenguang.chat.utils.common.SortModel;

import java.util.List;

/**
* Created by MVPHelper on 2016/11/16
*/

public interface ContactFragmentView extends BaseView{
   void  setAdapter(List<SortModel> models);
}