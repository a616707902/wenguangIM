package com.wenguang.chat.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.wenguang.chat.R;
import com.wenguang.chat.activity.EMCallPhoneActivity;
import com.wenguang.chat.utils.DimenUtil;
import com.wenguang.chat.utils.MobileUtils;

/**
 * 作者：chenpan
 * 时间：2016/12/7 17:27
 * 邮箱：616707902@qq.com
 * 描述：
 */

public class CallPhoneDialog extends Dialog {
    static CallPhoneDialog instance;
    Context context;
    View view;
    TextView phoneNum;
    Button freeCall;
    Button puCall;
    Button cancle;
    View.OnClickListener freeOnClick;
    View.OnClickListener puOnClick;

    public CallPhoneDialog(Context context) {
        super(context, R.style.DialogTheme);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        this.setCanceledOnTouchOutside(true);
        this.context = context;
        view = getLayoutInflater().inflate(R.layout.callphone_dialog, null);
        phoneNum = (TextView) view.findViewById(R.id.show_num);
        freeCall = (Button) view.findViewById(R.id.free_call);

        puCall = (Button) view.findViewById(R.id.pu_call);
        cancle = (Button) view.findViewById(R.id.cancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        this.setContentView(view);
    }

    public static CallPhoneDialog getInstance(Context context) {
        if (instance == null) {
            instance = new CallPhoneDialog(context);
        }else{
//            instance.dismiss();
            instance=null;
            instance = new CallPhoneDialog(context);
        }
        return instance;
    }


    @Override
    public void show() {
        if (!((Activity) context).isFinishing()) {
            super.show();
        } else {
            instance = null;
        }
    }

    public void setText(final String phonenum,String string) {
        phoneNum.setText(phonenum);
        if (!MobileUtils.isMobileNo(phonenum)) {
            setFreeString("不支持错误格式号码");
        }else if (!TextUtils.isEmpty(string)){
            setFreeString(string);
        }else {
            setFreeOnClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, EMCallPhoneActivity.class);
                    intent.putExtra("phonenum", phonenum);
                    context.startActivity(intent);
                    instance.dismiss();
                   // ((Activity)context).finish();

                }
            });
        }
    }

    public void setFreeOnClick(View.OnClickListener OnClick) {
       this.freeOnClick =OnClick;
        freeCall.setOnClickListener(freeOnClick);
    }

    public void setPuOnClick(View.OnClickListener OnClick) {
        this.puOnClick = OnClick;
        puCall.setOnClickListener(puOnClick);


    }

    public void setFreeString(String msg) {
        String string = context.getString(R.string.free_call_style, "免费电话", msg);
        int index = string.lastIndexOf("话") + 1;
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(string);
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(((int) DimenUtil.dp2px(10f))), index, string.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        freeCall.setText(spannableStringBuilder);
        freeCall.setBackgroundResource(R.drawable.call_phone_btn_p);
        freeCall.setTextColor(context.getResources().getColor(R.color.white));
        freeCall.setEnabled(false);
    }

    @Override
    public void dismiss() {
        reset();
        super.dismiss();
    }

    private void reset() {
        freeCall.setEnabled(true);
        freeCall.setBackgroundResource(R.drawable.call_phone_btn_n);
        freeCall.setTextColor(context.getResources().getColor(R.color.qing));
        freeCall.setText(R.string.free_call);
        freeOnClick = null;
        puOnClick = null;

    }
}
