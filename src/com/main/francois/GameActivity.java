package com.main.francois;

import android.app.Activity;
import android.content.Context;
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

	private int screenHeight, screenWidth, score, time;
	private WindowManager wm;
	private Display display;
	private Point size;
	private GameLogic gameLogic;
	private Animation inFromTop;
	private LinearLayout topBar;
	private FrameLayout view;
	private TextView scoreText, timeText;
	
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

		// set font
		Typeface exo2 = Typeface.createFromAsset(getAssets(), "fonts/exo2medium.ttf");
		scoreText.setTypeface(exo2);
		timeText.setTypeface(exo2);

		// set animations
		/*inFromTop = AnimationUtils.loadAnimation(this, R.anim.infromtop);
		topBar.startAnimation(inFromTop);*/

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

	// handle hardware back button
	@Override
	public void onBackPressed() {
	}

	@Override
	public void onPause() {
		super.onPause();
		gameLogic.save(score, time);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "Destroying...");
		super.onDestroy();
		gameLogic.save(score, time);
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "Stopping...");
		super.onStop();
	}

}
