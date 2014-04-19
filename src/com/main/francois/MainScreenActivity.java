package com.main.francois;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;

public class MainScreenActivity extends BaseGameActivity implements View.OnClickListener {

	private long lastPress;
	private int lastScore, highscore;
	private Button playButton, settingsButton, signOutButton, leaderboardsButton;
	private SignInButton signInButton;
	private TextView title, highscoreText, highscoreValue, lastScoreText, lastScoreValue;
	private SharedPreferences scorePreferences, highscorePreferences;
	private Animation inFromBottom, inFromBottomDelayed, inFromTop, fadeIn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// requesting to turn the title OFF
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		// making it full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// set layout file for this activity
		setContentView(R.layout.mainscreen);
		// lock orientation to portrait
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// get id's
		playButton = (Button) findViewById(R.id.playButton);
		settingsButton = (Button) findViewById(R.id.settingsButton);
		leaderboardsButton = (Button) findViewById(R.id.leaderboardsButton);
		title = (TextView) findViewById(R.id.title);
		highscoreText = (TextView) findViewById(R.id.highscoreText);
		highscoreValue = (TextView) findViewById(R.id.highscoreValue);
		lastScoreText = (TextView) findViewById(R.id.lastScoreText);
		lastScoreValue = (TextView) findViewById(R.id.lastScoreValue);
		signInButton = (SignInButton) findViewById(R.id.signInButton);
		signOutButton = (Button) findViewById(R.id.signOutButton);
		signInButton.setOnClickListener(this);
		signOutButton.setOnClickListener(this);

		// set font
		Typeface exo2 = Typeface.createFromAsset(getAssets(), "fonts/exo2medium.ttf");
		playButton.setTypeface(exo2);
		leaderboardsButton.setTypeface(exo2);
		settingsButton.setTypeface(exo2);
		title.setTypeface(exo2);
		highscoreText.setTypeface(exo2);
		highscoreValue.setTypeface(exo2);
		lastScoreText.setTypeface(exo2);
		lastScoreValue.setTypeface(exo2);
		signOutButton.setTypeface(exo2);

		// set animations
		inFromBottom = AnimationUtils.loadAnimation(this, R.anim.infrombottom);
		inFromBottomDelayed = AnimationUtils.loadAnimation(this, R.anim.infrombottomdelayed);
		inFromTop = AnimationUtils.loadAnimation(this, R.anim.infromtop);
		fadeIn = new AlphaAnimation(0, 1);
		fadeIn.setInterpolator(new DecelerateInterpolator());
		fadeIn.setDuration(2500);
		playButton.startAnimation(inFromBottom);
		leaderboardsButton.startAnimation(inFromBottom);
		settingsButton.startAnimation(inFromBottom);
		title.startAnimation(inFromTop);
		lastScoreText.startAnimation(fadeIn);
		lastScoreValue.startAnimation(fadeIn);
		highscoreText.startAnimation(fadeIn);
		highscoreValue.startAnimation(fadeIn);
		signInButton.startAnimation(inFromBottomDelayed);
		signOutButton.startAnimation(inFromBottomDelayed);

		load();

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
				Intent settingsActivityIntent = new Intent(MainScreenActivity.this, SettingsActivity.class);
				MainScreenActivity.this.startActivity(settingsActivityIntent);
				overridePendingTransition(R.anim.righttocenter, R.anim.centertoleft);

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
		leaderboardsButton.startAnimation(inFromBottom);
		settingsButton.startAnimation(inFromBottom);
		title.startAnimation(inFromTop);
		signInButton.startAnimation(inFromBottomDelayed);
		signOutButton.startAnimation(inFromBottomDelayed);
		lastScoreText.startAnimation(fadeIn);
		lastScoreValue.startAnimation(fadeIn);
		highscoreText.startAnimation(fadeIn);
		highscoreValue.startAnimation(fadeIn);
	}

	@Override
	public void onSignInFailed() {
		// Sign in has failed. So show the user the sign-in button.
		findViewById(R.id.signInButton).setVisibility(View.VISIBLE);
		findViewById(R.id.signOutButton).setVisibility(View.GONE);
	}

	@Override
	public void onSignInSucceeded() {
		// show sign-out button, hide the sign-in button
		findViewById(R.id.signInButton).setVisibility(View.GONE);
		findViewById(R.id.signOutButton).setVisibility(View.VISIBLE);
		// (your code here: update UI, enable functionality that depends on sign
		// in, etc)
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.signInButton) {
			// start the asynchronous sign in flow
			beginUserInitiatedSignIn();
		} else if (view.getId() == R.id.signOutButton) {
			// sign out.
			signOut();

			// show sign-in button, hide the sign-out button
			findViewById(R.id.signInButton).setVisibility(View.VISIBLE);
			findViewById(R.id.signOutButton).setVisibility(View.GONE);
		}
	}

}
