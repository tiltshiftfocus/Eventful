package com.jerry.eventful.database;

import android.content.Context;
import android.text.format.Time;

import com.jerry.eventful.helper.DateDiff;

public class Eventful {
	
	private int id;
	private String event;
	private Time time;
	private Context context;
	
	public Eventful(Context context, int id, String event, Time time){
		super();
		this.setId(id);
		this.event = event;
		this.time = time;
		this.context = context;
	}
	
	public String getAgoString(){
		Time localTime = new Time();
		localTime.setToNow();
		return new DateDiff(context, localTime, time).toString();
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public Time getTime() {
		return time;
	}
	
	public String getReadableTime(){
		Time localTime = new Time();
		localTime.setToNow();
		/*int mYear = readableTime.year;
		int mMonth = readableTime.month+1;
		int mDay = readableTime.monthDay;
		int mHour = readableTime.hour;
		int mMinute = readableTime.minute;
		return (mDay+"/"+mMonth+"/"+mYear+", "+String.format("%02d:%02d", mHour,mMinute));*/
		return new DateDiff(context, localTime, time).getReadableDateTime();
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public long getWhenMillis(){
		return time.toMillis(false);
	}
	

}
