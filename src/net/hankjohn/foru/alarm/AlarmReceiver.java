package net.hankjohn.foru.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver{

	private final static String TAG = AlarmReceiver.class.getName();
    @Override
    public void onReceive(Context context, Intent oldIntent) {
    	Intent intent=new Intent(context, AlarmActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(oldIntent.getExtras());
        //intent.getExtras().putAll(oldIntent.getExtras());    	
        context.startActivity(intent);
    }
}
