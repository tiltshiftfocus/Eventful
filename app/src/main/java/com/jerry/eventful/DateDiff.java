package com.jerry.eventful;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.Context;
import android.text.format.Time;

public class DateDiff{

	private Context context;

	private int days;
	private int hours;
	private int minutes;
	private int months;
	private boolean negative = false;
	private int seconds;
	private Time time1;
	private Time time2;
	private int years;

	public DateDiff(Context context, Time paramTime1, Time paramTime2){
		this.context = context;
		if (paramTime2.toMillis(false) > paramTime1.toMillis(false)){
			Time localTime = paramTime1;
			paramTime1 = paramTime2;
			paramTime2 = localTime;
			this.negative = true;
		}
		this.time1 = paramTime1;
		this.time2 = paramTime2;
		this.years = (paramTime1.year - paramTime2.year);
		this.months = (paramTime1.month - paramTime2.month);
		this.days = (paramTime1.monthDay - paramTime2.monthDay);
		this.hours = (paramTime1.hour - paramTime2.hour);
		this.minutes = (paramTime1.minute - paramTime2.minute);
		this.seconds = (paramTime1.second - paramTime2.second);
		normalize();
	}

	private void normalize(){
		if (this.seconds < 0){
			this.seconds = (60 + this.seconds);
			this.minutes -= 1;
		}
		if (this.minutes < 0){
			this.minutes = (60 + this.minutes);
			this.hours -= 1;
		}
		if (this.hours < 0){
			this.hours = (24 + this.hours);
			this.days -= 1;
		}
		if (this.days < 0){
			int i = this.time1.year;
			int j = this.time1.month;
			if (j == 0){
				j = 11;
				i--;
			}
			GregorianCalendar localGregorianCalendar = new GregorianCalendar(i, j - 1, 1);
			this.days += localGregorianCalendar.getActualMaximum(5);
			this.months -= 1;
		}
		if (this.months < 0){
			this.months = (12 + this.months);
			this.years -= 1;
		}
	}

	public String toString(){
		StringBuilder localStringBuilder = new StringBuilder();
		/*if (this.negative) {
      localStringBuilder.append("-");
    }*/
		int i = this.years;
		int j = 0;
		if (i > 0){
			localStringBuilder.append("<b>"+this.years+"</b>" + " year");
			j = 0 + 1;
			if (this.years > 1) {
				localStringBuilder.append("s");
			}
			localStringBuilder.append(", ");
		}
		if (this.months > 0){
			localStringBuilder.append("<b>"+this.months+"</b>" + " month");
			j++;
			if (this.months > 1) {
				localStringBuilder.append("s");
			}
			localStringBuilder.append(", ");
		}
		if ((this.days > 0) && (j < 3)){
			localStringBuilder.append("<b>"+this.days+"</b>" + " day");
			j++;
			if (this.days > 1) {
				localStringBuilder.append("s");
			}
			localStringBuilder.append(", ");
		}
		if ((this.hours > 0) && (j < 3)){
			localStringBuilder.append("<b>"+this.hours+"</b>" + " hour");
			j++;
			if (this.hours > 1) {
				localStringBuilder.append("s");
			}
			localStringBuilder.append(", ");
		}
		if ((this.minutes > 0) && (j < 3)){
			localStringBuilder.append("<b>"+this.minutes+"</b>" + " minute");
			j++;
			if (this.minutes > 1) {
				localStringBuilder.append("s");
			}
			localStringBuilder.append(", ");
		}
		if (j < 3){
			localStringBuilder.append("<b>"+this.seconds+"</b>" + " second");
			j++;
			if (this.seconds > 1) {
				localStringBuilder.append("s");
			}
			localStringBuilder.append(", ");
		}
		localStringBuilder.delete(localStringBuilder.length() - 2, localStringBuilder.length());
		if(this.negative){
			String later = " <b>later</b>";
			localStringBuilder.append(later);
		}else{
			String ago = " <b>ago</b>";
			localStringBuilder.append( ago);
		}
		return localStringBuilder.toString();
	}

	public String getReadableDateTime(){
		int mYear = time2.year;
		int mMonth = time2.month+1;
		int mDay = time2.monthDay;
		int mHour = time2.hour;
		int mMinute = time2.minute;
		String past = "Happened on: ";
		String future = "Happening on: ";
		
		SimpleDateFormat dateFormat24h = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
		SimpleDateFormat dateFormat12h = new SimpleDateFormat("dd/MM/yyyy, hh:mm aa");
		
		Date futureEvents = new Date(time1.toMillis(false));
		Date pastEvents = new Date(time2.toMillis(false));
		
		//String strDate24h = dateFormat24h.format(pastEvents);
		//String strDate12h = dateFormat12h.format(pastEvents);
		if(android.text.format.DateFormat.is24HourFormat(context)){
			if(negative){
				//return future + (mDay+"/"+mMonth+"/"+mYear+", "+String.format("%02d:%02d", mHour,mMinute));
				return future + dateFormat24h.format(futureEvents);
			}else{
				//return past + (mDay+"/"+mMonth+"/"+mYear+", "+String.format("%02d:%02d", mHour,mMinute));
				return past + dateFormat24h.format(pastEvents);
			}
		}else{
			if(negative){
				return future + dateFormat12h.format(futureEvents);
			}else{
				return past + dateFormat12h.format(pastEvents);
			}
				
		}
	}
}

