package com.entities.francois;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public abstract class Entity {

	public Entity(Bitmap bitmap, int x, int y) {
	}

	// draws the sprite to the screen
	public abstract void draw(Canvas canvas);
	//---------------------------------------------------------------//
	// getters and setters

	// get the bounds of the sprite
	public abstract Rect getBounds();

	public abstract Bitmap getBitmap();

	public abstract void setBitmap(Bitmap bitmap);

	public abstract int getX();

	public abstract void setX(int x);

	public abstract int getY();

	public abstract void setY(int y);

	public abstract int getWidth();

	public abstract void setWidth(int width);

	public abstract int getHeight();

	public abstract void setHeight(int height);

}
