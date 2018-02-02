package com.kaniadev.crystaldefender;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.kaniadev.crystaldefender.Arrow.ArrowState;
import com.kaniadev.crystaldefender.Bitmaps.BitmapList;
import com.kaniadev.crystaldefender.Brick.Condition;
import com.kaniadev.crystaldefender.LifePoints.LifeState;
import com.kaniadev.crystaldefender.Sound.SoundsList;

@SuppressLint("ViewConstructor")
public class GameScreen extends SurfaceView implements SurfaceHolder.Callback {
	
	private final int ASTEROID_RESPAWN_START = 2000;
	private final int ASTEROID_RESPAWN_MIN = 1400;
	private final int MAX_ASTEROIDS = 10;

	private final int MAX_UNUSED_BRICKS = 10;
	private final int UNUSED_BRICK_RESPAWN = 1500;
	
	private final int BLOCK_HIT_SCORE = 25;
	private final int FIVE_BLOCKS_SCORE = 250;
	
	private List<Brick> brick_box;
	private Resources res;
	private List<Brick> unused_bricks;
	private boolean pause_active = false;
	private boolean full_column;
	private boolean set_brick;
	private boolean started_new_game = true;
	private PauseButton pause_b;
	private LifePoints life_points;
	private Paint highscore_paint;
	private Highscore highscore;
	private Bitmap screenbot, screenmid, screentop;
	private GameLoop game_loop;
	private LinkedList<Arrow> arrow_box;
	private BlockLocation block_location;
	private float touch_x, touch_y;
	private Sound sound_player;
	private LinkedList<Asteroid> asteroid_box;
	private Arrow left_arrow, leftmid_arrow;
	private Arrow top_arrow;
	private Arrow rightmid_arrow, right_arrow;
	private int width, height;
	private int asteroid_respawn_time;
	private int current_highscore = 0;
	private long game_time;
	private long start_message_length;
	private long last_asteroid_spawn;
	private long last_unused_brick_spawn;
	private long las_time_diff;
	private long lubs_time_diff;
	private Rect screentop_rect, screenmid_rect, screenbot_rect;
	private Rect temp, temp2;
	private Rect highscore_rect;
	private Rect bricks_no_rect;
	private MainActivity m_a;
	private Bitmaps bitmaps;
	
	private int z = 0;
	
