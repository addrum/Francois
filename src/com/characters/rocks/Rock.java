package com.characters.rocks;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Rock {

	private Bitmap bitmap; // the actual bitmap
	private int x; // the X coordinate
	private int y; // the Y coordinate
	private boolean touched; // if droid is touched/picked up
	private Speed speed; // the speed with its directions

	//private final int WIDTH = bitmap.getWidth();
	//private final int HEIGHT = bitmap.getHeight();

	public Rock(Bitmap bitmap, int x, int y) {
		this.bitmap = bitmap;
		this.x = x;
		this.y = y;
		this.speed = new Speed();
	}

	// get the bound of the rock sprite
	//public Rect getBounds() {
	//return new Rect(x, y, WIDTH, HEIGHT);
	//}

	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2), y - (bitmap.getHeight() / 2), null);
	}

	/**
	 * Method which updates the droid's internal state every tick
	 */
	public void update() {
		if (!touched) {
			y += (speed.getYv() * speed.getyDirection());
		}
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

}
