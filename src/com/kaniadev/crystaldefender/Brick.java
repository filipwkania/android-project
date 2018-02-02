package com.kaniadev.crystaldefender;

import java.util.Random;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.kaniadev.crystaldefender.Bitmaps.BitmapList;

public class Brick {
	
	private Bitmap brick_pattern;
	private Rect destination_rect;
	private Rect resource_rect;
	private Random r = new Random();
	private Condition condition;
	private BlockLocation block_location;
	private int rand;
	private int x, y;
	
	public static enum Condition {
		GOOD, DAMAGED, DESTROYED;
	}
	public Brick(int x, int y, Resources res, BlockLocation block_location, Bitmaps bitmaps) {
		
		//Condition good from beginning.
		condition = Condition.GOOD;

		//Rolling pattern for brick.
		rand = rollPattern();
		switch(rand) {
			case 1: {
				brick_pattern = bitmaps.getBitmap(BitmapList.wall1);
			}	break;
			case 2: {
				brick_pattern = bitmaps.getBitmap(BitmapList.wall2);
			}	break;
		}
		
		//Rectangle for cutting parts from the image.
		resource_rect = new Rect(0,0,brick_pattern.getWidth()/2, brick_pattern.getHeight());
		
		//Destination rectangle for drawing on screen.
		//X - 1 because I'm numerating columns from 1-5, not from 0-4.
		destination_rect = block_location.getRectangle(x-1, y);
	}
	
	public Brick(double x, double y, Resources res, BlockLocation block_location, Bitmaps bitmaps) {
		
		//Condition good from beginning.
		condition = Condition.GOOD;
		
		//Rolling pattern for brick.
		rand = rollPattern();
		switch(rand) {
		case 1: {
			brick_pattern = bitmaps.getBitmap(BitmapList.wall1);
		}	break;
		case 2: {
			brick_pattern = bitmaps.getBitmap(BitmapList.wall2);
		}	break;
		}
		
		//Rectangle for cutting parts from the image.
		resource_rect = new Rect(0,0,brick_pattern.getWidth()/2, brick_pattern.getHeight());
		
		//Destination rectangle for drawing on screen.
		//X - 1 because I'm numerating columns from 1-5, not from 0-4.
		destination_rect = block_location.getRectangle(x-1, y);
	}
	
	public int rollPattern() {
		return r.nextInt(2)+1;
	}
	
	public void moveUp() {
		y -= 1;
		destination_rect = block_location.getRectangle(x, y);
	}
	
	public void moveDown() {
		y += 1;
		destination_rect = block_location.getRectangle(x, y);
	}
	
	public void setDamaged() {
		condition = Condition.DAMAGED;
		resource_rect.set(brick_pattern.getWidth()/2, 0, brick_pattern.getWidth(), brick_pattern.getHeight());
	}
	
	public void setDestroyed() {
		condition = Condition.DESTROYED;
	}
	
	public void onDraw(Canvas canvas) {
		canvas.drawBitmap(brick_pattern, resource_rect, destination_rect, null);
	}
	public Rect getDestinationRect() {
		return destination_rect;
	}
	public Condition getCondition() {
		return condition;
	}
}
