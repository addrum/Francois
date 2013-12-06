package com.characters.francois;

import java.util.Random;

public class Speed {
	public static final int DIRECTION_UP = -1;
	public static final int DIRECTION_DOWN = 1;

	private float xv; // velocity value on the X axis
	private float yv; // velocity value on the Y axis
	
	private int yDirection = DIRECTION_DOWN;
	
	private Random random;

	public Speed() {
		this.xv = 5;
		random = new Random();
		this.yv = random.nextInt(30 - 10) + 10;
	}

	public Speed(float xv, float yv) {
		this.xv = xv;
		this.yv = yv;
	}

	public float getXv() {
		return xv;
	}

	public void setXv(float xv) {
		this.xv = xv;
	}

	public float getYv() {
		return yv;
	}

	public void setYv(float yv) {
		this.yv = yv;
	}

	public int getyDirection() {
		return yDirection;
	}

	public void setyDirection(int yDirection) {
		this.yDirection = yDirection;
	}

	// changes the direction on the Y axis
	public void toggleYDirection() {
		yDirection = yDirection * -1;
	}

}
