package com.main.francois;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainScreenActivity extends Activity {

	private long lastPress;
	private int lastScore, highscore;
	private RelativeLayout mainLayout;
	private Button playButton, settingsButton;
	private TextView title, highscoreText, highscoreValue, lastScoreText, lastScoreValue;
	private SharedPreferences scorePreferences, highscorePreferences;
	private Animation inFromBottom, inFromTop, fadeIn;

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
		mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
		playButton = (Button) findViewById(R.id.playButton);
		settingsButton = (Button) findViewById(R.id.settingsButton);
		title = (TextView) findViewById(R.id.title);
		highscoreText = (TextView) findViewById(R.id.highscoreText);
		highscoreValue = (TextView) findViewById(R.id.highscoreValue);
		lastScoreText = (TextView) findViewById(R.id.lastScore);
		lastScoreValue = (TextView) findViewById(R.id.lastScoreValue);

		// set font
		Typeface exo2 = Typeface.createFromAsset(getAssets(), "fonts/exo2medium.ttf");
		playButton.setTypeface(exo2);
		settingsButton.setTypeface(exo2);
		title.setTypeface(exo2);
		highscoreText.setTypeface(exo2);
		highscoreValue.setTypeface(exo2);
		lastScoreText.setTypeface(exo2);
		lastScoreValue.setTypeface(exo2);

		// set animations
		inFromBottom = AnimationUtils.loadAnimation(this, R.anim.infrombottom);
		inFromTop = AnimationUtils.loadAnimation(this, R.anim.infromtop);
		fadeIn = new AlphaAnimation(0, 1);
		fadeIn.setInterpolator(new DecelerateInterpolator());
		fadeIn.setDuration(2500);
		playButton.startAnimation(inFromBottom);
		settingsButton.startAnimation(inFromBottom);
		title.startAnimation(inFromTop);
		lastScoreText.startAnimation(fadeIn);
		lastScoreValue.startAnimation(fadeIn);
		highscoreText.startAnimation(fadeIn);
		highscoreValue.startAnimation(fadeIn);

		load();

		// button listeners
		playButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent gameActivityIntent = new Intent(MainScreenActivity.this, GameActivity.class);
				startActivity(gameActivityIntent);
				//overridePendingTransition(R.anim.righttocenter, R.anim.centertoleft);
				finish();
			}

		});

		settingsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent settingsActivityIntent = new Intent(MainScreenActivity.this, SettingsActivity.class);
				MainScreenActivity.this.startActivity(settingsActivityIntent);
				//overridePendingTransition(R.anim.righttocenter, R.anim.centertoleft);
			}

		});
	}

	// load score from last session
	private void load() {

		// get score and set text field
		scorePreferences = getSharedPreferences("score", 0);
		lastScore = scorePreferences.getInt("score", 0);
		if (lastScore == 0) {
			lastScoreValue.setText("No score set.");
		} else {
			lastScoreValue.setText(Integer.toString(lastScore));
		}

		// get highscore and set text field
		highscorePreferences = getSharedPreferences("highscore", 0);
		highscore = highscorePreferences.getInt("highscore", 0);
		if (highscore == 0) {
			highscoreValue.setText("No score set.");
		} else {
			highscoreValue.setText(Integer.toString(highscore));
		}
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
		playButton.startAnimation(inFromBottom);
		settingsButton.startAnimation(inFromBottom);
		title.startAnimation(inFromTop);
	}

}
