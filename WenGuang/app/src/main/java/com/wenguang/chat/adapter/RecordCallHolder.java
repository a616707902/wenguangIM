package com.wenguang.chat.adapter;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.provider.CallLog;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.TextView;

import com.wenguang.chat.R;
import com.wenguang.chat.bean.RecordEntity;
import com.wenguang.chat.utils.DimenUtil;

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
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date callDate = new Date(mData.getlDate());
        String stringcallDate = sdf.format(callDate);
        String time = "AM";
        Drawable drawable = null;
        int circle = R.drawable.circle_blue;
        int typeIcon = R.drawable.icon_call_in;
        switch (mData.getType()) {
            case CallLog.Calls.INCOMING_TYPE:
                break;
            case CallLog.Calls.OUTGOING_TYPE:
                circle = R.drawable.circle_cyan;
                typeIcon = R.drawable.icon_call_out;
                break;
            case CallLog.Calls.MISSED_TYPE:
                circle = R.drawable.circle_pink;
                typeIcon = R.drawable.icon_miss_call;
                break;
        }
        mItemName.setText(mData.getName());
        Resources resources = mContext.getResources();
        drawable = resources.getDrawable(circle);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mItemName.setCompoundDrawables(drawable, null, null, null);
        String hours = stringcallDate.split(":")[0];

        int hour = Integer.valueOf(hours);
        if (hour > 12) {
            hour=hour-12;
            time = "PM";
        }
        stringcallDate=hour+":"+stringcallDate.split(":")[1];
        String dataTime = mContext.getString(R.string.record_call_time, stringcallDate, time);
        int index=dataTime.lastIndexOf(" ")+1;
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(dataTime);
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(((int) DimenUtil.dp2px(10f))), index, dataTime.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        mItemTime.setText(spannableStringBuilder);
        drawable = resources.getDrawable(typeIcon);
        mItemPhone.setText(mData.getNumber());
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mItemPhone.setCompoundDrawables(drawable, null, null, null);
    }

    @Override
    public void init() {
        super.init();
    }
}
