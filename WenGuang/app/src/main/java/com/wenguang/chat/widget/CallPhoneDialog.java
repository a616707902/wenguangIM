package com.wenguang.chat.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;

import com.wenguang.chat.R;

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

    public CallPhoneDialog(Context context) {
        super(context, R.style.DialogTheme);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        this.setCanceledOnTouchOutside(false);
        this.context = context;
        view = getLayoutInflater().inflate(R.layout.dialog_progress, null);
//        tvMessage = (TextView) view.findViewById(R.id.textview_message);
//        splshView= (SplshView) view.findViewById(R.id.imageview_progress_spinner);

        this.setContentView(view);
    }
    public static CallPhoneDialog getInstance(Context context) {
        if (instance == null) {
            instance = new CallPhoneDialog(context);
        }
        return instance;
    }
    public void setMessage(String message) {
        tvMessage.setText(message);
    }

    @Override
    public void show() {
        if (!((Activity) context).isFinishing()) {
            super.show();
        } else {
            instance = null;
        }
    }
}
