package net.hankjohn.foru.util;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class CalendarSelect {

	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;
	private EditText etDay;
	private EditText etTime;
	private Fragment view;

	public void initView(Fragment view, EditText etDay, EditText etTime) {
		this.view = view;
		this.etDay = etDay;
		this.etTime = etTime;

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
		//EditText for Day
		if(null != etDay){
			// action
			etDay.setFocusableInTouchMode(false);
			etDay.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (CalendarSelect.this.etDay.equals((EditText) v)) {
						DialogFragment newFragment = new DatePickerFragment();
						newFragment.show(
								CalendarSelect.this.view.getFragmentManager(),
								"选择日期");
					}
				}
			});
			updateDateDisplay();
		}
		//EditText for Time
		if(null != etTime){
			etTime.setFocusableInTouchMode(false);
			etTime.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (CalendarSelect.this.etTime.equals((EditText) v)) {
						DialogFragment newFragment = new TimePickerFragment();
						newFragment.show(
								CalendarSelect.this.view.getFragmentManager(),
								"选择时间");
					}
				}
			});
			updateTimeDisplay();
		}
	}
	public void setToday(){
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		if(null != etDay){
			updateDateDisplay();
		}
	}
	public Calendar getCalendar(){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, mYear);
		c.set(Calendar.MONTH, mMonth);
		c.set(Calendar.DAY_OF_MONTH, mDay);
		c.set(Calendar.HOUR_OF_DAY, mHour);
		c.set(Calendar.MINUTE, mMinute);
		return c;
	}
	private void updateDateDisplay(){  
		this.etDay.setText(new StringBuilder().append(mYear).append("-")  
	               .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-")  
	               .append((mDay < 10) ? "0" + mDay : mDay));   
	}
	private void updateTimeDisplay(){  
	       this.etTime.setText(new StringBuilder().append(mHour).append(":")  
	               .append((mMinute < 10) ? "0" + mMinute : mMinute));   
	    } 

	public class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, mYear, mMonth, mDay);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			// Do something with the date chosen by the user
			mYear = year;
			mMonth = month;
			mDay = day;
			updateDateDisplay();
		}
	}

	public class TimePickerFragment extends DialogFragment implements
			TimePickerDialog.OnTimeSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new TimePickerDialog(getActivity(), this, mHour, mMinute,
					DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// Do something with the time chosen by the user
			mHour = hourOfDay;
			mMinute = minute;
			updateTimeDisplay();
		}
	}
}
