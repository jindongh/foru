package net.hankjohn.foru.data;

import java.util.Calendar;

import net.hankjohn.foru.CourseFragment;

public class Course extends ForUEvent {
	public static String TYPE = "Course";
	public static String[] array = { "周一", "周二", "周三", "周四", "周五" };
	public static int[] arrayCalendar = {Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY};
   
	
	public Course(ForUEvent event){
		super(event);
	}
	public Course(String course, String week, String time){
		super();
		for(int i=0; i<Course.array.length; i++){
			if(Course.array[i] == week){
				this.due.set(Calendar.DAY_OF_WEEK, Course.arrayCalendar[i]);
			}
		}
		String timeParts[] = time.split(":");
		this.due.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeParts[0]));
		this.due.set(Calendar.MINUTE, Integer.parseInt(timeParts[1]));
		this.description = course;
		this.note = week;
		this.repeat = ForUEvent.REPEAT_WEEK;
		this.type = TYPE;
	}
}
