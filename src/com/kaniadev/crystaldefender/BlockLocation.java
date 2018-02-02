package com.kaniadev.crystaldefender;

import android.graphics.Rect;

public class BlockLocation {
	
	private static int HORIZONTAL_BLOCKS_NUMBER = 5;
	private static int VERTICAL_BLOCKS_NUMBER = 15;
	private int screen_width, screen_height;
	
	
	public BlockLocation(int screen_width, int screen_height) {
		this.screen_height = screen_height;
		this.screen_width = screen_width;
	}
	
	public Rect getRectangle(int x, int y) {
		
		return new Rect(screen_width/HORIZONTAL_BLOCKS_NUMBER * x,
				screen_height/VERTICAL_BLOCKS_NUMBER * y,
				screen_width/HORIZONTAL_BLOCKS_NUMBER * (x + 1),
				screen_height/VERTICAL_BLOCKS_NUMBER * (y + 1));
	}
	
	public Rect getRectangle(double x, double y) {
		
		return new Rect(Double.valueOf(screen_width/HORIZONTAL_BLOCKS_NUMBER * x).intValue(),
				Double.valueOf(screen_height/VERTICAL_BLOCKS_NUMBER * y).intValue(),
				Double.valueOf(screen_width/HORIZONTAL_BLOCKS_NUMBER * (x + 1)).intValue(),
				Double.valueOf(screen_height/VERTICAL_BLOCKS_NUMBER * (y + 1)).intValue());
	}
	public Rect setRectangle(Rect rect, int x, int y) {
		
		rect.set(screen_width/HORIZONTAL_BLOCKS_NUMBER * x,
				screen_height/VERTICAL_BLOCKS_NUMBER * y,
				screen_width/HORIZONTAL_BLOCKS_NUMBER * (x + 1),
				screen_height/VERTICAL_BLOCKS_NUMBER * (y + 1));
		return rect;
	}
}
