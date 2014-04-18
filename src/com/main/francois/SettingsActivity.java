package com.main.francois;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SettingsActivity extends Activity {

	private boolean clear = false;
	private RelativeLayout mainLayout;
	private TextView title, clearText;
	private ToggleButton clearButton;
	private Button save;
	private Animation inFromTop, inFromBottom;
	private SharedPreferences sharedPrefences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requesting to turn the title OFF
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// making it full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// set layout file for this activity
		setContentView(R.layout.settings);
		// lock orientation to portrait
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// get id's
		mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
		title = (TextView) findViewById(R.id.settings);
		clearText = (TextView) findViewById(R.id.clear);
		clearButton = (ToggleButton) findViewById(R.id.clearButton);
		save = (Button) findViewById(R.id.saveButton);

		// set font
		Typeface exo2 = Typeface.createFromAsset(getAssets(), "fonts/exo2medium.ttf");
		title.setTypeface(exo2);
		clearText.setTypeface(exo2);
		clearButton.setTypeface(exo2);
		save.setTypeface(exo2);

		// set animations
		inFromTop = AnimationUtils.loadAnimation(this, R.anim.infromtop);
		inFromBottom = AnimationUtils.loadAnimation(this, R.anim.infrombottom);
		title.startAnimation(inFromTop);
		save.startAnimation(inFromBottom);
		
		// button listeners
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (clear) {

					// reset last score to 0
					sharedPrefences = getSharedPreferences("score", 0);
					SharedPreferences.Editor editorScore = sharedPrefences.edit();
					editorScore.putInt("score", 0);

					// reset high score to 0
					sharedPrefences = getSharedPreferences("highscore", 0);
					SharedPreferences.Editor editorHighscore = sharedPrefences.edit();
					editorHighscore.putInt("highscore", 0);

					// commit all changes
					editorScore.commit();
					editorHighscore.commit();
				}

				Intent mainScreenActivityIntent = new Intent(SettingsActivity.this, MainScreenActivity.class);
				SettingsActivity.this.startActivity(mainScreenActivityIntent);
				overridePendingTransition(R.anim.lefttocenter, R.anim.centertoright);
				finish();
			}

		});

		clearButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					clear = true;
				} else {
					clear = false;
				}
			}

		});
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	// handle hardware back button
	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.lefttocenter, R.anim.centertoright);
	}

}
