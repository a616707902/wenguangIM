package com.wenguang.chat.widget;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.wenguang.chat.R;


public class LoadProgressDialog extends Dialog {
    
	public static final int FADED_ROUND_SPINNER = 0;
	public static final int GEAR_SPINNER = 1;
	public static final int SIMPLE_ROUND_SPINNER = 2;
    
	static LoadProgressDialog instance;
	View view;
	TextView tvMessage;
//	ImageView ivProgressSpinner;
	//AnimationDrawable adProgressSpinner;
	SplshView splshView;
	Context context;
    
	OnDialogDismiss onDialogDismiss;
    
	public OnDialogDismiss getOnDialogDismiss() {
		return onDialogDismiss;
	}
    
	public void setOnDialogDismiss(OnDialogDismiss onDialogDismiss) {
		this.onDialogDismiss = onDialogDismiss;
	}
    
	public static LoadProgressDialog getInstance(Context context) {
		if (instance == null) {
			instance = new LoadProgressDialog(context);
		}
		return instance;
	}
    
	public LoadProgressDialog(Context context) {
		super(context, R.style.DialogTheme);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawable(
                                               new ColorDrawable(Color.TRANSPARENT));
		this.setCanceledOnTouchOutside(false);
		this.context = context;
		view = getLayoutInflater().inflate(R.layout.dialog_progress, null);
		tvMessage = (TextView) view.findViewById(R.id.textview_message);
		splshView= (SplshView) view.findViewById(R.id.imageview_progress_spinner);

		this.setContentView(view);
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
    
	public void dismissWithSuccess() {
		tvMessage.setText("Success");

		if (onDialogDismiss != null) {
			this.setOnDismissListener(new OnDismissListener() {
                
				@Override
				public void onDismiss(DialogInterface dialog) {
					onDialogDismiss.onDismiss();
				}
			});
		}
		dismissHUD();
	}
    
	public void dismissWithSuccess(String message) {
		if (message != null) {
			tvMessage.setText(message);
		} else {
			tvMessage.setText("");
		}
        
		if (onDialogDismiss != null) {
			this.setOnDismissListener(new OnDismissListener() {
                
				@Override
				public void onDismiss(DialogInterface dialog) {
					onDialogDismiss.onDismiss();
				}
			});
		}
		dismissHUD();
	}
    
	public void dismissWithFailure() {
		tvMessage.setText("Failure");
		if (onDialogDismiss != null) {
			this.setOnDismissListener(new OnDismissListener() {
                
				@Override
				public void onDismiss(DialogInterface dialog) {
					onDialogDismiss.onDismiss();
				}
			});
		}
		dismissHUD();
	}
    
	public void dismissWithFailure(String message) {

		if (message != null) {
			tvMessage.setText(message);
		} else {
			tvMessage.setText("");
		}
		if (onDialogDismiss != null) {
			this.setOnDismissListener(new OnDismissListener() {
                
				@Override
				public void onDismiss(DialogInterface dialog) {
					onDialogDismiss.onDismiss();
				}
			});
		}
		dismissHUD();
	}
    

	protected void reset() {
//		ivProgressSpinner.setVisibility(View.VISIBLE);
//		ivFailure.setVisibility(View.GONE);
//		ivSuccess.setVisibility(View.GONE);
//		tvMessage.setText("Loading ...");
	}
    
	protected void dismissHUD() {
		splshView.splshDisapaer();

	}
    
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
//		ivProgressSpinner.post(new Runnable() {
//
//			@Override
//			public void run() {
//				adProgressSpinner.start();
//
//			}
//		});
	}
    
	public interface OnDialogDismiss {
		public void onDismiss();
	}
    
}
