package com.entities.francois;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public abstract class Weight extends Entity {

	private Bitmap bitmap; // the actual bitmap
	private int x; // the X coordinate
	private int y; // the Y coordinate

	public Weight(Bitmap bitmap, int spawnX, int y) {
		super(bitmap, spawnX, y);
	}
	
	// updates the weight's internal state every tick
	public abstract void update();

	// draws the sprite to the screen
	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2), y - (bitmap.getHeight() / 2), null);
	}

	//---------------------------------------------------------------//
	// getters and setters

	// get the bounds of the sprite
	public abstract Rect getBounds();

	public abstract Bitmap getBitmap();

	public abstract void setBitmap(Bitmap bitmap);
	
	public abstract int getOnScreen();
	
	public abstract int getX();

	public abstract void setX(int x);

	public abstract int getY();

	public abstract void setY(int y);

	public abstract int getWidth();

	public abstract void setWidth(int width);

	public abstract int getHeight();
	
	public abstract void setHeight(int height);

}
