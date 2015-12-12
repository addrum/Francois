package com.entities.francois;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public interface Entity {

	// draws the sprite to the screen
	public abstract void draw(Canvas canvas);
	
	// update the sprite's positions
	public abstract void update();
	
	// moves the sprite offscreen
	public abstract void destroy();
	
	//---------------------------------------------------------------//
	// getters and setters

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
