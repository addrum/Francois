package com.main.rocks;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;

public class SettingsActivity extends Activity {

	RadioButton playerOrangeRadio, playerLilacRadio, playerRedRadio, playerTealRadio;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requesting to turn the title OFF
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// making it full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// set layout file for this activity
		setContentView(R.layout.settings);

		// radio button id's
		playerOrangeRadio = (RadioButton) findViewById(R.id.radioOrange);
		playerLilacRadio = (RadioButton) findViewById(R.id.radioLilac);
		playerRedRadio = (RadioButton) findViewById(R.id.radioRed);
		playerTealRadio = (RadioButton) findViewById(R.id.radioTeal);
	}

	// save preferences
	private void save(final String colour) {
		if ()
			
	}

	@Override
	public void onBackPressed() {
		Intent mainScreenActivityIntent = new Intent(SettingsActivity.this, MainScreenActivity.class);
		startActivity(mainScreenActivityIntent);
		finish();
	}

}
