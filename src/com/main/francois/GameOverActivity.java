package com.main.francois;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class GameOverActivity extends Activity {

	private Button playAgainButton, settingsButton;
	private TextView gameOver;

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
		Typeface exo2 = Typeface.createFromAsset(getAssets(), "fonts/exo2medium.ttf");
		playAgainButton.setTypeface(exo2);
		settingsButton.setTypeface(exo2);
		gameOver.setTypeface(exo2);
		Animation slideUpIn = AnimationUtils.loadAnimation(this, R.anim.infrombottom);
		Animation slideDownIn = AnimationUtils.loadAnimation(this, R.anim.infromtop);
		playAgainButton.startAnimation(slideUpIn);
		settingsButton.startAnimation(slideUpIn);
		gameOver.startAnimation(slideDownIn);
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

	@Override
	public void onBackPressed() {
		Intent mainScreenActivityIntent = new Intent(GameOverActivity.this, MainScreenActivity.class);
		GameOverActivity.this.startActivity(mainScreenActivityIntent);
		overridePendingTransition(R.anim.lefttocenter, R.anim.centertoright);
		finish();
		super.onBackPressed();
	}

}
