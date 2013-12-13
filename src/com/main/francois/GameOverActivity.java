package com.main.francois;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.main.francois.R;

public class GameOverActivity extends Activity {

	Button playButton, backButton;

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
		playButton = (Button) findViewById(R.id.playButton);
		backButton = (Button) findViewById(R.id.backButton);

		// button listeners
		playButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent gameActivityIntent = new Intent(GameOverActivity.this, GameActivity.class);
				GameOverActivity.this.startActivity(gameActivityIntent);
				finish();
			}

		});
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent mainScreenActivityIntent = new Intent(GameOverActivity.this, MainScreenActivity.class);
				startActivity(mainScreenActivityIntent);
				finish();
			}

		});
	}

	@Override
	public void onBackPressed() {
		Intent mainScreenActivityIntent = new Intent(GameOverActivity.this, MainScreenActivity.class);
		startActivity(mainScreenActivityIntent);
		finish();
	}

}
