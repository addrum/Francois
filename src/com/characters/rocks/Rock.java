package com.characters.rocks;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Rock {

	private Bitmap bitmap; // the actual bitmap
	private int x; // the X coordinate
	private int y; // the Y coordinate
	private Speed speed; // the speed with its directions
	private int width, height;

	public Rock(Bitmap bitmap, int x, int y) {
		this.bitmap = bitmap;
		this.x = x;
		this.y = y;
		this.speed = new Speed();
		setWidth(bitmap.getWidth());
		setHeight(bitmap.getHeight());
	}

	// draws the each rock to the screen
	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2), y - (bitmap.getHeight() / 2), null);
	}

	// updates the rock's internal state every tick
	public void update() {
		y += (speed.getYv() * speed.getyDirection());
	}

	//---------------------------------------------------------------//
	// getters and setters

	// get the bound of the rock sprite
	public Rect getBounds() {
		return new Rect(getX(), getY(), width, height);
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

}
