package com.main.francois;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
	private int score, highscore, time;
	private Button playAgainButton, leaderboardsButton, achievementsButton;
	private TextView gameOver, scoreText, scoreValue, highscoreText, highscoreValue;
	private Animation inFromTop, inFromBottom, fadeIn;
	private SharedPreferences scorePreferences, highscorePreferences, timePreferences;
	AlertDialog.Builder alertDialogBuilder;

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
		achievementsButton = (Button) findViewById(R.id.achievementsButton);
		gameOver = (TextView) findViewById(R.id.gameOver);
		scoreText = (TextView) findViewById(R.id.scoreText);
		scoreValue = (TextView) findViewById(R.id.scoreValue);
		highscoreText = (TextView) findViewById(R.id.highscoreText);
		highscoreValue = (TextView) findViewById(R.id.highscoreValue);

		// set font
		Typeface exo2 = Typeface.createFromAsset(getAssets(), "fonts/exo2medium.ttf");
		playAgainButton.setTypeface(exo2);
		leaderboardsButton.setTypeface(exo2);
		achievementsButton.setTypeface(exo2);
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
		achievementsButton.startAnimation(inFromBottom);
		gameOver.startAnimation(inFromTop);
		scoreText.startAnimation(fadeIn);
		scoreValue.startAnimation(fadeIn);
		highscoreText.startAnimation(fadeIn);
		highscoreValue.startAnimation(fadeIn);

		alertDialogBuilder = new AlertDialog.Builder(this);
		
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
				startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()), 1);
			}

		});

	}

	// load score from last session
	private void load() {
		// get score and set text field
		scorePreferences = getSharedPreferences("score", 0);
		score = scorePreferences.getInt("score", 0);
		scoreValue.setText(String.valueOf(score));

		timePreferences = getSharedPreferences("time", 0);
		time = timePreferences.getInt("time", 0);

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
			Games.Leaderboards.submitScore(getApiClient(), getString(R.string.score_leaderboard), score);
			Games.Leaderboards.submitScore(getApiClient(), getString(R.string.time_leaderboard), time);
			if (score >= 5)
				Games.Achievements.unlock(getApiClient(), getString(R.string.warming_up_achievement));
			if (score >= 10)
				Games.Achievements.unlock(getApiClient(), getString(R.string.natural_achievement));
			if (time >= 30)
				Games.Achievements.unlock(getApiClient(), getString(R.string.novice_evader_achievement));
			if (time >= 60) {
				Games.Achievements.unlock(getApiClient(), getString(R.string.evader_achievement));
				if (score == 0)
					Games.Achievements.unlock(getApiClient(), getString(R.string.score_means_nothing_achievement));
			}
		} else {
			Log.d("not signed in", "Not signed in to submit score");
		}
	}

	@Override
	public void onSignInFailed() {
		// TODO Auto-generated method stub

	}

}
