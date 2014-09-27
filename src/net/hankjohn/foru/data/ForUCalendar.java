package net.hankjohn.foru.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.content.Context;

public class ForUCalendar {
	private DB db;
	private static ForUCalendar instance = null;
	public static ForUCalendar getInstance(Context context){
		if(instance == null){
			instance = new ForUCalendar(context);
		}
		return instance;
	}
	private ForUCalendar(Context c){
		db = new DB(c);
	}
	public List<ForUItem> getToday(){
		Calendar now = Calendar.getInstance();
		return getTodo(now);
	}
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
	public void addTip(Tip tip){
		db.addEvent(tip);
	}
	public void addCourse(Course course){
		db.addEvent(course);
	}
	public void rmEvent(long eid){
		db.rmEvent(eid);
	}
}
