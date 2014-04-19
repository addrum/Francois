package com.background.francois;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameTimers extends Thread {

	private Thread thread;
	private GameLogic gameLogic;
	
	public GameTimers(GameLogic gameLogic) {
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
		time();
		scoreItemTimer();
	}
	
	public void smallRockTimer() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				gameLogic.spawnEntity(getChance(), "small");
			}
		}, 0, 800);
	}
	
	public void mediumRockTimer() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				gameLogic.spawnEntity(getChance(), "medium");
			}
		}, 1000, 1800);
	}
	
	public void largeRockTimer() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				gameLogic.spawnEntity(getChance(), "large");
			}
		}, 3000, 4500);
	}
	
	public void scoreItemTimer() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				gameLogic.spawnEntity(getChance(), "score");
			}
		}, 1000, 2300);
	}
	
	public void time() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				int time = gameLogic.getTime();
				time++;
				gameLogic.setTime(time);
			}
		}, 0, 1000);
	}
	
	public int getChance() {
		Random random = new Random();
		int chance = random.nextInt(100);
		return chance;
	}
	
}
