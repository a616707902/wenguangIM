package com.wenguang.chat.adapter;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.provider.CallLog;
import android.view.View;
import android.widget.TextView;

import com.wenguang.chat.R;
import com.wenguang.chat.bean.RecordEntity;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;

/**
 * 作者：chenpan
 * 时间：2016/11/18 16:32
 * 邮箱：616707902@qq.com
 * 描述：
 */

public class RecordCallHolder extends BaseHolder<RecordEntity> {
    @Bind(R.id.item_name)
    TextView mItemName;
    @Bind(R.id.item_time)
    TextView mItemTime;
    @Bind(R.id.item_phone)
    TextView mItemPhone;
    public RecordCallHolder(View view) {
        super(view);
    }

    @Override
    public void setData(RecordEntity mData) {
         SimpleDateFormat sdf= new SimpleDateFormat("HH:mm");
         Date callDate=new Date(mData.getlDate());
        String time=sdf.format(callDate);
        String stringcallDate=sdf.format(callDate);
        Drawable drawable=null;
        int circle=R.drawable.circle_blue;

        switch (mData.getType()) {
            case CallLog.Calls.INCOMING_TYPE:

                break;
            case CallLog.Calls.OUTGOING_TYPE:
                circle=R.drawable.circle_cyan;
                break;
            case CallLog.Calls.MISSED_TYPE:
                circle=R.drawable.circle_pink;
                break;
        }
        mItemName.setText(mData.getName());
        Resources resources = mContext.getResources();
         drawable = resources.getDrawable(circle);
        mItemName.setCompoundDrawables(drawable,null,null,null);
        mItemTime.setText(stringcallDate);
        mItemPhone.setText(mData.getNumber());
    }

    @Override
    public void init() {
        super.init();
    }
}
