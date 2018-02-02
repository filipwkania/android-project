package com.kaniadev.crystaldefender;

import com.kaniadev.crystaldefender.Bitmaps.BitmapList;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;;

public class LifePoints {
	
	private Bitmap life_pattern;
	private Rect destination_rect;
	private Rect resource_rect;
	private LifeState life_state;
	
	public static enum LifeState {
		FULL, HALF, MINIMUM, DEAD;
	}
	
	public LifePoints(double x, double y, Resources res, BlockLocation bl, Bitmaps bitmaps) {
		life_pattern = bitmaps.getBitmap(BitmapList.life_bar);
		life_state = LifeState.FULL;
		destination_rect = bl.getRectangle(x, y);
		resource_rect = new Rect(0,0,life_pattern.getWidth()/4, life_pattern.getHeight());
	}
	
	public void onDraw(Canvas canvas) {
		canvas.drawBitmap(life_pattern, resource_rect, destination_rect, null);
	}
	
	public void setLifeState(LifeState life_state) {
		
		this.life_state = life_state;
		
		switch(life_state) {
		
			case FULL: {
				resource_rect.set(0,0,life_pattern.getWidth()/4, life_pattern.getHeight());
			} break;
			case HALF: {
				resource_rect.set(life_pattern.getWidth()/4, 0, life_pattern.getWidth()/2, life_pattern.getHeight());
			} break;
			case MINIMUM: {
				resource_rect.set(life_pattern.getWidth()/2, 0, life_pattern.getWidth()/4*3, life_pattern.getHeight());
			} break;
			case DEAD: {
				resource_rect.set(life_pattern.getWidth()/4*3, 0, life_pattern.getWidth(), life_pattern.getHeight());
			} break;
		}
	}
	
	public LifeState getLifeState() {
		
		return life_state;
	}
	
	public void lostLife() {
		
		switch(life_state) {
		
		case FULL: {
			setLifeState(LifeState.HALF);
		} break;
		case HALF: {
			setLifeState(LifeState.MINIMUM);
		} break;
		case MINIMUM: {
			setLifeState(LifeState.DEAD);
		} break;
		case DEAD: {
			setLifeState(LifeState.FULL);
		} break;
	}		
	}
}
