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
import android.os.Message;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.entities.francois.Entity;
import com.entities.francois.Player;
import com.entities.francois.ScoreItem;
import com.entities.francois.WeightLarge;
import com.entities.francois.WeightMedium;
import com.entities.francois.WeightSmall;
import com.main.francois.GameActivity;
import com.main.francois.GameOverActivity;
import com.main.francois.R;

public class GameLogic extends SurfaceView implements SurfaceHolder.Callback {

	private boolean started, gameOver;
	private int screenHeight, screenWidth;
	private int score, time = 0;
	private int smallX, mediumX, largeX, scoreItemX;
	private WindowManager wm;
	private Display display;
	private Point size;
	private GameThread thread;
	private Player player;
	private ArrayList<Entity> weights = new ArrayList<Entity>();
	private ArrayList<Entity> items = new ArrayList<Entity>();
	private SharedPreferences scorePreferences, highscorePreferences, timePreferences;
	private Random random = new Random();
	private GameTimers gameTimers;

	public GameLogic(Context context) {
		super(context);
		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);

		// create the game loop thread
		thread = new GameThread(getHolder(), this);

		// make the GamePanel focusable so it can handle events
		setFocusable(true);

		// get screen size
		wm = (WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE);
		display = wm.getDefaultDisplay();
		size = new Point();
		display.getSize(size);
		screenHeight = size.y;
		screenWidth = size.x;

		gameTimers = new GameTimers(this);

		gameOver = false;

