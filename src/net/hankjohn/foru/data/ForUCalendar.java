package net.hankjohn.foru.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.hankjohn.foru.alarm.AlarmWatcher;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class ForUCalendar {
	private final static String TAG = ForUCalendar.class.getName();
	private DB db;
	private Context context;
	private static ForUCalendar instance = null;
	private ForUWatcher watcher;
	/**
	 * @description 单例模式
	 * @param context
	 * @return
	 */
	public static ForUCalendar getInstance(Context context){
		if(instance == null){
			instance = new ForUCalendar(context);
		}
		return instance;
	}
	private ForUCalendar(Context context){
		this.context = context;
		db = new DB(context);
		watcher = new AlarmWatcher();
		watcher.onUpdate(this.getToday(), this.context);
	}
	/**
	 * @description 获取当天的TODO
	 * @return
	 */
	public List<ForUItem> getToday(){
		Calendar now = Calendar.getInstance();
		return getTodo(now);
	}
	/**
	 * @bref 获取某天的TODO
	 * @param date
	 * @return
	 */
	public List<ForUItem> getTodo(Calendar date){
		List<ForUItem> result = db.getItems(date);
		return result;
	}
	public List<Course> getCourses(String week){
		List<Course> result = new ArrayList<Course>();
		List<ForUEvent> events = db.getEvents(new String[]{Course.TYPE});
		int weekDay = Course.arrayCalendar[0];
		for(int i = 0; i < Course.array.length; i++){
			if(week == Course.array[i]){
				weekDay = Course.arrayCalendar[i];
			}
		}
		for(ForUEvent event : events){
			if(weekDay == event.getDue().get(Calendar.DAY_OF_WEEK)){
				result.add(new Course(event));
			}
		}
		return result;
	}
	public List<Tip> getTips(){
		List<Tip> result = new ArrayList<Tip>();
		List<ForUEvent> events = db.getEvents(new String[]{Tip.TYPE});
		for(ForUEvent event : events){
			result.add(new Tip(event));
		}
		return result;
	}
	public void addEvent(ForUEvent event){
		db.addEvent(event);
		watcher.onUpdate(this.getToday(), this.context);
	}
	public void rmEvent(long eid){
		db.rmEvent(eid);
		watcher.onUpdate(this.getToday(), this.context);
	}
	
	/**
	 * Vibernate and Audio
	 */
	private final static String AUDIO = "Audio";
	private final static String VIBERATE = "Viberate";
	public void setViberate(boolean enable){
		Log.d(TAG, "Set Viberate "+enable);
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		pref.edit().putBoolean(VIBERATE, enable).commit();
	}
	public boolean getViberate(){
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		return pref.getBoolean(VIBERATE, true);
	}
	public void setAudio(boolean enable){
		Log.d(TAG, "Set Audio "+enable);
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		pref.edit().putBoolean(AUDIO, enable).commit();
		getAudio();
	}
	public boolean getAudio(){
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		boolean enable = pref.getBoolean(AUDIO, true);
		return enable;
	}
}
