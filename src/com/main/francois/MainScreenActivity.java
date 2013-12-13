package com.main.francois;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import com.main.francois.R;

public class MainScreenActivity extends Activity {

	Button playButton, settingsButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requesting to turn the title OFF
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// making it full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// set layout file for this activity
		setContentView(R.layout.mainscreen);
		// lock orientation to portrait
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		// get id's
		playButton = (Button) findViewById(R.id.playButton);
		settingsButton = (Button) findViewById(R.id.settingsButton); 
				
		// button listeners
		playButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent gameActivityIntent = new Intent(MainScreenActivity.this, GameActivity.class);
				MainScreenActivity.this.startActivity(gameActivityIntent);
			}

		});
		// borked for some retarded reason
		settingsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent settingsActivityIntent = new Intent(MainScreenActivity.this, SettingsActivity.class);
				MainScreenActivity.this.startActivity(settingsActivityIntent);
				finish();
			}
			
		});
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
	}

}
