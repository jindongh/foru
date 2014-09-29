package net.hankjohn.foru.alarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.hankjohn.foru.data.ForUItem;
import net.hankjohn.foru.data.ForUWatcher;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmWatcher implements ForUWatcher {
	private final static String TAG = AlarmWatcher.class.getName();
	private List<Integer> alarms = new ArrayList<Integer>();
	private void clearAlarm(Context context){
		for(Integer iid : alarms){
			int id = iid.intValue();
			Intent intent = new Intent(context, AlarmReceiver.class);  
	        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, 0);  
	        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);  
	        am.cancel(pi);  
	        Log.i(TAG, "CancelAlarm id=" + id);
		}
		alarms.clear();
	}
	private void addAlarm(ForUItem item, Context context, int id) {
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AlarmReceiver.class); // 创建Intent对象
		intent.putExtra("msg", item.toString());
		PendingIntent pi = PendingIntent.getBroadcast(context, id, intent,
				0);
		Calendar due = item.getEvent().getDue();
		Calendar c = Calendar.getInstance();
		String tip = "id=" + id + " msg="+item.toString() + "due="+due.get(Calendar.HOUR_OF_DAY)+due.get(Calendar.MINUTE);
		if(due.before(c)){
			Log.d(TAG, "SkipAlarm "+tip);		
			return ;
		}
		c.set(Calendar.HOUR_OF_DAY, due.get(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, due.get(Calendar.MINUTE));
		c.set(Calendar.SECOND, 0);
		alarmManager.set(AlarmManager.RTC_WAKEUP,
				c.getTimeInMillis(), pi); // 设置闹钟，当前时间就唤醒
		Log.d(TAG, "AddAlarm "+tip);
		alarms.add(Integer.valueOf(id));
	}

	@Override
	public void onUpdate(List<ForUItem> items, Context context) {
		this.clearAlarm(context);
		for(int i=0; i<items.size(); i++){
			this.addAlarm(items.get(i), context, i);
		}
		Log.i(TAG, "Alarm Updated "+items.size());
	}
}
