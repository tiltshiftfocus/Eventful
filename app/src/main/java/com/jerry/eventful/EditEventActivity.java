package com.jerry.eventful;

import java.util.Calendar;
import java.util.Date;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class EditEventActivity extends Activity {
	
	private String PACKAGE_NAME;
	
	private Intent resultData = new Intent();
	private int id;
	
	private static final int DATE_DIALOG_ID = 0;
	private static final int TIME_DIALOG_ID = 1;
	
	private int mYear, mMonth, mDay, mHour, mMinute, mSecond;
	
	private EditText editText1;
	private Button setDateBtn;
	private Button setTimeBtn;
	
	
	private DatePickerDialog.OnDateSetListener mDateSetListener = 
			new DatePickerDialog.OnDateSetListener() {
				
				@Override
				public void onDateSet(DatePicker view, int yearSel, int monthOfYear,
						int dayOfMonth) {
					// TODO Auto-generated method stub
					mYear = yearSel;
					mMonth = monthOfYear;
					mDay = dayOfMonth;
					updateDisplay();
					//setDateBtn.setText(day + "." + month + "." + year);
				}
			};
			
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = 
			new TimePickerDialog.OnTimeSetListener() {
				
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					// TODO Auto-generated method stub
					/*hour = hourOfDay;
					minute = minuteSel;*/
					mHour = hourOfDay;
					mMinute = minute;
					updateDisplay();
					
					//setTimeBtn.setText(String.format("%02d:%02d", hourOfDay,minute));
					
				}
			};

	
	@Override
	protected void onCreate(Bundle instance){
		PACKAGE_NAME = this.getPackageName();
		
		SharedPreferences pref = this.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
		if(pref.getBoolean("darktheme", false)==true){
			setTheme(R.style.AppDarkTheme);
		}else
			setTheme(R.style.AppTheme);
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
		
		super.onCreate(instance);
		setContentView(R.layout.newevent_activity);
		
		editText1 = (EditText)findViewById(R.id.editTextEvent);

		setDateBtn = (Button)findViewById(R.id.set_date);
		setTimeBtn = (Button)findViewById(R.id.set_time);
		
		
		
		
		setDateBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
				
			}
		});
		
		setTimeBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog(TIME_DIALOG_ID);
				
			}
		});
		
		Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
		mSecond = c.get(Calendar.SECOND);
		
		Intent localIntent = getIntent();
		int i = localIntent.getIntExtra("id", -1);
		if(i>=0){
			if(localIntent.getBooleanExtra("editTitle", false)){
				this.setTitle("Edit Event");
			}
			this.id = i;
			long l = localIntent.getLongExtra("when", 0L);
			String str = localIntent.getStringExtra("eventname");
			Time localTime = new Time();
			localTime.set(l);
			mYear = localTime.year;
			mMonth = localTime.month;
			mDay = localTime.monthDay;
			mHour = localTime.hour;
			mMinute = localTime.minute;
			mSecond = localTime.second;
			editText1.setText(str);
			resultData.putExtra("id", i);
			resultData.putExtra("when", l);
			resultData.putExtra("eventname", str);
			setResult(RESULT_OK,resultData);
		}
		
		updateDisplay();
		
	}
	
	@Override
	public void onBackPressed(){
		setResult(RESULT_CANCELED);
		moveTaskToBack(false);
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.newevent_actions, menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
		default:
			return super.onOptionsItemSelected(item);
		case R.id.saveBtn:
			newEventSave();
			return true;
		case R.id.delete:
			deleteEvent();
			return true;
		
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			// create a new DatePickerDialog with values you want to show
			return new DatePickerDialog(this,
					mDateSetListener,
					mYear, mMonth, mDay);
			// create a new TimePickerDialog with values you want to show
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this,
					mTimeSetListener, mHour, mMinute, android.text.format.DateFormat.is24HourFormat(this));

		}
		return null;
	}
		
		
	private void newEventSave(){
		String strText = editText1.getText().toString();		
		
		if(!strText.isEmpty() && strText.length()>0){
			resultData.putExtra("eventname", strText);
			
			Time localTime = new Time();
			localTime.set(mSecond,mMinute,mHour,mDay,mMonth,mYear);
			resultData.putExtra("when", localTime.toMillis(false));
			
			setResult(RESULT_OK, resultData);
			finish();
		}else
			Toast.makeText(this, "Enter an event!", Toast.LENGTH_SHORT).show();
	}
	
	private void deleteEvent(){
		resultData.putExtra("delete", true);
		setResult(RESULT_OK,resultData);
		finish();
	}
	
	
	private void updateDisplay(){
		Time localTime = new Time();
		localTime.set(mSecond,mMinute,mHour,mDay,mMonth,mYear);
		//resultData.putExtra("when", localTime.toMillis(false));
		java.text.DateFormat localDateFormat = android.text.format.DateFormat.getDateFormat(this);
		
		setDateBtn.setText(localDateFormat.format(new Date(localTime.toMillis(false))));
		if (android.text.format.DateFormat.is24HourFormat(this)){
			setTimeBtn.setText(String.format("%02d:%02d", mHour,mMinute));
			return;
			
		}else{
			if(mHour>12){
				setTimeBtn.setText(String.format("%02d:%02d PM", mHour-12,mMinute));
			}else{
				setTimeBtn.setText(String.format("%02d:%02d AM", mHour,mMinute));
			}
		}
	   
	}

}
