package net.hankjohn.foru.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DB extends SQLiteOpenHelper{
	private static final String DB_NAME = "hankjohn.db";
	private static final String TBL_NAME = "foruevent";
	public static final String COL_ID = "_id";
	public static final String COL_TYPE = "type";
	public static final String COL_DESCRIPTION = "description";
	public static final String COL_DUE = "due";
	public static final String COL_NOTE = "note";
	public static final String COL_REPEAT = "repeat";
	public static final String COL_LUNAR = "lunar";
	private static final String CRT_SQL = "create table "
			+ TBL_NAME + "("
			+ COL_ID + " integer primary key autoincrement, "
			+ COL_TYPE + " text, "
			+ COL_DESCRIPTION + " text, "
			+ COL_DUE + " integer, "
			+ COL_NOTE + " text, "
			+ COL_REPEAT + " text, "
			+ COL_LUNAR + " integer"
			+ ")";
	private static final String COLS[] = new String[]{
		COL_ID,
		COL_TYPE,
		COL_DESCRIPTION,
		COL_DUE,
		COL_NOTE,
		COL_REPEAT,
		COL_LUNAR
	};
	public DB(Context c){
		super(c, DB_NAME, null, 2);
	}
	@Override
	public void onCreate(SQLiteDatabase db){
		db.execSQL(CRT_SQL);
	}
	public List<ForUItem> getItems(Calendar now){
		List<ForUItem> result = new ArrayList<ForUItem>();
		List<ForUEvent> events = this.getEvents(new String[]{});
		for(ForUEvent event : events){
			if(event.match(now)){
				result.add(new ForUItem(event));
			}
		}
		return result;
	}
	public long addEvent(ForUEvent event){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values=new ContentValues(); 
		values.put(COL_TYPE, event.getType());
		values.put(COL_DESCRIPTION, event.getDescription());
		values.put(COL_DUE, event.getDue().getTimeInMillis());
		values.put(COL_NOTE, event.getNote());
		values.put(COL_REPEAT, event.getRepeat());
		values.put(COL_LUNAR, event.getLunar());
		long id = db.insert(TBL_NAME, null, values);
		event.setId(id);
		Log.d("DB", "add event"+id);
		return id;
	}
	public void rmEvent(long id){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TBL_NAME, "_id=?", new String[]{String.valueOf(id)});
	}
	public List<ForUEvent> getEvents(String []types){
		List<ForUEvent> result = new ArrayList<ForUEvent>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.query(TBL_NAME, null, null, null, null, null, null);
		if(c.moveToFirst()){
			do{
				ForUEvent event = new ForUEvent(c);
				Log.v("DB", "get event " +event.getType() +event.description+types.length);
				if(0 == types.length){
					result.add(event);
				}
				else{
					for(String type : types){
						if(event.getType().equals(type)){
							result.add(event);
							break;
						}
					}
				}
			}while(c.moveToNext());
		}
		Log.d("DB", "get events " + result.size());
		return result;
	}
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	public static void main(String args[]){
		
	}
}
