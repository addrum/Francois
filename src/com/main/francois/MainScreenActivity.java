package com.main.francois;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainScreenActivity extends Activity {

	private Button playButton, settingsButton;
	private TextView title;
	private long lastPress;
	private Animation slideUpIn, slideDownIn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requesting to turn the title OFF
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// making it full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// set layout file for this activity
		setContentView(R.layout.mainscreen);
		// lock orientation to portrait
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// get id's
		playButton = (Button) findViewById(R.id.playButton);
		settingsButton = (Button) findViewById(R.id.settingsButton);
		title = (TextView) findViewById(R.id.title);

		// set font
		Typeface exo2 = Typeface.createFromAsset(getAssets(), "fonts/exo2medium.ttf");
		playButton.setTypeface(exo2);
		settingsButton.setTypeface(exo2);
		title.setTypeface(exo2);

		// set animations
		slideUpIn = AnimationUtils.loadAnimation(this, R.anim.infrombottom);
		slideDownIn = AnimationUtils.loadAnimation(this, R.anim.infromtop);
		playButton.startAnimation(slideUpIn);
		settingsButton.startAnimation(slideUpIn);
		title.startAnimation(slideDownIn);

		// button listeners
		playButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent gameActivityIntent = new Intent(MainScreenActivity.this, GameActivity.class);
				startActivity(gameActivityIntent);
				overridePendingTransition(R.anim.righttocenter, R.anim.centertoleft);
				finish();
			}

		});

		settingsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent settingsActivityIntent = new Intent(MainScreenActivity.this, SettingsActivity.class);
				MainScreenActivity.this.startActivity(settingsActivityIntent);
				overridePendingTransition(R.anim.righttocenter, R.anim.centertoleft);
			}

		});
	}

	// handle hardware back button
	@Override
	public void onBackPressed() {
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastPress > 2000) {
			Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
			lastPress = currentTime;
		} else {
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		playButton.startAnimation(slideUpIn);
		settingsButton.startAnimation(slideUpIn);
		title.startAnimation(slideDownIn);
	}

}
