package com.background.francois;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.os.CountDownTimer;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.entities.francois.Player;
import com.entities.francois.Weight;
import com.entities.francois.WeightLarge;
import com.entities.francois.WeightMedium;
import com.entities.francois.WeightSmall;
import com.main.francois.GameActivity;
import com.main.francois.GameOverActivity;
import com.main.francois.R;

public class GameLogic extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = GameLogic.class.getSimpleName();

	private MainThread thread;
	private Player player;
	private ArrayList<Weight> weights = new ArrayList<Weight>();
	private CountDownTimer gameTimer;
	private boolean start = false;
	private WindowManager wm;
	private Display display;
	private Point size;
	private int screenHeight, screenWidth;
	private double decider;
	private int score = 0;
	private Timer timer = new Timer();
	private int delay = 0;
	private int period = 20;
	private int time = 0;
	private int second = 1000;
	private SharedPreferences scorePreferences, highscorePreferences;

	public GameLogic(Context context) {
		super(context);
		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);

		// create the game loop thread
		thread = new MainThread(getHolder(), this);

		// make the GamePanel focusable so it can handle events
		setFocusable(true);

		// initial countdown to stop rocks spawning in before canvas is fully loaded
		startTimer();

		// get screen size
		wm = (WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE);
		display = wm.getDefaultDisplay();
		size = new Point();
		display.getSize(size);
		screenHeight = size.y;
		screenWidth = size.x;

		player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.player), (screenWidth / 2), (int) (screenHeight / 1.2));
	}

	public GameLogic(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
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
			Message message = Message.obtain();
			message.arg1 = score;
			message.arg2 = time;
			GameActivity.handler.sendMessage(message);
			Weight[] weightArray = weights.toArray(new Weight[0]);
			for (Weight weight : weightArray) {
				weight.draw(canvas);
			}
		}
	}

	// updates the weights position on the screen and checks collision with the player
	public void update() {
		Weight[] weightArray = weights.toArray(new Weight[0]);
		for (Weight weight : weightArray) {
			weight.update();
			if (weight.getBounds().intersect(player.getBounds())) {
				timer.cancel();
				gameTimer.cancel();
				player.setTouched(false);
				save(score, time);
				Intent gameOverIntent = new Intent(this.getContext(), GameOverActivity.class);
				gameOverIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				this.getContext().startActivity(gameOverIntent);
				((Activity) this.getContext()).finish();
			}
		}
	}

	// count down timer spawning weights in every tick
	public void spawnTimer() {
		if (start == true) {
			if (gameTimer != null) {
				gameTimer.cancel();
				gameTimer = null;
			}
			gameTimer = new CountDownTimer(30000, 800) {

				public void onTick(long millisUntilFinished) {
					weights.add(createWeight());
				}

				@Override
				public void onFinish() {
					gameTimer.start();
				}
			}.start();
		}
	}

	public Weight createWeight() {
		decider = Math.random() * 1;
		// creates rocks randomly with the lowest chance for l, and the highest chance for m
		if (decider <= 0.33) {
			// small weight
			return new WeightSmall(BitmapFactory.decodeResource(getResources(), R.drawable.weight_s), new Random().nextInt(screenWidth), -10);
		} else if (decider <= 0.5 && decider > 0.33) {
			// large weight
			return new WeightLarge(BitmapFactory.decodeResource(getResources(), R.drawable.weight_l), new Random().nextInt(screenWidth), -10);
		} else {
			// medium weight
			return new WeightMedium(BitmapFactory.decodeResource(getResources(), R.drawable.weight_m), new Random().nextInt(screenWidth), -10);
		}
	}

	// initial countdown timer to stop weights from spawning in too fast at the start (3 seconds)
	public void startTimer() {
		new CountDownTimer(3000, 1000) {

			public void onTick(long millisUntilFinished) {
			}

			public void onFinish() {
				start = true;
				// timer for spawning weights per tick
				spawnTimer();
				scoreTimer();
				time();
			}
		}.start();
	}

	public void scoreTimer() {
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				score++;
			}
		}, delay, period);
	}

	public void time() {
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				time++;
			}
		}, delay, second);
	}

	// save score choice
	private void save(int score, int time) {
		// save the current score to pass to gameover
		scorePreferences = getContext().getSharedPreferences("score", 0);
		SharedPreferences.Editor editorScore = scorePreferences.edit();
		editorScore.putInt("score", score);

		// commit all changes
		editorScore.commit();
		
		highscorePreferences = getContext().getSharedPreferences("highscore", 0);
		
		// save score and time if current score is > than current highscore and time is > than current hightime
		if (score > highscorePreferences.getInt("highscore", 0)) {			
			SharedPreferences.Editor editorHighscore = highscorePreferences.edit();
			editorHighscore.putInt("highscore", score);
			editorHighscore.commit();
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
				((Activity) this.getContext()).finish();
				retry = false;
			} catch (InterruptedException e) {
				// try again shutting down the thread
				Log.e("surfaceDestroyed", "thread couldn't shut down properly");
			}
		}
		Log.d(TAG, "Thread was shut down cleanly");
	}

}
