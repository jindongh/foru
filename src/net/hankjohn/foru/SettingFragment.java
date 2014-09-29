package net.hankjohn.foru;

import net.hankjohn.foru.alarm.AlarmReceiver;
import net.hankjohn.foru.data.ForUCalendar;
import net.hankjohn.foru.util.UpgradeUtil;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

public class SettingFragment extends ForUFragment implements OnCheckedChangeListener, View.OnClickListener{

	public final static String URL_REGISTER = "http://hankjohn.net/";
	@Override
	public int getTitle(){
		return R.string.title_sectionSetting;
	}
	private CheckBox cbViberate;
	private CheckBox cbAudio;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        //
        ForUCalendar calendar = ForUCalendar.getInstance(getActivity().getApplicationContext());
        cbViberate = (CheckBox)rootView.findViewById(R.id.cb_setting_viberate);
        cbViberate.setChecked(calendar.getViberate());
        cbViberate.setOnCheckedChangeListener(this);
        cbAudio = (CheckBox)rootView.findViewById(R.id.cb_setting_audio);
        cbAudio.setChecked(calendar.getAudio());
        cbAudio.setOnCheckedChangeListener(this);
        //Register
        Button btnRegister = (Button)rootView.findViewById(R.id.btn_setting_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setData(Uri.parse(URL_REGISTER));
				intent.setAction(Intent.ACTION_VIEW);
				SettingFragment.this.startActivity(intent);
			}
		});
        //update
        Button btnUpdate = (Button)rootView.findViewById(R.id.btn_setting_upgrade);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				new UpgradeUtil().checkVersion(getActivity());
			}
		});
        //test
        Button btnTest = (Button)rootView.findViewById(R.id.btn_setting_test);
        btnTest.setOnClickListener(this);
        return rootView;
    }
	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
        ForUCalendar calendar = ForUCalendar.getInstance(getActivity().getApplicationContext());
		// TODO Auto-generated method stub
		if(cbViberate == (CheckBox)arg0){
			calendar.setViberate(arg1);
		}
		if(cbAudio == (CheckBox)arg0){
			calendar.setAudio(arg1);
		}
	}
	private void cancelAlarm(int i){
		Context context = getActivity().getApplicationContext();
		AlarmManager alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		
		Intent intent = new Intent(getActivity(), AlarmReceiver.class);    //创建Intent对象
        intent.putExtra("msg", "Hello, Worlp"+i);
		PendingIntent pi = PendingIntent.getBroadcast(getActivity(), i, intent, 0);    //创建PendingIntent
        alarmManager.cancel(pi);
	}
	private void addAlarm(int i){

		Context context = getActivity().getApplicationContext();
		AlarmManager alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity(), AlarmReceiver.class);    //创建Intent对象
        intent.putExtra("msg", "Hello, Worlp"+i);
		PendingIntent pi = PendingIntent.getBroadcast(getActivity(), i, intent, 0);    //创建PendingIntent
        //alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);        //设置闹钟
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+i*1000, pi);        //设置闹钟，当前时间就唤醒
        Toast.makeText(getActivity(), "闹钟设置成功", Toast.LENGTH_LONG).show();//提示用户
	}
	@Override
	public void onClick(View arg0) {
		addAlarm(3);
		addAlarm(5);
		cancelAlarm(3);
	}
}
