package com.jerry.eventful;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class AboutActivity extends Activity implements AnimationListener {
	
	private String PACKAGE_NAME;
	Animation animFadeIn;
	ImageView appIcon;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		PACKAGE_NAME = this.getPackageName();
		SharedPreferences pref = this.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
		if(pref.getBoolean("darktheme", false)==true){
			setTheme(R.style.AppDarkTheme);
		}else{
			setTheme(R.style.AppTheme);
		}
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_activity);
		
		appIcon = (ImageView)findViewById(R.id.imageView1);
	    animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
	    animFadeIn.setAnimationListener(this);
	    appIcon.setVisibility(View.VISIBLE);
		appIcon.startAnimation(animFadeIn);
		
		
		
	}
	
	public void sendEmail(View v){
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/html");
		i.putExtra(Intent.EXTRA_EMAIL, "limzming@gmail.com");
		i.putExtra(Intent.EXTRA_SUBJECT, "Reboot Tool");
		i.putExtra(Intent.EXTRA_TEXT, "Feedback");

		startActivity(Intent.createChooser(i, "Send Email"));
	}
	
	@Override
	public void onBackPressed(){
		finish();
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}

}
