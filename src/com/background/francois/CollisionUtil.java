package com.background.francois;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;

public class CollisionUtil {
	public static boolean isCollisionDetected(Bitmap bitmap1, int x1, int y1, Bitmap bitmap2, int x2, int y2) {

		Rect bounds1 = new Rect(x1, y1, x1 + bitmap1.getWidth(), y1 + bitmap1.getHeight());
		Rect bounds2 = new Rect(x2, y2, x2 + bitmap2.getWidth(), y2 + bitmap2.getHeight());

		if (Rect.intersects(bounds1, bounds2)) {
			Rect collisionBounds = getCollisionBounds(bounds1, bounds2);
			for (int i = collisionBounds.left; i < collisionBounds.right; i++) {
				for (int j = collisionBounds.top; j < collisionBounds.bottom; j++) {
					int bitmap1Pixel = bitmap1.getPixel(i - x1, j - y1);
					int bitmap2Pixel = bitmap2.getPixel(i - x2, j - y2);
					if (isFilled(bitmap1Pixel) && isFilled(bitmap2Pixel)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private static Rect getCollisionBounds(Rect rect1, Rect rect2) {
		int left = (int) Math.max(rect1.left, rect2.left);
		int top = (int) Math.max(rect1.top, rect2.top);
		int right = (int) Math.min(rect1.right, rect2.right);
		int bottom = (int) Math.min(rect1.bottom, rect2.bottom);
		return new Rect(left, top, right, bottom);
	}

	private static boolean isFilled(int pixel) {
		return pixel != Color.TRANSPARENT;
	}
}