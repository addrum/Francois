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

	private WindowManager wm;
	private Display display;
	private Point size;
	private MainThread thread;
	private Player player;
	private ArrayList<Weight> weights = new ArrayList<Weight>();
	private boolean ready = false;
	private int screenHeight, screenWidth;
	private int score, delay, time = 0;
	private int mediumRockDelay = 15000;
	private int largeRockDelay = 30000;
	private int period = 20;
	private int smallRockPeriod = 1000;
	private int mediumRockPeriod = 4500;
	private int largeRockPeriod = 10000;
	private int second = 1000;
	private Timer timer = new Timer();
	private SharedPreferences scorePreferences, highscorePreferences;
	private Random random = new Random();

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
		if (ready == true) {
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
		} else {
			return false;
		}
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
			if (CollisionUtil.isCollisionDetected(weight.getBitmap(), weight.getX(), weight.getY(), player.getBitmap(), player.getX(), player.getY())) {
				Intent gameOverIntent = new Intent(this.getContext(), GameOverActivity.class);
				gameOverIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				this.getContext().startActivity(gameOverIntent);
				((Activity) this.getContext()).finish();
				timer.cancel();
				player.setTouched(false);
				save(score, time);
			}
		}
	}

	// initial countdown timer to stop weights from spawning in too fast at the start (3 seconds)
	public void startTimer() {
		new CountDownTimer(3000, 1000) {

			public void onTick(long millisUntilFinished) {
			}

			public void onFinish() {
				ready = true;
				// start weight spawn timers
				smallWeightTimer();
				mediumWeightTimer();
				largeWeightTimer();
				scoreTimer();
				time();
			}
		}.start();
	}

	public void smallWeightTimer() {
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				int spawnX = 0;
				int chance = random.nextInt(10);
				if (player.getX() > screenWidth / 2) {
					if (chance > 3) {
						spawnX = random.nextInt(screenWidth - (screenWidth / 2)) + (screenWidth / 2);
					} else {
						spawnX = random.nextInt((screenWidth / 2) - 0);
					}
				} else {
					if (chance > 3) {
						spawnX = random.nextInt((screenWidth / 2) - 0);
					} else {
						spawnX = random.nextInt(screenWidth - (screenWidth / 2)) + (screenWidth / 2);
					}
				}
				weights.add(new WeightSmall(BitmapFactory.decodeResource(getResources(), R.drawable.weight_s), spawnX, -10));
			}
		}, delay, smallRockPeriod);
	}

	public void mediumWeightTimer() {
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				int spawnX = 0;
				int chance = random.nextInt(10);
				if (player.getX() > screenWidth / 2) {
					if (chance > 4) {
						spawnX = random.nextInt(screenWidth - (screenWidth / 2)) + (screenWidth / 2);
					} else {
						spawnX = random.nextInt((screenWidth / 2) - 0);
					}
				} else {
					if (chance > 4) {
						spawnX = random.nextInt((screenWidth / 2) - 0);
					} else {
						spawnX = random.nextInt(screenWidth - (screenWidth / 2)) + (screenWidth / 2);
					}
				}
				weights.add(new WeightMedium(BitmapFactory.decodeResource(getResources(), R.drawable.weight_m), spawnX, -10));
			}
		}, mediumRockDelay, mediumRockPeriod);
	}

	public void largeWeightTimer() {
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				int spawnX = 0;
				int chance = random.nextInt(10);
				if (player.getX() > screenWidth / 2) {
					if (chance > 5) {
						spawnX = random.nextInt(screenWidth - (screenWidth / 2)) + (screenWidth / 2);
					} else {
						spawnX = random.nextInt((screenWidth / 2) - 0);
					}
				} else {
					if (chance > 5) {
						spawnX = random.nextInt((screenWidth / 2) - 0);
					} else {
						spawnX = random.nextInt(screenWidth - (screenWidth / 2)) + (screenWidth / 2);
					}
				}
				weights.add(new WeightLarge(BitmapFactory.decodeResource(getResources(), R.drawable.weight_l), spawnX, -10));
			}
		}, largeRockDelay, largeRockPeriod);
	}

	public void scoreTimer() {
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				if (time > 30) {
					mediumRockPeriod--;
				} else if (time > 45) {
					largeRockPeriod--;
				}
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
