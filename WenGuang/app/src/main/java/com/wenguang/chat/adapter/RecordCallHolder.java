package com.wenguang.chat.adapter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.CallLog;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.TextView;

import com.wenguang.chat.R;
import com.wenguang.chat.bean.RecordEntity;
import com.wenguang.chat.event.CallBackBmob;
import com.wenguang.chat.mvp.model.CallPhoneModel;
import com.wenguang.chat.mvp.model.CallPhoneModelImpl;
import com.wenguang.chat.mvp.model.ContactFragmentModel;
import com.wenguang.chat.mvp.model.ContactFragmentModelImpl;
import com.wenguang.chat.utils.DimenUtil;
import com.wenguang.chat.utils.MobileUtils;
import com.wenguang.chat.utils.ToastUtils;
import com.wenguang.chat.widget.CallPhoneDialog;

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
    private CallPhoneDialog callPhoneDialog;
    String callPhoneNum;
    ContactFragmentModel mContactFragmentModel = new ContactFragmentModelImpl();

    public RecordCallHolder(View view) {
        super(view);
    }

    @Override
    public void setData(RecordEntity mData) {
        super.setData(mData);
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
            hour = hour - 12;
            time = "PM";
        }
        stringcallDate = hour + ":" + stringcallDate.split(":")[1];
        String dataTime = mContext.getString(R.string.record_call_time, stringcallDate, time);
        int index = dataTime.lastIndexOf(" ") + 1;
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
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPhoneNum = mData.getNumber();
                if (MobileUtils.isMobileNo(callPhoneNum)) {
                    //  ((CallPhonePresenter) mPresenter).queryAccount(CallPhoneActivity.this, callPhoneNum);
                    mContactFragmentModel.queryData(callPhoneNum, new CallBackBmob<Boolean>() {
                        @Override
                        public void succssCallBack(Boolean jsonArray) {
                            if (jsonArray) {
//                                if (null!=mView)
//                                {
                                showDialog(callPhoneNum, null);
//                                }
                            } else {
//                                if (null!=mView)
//                                {
                                showDialog(callPhoneNum, "该号码不支持拨打免费电话");
//                                }
                            }
                        }

                        @Override
                        public void failed(String e) {
//                            if (null!=mView)
//                            {
                            showDialog(callPhoneNum, "该号码不支持拨打免费电话");
//                            }
                        }
                    });
                } else {
                    showLoadProgressDialog(callPhoneNum);
                }

            }
        });
        mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
                intent.setType("vnd.android.cursor.item/person");
                intent.setType("vnd.android.cursor.item/contact");
                intent.setType("vnd.android.cursor.item/raw_contact");
                intent.putExtra(android.provider.ContactsContract.Intents.Insert.NAME, mData.getName());
                intent.putExtra(android.provider.ContactsContract.Intents.Insert.PHONE, mData.getNumber());
                //intent.putExtra(android.provider.ContactsContract.Intents.Insert.PHONE_TYPE, 2);
                mContext.startActivity(intent);
                return false;
            }
        });

    }

    public void showDialog(String phone, String str) {
        callPhoneDialog = CallPhoneDialog.getInstance(mContext);
        callPhoneDialog.setPuOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhone();
                callPhoneDialog.dismiss();
            }
        });
        callPhoneDialog.setText(phone, str);
        callPhoneDialog.show();
    }

    public void showLoadProgressDialog(String phone) {

        callPhoneDialog = CallPhoneDialog.getInstance(mContext);
        callPhoneDialog.setPuOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhone();
                callPhoneDialog.dismiss();
            }
        });
        callPhoneDialog.setText(phone, null);
        callPhoneDialog.show();
    }

    private void callPhone() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(mContext,
                    Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                CallPhoneModel callPhone = new CallPhoneModelImpl();
                callPhone.callPhone(mContext, callPhoneNum);
            } else {
                //
                ToastUtils.showToast(mContext, "请在设置界面打开权限");
            }
        } else {
            CallPhoneModel callPhone = new CallPhoneModelImpl();
            callPhone.callPhone(mContext, callPhoneNum);
        }
        //  MPermissions.requestPermissions((Activity) mContext, Common.REQUECT_CALL_PHONE, Manifest.permission.CALL_PHONE);

    }

//    @PermissionGrant(Common.REQUECT_CALL_PHONE)
//    public void requestCallPhone() {
//        CallPhoneModel callPhone = new CallPhoneModelImpl();
////        ((CallPhonePresenter) mPresenter).callPhone(this, callPhoneNum);
//        callPhone.callPhone(mContext, callPhoneNum);
//
//    }
//
//    @PermissionDenied(Common.REQUECT_CALL_PHONE)
//    public void requestCallFailed() {
//    }
}
