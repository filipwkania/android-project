package com.kaniadev.crystaldefender;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.kaniadev.dionakra.R;

import android.content.Context;
import android.media.MediaPlayer;

public class Sound {
	
	private Map<SoundsList, MediaPlayer> sound_box;
	
	public static enum SoundsList {
		arrow_pressed, arrow_released, side_hit_1, side_hit_2, asteroid_blow
		,brick_load, brick_teleport, no_bricks, block_destroyed, menu_loop,
		game_background;
	}
	
	public Sound(Context context) {
		
		sound_box = new HashMap<SoundsList, MediaPlayer>();
		
		sound_box.put(SoundsList.arrow_pressed, MediaPlayer.create(context, R.raw.swing));
		sound_box.put(SoundsList.arrow_released, MediaPlayer.create(context, R.raw.swing2));
		sound_box.put(SoundsList.side_hit_1, MediaPlayer.create(context, R.raw.bang_1));
		sound_box.put(SoundsList.side_hit_2, MediaPlayer.create(context, R.raw.bang_2));
		sound_box.put(SoundsList.asteroid_blow, MediaPlayer.create(context, R.raw.barlfire));
		sound_box.put(SoundsList.brick_load, MediaPlayer.create(context, R.raw.reload));
		sound_box.put(SoundsList.brick_teleport, MediaPlayer.create(context, R.raw.setblock));
		sound_box.put(SoundsList.no_bricks, MediaPlayer.create(context, R.raw.nomoreblocks));
		sound_box.put(SoundsList.block_destroyed, MediaPlayer.create(context, R.raw.block_destroy));
		sound_box.put(SoundsList.game_background, MediaPlayer.create(context, R.raw.game_background));
		sound_box.put(SoundsList.menu_loop, MediaPlayer.create(context, R.raw.menu_loop));
	}
	
	public void play(SoundsList sound) {
		
		sound_box.get(sound).start();
	}
	
	public void pause(SoundsList sound) {
		
		sound_box.get(sound).pause();
	}
	
	public void resume(SoundsList sound) {
		
		sound_box.get(sound).start();
	}
	public void stop(SoundsList sound) {
		
		sound_box.get(sound).stop();
		try {
			sound_box.get(sound).prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void setLooping(SoundsList sound, boolean looping ) {
		sound_box.get(sound).setLooping(looping);
	}
}
