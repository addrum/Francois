package com.main.francois;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.background.francois.GameLogic;

public class GameActivity extends Activity {

	public static Handler handler;
	
	private static final String TAG = GameActivity.class.getSimpleName();
	private GameLogic gameLogic;
	private TextView scoreText;
	private WindowManager wm;
	private Display display;
	private Point size;
	private int screenHeight, screenWidth;

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
		FrameLayout view = (FrameLayout) findViewById(R.id.surfaceView);
		scoreText = (TextView) findViewById(R.id.scoreText);
		view.addView(gameLogic);

		// get screen size
		wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		display = wm.getDefaultDisplay();
		size = new Point();
		display.getSize(size);
		screenHeight = size.y;
		screenWidth = size.x;
		
		// set padding based on 5% of screen dimensions
		scoreText.setPadding((screenWidth / 100 * 2), (screenHeight / 100 * 2), (screenWidth / 100 * 2), (screenHeight / 100 * 2));

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				scoreText.setText("Score: " + String.valueOf(msg.arg1));
			}
		};

	}

	@Override
	public void onBackPressed() {
		Intent mainScreenActivityIntent = new Intent(GameActivity.this, MainScreenActivity.class);
		startActivity(mainScreenActivityIntent);
		finish();
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "Destroying...");
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "Stopping...");
		super.onStop();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		getIntent();
	}

}
