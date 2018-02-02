package com.kaniadev.crystaldefender;

import java.util.Random;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.kaniadev.crystaldefender.Bitmaps.BitmapList;
import com.kaniadev.crystaldefender.Sound.SoundsList;

public class Asteroid {
	
	private Bitmap asteroid_1;
	private Speed speed;
	private Rect destination_rect;
	private Sound sound_player;
	private LifePoints life_points;
	private Bitmaps bitmaps;
	private int x, y;
	private int screen_width, screen_height;
	private boolean alive = true;
	
	private Random r = new Random();
	
	public Asteroid(int screen_width, int screen_height, Resources res, Sound sound_player,
			Bitmaps bitmaps, LifePoints life_points) {
		
		this.screen_width = screen_width;
		this.screen_height = screen_height;
		this.life_points = life_points;
		this.bitmaps = bitmaps;
		int pattern = rollPattern();
		switch(pattern) {
			case 1: {
				asteroid_1 = this.bitmaps.getBitmap(BitmapList.asteroid);
			} break;
			case 2: {
				asteroid_1 = this.bitmaps.getBitmap(BitmapList.asteroid2);
			} break;
		}
		speed = new Speed();
		speed.setXv(randX());
		this.sound_player = sound_player; 
		x = randXScreen();
		y = screen_height / 15 * -1;
		destination_rect = new Rect(x, y, screen_width/10, screen_height/15);
	}
	
	public void update() {
		x += (speed.getXv() * speed.getxDirection());
		y += (speed.getYv() * speed.getyDirection());
		
		if( x <= 0 && speed.getxDirection() == Speed.DIRECTION_LEFT) {
			speed.toggleXDirection();
			
			if(destination_rect.bottom > screen_height / 3) {
				sound_player.play(SoundsList.side_hit_1);
			}
		} else if( x + destination_rect.width() >= screen_width && speed.getxDirection() == Speed.DIRECTION_RIGHT){
			speed.toggleXDirection();
				
			if(destination_rect.bottom > screen_height / 3) {
				sound_player.play(SoundsList.side_hit_2);
			}
		}
		
		if(destination_rect.bottom > screen_height/3*2) {
			alive = false;
			sound_player.play(SoundsList.asteroid_blow);
			MainActivity.getVibrator().vibrate(200);
			life_points.lostLife();
		}
	}
	
	public void onDraw(Canvas canvas) {
		destination_rect.set(x, y, x + screen_width/10, y + screen_height/15);
		canvas.drawBitmap(asteroid_1, null, destination_rect, null);
	}
	
	public int rollPattern() {
		return r.nextInt(2)+1;
	}
	
	public int randXScreen() {
		return r.nextInt(screen_width - screen_width/10) +  1;
	}	
	
	public int randX() {
		return r.nextInt(3)+1;
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public Rect getDestinationRect() {
		return destination_rect;
	}
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
}
