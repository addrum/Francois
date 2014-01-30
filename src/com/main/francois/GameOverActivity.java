package com.main.francois;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
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

public class GameOverActivity extends Activity {

	private RelativeLayout mainLayout;
	private Button playAgainButton, settingsButton;
	private TextView gameOver, scoreText, scoreValue, highscoreText, highscoreValue;
	private int score, highscore, incrementScore;
	private int delay = 0;
	private int period = 20;
	private Animation slideUpIn, slideDownIn, fadeIn;
	private SharedPreferences scorePreferences, highscorePreferences, themePreferences;
	private Timer timer = new Timer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requesting to turn the title OFF
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// making it full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// set layout file for this activity
		setContentView(R.layout.gameover);
		// lock orientation to portrait
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// get id's
		mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
		playAgainButton = (Button) findViewById(R.id.playAgainButton);
		settingsButton = (Button) findViewById(R.id.settingsButton);
		gameOver = (TextView) findViewById(R.id.gameOver);
		scoreText = (TextView) findViewById(R.id.scoreText);
		scoreValue = (TextView) findViewById(R.id.scoreValue);
		highscoreText = (TextView) findViewById(R.id.highscoreText);
		highscoreValue = (TextView) findViewById(R.id.highscoreValue);

		// set font
		Typeface exo2 = Typeface.createFromAsset(getAssets(), "fonts/exo2medium.ttf");
		playAgainButton.setTypeface(exo2);
		settingsButton.setTypeface(exo2);
		gameOver.setTypeface(exo2);
		scoreText.setTypeface(exo2);
		scoreValue.setTypeface(exo2);
		highscoreText.setTypeface(exo2);
		highscoreValue.setTypeface(exo2);

		// set animations
		slideUpIn = AnimationUtils.loadAnimation(this, R.anim.infrombottom);
		slideDownIn = AnimationUtils.loadAnimation(this, R.anim.infromtop);
		fadeIn = new AlphaAnimation(0, 1);
		fadeIn.setInterpolator(new DecelerateInterpolator());
		fadeIn.setDuration(2500);
		playAgainButton.startAnimation(slideUpIn);
		settingsButton.startAnimation(slideUpIn);
		gameOver.startAnimation(slideDownIn);
		scoreText.startAnimation(fadeIn);
		scoreValue.startAnimation(fadeIn);
		highscoreText.startAnimation(fadeIn);
		highscoreValue.startAnimation(fadeIn);

		// get score
		load();

		// button listeners
		playAgainButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent gameActivityIntent = new Intent(GameOverActivity.this, GameActivity.class);
				GameOverActivity.this.startActivity(gameActivityIntent);
				overridePendingTransition(R.anim.righttocenter, R.anim.centertoleft);
				finish();
			}

		});

		settingsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent settingsActivityIntent = new Intent(GameOverActivity.this, SettingsActivity.class);
				GameOverActivity.this.startActivity(settingsActivityIntent);
				overridePendingTransition(R.anim.righttocenter, R.anim.centertoleft);
			}

		});

	}

	// load score from last session
	private void load() {
		// get score and set text field
		scorePreferences = getSharedPreferences("score", 0);
		score = scorePreferences.getInt("score", 0);
		scoreValue.setText(String.valueOf(score));

		// get highscore and set text field
		highscorePreferences = getSharedPreferences("highscore", 0);
		highscore = highscorePreferences.getInt("highscore", 0);
		highscoreValue.setText(Integer.toString(highscore));

		// get theme prefs
		themePreferences = getSharedPreferences("theme", 0);
		boolean theme = themePreferences.getBoolean("theme", false);
		if (theme == true) {
			mainLayout.setBackgroundColor(Color.BLACK);
			settingsButton.setBackgroundColor(Color.BLACK);
			settingsButton.setTextColor(Color.WHITE);
			highscoreText.setTextColor(Color.WHITE);
			highscoreValue.setTextColor(Color.WHITE);
			scoreText.setTextColor(Color.WHITE);
			scoreValue.setTextColor(Color.WHITE);
		}

	}

	// handle hardware back button
	@Override
	public void onBackPressed() {
		Intent mainScreenActivityIntent = new Intent(GameOverActivity.this, MainScreenActivity.class);
		GameOverActivity.this.startActivity(mainScreenActivityIntent);
		overridePendingTransition(R.anim.lefttocenter, R.anim.centertoright);
		finish();
	}

	@Override
	public void onResume() {
		super.onResume();
		playAgainButton.startAnimation(slideUpIn);
		settingsButton.startAnimation(slideUpIn);
		gameOver.startAnimation(slideDownIn);
	}

}
