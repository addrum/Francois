package com.main.francois;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

public class GameOverActivity extends Activity {

	private Button playAgainButton, settingsButton;
	private TextView gameOver, scoreText, scoreValue, highscoreText, highscoreValue;
	private int score, highscore;
	private Animation slideUpIn, slideDownIn, fadeIn;
	private SharedPreferences scorePreferences, highscorePreferences;

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
		fadeIn.setDuration(1000);
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
				finish();
			}

		});

	}

	// load score from last session
	private void load() {
		// get score and set text field
		scorePreferences = getSharedPreferences("score", 0);
		score = scorePreferences.getInt("score", 0);
		scoreValue.setText(Integer.toString(score));

		// get highscore and set text field
		highscorePreferences = getSharedPreferences("highscore", 0);
		highscore = highscorePreferences.getInt("highscore", 0);
		highscoreValue.setText(Integer.toString(highscore));
	}

	// handle hardware back button
	@Override
	public void onBackPressed() {
		Intent mainScreenActivityIntent = new Intent(GameOverActivity.this, MainScreenActivity.class);
		GameOverActivity.this.startActivity(mainScreenActivityIntent);
		overridePendingTransition(R.anim.lefttocenter, R.anim.centertoright);
		finish();
	}

}
