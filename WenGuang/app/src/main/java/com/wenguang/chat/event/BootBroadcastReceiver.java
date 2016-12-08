package com.wenguang.chat.event;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wenguang.chat.activity.SplashActivity;

public class BootBroadcastReceiver extends BroadcastReceiver {
	static final String action_boot = "android.intent.action.BOOT_COMPLETED";
	static final String action_boot1 = "android.intent.action.RECEIVE_BOOT_COMPLETED";
	static final String action_boot2 = "android.intent.action.ACTION_BOOT_COMPLETED";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(action_boot)
				|| intent.getAction().equals(action_boot1)
				|| intent.getAction().equals(action_boot2)) {
			Intent ootStartIntent = new Intent(context, SplashActivity.class);
			ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(ootStartIntent);
		}
	}
}