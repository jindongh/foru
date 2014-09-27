package net.hankjohn.foru.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.util.Log;

public class Tip extends ForUEvent{
	public static String TYPE = "Tip";
	public Tip(ForUEvent event){
		super(event);
	}
	public Tip(String description, String date, String time, String repeat){
		super();
		this.description = description;
		this.type = TYPE;
		this.repeat = repeat;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		this.due = Calendar.getInstance();
		try{
			this.due.setTime(sdf.parse(date + " " + time));
		}
		catch(Exception pe){
			Log.w("parseTime", Log.getStackTraceString(pe));
		}
	}
}
