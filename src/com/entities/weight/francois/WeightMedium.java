package com.entities.weight.francois;

import com.entities.francois.Entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class WeightMedium implements Entity {

	private static Bitmap bitmap; // the actual bitmap
	private int x; // the X coordinate
	private int y; // the Y coordinate
	private int width, height;
	private float yv; // velocity value on the Y axis
	
	public WeightMedium(Bitmap bitmap, int x, int y) {
		WeightMedium.bitmap = bitmap;
		this.x = x;
		this.y = y;
		this.yv = 20;
		setWidth(bitmap.getWidth());
		setHeight(bitmap.getHeight());
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
		WeightMedium.bitmap = bitmap;
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
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
