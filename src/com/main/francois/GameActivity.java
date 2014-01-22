package com.main.francois;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.background.francois.GameLogic;

public class GameActivity extends Activity {

	private static final String TAG = GameActivity.class.getSimpleName();
	private GameLogic gameLogic;

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
		view.addView(gameLogic);
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
