package com.jerry.eventful;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutActivity extends ActionBarActivity implements AnimationListener {

    private Toolbar toolbar;
	
	private String PACKAGE_NAME;
	private Animation animFadeIn;
	private ImageView appIcon;
    private TextView mVersion;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		PACKAGE_NAME = this.getPackageName();
        setTheme();
	    
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_activity);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		appIcon = (ImageView)findViewById(R.id.imageView1);
	    animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
	    animFadeIn.setAnimationListener(this);
	    appIcon.setVisibility(View.VISIBLE);
		appIcon.startAnimation(animFadeIn);

        mVersion = (TextView) findViewById(R.id.version);
        try {
            String versionNumber = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            mVersion.setText(versionNumber);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
	}

    private void setTheme() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if(pref.getBoolean("darktheme", false)==true){
            setTheme(R.style.AppDarkTheme);
        }else{
            setTheme(R.style.AppTheme);
        }
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
		
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		
	}

}
