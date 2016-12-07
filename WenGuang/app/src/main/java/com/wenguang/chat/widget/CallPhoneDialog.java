package com.wenguang.chat.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

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
        freeCall.setOnClickListener(freeOnClick);
        puCall.setOnClickListener(puOnClick);
        this.setContentView(view);
    }

    public static CallPhoneDialog getInstance(Context context) {
        if (instance == null) {
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

    public void setFreeOnClick(View.OnClickListener freeOnClick) {
        this.freeOnClick = freeOnClick;
    }

    public void setPuOnClick(View.OnClickListener puOnClick) {
        this.puOnClick = puOnClick;
    }
}