	public GameScreen(Activity activity, Sound sound_player, Highscore highscore, Typeface font) {
		
		super(activity.getApplicationContext());
		m_a = (MainActivity) activity;
		getHolder().addCallback(this);
		res = getResources();
		
		//Loading sounds files.
		this.sound_player = sound_player;
		
		//Background music looped.
		sound_player.setLooping(SoundsList.game_background, true);
		
		//Getting screen metrics.
		DisplayMetrics dm = res.getDisplayMetrics();
		height = dm.heightPixels;
		width = dm.widthPixels;
		
		//Getting block location creator/setter.
		block_location = new BlockLocation(width, height);
		
		//Loading bitmaps.
		bitmaps = new Bitmaps(res);
		
		//Screen pieces.
		screenbot = bitmaps.getBitmap(BitmapList.screen_bot);
		screenmid = bitmaps.getBitmap(BitmapList.screen_mid);
		screentop = bitmaps.getBitmap(BitmapList.screen_top);
		
		//Creating brick container with 25 spots.
		brick_box = new ArrayList<Brick>();
		for( int i = 0; i < 25; i++) {
			brick_box.add(i, null);
		}
		
		//Creating list for bricks that are not set on the place yet.
		unused_bricks = new LinkedList<Brick>();
		
		//First unused brick add.
		unused_bricks.add(new Brick(3.0, 12.75, res, block_location, bitmaps));
		last_unused_brick_spawn = System.currentTimeMillis();
		
		//Creating destination rectangles for screen background bitmaps.
		screentop_rect = new Rect(0, 0, width, height/3);
		screenmid_rect = new Rect(0, height/3, width, height/3*2);
		screenbot_rect = new Rect(0, height/3*2, width, height);
		
		//Creating container for Asteroids.
		asteroid_box = new LinkedList<Asteroid>();
		
		//Creating Arrows.
		left_arrow = new Arrow(block_location.getRectangle(0, 13), this,1, bitmaps);
		leftmid_arrow = new Arrow(block_location.getRectangle(1, 12), this,2, bitmaps);
		top_arrow = new Arrow(block_location.getRectangle(2, 11), this,3, bitmaps);
		rightmid_arrow = new Arrow(block_location.getRectangle(3, 12), this,4, bitmaps);
		right_arrow = new Arrow(block_location.getRectangle(4, 13), this,5, bitmaps);
		
		//Placing arrow in a container.
		arrow_box = new LinkedList<Arrow>();
		arrow_box.add(left_arrow);
		arrow_box.add(leftmid_arrow);
		arrow_box.add(top_arrow);
		arrow_box.add(rightmid_arrow);
		arrow_box.add(right_arrow);
		
		//Pause button.
		pause_b = new PauseButton(2, 14, block_location, bitmaps);
		
		//Life points.
		life_points = new LifePoints(0.0, 14.10, res, block_location, bitmaps);
		
		//Paint for high score and bricks number drawing.
		highscore_paint = new Paint();
		highscore_paint.setTextAlign(Align.CENTER);
		highscore_paint.setTypeface(font);
		highscore_paint.setColor(Color.WHITE);
		highscore_paint.setTextSize(90);
		
		//High score.
		this.highscore = highscore;
		
		//High score rectangle.
		highscore_rect = block_location.getRectangle(3.5, 0.25);
		
		//Bricks number rectangle.
		bricks_no_rect = block_location.getRectangle(4.0, 14.65);
	
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d("GS", "Surface created.");
		//Creating game thread.
		game_loop = new GameLoop(getHolder(), this);
		((GameLoop) game_loop).setRunning(true);
		game_loop.start();

		start_message_length = System.currentTimeMillis() + 5000;
		
		//Turning music on.
		sound_player.stop(SoundsList.menu_loop);
		if(pause_active != true ) {
			sound_player.play(SoundsList.game_background);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		killThread();
	}
	
	public void update() {
		
		if( pause_active != false) {
			
		} else {
			/*
			 * 
			 * ASTEROID UPDATE
			 * 
			 */
			game_time = System.currentTimeMillis();
			
			//First asteroid creation.	
			if(asteroid_box.isEmpty() != false && last_asteroid_spawn < 5) {
				//First asteroid spawn time.
				asteroid_respawn_time = ASTEROID_RESPAWN_START;
				asteroid_box.add(new Asteroid(width, height, res,sound_player, bitmaps, life_points));
				last_asteroid_spawn = System.currentTimeMillis();
				// Checking if there are destroyed asteroids and removing them.
			} else {
				for (int i=0; i<asteroid_box.size(); i++) {
					
					if(asteroid_box.get(i).isAlive() != true) {
						asteroid_box.remove(i);
						
						//Debugging
	//					Log.d("BLABLA", "Game time: "+game_time + " Last spawn: "+last_asteroid_spawn);	,
						
					} else {
						asteroid_box.get(i).update();	
						
						//Checking collision.
						temp = asteroid_box.get(i).getDestinationRect();
							
						for (Brick b : brick_box) {
								
							if( b != null) {
								temp2 = b.getDestinationRect();
								
								if(temp.bottom >= temp2.top) {
									
								if(temp.left >= temp2.left && temp.left <= temp2.right 
										|| temp.right <= temp2.right && temp.right >= temp2.left) {
										
									b.setDestroyed();		
									asteroid_box.get(i).setAlive(false);
									sound_player.play(SoundsList.block_destroyed);
									current_highscore += BLOCK_HIT_SCORE;
									break;
									}
								}
							}
						}
					}
				}
				//Adding new asteroid if respawn time came. 
				if( game_time - last_asteroid_spawn > asteroid_respawn_time
						&& asteroid_box.size() <= MAX_ASTEROIDS) {
					asteroid_box.add(new Asteroid(width, height, res,sound_player, bitmaps, life_points));
					last_asteroid_spawn = System.currentTimeMillis();
					if(asteroid_respawn_time > ASTEROID_RESPAWN_MIN) {
						asteroid_respawn_time -= 25;	
					}
				}			
			}
			/*
			 * 
			 * BRICKS UPDATE
			 * 
			 */
			if( game_time - last_unused_brick_spawn > UNUSED_BRICK_RESPAWN
					&& unused_bricks.size() < MAX_UNUSED_BRICKS) {
				last_unused_brick_spawn = System.currentTimeMillis();
				unused_bricks.add((Brick)new Brick(3.0, 12.75, res, block_location, bitmaps));
				sound_player.play(SoundsList.brick_load);
			}
			
			for(int i = 0; i < brick_box.size(); i++) {
				if (brick_box.get(i) != null && brick_box.get(i).getCondition() == Condition.DESTROYED) {
					//If brick in a full column is destroyed, allow to put new one.
					if ( i < 5) {
						if(left_arrow.getArrowState().equals(ArrowState.BLOCKED)) {
							left_arrow.setArrowState(ArrowState.NORMAL);
						}
					} else if ( i < 10) {
						if(leftmid_arrow.getArrowState().equals(ArrowState.BLOCKED)) {
							leftmid_arrow.setArrowState(ArrowState.NORMAL);
						}					
					} else if ( i < 15) {
						if(top_arrow.getArrowState().equals(ArrowState.BLOCKED)) {
							top_arrow.setArrowState(ArrowState.NORMAL);
						}					
					} else if ( i < 20) {
						if(rightmid_arrow.getArrowState().equals(ArrowState.BLOCKED)) {
							rightmid_arrow.setArrowState(ArrowState.NORMAL);
						}					
					} else if ( i < 25) {
						if(right_arrow.getArrowState().equals(ArrowState.BLOCKED)) {
							right_arrow.setArrowState(ArrowState.NORMAL);
						}					
					} 
					brick_box.set(i, null);
				}
			}
		}	

	//Check for game over.
		if(life_points.getLifeState().equals(LifeState.DEAD) != false) {
			m_a.runOnUiThread(new Runnable() {
	            @Override
	            public void run() {
	            	
	            	if(z == 0) {
	            		m_a.playerDead(current_highscore);
	            		Log.d("GS", "Player dead.");
	            	}
	            	z++;
	            }
	        });	
		}
	}	
	@SuppressLint("WrongCall")
	public void onDraw(Canvas canvas) {
		
		if( canvas != null ) {
			//Drawing default black background.
			canvas.drawColor(Color.BLACK);
			//Drawing 1/3 top of the screen.
			canvas.drawBitmap(screentop, null, screentop_rect, null);
			//Drawing 1/3 to 2/3 part of the screen.
			canvas.drawBitmap(screenmid, null, screenmid_rect, null);
			//Drawing 2/3 to bottom part of the screen.
			canvas.drawBitmap(screenbot, null, screenbot_rect, null);
			
			//Drawing asteroids.
			for(Asteroid a : asteroid_box) {
				a.onDraw(canvas);
			}
			
			//Drawing arrows.
			for(Arrow a : arrow_box) {
				a.onDraw(canvas);
			}
			
			//Drawing bricks.
			if(brick_box.isEmpty() != true) {
				for(Brick b : brick_box) {
					if ( b != null ) {
						b.onDraw(canvas);
					}
				}
			}
			//Drawing unused brick.
			if(unused_bricks.isEmpty() != true) {
				unused_bricks.get(0).onDraw(canvas);
			}
			
			//Drawing pause button.
			pause_b.onDraw(canvas);
			
			//Drawing life points.
			life_points.onDraw(canvas);
			
			//Drawing high score.
			canvas.drawText(""+current_highscore, highscore_rect.centerX(), highscore_rect.centerY()*3/2, highscore_paint);
			
			//Drawing current bricks number.
			canvas.drawText(""+unused_bricks.size(), bricks_no_rect.centerX(), bricks_no_rect.centerY(), highscore_paint);
			
			//Drawing start message.
			if(started_new_game == true) {
				if( game_time < start_message_length ) {

					canvas.drawText("Protect crystals!", width/2, height/3, highscore_paint);
					canvas.drawText("Hit arrows!", width/2, height/3*2, highscore_paint);
				} else {
					started_new_game = false;
				}
			}

		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		touch_x = e.getX();
		touch_y = e.getY();
		
//		Debugging		
//		Log.d("GAMESCREEN","Touch x: " + touch_x + "Touch y: "+ touch_y);
//		Log.d("GAMESCREEN","Left: "+left_arrow.getDestinationRect().left+" Right: "+left_arrow.getDestinationRect().right
//				+ "Top: "+left_arrow.getDestinationRect().top+" Bottom: "+left_arrow.getDestinationRect().bottom);
		
		if(e.getAction() == MotionEvent.ACTION_DOWN) {
			//Checking if pause button pressed.
			if(touch_x > pause_b.getDestinationRect().left && touch_x < pause_b.getDestinationRect().right 
					&& touch_y < pause_b.getDestinationRect().bottom && touch_y > pause_b.getDestinationRect().top) {
				pause_b.setPressed(true);
			}
			if (pause_active != true) {
			//Checking if arrow pressed.
				for(Arrow a : arrow_box) {
					if(touch_x > a.getDestinationRect().left && touch_x < a.getDestinationRect().right 
							&& touch_y < a.getDestinationRect().bottom && touch_y > a.getDestinationRect().top) {
						if(a.getArrowState() == ArrowState.BLOCKED) {
							
						} else {
							a.setArrowState(ArrowState.PRESSED);
						}
					}
				}	
			}
		}
		if(e.getAction() == MotionEvent.ACTION_UP) {
			//Pause
			if(touch_x > pause_b.getDestinationRect().left && touch_x < pause_b.getDestinationRect().right 
					&& touch_y < pause_b.getDestinationRect().bottom && touch_y > pause_b.getDestinationRect().top
					&& pause_b.isPressed() != false) {
				
				pause_b.setPressed(false);
				
				if( pause_active != true ) {
					
					pause_active = true;	
					pause_b.changeToPlay();
					sound_player.pause(SoundsList.game_background);
					las_time_diff = game_time - last_asteroid_spawn;
					lubs_time_diff = game_time - last_unused_brick_spawn;
				} else {
					
					pause_active = false;		
					pause_b.changeToPause();
					sound_player.resume(SoundsList.game_background);
					afterPause(las_time_diff, lubs_time_diff);
				}

			}		
			
			if(pause_active != true) {
			//Arrows
				for(Arrow a : arrow_box) {
					if(a.getArrowState() == ArrowState.BLOCKED) {
						
					} else if (touch_x > a.getDestinationRect().left && touch_x < a.getDestinationRect().right 
							&& touch_y < a.getDestinationRect().bottom && touch_y > a.getDestinationRect().top
							&& a.getArrowState() == ArrowState.PRESSED ) {
						
						if(unused_bricks.isEmpty()) {
							
							sound_player.play(SoundsList.no_bricks);
							a.setArrowState(ArrowState.NORMAL);
						} else {
							
							addBrick(a, unused_bricks.get(0));
							sound_player.play(SoundsList.brick_teleport);
						}
						
					} else {
						
						a.setArrowState(ArrowState.NORMAL);
					}
				}
			}
		}
		return true;
	}
	public void addBrick(Arrow a, Brick b) {
		
		int column = a.getColumn();
		full_column = true;
		set_brick = false; 
		
		for( int i = 5; i > 0; i--) {
			
			if( brick_box.get(5*column - i) == null ) {
				
				full_column = false;
				
				if( set_brick == false ) {
				//Changing location of unused brick.
					block_location.setRectangle(b.getDestinationRect(), column-1, i+4);
					
					//Adding the brick to different container.
					brick_box.set(5*column - i, b);
					
	//				Debugging
	//				Log.d("ADDBRICK","Change to column: "+(column-1)+" and y position: "+(i+4));
	//				Log.d("BRICKLIST","Added to list at position: "+(5*column-i));
	//				Log.d("BRICKLIST",toNiceString(brick_box));
					
					//Adding special points.
					if( i == 1) {
						current_highscore += FIVE_BLOCKS_SCORE;
					}
					//Removing the brick that has been used.
					unused_bricks.remove(0);
					set_brick = true;
					full_column = true;
				}
			}
		}
		
		if( full_column == true) {
			a.setArrowState(ArrowState.BLOCKED);
		} else {
			a.setArrowState(ArrowState.NORMAL);
		}
	}
	public GameLoop getGameLoop() {
		return game_loop;
	}
	
	public void afterPause(long las_time_diff, long lubs_time_diff) {
		game_time = System.currentTimeMillis();
		last_asteroid_spawn = System.currentTimeMillis() - las_time_diff;
		last_unused_brick_spawn = System.currentTimeMillis() - lubs_time_diff;
	}
	
	public Highscore getHighscore() {
		return highscore;
	}
	
	public void killThread() {
		//Gently killing game thread.
		boolean retry = true;
		while (retry) {
			try {
				game_loop.join();
				retry = false;				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
	}
	
	public void resetGame() {
		
		for (int i =0; i < 25; i++) {
			brick_box.set(i, null);
		}
		this.unused_bricks.clear();
		this.asteroid_box.clear();
		this.life_points.setLifeState(LifeState.FULL);
		this.current_highscore = 0;
		this.game_time = 0;
		this.last_unused_brick_spawn = 0;
		this.last_asteroid_spawn = 0;
		this.asteroid_respawn_time = ASTEROID_RESPAWN_START;
		this.z = 0;
		this.pause_active = false;
		this.started_new_game = true;
		this.pause_b.changeToPause();
		
		for(Arrow a : arrow_box) {
			a.setArrowState(ArrowState.NORMAL);
		}
	}
// 	Debugging
//	public String toNiceString(List a) {
//		String text = "";
//		int i = 0;
//		for(Object o : a) {
//			if(o != null) {
//				text += "["+i+" box]";
//			} else {
//				text += "["+i+" n]";
//			}
//			i++;
//		}
//		return text;
//	}
}
