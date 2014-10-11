package com.bsent.actbeat1;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenReceiver extends BroadcastReceiver{
	public boolean wasScreenOn = true;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
			Log.d("llog","!!!!!!!!!!!!!!!!!!!!!1");
		}
	}

}