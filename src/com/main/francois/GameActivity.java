package com.main.francois;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.background.francois.GameLogic;

public class GameActivity extends Activity {

	public static Handler handler;
	private static final String TAG = GameActivity.class.getSimpleName();

	private WindowManager wm;
	private Display display;
	private Point size;
	private GameLogic gameLogic;
	private Animation slideDownIn;
	private LinearLayout topBar;
	private FrameLayout view;
	private TextView scoreText, timeText, countdownText;
	private CountDownTimer goTimer;
	private int screenHeight, screenWidth, score, time;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requesting to turn the title OFF
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// making it full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// set our MainGamePanel as the View
		setContentView(R.layout.game);
		Log.d(TAG, "View added");
		// set orientation to portrait
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		gameLogic = new GameLogic(this);

		// get id's
		view = (FrameLayout) findViewById(R.id.surfaceView);
		topBar = (LinearLayout) findViewById(R.id.topBar);
		scoreText = (TextView) findViewById(R.id.scoreText);
		timeText = (TextView) findViewById(R.id.timeText);
		countdownText = (TextView) findViewById(R.id.countdownText);

		// set font
		Typeface exo2 = Typeface.createFromAsset(getAssets(), "fonts/exo2medium.ttf");
		scoreText.setTypeface(exo2);
		timeText.setTypeface(exo2);
		countdownText.setTypeface(exo2);

		// set animations
		slideDownIn = AnimationUtils.loadAnimation(this, R.anim.infromtop);
		topBar.startAnimation(slideDownIn);

		// add the view to display on screen and run game logic
		view.addView(gameLogic);

		// get screen size
		wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		display = wm.getDefaultDisplay();
		size = new Point();
		display.getSize(size);
		screenHeight = size.y;
		screenWidth = size.x;

		// set padding based on 2% of screen dimensions
		scoreText.setPadding((screenWidth / 100 * 2), (screenHeight / 100 * 2), (screenWidth / 100 * 2), (screenHeight / 100 * 2));
		timeText.setPadding((screenWidth / 100 * 2), (screenHeight / 100 * 2), (screenWidth / 100 * 2), (screenHeight / 100 * 2));

		goTimer();

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				scoreText.setText("Score: " + String.valueOf(msg.arg1));
				score = msg.arg1;
				timeText.setText("Time: " + String.valueOf(msg.arg2));
				time = msg.arg2;
			}
		};

	}

	public void goTimer() {
		goTimer = new CountDownTimer(3500, 500) {

			public void onTick(long millisUntilFinished) {
				if (millisUntilFinished / 1000 == 0) {
					countdownText.setText("GO");
				} else {
					countdownText.setText("" + millisUntilFinished / 1000);
				}
			}

			public void onFinish() {
				countdownText.setVisibility(View.GONE);
				gameLogic.setPaused(false);
			}
		}.start();
	}

	// handle hardware back button
	@Override
	public void onBackPressed() {
		if (gameLogic.getReady()) {
			gameLogic.setPaused(true);
			new AlertDialog.Builder(this).setTitle("Paused").setPositiveButton(R.string.resume, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					gameLogic.setPaused(false);
				}
			}).setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Intent mainScreenActivityIntent = new Intent(GameActivity.this, MainScreenActivity.class);
					startActivity(mainScreenActivityIntent);
					overridePendingTransition(R.anim.lefttocenter, R.anim.centertoright);
					finish();
				}
			}).show().setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					gameLogic.setPaused(false);
				}
			});
		}
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "Destroying...");
		super.onDestroy();
		gameLogic.setPaused(true);
		gameLogic.getStartTimer().cancel();
		gameLogic.save(score, time);
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "Stopping...");
		super.onStop();
	}

	@Override
	public void onPause() {
		super.onPause();
		gameLogic.setPaused(true);
		gameLogic.getStartTimer().cancel();
		gameLogic.save(score, time);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (gameLogic.getPaused()) {
			countdownText.setVisibility(View.VISIBLE);
			goTimer.start();
		}
	}

}
