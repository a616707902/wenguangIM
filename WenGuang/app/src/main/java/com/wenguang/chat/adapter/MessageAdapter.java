package com.wenguang.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenguang.chat.R;
import com.wenguang.chat.bean.MessageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：chenpan
 * 时间：2016/12/23 15:06
 * 邮箱：616707902@qq.com
 * 描述：
 */

public class MessageAdapter extends BaseAdapter {

    private List<MessageInfo> mList;
    private Context mContext;
    public MessageAdapter(Context mContext, List<MessageInfo> list) {
        this.mContext = mContext;
        if (list == null) {
            this.mList = new ArrayList<MessageInfo>();
        } else {
            this.mList = list;
        }
    }
    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * @param list
     */
    public void updateListView(List<MessageInfo> list) {
        if (list == null) {
            this.mList = new ArrayList<MessageInfo>();
        } else {
            this.mList = list;
        }
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.mList.size();
    }

    public Object getItem(int position) {
        return mList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.message_item, null);
            viewHolder.mImageView = (ImageView) view.findViewById(R.id.msg_photo);
            viewHolder.tvName = (TextView) view.findViewById(R.id.msg_name);
            viewHolder.tvContent = (TextView) view.findViewById(R.id.msg_content);
            viewHolder.tvDate = (TextView) view.findViewById(R.id.msg_date);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }


        viewHolder.tvName.setText(this.mList.get(position).getName());
        viewHolder.tvContent.setText(this.mList.get(position).getSmsContent());
        viewHolder.mImageView.setImageBitmap(this.mList.get(position).getContactPhoto());
        viewHolder.tvDate.setText(this.mList.get(position).getSmsDate());
        return view;

    }

    public static class ViewHolder {
        public ImageView mImageView;
        public TextView tvName;
        public TextView tvContent;
        public TextView tvDate;
    }
}
