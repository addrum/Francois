package com.entities.francois;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Player implements Entity {

	private boolean touched; // if player is touched/picked up
	private Bitmap bitmap; // the actual bitmap
	private int x; // the X coordinate
	private int y; // the Y coordinate
	private int width, height;

	public Player(Bitmap bitmap, int x, int y) {
		this.bitmap = bitmap;
		this.x = x;
		this.y = y;
		setWidth(bitmap.getWidth());
		setHeight(bitmap.getHeight());
	}

	// handles the player being touched (hehe)
	public synchronized void handleActionDown(int eventX, int eventY) {
		if (eventX >= (getX() - getBitmap().getWidth() / 2) && (eventX <= (getX() + getBitmap().getWidth() / 2))) {
			if (eventY >= (getY() - getBitmap().getHeight() / 2) && (getY() <= (getY() + getBitmap().getHeight() / 2))) {
				// player touched
				setTouched(true);
			} else {
				setTouched(false);
			}
		} else {
			setTouched(false);
		}

	}

	// draws the sprite to the screen
	public synchronized void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2), y - (bitmap.getHeight() / 2), null);
	}
	
	@Override
	public synchronized void destroy() {
		setY(2000);
	}

	//---------------------------------------------------------------//
	// getters and setters

	public synchronized Bitmap getBitmap() {
		return bitmap;
	}

	public synchronized void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public synchronized int getX() {
		return x;
	}

	public synchronized void setX(int x) {
		this.x = x;
	}

	public synchronized int getY() {
		return y;
	}

	public synchronized void setY(int y) {
		this.y = y;
	}

	public synchronized int getWidth() {
		return width;
	}

	public synchronized void setWidth(int width) {
		this.width = width;
	}

	public synchronized int getHeight() {
		return height;
	}

	public synchronized void setHeight(int height) {
		this.height = height;
	}

	public synchronized boolean isTouched() {
		return touched;
	}

	public synchronized void setTouched(boolean touched) {
		this.touched = touched;
	}

	@Override
	public synchronized void update() {
		// TODO Auto-generated method stub
		
	}
	
}
