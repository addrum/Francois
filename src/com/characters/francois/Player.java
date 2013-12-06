package com.characters.francois;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Player extends Character {

	private boolean touched; // if player is touched/picked up
	private Bitmap bitmap; // the actual bitmap
	private int x; // the X coordinate
	private int y; // the Y coordinate
	private Speed speed; // the speed with its directions
	private int width, height;

	public Player(Bitmap bitmap, int x, int y) {
		super(bitmap, x, y);
		this.bitmap = bitmap;
		this.x = x;
		this.y = y;
		this.speed = new Speed();
		setWidth(bitmap.getWidth());
		setHeight(bitmap.getHeight());
	}

	// handles the player being touched (hehe)
	public void handleActionDown(int eventX, int eventY) {
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
	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2), y - (bitmap.getHeight() / 2), null);
	}

	//---------------------------------------------------------------//
	// getters and setters

	// get the bounds of the sprite
	public Rect getBounds() {
		return new Rect(x - (bitmap.getWidth() / 2), y - (bitmap.getHeight() / 2), x + (bitmap.getWidth() / 2), y + (bitmap.getHeight() / 2));
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
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

	public Speed getSpeed() {
		return speed;
	}

	public void setSpeed(Speed speed) {
		this.speed = speed;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isTouched() {
		return touched;
	}

	public void setTouched(boolean touched) {
		this.touched = touched;
	}

}
