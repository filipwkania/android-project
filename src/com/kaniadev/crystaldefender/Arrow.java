package com.kaniadev.crystaldefender;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceView;

import com.kaniadev.crystaldefender.Bitmaps.BitmapList;

public class Arrow {
	
	private Bitmap arrowpressed, arrownormal, arrowblocked;
	private Rect destination_rect;
	private int column;
	
	public static enum ArrowState {
		PRESSED, BLOCKED, NORMAL
	}
	
	private ArrowState actual_state;
	
	public Arrow(Rect destination_rect, SurfaceView surface_view, int column, Bitmaps bitmaps) {
		this.column = column;
		this.destination_rect = destination_rect;
		actual_state = ArrowState.NORMAL;
		arrowpressed = bitmaps.getBitmap(BitmapList.arrow_pressed);
		arrowblocked = bitmaps.getBitmap(BitmapList.arrow_blocked);
		arrownormal = bitmaps.getBitmap(BitmapList.arrow_normal);
		
	}
	
	public void update() {
		
	}
	
	public void onDraw(Canvas canvas) {
		switch (actual_state) {
			case BLOCKED: {
				canvas.drawBitmap(arrowblocked, null, destination_rect, null);
			}	break;
			
			case NORMAL: {
				canvas.drawBitmap(arrownormal, null, destination_rect, null);
			}	break;
			
			case PRESSED: {
				canvas.drawBitmap(arrowpressed, null, destination_rect, null);
			}	break;
		}
	}
	public void setArrowState(ArrowState arrow_state) {
		actual_state = arrow_state;
	}
	public ArrowState getArrowState() {
		return actual_state;
	}
	public Rect getDestinationRect() {
		return destination_rect;
	}	
	public int getColumn() {
		return column;
	}
}
