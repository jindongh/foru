package net.hankjohn.foru.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.database.Cursor;
import android.util.Log;

public class ForUEvent {
	Calendar due;
	long id;
	String description;
	String note;
	String type;
	String repeat;
	private boolean lunar;
	public static final String REPEAT_NONE = "不重复";
	public static final String REPEAT_DAILY = "每天";
	public static final String REPEAT_MONTH = "每月";
	public static final String REPEAT_YEAR = "每年";
	public static final String REPEAT_WEEK = "每周";

	public ForUEvent() {
		due = Calendar.getInstance();
	}

	public ForUEvent(ForUEvent event) {
		this.due = event.getDue();
		this.id = event.getId();
		this.description = event.getDescription();
		this.note = event.getNote();
		this.type = event.getType();
		this.repeat = event.getRepeat();
		this.lunar = event.getLunar();
	}

	public ForUEvent(Cursor c) {
		this.id = c.getLong(c.getColumnIndex(DB.COL_ID));
		this.due = Calendar.getInstance();
		this.due.setTimeInMillis(c.getLong(c.getColumnIndex(DB.COL_DUE)));
		this.description = c.getString(c.getColumnIndex(DB.COL_DESCRIPTION));
		this.note = c.getString(c.getColumnIndex(DB.COL_NOTE));
		this.type = c.getString(c.getColumnIndex(DB.COL_TYPE));
		this.repeat = c.getString(c.getColumnIndex(DB.COL_REPEAT));
		this.lunar = c.getLong(c.getColumnIndex(DB.COL_LUNAR)) == 0;
	}

	public boolean match(Calendar day) {
		switch (this.repeat) {
		case REPEAT_NONE:
			if (day.get(Calendar.DATE) == due.get(Calendar.DATE)){
				Log.d("ForUEvent", "Same Day");
				return true;
			} else {
				return false;
			}
		case REPEAT_WEEK:
			if(day.get(Calendar.DAY_OF_WEEK) == due.get(Calendar.DAY_OF_WEEK)){
				Log.d("ForUEvent", "Same Week Day" + due.get(Calendar.DAY_OF_WEEK));
				return true;
			}
			else{
				return false;
			}
		case REPEAT_DAILY:
			if(day.get(Calendar.DATE) >= due.get(Calendar.DATE)){
				Log.d("ForUEvent", "daily");
				return true;
			}
			else{
				return false;
			}
		case REPEAT_MONTH:
			if(day.get(Calendar.DAY_OF_MONTH) == due.get(Calendar.DAY_OF_MONTH)){
				Log.d("ForUEvent", "Same month day");
				return true;
			}
			else{
				return false;
			}
		case REPEAT_YEAR:
			if(day.get(Calendar.DAY_OF_YEAR) == due.get(Calendar.DAY_OF_YEAR)){
				Log.d("ForUEvent", "Same Year Day");
				return true;
			}
			else{
				return false;
			}
		}
		Log.w("ForUEvent", "match unknown type " + repeat);
		return true;
	}

	public String getType() {
		return this.type;
	}

	public String getDescription() {
		return description;
	}

	public String getNote() {
		return note;
	}

	public Calendar getDue() {
		return due;
	}

	public String getDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(due.getTime());
	}

	public String getTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		return formatter.format(due.getTime());
	}

	public String getRepeat() {
		return repeat;
	}

	public boolean getLunar() {
		return lunar;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}
}
