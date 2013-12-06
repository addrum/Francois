package com.background.francois;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.characters.francois.Player;
import com.characters.francois.Weight;
import com.main.francois.GameOverActivity;
import com.main.francois.R;

public class Graphics extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = Graphics.class.getSimpleName();

	private MainThread thread;
	private Player player;
	private ArrayList<Weight> rocks = new ArrayList<Weight>();
	private CountDownTimer weightSpawnTimer;
	private boolean start = false;
	private int maxWeights = 10;
	private int weightsOnScreen = 0;
	private WindowManager wm;
	private Display display;
	private Point size;
	private int screenHeight, screenWidth;
	private Random random;

	public Graphics(Context context) {
		super(context);
		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);

		// get player colour based on preferences and create an instance of player

		player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.player), (screenWidth), 1500);

		// create the game loop thread
		thread = new MainThread(getHolder(), this);

		// make the GamePanel focusable so it can handle events
		setFocusable(true);

		// initial countdown to stop rocks spawning
		startTimer();

		// get screen size
		wm = (WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE);
		display = wm.getDefaultDisplay();
		size = new Point();
		display.getSize(size);
		screenHeight = size.y;
		screenWidth = size.x;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// delegating event handling to the shape
			player.handleActionDown((int) event.getX(), (int) event.getY());

			// check if in the lower part of the screen we exit
			if (event.getY() > getHeight() - 50) {
				thread.setRunning(false);
				((Activity) getContext()).finish();
			} else {
				Log.d(TAG, "Coords: x=" + event.getX() + ",y=" + event.getY());
			}
		}
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			// the gestures
			if (player.isTouched()) {
				// the shape was picked up and is being dragged
				player.setX((int) event.getX());
				player.setY((int) event.getY());
			}
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			// touch was released
			if (player.isTouched()) {
				player.setTouched(false);
			}
		}
		return true;
	}

	public void render(Canvas canvas) {
		if (canvas != null) {
			canvas.drawColor(Color.WHITE);
			player.draw(canvas);
			Weight[] rockArray = rocks.toArray(new Weight[0]);
			for (Weight rock : rockArray) {
				rock.draw(canvas);
			}
		}
	}

	// updates the rocks' position on the screen and checks collision with the player
	public void update() {
		Weight[] rockArray = rocks.toArray(new Weight[0]);
		for (Weight rock : rockArray) {
			rock.update();
			if (rock.getBounds().intersect(player.getBounds())) {
				player.setTouched(false);
				Intent gameOverIntent = new Intent(this.getContext(), GameOverActivity.class);
				this.getContext().startActivity(gameOverIntent);
				((Activity) getContext()).finish();
			}
			if (rock.getY() > screenHeight) {
				weightsOnScreen--;
				//Log.d("rocksOnScreen", Integer.toString(rocksOnScreen));
			}
		}
	}

	// count down timer spawning rocks in every tick
	public void timer() {
		if (start == true) {
			if (weightSpawnTimer != null) {
				weightSpawnTimer.cancel();
				weightSpawnTimer = null;
			}
			weightSpawnTimer = new CountDownTimer(30000, 800) {

				public void onTick(long millisUntilFinished) {
					// limits number of rocks on screen
					if (weightsOnScreen < maxWeights) {
						// if (rock exists on y axis and between rock width)
						// set speed to less than the one currently in the "column"
						// so that they do not overlap else create new one
						rocks.add(createWeight());
						weightsOnScreen++;
						//Log.d("rocksOnScreen", Integer.toString(rocksOnScreen));
					}
				}

				public void onFinish() {
					weightSpawnTimer.start();
				}
			}.start();
		}
	}

	// initial countdown timer to stop rocks from spawning in before canvas is fully loaded
	public void startTimer() {
		new CountDownTimer(1000, 1000) {

			public void onTick(long millisUntilFinished) {

			}

			public void onFinish() {
				start = true;
				// timer for spawning rocks per tick
				timer();
			}
		}.start();
	}

	// check preferences as to which colour to use
	public void getPlayerColour() {
		SharedPreferences sharedPreferences = this.getContext().getSharedPreferences("colour", 0);
		if (sharedPreferences.getString("colour", null).equals("orange")) {
			player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.player_orange), (screenWidth), 1500);
			Log.d("screenwidth", Integer.toString(screenWidth));
		} else if (sharedPreferences.getString("colour", null).equals("lilac")) {
			player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.player_lilac), (screenWidth), 1500);
		} else if (sharedPreferences.getString("colour", null).equals("red")) {
			player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.player_red), (screenWidth), 1500);
		} else {
			player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.player_teal), (screenWidth), 1500);
		}
	}

	// thread and surface
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// at this point the surface is created and we can safely start the game loop
		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		screenWidth = size.x;
		Log.d("screenwidth", Integer.toString(screenWidth));
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");
		// tell the thread to shut down and wait for it to finish this is a clean shutdown
		boolean retry = true;
		while (retry) {
			try {
				thread.setRunning(false);
				thread.join();
				((Activity) getContext()).finish();
				retry = false;
			} catch (InterruptedException e) {
				// try again shutting down the thread
				Log.e("surfaceDestroyed", "thread couldn't shut down properly");
			}
		}
		Log.d(TAG, "Thread was shut down cleanly");
	}

	public Weight createWeight() {
		random = new Random();
		int decider = random.nextInt(1);
		if (decider <= 0.33) {
			return new Weight(BitmapFactory.decodeResource(getResources(), R.drawable.weight_s), new Random().nextInt(screenWidth), 0);
		} else if (decider <= 0.5 && decider > 0.33) {
			return new Weight(BitmapFactory.decodeResource(getResources(), R.drawable.weight_l), new Random().nextInt(screenWidth), 0);
		} else {
			return new Weight(BitmapFactory.decodeResource(getResources(), R.drawable.weight_m), new Random().nextInt(screenWidth), 0);
		}
	}

}