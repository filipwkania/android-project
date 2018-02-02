package com.kaniadev.crystaldefender;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.kaniadev.crystaldefender.Bitmaps.BitmapList;

public class PauseButton {
	
	private Bitmap pause_pattern;
	private Rect destination_rect;
	private Rect resource_rect;
	private Bitmaps bitmaps;
	private boolean is_pressed = false;
	
	public PauseButton(int x, int y, BlockLocation bl, Bitmaps bitmaps) {
		this.bitmaps = bitmaps;
		pause_pattern = this.bitmaps.getBitmap(BitmapList.pause_button);
		destination_rect = bl.getRectangle(x, y);
		resource_rect = new Rect(0,0,pause_pattern.getWidth()/2, pause_pattern.getHeight());
	}
	
	public void onDraw(Canvas canvas) {
		canvas.drawBitmap(pause_pattern, resource_rect, destination_rect, null);
	}
	
	public void setPressed(boolean is_pressed) {
		this.is_pressed = is_pressed;
		
		if( is_pressed == true ) {
			resource_rect.set(pause_pattern.getWidth()/2, 0, pause_pattern.getWidth(), pause_pattern.getHeight());
		} else {
			resource_rect.set(0,0,pause_pattern.getWidth()/2, pause_pattern.getHeight());
		}
	}
	
	public boolean isPressed() {
		return is_pressed;
	}
	
	public Rect getDestinationRect() {
		return destination_rect;
	}
	
	public void changeToPlay() {
		pause_pattern = bitmaps.getBitmap(BitmapList.play_button);
	}
	
	public void changeToPause() {
		pause_pattern = bitmaps.getBitmap(BitmapList.pause_button);
	}
}
