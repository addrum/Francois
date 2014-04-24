package com.main.francois;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;

public class MainScreenActivity extends BaseGameActivity {

	private static final String AD_UNIT_ID = "ca-app-pub-6066342211060091/8064908168";
	private long lastPress;
	private int lastScore, highscore;
	private RelativeLayout mainLayout;
	private Button playButton, achievementsButton, leaderboardsButton;
	private TextView title, highscoreText, highscoreValue, lastScoreText, lastScoreValue;
	private SharedPreferences scorePreferences, highscorePreferences;
	private Animation inFromBottom, inFromTop, fadeIn;
	private AdView adView;
	AlertDialog.Builder alertDialogBuilder;

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

		// Create an ad.
		adView = new AdView(this);
		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId(AD_UNIT_ID);

		// get id's
		mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
		mainLayout.addView(adView);
		playButton = (Button) findViewById(R.id.playButton);
		achievementsButton = (Button) findViewById(R.id.achievementsButton);
		leaderboardsButton = (Button) findViewById(R.id.leaderboardsButton);
		title = (TextView) findViewById(R.id.title);
		highscoreText = (TextView) findViewById(R.id.highscoreText);
		highscoreValue = (TextView) findViewById(R.id.highscoreValue);
		lastScoreText = (TextView) findViewById(R.id.lastScoreText);
		lastScoreValue = (TextView) findViewById(R.id.lastScoreValue);

		// Create an ad request. Check logcat output for the hashed device ID to
		// get test ads on a physical device.
		AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("96EF0C9ECBC8FA36DB5DFC5AC5B5FA2C41BC7A7D").build();

		// Start loading the ad in the background.
		adView.loadAd(adRequest);

		// set font
		Typeface exo2 = Typeface.createFromAsset(getAssets(), "fonts/exo2medium.ttf");
		playButton.setTypeface(exo2);
		leaderboardsButton.setTypeface(exo2);
		achievementsButton.setTypeface(exo2);
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
		fadeIn.setDuration(4000);
		playButton.startAnimation(inFromBottom);
		leaderboardsButton.startAnimation(inFromBottom);
		achievementsButton.startAnimation(inFromBottom);
		title.startAnimation(inFromTop);
		adView.startAnimation(inFromTop);
		lastScoreText.startAnimation(fadeIn);
		lastScoreValue.startAnimation(fadeIn);
		highscoreText.startAnimation(fadeIn);
		highscoreValue.startAnimation(fadeIn);

		alertDialogBuilder = new AlertDialog.Builder(this);

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
				if (isSignedIn()) {
					alertDialogBuilder.setTitle(getString(R.string.leaderboard_title));
					alertDialogBuilder.setMessage("Click outside the box to close.").setCancelable(true).setPositiveButton(getString(R.string.time_option), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient(), getString(R.string.time_leaderboard)), 0);
						}
					}).setNegativeButton(getString(R.string.score_option), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient(), getString(R.string.score_leaderboard)), 0);
						}
					});

					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
					// show it
					alertDialog.show();
				} else {
					beginUserInitiatedSignIn();
				}
			}

		});

		achievementsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isSignedIn()) {
					startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()), 1);
				} else {
					beginUserInitiatedSignIn();
				}
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
	
	@Override
	public void onSignInFailed() {
	}

	@Override
	public void onSignInSucceeded() {
		// (your code here: update UI, enable functionality that depends on sign
		// in, etc)
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
	protected void onPause() {
		if (adView != null) {
			adView.pause();
		}
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (adView != null) {
			adView.resume();
		}
	}

	@Override
	protected void onDestroy() {
		if (adView != null) {
			adView.destroy();
		}
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

}
