package com.kaniadev.crystaldefender;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaniadev.crystaldefender.Sound.SoundsList;
import com.kaniadev.dionakra.R;

public class MainActivity extends Activity {
	
	private GameScreen game_screen;
	private ImageView new_game;
	private ImageView resume;
	private ImageView credits;
	private TextView highscore_text;
	private Sound sound_player;
	private Highscore highscore;
	private Typeface my_font;
	private static Vibrator vibrator; //lol
	private boolean game_was_played, game_on_screen;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//Making it full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		//Setting main layout on screen.
		setContentView(R.layout.activity_main);
		
		//Primal state.
		game_on_screen = false;
		game_was_played = false;
		
		//Highscore
		highscore = new Highscore(getApplicationContext());
		
		//Creating sound player.
		if (sound_player == null ) {
			sound_player = new Sound(this);
		}
		//Launching VIBRATOR
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("MA","OnDestroy");
		if (game_screen!= null) {
			game_screen.getGameLoop().setRunning(false);
		}
	}
	@Override
	public void onPause() {
		super.onPause();
		Log.d("MA","OnPause");
		if (game_screen!= null) {
			game_screen.getGameLoop().setRunning(false);
		}
		sound_player.stop(SoundsList.menu_loop);
		sound_player.stop(SoundsList.game_background);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.d("MA","OnResume");
		setContentView(R.layout.activity_main);
		loadFunctionality();
	}
	@Override
	public void onBackPressed() {
		
		if (game_on_screen == false) {
			moveTaskToBack(true);
		} else {
			game_screen.getGameLoop().setRunning(false);
			setContentView(R.layout.activity_main);	
			sound_player.stop(SoundsList.game_background);
			game_on_screen = false;
			game_was_played = true;
			loadFunctionality();
			sound_player.resume(SoundsList.menu_loop);
		}	
	}
	
	public Activity getMainActivity() {
		return this;
	}
	
	public void loadFunctionality() {
		//Setting high scores.
		highscore_text = (TextView) findViewById(R.id.highscore_menu);
		my_font = Typeface.createFromAsset(getAssets(),"fonts/Arcade.ttf" );
		highscore_text.setTypeface(my_font);
		highscore_text.setTextSize(90);
		highscore_text.setTextColor(Color.WHITE);
		highscore_text.setText(highscore.loadHighscore());
		
		//Creating buttons.
		//New game.
		new_game = (ImageView) findViewById(R.string.newgame_button);
		new_game.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if( game_was_played != false) {
					game_screen.killThread();
					game_was_played = false;
				}
				
				game_on_screen = true;
				
				if( game_screen != null) {
					
					game_screen.resetGame();
				} else {
				
					game_screen = new GameScreen(getMainActivity(), sound_player, highscore, my_font);
				}
				
				setContentView(game_screen);
			}
		});
		//Resume.
		resume = (ImageView) findViewById(R.string.resume_button);
		resume.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ( game_was_played != false) {
					game_on_screen = true;
					setContentView(game_screen);
					sound_player.stop(SoundsList.menu_loop);
					game_screen.getGameLoop().setRunning(true);
				} else {
					Toast toast = Toast.makeText(getApplicationContext(), "No game running", Toast.LENGTH_SHORT);
					toast.show();
				}
			}
		});
		//Credits.
		credits = (ImageView) findViewById(R.string.credits_button);
		credits.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast toast = Toast.makeText(getApplicationContext(), "Author: Filip Kania\nE-mail: livarion@gmail.com\n\n" +
						"Music: opengameart.com\nGraphics: opengameart.com", Toast.LENGTH_LONG);
				toast.setDuration(5000);
				toast.show();
			}
		});
		
		//Playing menu music.
		sound_player.setLooping(SoundsList.menu_loop, true);
		sound_player.play(SoundsList.menu_loop);			
	}
	
	public void playerDead(int highscore) {
		
		game_screen.getGameLoop().setRunning(false);

		vibrator.vibrate(500);

		setContentView(R.layout.activity_main);
		sound_player.stop(SoundsList.game_background);
		sound_player.resume(SoundsList.menu_loop);
		game_was_played = false;
		game_on_screen = false;
		if(highscore > Integer.parseInt((String) this.highscore.loadHighscore())) {
			this.highscore.saveHighscore(highscore);
			Toast toast = Toast.makeText(getApplicationContext(), "NEW HIGHSCORE!", Toast.LENGTH_SHORT);
			toast.show();
		}
		
		loadFunctionality();
	}
	
	public static Vibrator getVibrator() {
		return vibrator;
	}
}