		player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.player), (screenWidth / 2), (int) (screenHeight / 1.2));
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
			}
		}
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			// the gestures
			if (player.isTouched()) {
				// the shape was picked up and is being dragged
				player.setX((int) event.getX());
				if (!started) {
					gameTimers.start();
					started = false;
				}
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
		if (!gameOver)
			if (canvas != null) {
				// draw canvas color, player, items and weights
				canvas.drawColor(Color.WHITE);
				player.draw(canvas);

				// draws items to screen
				Entity[] itemArray = items.toArray(new Entity[0]);
				for (Entity items : itemArray) {
					items.draw(canvas);
				}

				// draws weights to screen
				Entity[] weightArray = weights.toArray(new Entity[0]);
				for (Entity weight : weightArray) {
					weight.draw(canvas);
				}

				// post score and time to UI
				Message message = Message.obtain();
				message.arg1 = score;
				message.arg2 = time;
				GameActivity.handler.sendMessage(message);
			}
	}

	// updates the weights position on the screen and checks collision with the
	// player
	public void update() {
		if (!gameOver) {
			// updates all weight's positions
			Entity[] weightArray = weights.toArray(new Entity[0]);
			for (Entity weight : weightArray) {
				weight.update();

				// handles game over circumstances
				if (CollisionUtil.isCollisionDetected(weight.getBitmap(), weight.getX(), weight.getY(), player.getBitmap(), player.getX(), player.getY())) {
					Intent gameOverIntent = new Intent(this.getContext(), GameOverActivity.class);
					player.setTouched(false);
					this.getContext().startActivity(gameOverIntent);
					((Activity) this.getContext()).finish();
					save(score, time);
					try {
						gameTimers.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					gameOver = true;
				}
			}

			Entity[] itemArray = items.toArray(new Entity[0]);
			for (Entity item : itemArray) {
				item.update();

				if (CollisionUtil.isCollisionDetected(item.getBitmap(), item.getX(), item.getY(), player.getBitmap(), player.getX(), player.getY())) {
					item.destroy();
					score++;
				}
			}
		}
	}

	public void spawnEntity(int chance, String entityType) {
		if (entityType.equals("small")) {
			checkChance("smallX", chance, 45);
			weights.add(new WeightSmall(BitmapFactory.decodeResource(getResources(), R.drawable.weight_s), smallX, -10));
		} else if (entityType.equals("medium")) {
			checkChance("mediumX", chance, 50);
			weights.add(new WeightMedium(BitmapFactory.decodeResource(getResources(), R.drawable.weight_m), mediumX, -10));
		} else if (entityType.equals("large")) {
			checkChance("largeX", chance, 60);
			weights.add(new WeightLarge(BitmapFactory.decodeResource(getResources(), R.drawable.weight_l), largeX, -10));
		} else if (entityType.equals("score")) {
			checkChance("scoreItemX", chance, 50);
			items.add(new ScoreItem(BitmapFactory.decodeResource(getResources(), R.drawable.score_item), scoreItemX, -10));
		}
	}

	public void spawnSmallWeight(int chance) {
		checkChance("smallX", chance, 45);
		weights.add(new WeightSmall(BitmapFactory.decodeResource(getResources(), R.drawable.weight_s), smallX, -10));
	}

	public void spawnMediumWeight(int chance) {
		checkChance("mediumX", chance, 50);
		weights.add(new WeightMedium(BitmapFactory.decodeResource(getResources(), R.drawable.weight_m), mediumX, -10));
	}

	public void spawnLargeWeight(int chance) {
		checkChance("largeX", chance, 60);
		weights.add(new WeightLarge(BitmapFactory.decodeResource(getResources(), R.drawable.weight_l), largeX, -10));
	}

	public void spawnScoreItem(int chance) {
		checkChance("scoreItemX", chance, 50);
		items.add(new ScoreItem(BitmapFactory.decodeResource(getResources(), R.drawable.score_item), scoreItemX, -10));
	}

	public void checkChance(String spawnX, int chance, int value) {
		int intermediary;

		if (player.getX() > screenWidth / 2) {
			if (chance > value) {
				intermediary = random.nextInt(screenWidth - (screenWidth / 2)) + (screenWidth / 2);
				checkIntermediary(intermediary);
			} else {
				intermediary = random.nextInt(screenWidth / 2);
				checkIntermediary(intermediary);
			}
		} else {
			if (chance > value) {
				intermediary = random.nextInt(screenWidth / 2);
				checkIntermediary(intermediary);
			} else {
				intermediary = random.nextInt(screenWidth - (screenWidth / 2)) + (screenWidth / 2);
				checkIntermediary(intermediary);
			}
		}

		if (spawnX.equals("smallX")) {
			smallX = intermediary;
		} else if (spawnX.equals("mediumX")) {
			mediumX = intermediary;
		} else if (spawnX.equals("largeX")) {
			largeX = intermediary;
		} else if (spawnX.equals("scoreItemX")) {
			scoreItemX = intermediary;
		}
	}

	public int checkIntermediary(int intermediary) {
		if (intermediary < 0) {
			intermediary += (player.getWidth() / 2);
		} else if (intermediary > screenWidth) {
			intermediary -= (player.getWidth() / 2);
		}
		return intermediary;
	}

	// save score choice
	public void save(int score, int time) {
		// save the current score to pass to gameover
		scorePreferences = getContext().getSharedPreferences("score", 0);
		SharedPreferences.Editor editorScore = scorePreferences.edit();
		editorScore.putInt("score", score);

		timePreferences = getContext().getSharedPreferences("time", 0);
		SharedPreferences.Editor editorTime = timePreferences.edit();
		editorTime.putInt("time", time);
		
		// commit all changes
		editorScore.commit();
		editorTime.commit();

		highscorePreferences = getContext().getSharedPreferences("highscore", 0);

		// save score and time if current score is > than current highscore and
		// time is > than current hightime
		if (score > highscorePreferences.getInt("highscore", 0)) {
			SharedPreferences.Editor editorHighscore = highscorePreferences.edit();
			editorHighscore.putInt("highscore", score);
			editorHighscore.commit();
		}

	}

	// thread and surface
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!thread.isRunning()) {
			// at this point the surface is created and we can safely start the
			// game loop
			thread.setRunning(true);
			thread.start();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// set screen width equal to physical device screen width
		screenWidth = size.x;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}

	public Player getPlayer() {
		return player;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

}
