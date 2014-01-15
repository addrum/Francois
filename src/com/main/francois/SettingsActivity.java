package com.main.francois;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class SettingsActivity extends Activity {

	RadioGroup colourPicker;

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

		// radio group id
		colourPicker = (RadioGroup) findViewById(R.id.colourPicker);

		// listeners
		colourPicker.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radioOrange:
					save("orange");
					Log.d("colour", "saving orange");
					break;
				case R.id.radioLilac:
					save("lilac");
					Log.d("colour", "saving lilac");
					break;
				case R.id.radioRed:
					save("red");
					Log.d("colour", "saving red");
					break;
				case R.id.radioTeal:
					save("teal");
					Log.d("colour", "saving teal");
					break;
				}
			}

		});
	}

	@Override
	public void onResume() {
		super.onResume();
		load();
	}

	// save colour choice
	private void save(final String colour) {
		SharedPreferences sharedPreferences = getSharedPreferences("colour", 0);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("colour", colour);
		editor.commit();
	}

	// load colour choice
	private void load() {
		SharedPreferences sharedPreferences = getSharedPreferences("colour", 0);
		String colour = sharedPreferences.getString("colour", null);

		if (colour == null)
			colourPicker.check(R.id.radioOrange); //Default if no preference exists
		else if (colour.equals("orange")) {
			colourPicker.check(R.id.radioOrange);
		} else if (colour.equals("lilac")) {
			colourPicker.check(R.id.radioLilac);
		} else if (colour.equals("red")) {
			colourPicker.check(R.id.radioRed);
		} else {
			colourPicker.check(R.id.radioTeal);
		}
	}

	@Override
	public void onBackPressed() {
		Intent mainScreenActivityIntent = new Intent(SettingsActivity.this, MainScreenActivity.class);
		startActivity(mainScreenActivityIntent);
		finish();
	}

}
