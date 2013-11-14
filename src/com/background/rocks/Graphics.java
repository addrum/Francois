package com.background.rocks;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.characters.rocks.Player;
import com.characters.rocks.Rock;
import com.main.rocks.R;

public class Graphics extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = Graphics.class.getSimpleName();

	private MainThread thread;
	private Player player;
	private ArrayList<Rock> rocks = new ArrayList<Rock>();
	private Random random = new Random();
	private CountDownTimer countdown;

	public Graphics(Context context) {
		super(context);
		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);

		// create shape and load bitmap
		player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.player_orange), 540, 1500);

		// create the game loop thread
		thread = new MainThread(getHolder(), this);

		// make the GamePanel focusable so it can handle events
		setFocusable(true);

		timer();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// at this point the surface is created and
		// we can safely start the game loop
		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");
		// tell the thread to shut down and wait for it to finish
		// this is a clean shutdown
		boolean retry = true;
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// try again shutting down the thread
			}
		}
		Log.d(TAG, "Thread was shut down cleanly");
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
		canvas.drawColor(Color.WHITE);
		player.draw(canvas);
		Rock[] rockArray = rocks.toArray(new Rock[0]);
		for (Rock rock : rocks) {
			rock.draw(canvas);
		}
	}

	/**
	 * This is the game update method. It iterates through all the objects and
	 * calls their update method if they have one or calls specific engine's
	 * update method.
	 */
	public void update() {
		Rock[] rockArray = rocks.toArray(new Rock[0]);
		for (Rock rock : rocks) {
			rock.update();
		}
	}

	public void timer() {
		if (countdown != null) {
			countdown.cancel();
		}
		countdown = new CountDownTimer(30000, 800) {

			public void onTick(long millisUntilFinished) {
				rocks.add(new Rock(BitmapFactory.decodeResource(getResources(), R.drawable.rock), random.nextInt(1080 - 1) + 1, 0));
			}

			public void onFinish() {
				countdown.start();
			}
		}.start();
	}

}
