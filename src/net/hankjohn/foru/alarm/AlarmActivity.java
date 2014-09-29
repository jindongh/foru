package net.hankjohn.foru.alarm;

import java.io.IOException;

import net.hankjohn.foru.data.ForUCalendar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

public class AlarmActivity extends Activity {
	private final String TAG = AlarmActivity.class.getName();

	MediaPlayer mMediaPlayer = null;
	Vibrator vibrator = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String msg = this.getIntent().getExtras().getString("msg");
		viberate();
		audio();
		 //显示对话框
        new AlertDialog.Builder(AlarmActivity.this).
            setTitle("ForU").//设置标题
            setMessage(msg).//设置内容
            setPositiveButton("知道了", new OnClickListener(){//设置按钮
                public void onClick(DialogInterface dialog, int which) {
                	AlarmActivity.this.stop();
                    AlarmActivity.this.finish();//关闭Activity
                }
            }).create().show();
	}
	public void stop(){
		if(null != mMediaPlayer){
			mMediaPlayer.stop();
		}
		if(null != vibrator){
			vibrator.cancel();
		}
	}
	public void viberate(){
		ForUCalendar calendar = ForUCalendar.getInstance(getApplicationContext());
		if(!calendar.getViberate()){
			Log.d("AlarmActivity", "Viberate disabled");
			return;
		}
		vibrator = (Vibrator)getApplication().getSystemService(Context.VIBRATOR_SERVICE);  
		// 等待3秒，震动3秒，从第0个索引开始，一直循环  
		vibrator.vibrate(new long[]{3000, 3000}, 0);  
	}
	public void audio(){
		ForUCalendar calendar = ForUCalendar.getInstance(getApplicationContext());
		if(!calendar.getAudio()){
			Log.d("AlarmActivity", "Audio disabled");
			return;
		}
		Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);   
		// 如果为空，才构造，不为空，说明之前有构造过  
		if(null == mMediaPlayer){
			mMediaPlayer = new MediaPlayer();  
		}
		try{
		mMediaPlayer.setDataSource(getApplicationContext(), uri);  
		mMediaPlayer.setLooping(true); //循环播放  
		mMediaPlayer.prepare();  
		mMediaPlayer.start();
		}
		catch(IOException ioe){
			Log.w("AlarmActivity", "Play Audio Error"+ioe.toString());
		}
	}

}
