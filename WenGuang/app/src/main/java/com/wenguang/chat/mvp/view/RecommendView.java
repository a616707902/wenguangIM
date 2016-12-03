package com.wenguang.chat.mvp.view;

import com.wenguang.chat.utils.common.SortModel;

import java.util.List;

/**
* Created by MVPHelper on 2016/12/03
*/

public interface RecommendView extends BaseView{
    void  setAdapter(List<SortModel> models);

    void setList(List<SortModel> models);


}