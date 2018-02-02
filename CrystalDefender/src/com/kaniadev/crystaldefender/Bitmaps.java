package com.kaniadev.crystaldefender;

import java.util.HashMap;
import java.util.Map;

import com.kaniadev.dionakra.R;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Bitmaps {
	
	public static enum BitmapList {
		arrow_blocked, arrow_normal, arrow_pressed, asteroid, asteroid2,
		screen_bot, screen_mid, screen_top, pause_button, play_button, life_bar,
		wall1, wall2;
	}
	
	private Map<BitmapList, Bitmap> bitmap_box;
	
	public Bitmaps(Resources res) {
		
		bitmap_box = new HashMap<Bitmaps.BitmapList, Bitmap>();
		
		bitmap_box.put(BitmapList.arrow_blocked, BitmapFactory.decodeResource(res, R.drawable.arrowblocked));
		bitmap_box.put(BitmapList.arrow_normal, BitmapFactory.decodeResource(res, R.drawable.arrownormal));
		bitmap_box.put(BitmapList.arrow_pressed, BitmapFactory.decodeResource(res, R.drawable.arrowpressed));
		bitmap_box.put(BitmapList.asteroid, BitmapFactory.decodeResource(res, R.drawable.asteroid));
		bitmap_box.put(BitmapList.asteroid2, BitmapFactory.decodeResource(res, R.drawable.asteroid2));
		bitmap_box.put(BitmapList.screen_bot, BitmapFactory.decodeResource(res, R.drawable.screenbot));
		bitmap_box.put(BitmapList.screen_mid, BitmapFactory.decodeResource(res, R.drawable.screenmid));
		bitmap_box.put(BitmapList.screen_top, BitmapFactory.decodeResource(res, R.drawable.screentop));
		bitmap_box.put(BitmapList.pause_button, BitmapFactory.decodeResource(res, R.drawable.pause));
		bitmap_box.put(BitmapList.play_button, BitmapFactory.decodeResource(res, R.drawable.play));
		bitmap_box.put(BitmapList.life_bar, BitmapFactory.decodeResource(res, R.drawable.life));
		bitmap_box.put(BitmapList.wall1, BitmapFactory.decodeResource(res, R.drawable.wall));
		bitmap_box.put(BitmapList.wall2, BitmapFactory.decodeResource(res, R.drawable.wall2));
	}
	
	public Bitmap getBitmap(Bitmaps.BitmapList bitmap) {
		return bitmap_box.get(bitmap);
	}
}
