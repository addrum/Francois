package com.main.francois;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;

public class GameOverActivity extends BaseGameActivity {

	private long lastPress;
	private int score, highscore;
	private Button playAgainButton, leaderboardsButton, settingsButton;
	private TextView gameOver, scoreText, scoreValue, highscoreText, highscoreValue;
	private Animation inFromTop, inFromBottom, fadeIn;
	private SharedPreferences scorePreferences, highscorePreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setRequestedClients(BaseGameActivity.CLIENT_GAMES);
		// requesting to turn the title OFF
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// set requested clients (games and cloud save)
		setRequestedClients(BaseGameActivity.CLIENT_GAMES);
		super.onCreate(savedInstanceState);
		// making it full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// set layout file for this activity
		setContentView(R.layout.gameover);
		// lock orientation to portrait
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// get id's
		playAgainButton = (Button) findViewById(R.id.playButton);
		leaderboardsButton = (Button) findViewById(R.id.leaderboardsButton);
		settingsButton = (Button) findViewById(R.id.settingsButton);
		gameOver = (TextView) findViewById(R.id.gameOver);
		scoreText = (TextView) findViewById(R.id.scoreText);
		scoreValue = (TextView) findViewById(R.id.scoreValue);
		highscoreText = (TextView) findViewById(R.id.highscoreText);
		highscoreValue = (TextView) findViewById(R.id.highscoreValue);

		// set font
		Typeface exo2 = Typeface.createFromAsset(getAssets(), "fonts/exo2medium.ttf");
		playAgainButton.setTypeface(exo2);
		leaderboardsButton.setTypeface(exo2);
		settingsButton.setTypeface(exo2);
		gameOver.setTypeface(exo2);
		scoreText.setTypeface(exo2);
		scoreValue.setTypeface(exo2);
		highscoreText.setTypeface(exo2);
		highscoreValue.setTypeface(exo2);

		// set animations
		inFromBottom = AnimationUtils.loadAnimation(this, R.anim.infrombottom);
		inFromTop = AnimationUtils.loadAnimation(this, R.anim.infromtop);
		fadeIn = new AlphaAnimation(0, 1);
		fadeIn.setInterpolator(new DecelerateInterpolator());
		fadeIn.setDuration(4000);
		playAgainButton.startAnimation(inFromBottom);
		leaderboardsButton.startAnimation(inFromBottom);
		settingsButton.startAnimation(inFromBottom);
		gameOver.startAnimation(inFromTop);
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

		leaderboardsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (isSignedIn())
					startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient(), "CgkIkNf1ofsQEAIQAQ"), 0);
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
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onSignInSucceeded() {
		if (isSignedIn()) {
			Games.Leaderboards.submitScore(getApiClient(), "CgkIkNf1ofsQEAIQAQ", score);
		} else {
			Log.d("not signed in", "Not signed in to submit score");
		}
	}

	@Override
	public void onSignInFailed() {
		// TODO Auto-generated method stub

	}

}
