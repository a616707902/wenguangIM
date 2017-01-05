package com.wenguang.chat.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wenguang.chat.R;
import com.wenguang.chat.bean.MessageInfo;

import java.util.ArrayList;

/**
 * 作者：chenpan
 * 时间：2016/12/26 14:04
 * 邮箱：616707902@qq.com
 * 描述：
 */

public class ConversationAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<MessageInfo> mDatas;
    public ConversationAdapter(Context mContext,
                                 ArrayList<MessageInfo> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MessageInfo threadDetailBean = mDatas.get(position);
        int layoutId = threadDetailBean.getLayoutId();
        if (layoutId==R.layout.receive_item) {
            //type 1 对方
            convertView = View.inflate(mContext, R.layout.receive_item, null);
        }else if (layoutId==R.layout.send_item) {
            //type 2 自己
            convertView = View.inflate(mContext, R.layout.send_item, null);
        }
      //  TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView  content = (TextView) convertView.findViewById(R.id.tv_chatcontent);

       // name.setText(threadDetailBean.getPhone()+":"+threadDetailBean.getDate());
        content.setText(threadDetailBean.getContent());


        return convertView;

    }

//    /**
//     * 这里如果填写2的话 ，滑动屏幕就会崩溃？？为什么呢？？？ 我也不知道，
//     * 查查资料？？
//     */
//    @Override
//    public int getViewTypeCount() {
//        // TODO Auto-generated method stub
//        return mDatas.size();
//    }
}
