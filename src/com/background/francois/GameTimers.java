package com.background.francois;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.CountDownTimer;

public class GameTimers extends Thread {

	private Thread thread;
	private Context context;
	private GameLogic gameLogic;
	private CountDownTimer goTimer;
	private Random random;
	
	public GameTimers(Context context, GameLogic gameLogic) {
		this.context = context;
		this.gameLogic = gameLogic;
	}
	
	@Override
	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	@Override
	public void run() {
		smallRockTimer();
		mediumRockTimer();
		largeRockTimer();
	}
	
	public void smallRockTimer() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				gameLogic.spawnEntity(getChance(), "small");
			}
		}, 0, 1000);
	}
	
	public void mediumRockTimer() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				gameLogic.spawnEntity(getChance(), "medium");
			}
		}, 0, 2000);
	}
	
	public void largeRockTimer() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				gameLogic.spawnEntity(getChance(), "large");
			}
		}, 0, 3000);
	}
	
	public int getChance() {
		Random random = new Random();
		int chance = random.nextInt(100);
		return chance;
	}
	
	public CountDownTimer getGoTimer() {
		return goTimer;
	}
	
}
