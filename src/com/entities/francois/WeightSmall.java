package com.entities.francois;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class WeightSmall implements Entity {

	private static Bitmap bitmap; // the actual bitmap
	private int x; // the X coordinate
	private int y; // the Y coordinate
	private static int width;
	private int height;
	private float yv; // velocity value on the Y axis
	private static int onScreen;
	
	public WeightSmall(Bitmap bitmap, int spawnX, int y) {
		WeightSmall.bitmap = bitmap;
		this.x = spawnX;
		this.y = y;
		this.yv = 10;
		setWidth(bitmap.getWidth());
		setHeight(bitmap.getHeight());
		onScreen++;
	}
	
	// updates the weight's internal state every tick
	public void update() {
		setY((int) (getY() + getYv()));
	}
	
	@Override
	public void destroy() {
		setY(2000);
	}

	// draws the sprite to the screen
	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2), y - (bitmap.getHeight() / 2), null);
	}

	//---------------------------------------------------------------//
	// getters and setters

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		WeightSmall.bitmap = bitmap;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public float getYv() {
		return yv;
	}

	public void setYv(float yv) {
		this.yv = yv;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		WeightSmall.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getOnScreen() {
		return onScreen;
	}

	public void setOnScreen(int onScreen) {
		WeightSmall.onScreen = onScreen;
	}

}
