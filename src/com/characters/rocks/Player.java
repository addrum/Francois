package com.characters.rocks;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

public class Player {

	private Bitmap bitmap; // the actual bitmap
	private int x; // the X coordinate
	private int y; // the Y coordinate
	private boolean touched; // if droid is touched/picked up
	private Speed speed; // the speed with its directions

	//private final int WIDTH = bitmap.getWidth();
	//private final int HEIGHT = bitmap.getHeight();

	public Player(Bitmap bitmap, int x, int y) {
		this.bitmap = bitmap;
		this.x = x;
		this.y = y;
		this.speed = new Speed();
	}

	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2), y - (bitmap.getHeight() / 2), null);
	}

	// get the bounds of the player sprite
	//public Rect getBounds() {
	//	return new Rect(x, y, WIDTH, HEIGHT);
	//}

	/**
	 * Handles the {@link MotionEvent.ACTION_DOWN} event. If the event happens
	 * on the bitmap surface then the touched state is set to <code>true</code>
	 * otherwise to <code>false</code>
	 * 
	 * @param eventX
	 *            - the event's X coordinate
	 * @param eventY
	 *            - the event's Y coordinate
	 */
	public void handleActionDown(int eventX, int eventY) {
		if (eventX >= (x - bitmap.getWidth() / 2) && (eventX <= (x + bitmap.getWidth() / 2))) {
			if (eventY >= (y - bitmap.getHeight() / 2) && (y <= (y + bitmap.getHeight() / 2))) {
				// droid touched
				setTouched(true);
			} else {
				setTouched(false);
			}
		} else {
			setTouched(false);
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

	public boolean isTouched() {
		return touched;
	}

	public void setTouched(boolean touched) {
		this.touched = touched;
	}

	public Speed getSpeed() {
		return speed;
	}

	public void setSpeed(Speed speed) {
		this.speed = speed;
	}

}
