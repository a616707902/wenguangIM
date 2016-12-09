package com.wenguang.chat.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hyphenate.chat.EMClient;
import com.hyphenate.util.EMLog;
import com.wenguang.chat.activity.EMCallPhoneActivity;

public class CallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if(!EMClient.getInstance().isLoggedInBefore())
		    return;
		//username
		String from = intent.getStringExtra("from");
		//call type
		String type = intent.getStringExtra("type");
		if("voice".equals(type)){ //video call
			context.startActivity(new Intent(context, EMCallPhoneActivity.class).
					putExtra("phonenum", from).putExtra("isComingCall", true).
					addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
		}else{ //voice call

		}
		EMLog.d("CallReceiver", "app received a incoming call");
	}

}
