package net.hankjohn.foru.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.util.Log;
import junit.framework.TestCase;

public class Test extends TestCase {
	public void testDate(){
		String date = "2014-08-27";
		String time = "08:04";
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm");
		Calendar due = Calendar.getInstance();
		try{
			due.setTime(sdf.parse(date + " " + time));
		}
		catch(Exception pe){
			Log.w("parseTime", Log.getStackTraceString(pe));
		}
	}
}
