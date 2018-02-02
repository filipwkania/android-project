package com.kaniadev.crystaldefender;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;


/*
 *	 @author Filip Kania
 * 	livarion@gmail.com
 */

public class GameLoop extends Thread {
	
	private static int MAX_FPS = 50;
	private static int FRAME_PERIOD = 1000 / MAX_FPS;
	private static int MAX_FRAME_SKIPS = 5;
	
	private SurfaceHolder surfaceHolder;
	private GameScreen game_screen;
	private boolean running;
	
	public GameLoop(SurfaceHolder surfaceHolder, GameScreen game_screen) {
		super();
		this.surfaceHolder = surfaceHolder;
		this.game_screen = game_screen;
	}
	
	public void setRunning(boolean running) {
		this.running = running;
	}
	
	@Override
	public void run() {
		Canvas canvas;
		
		long updateTime;
		long timeDifference;
		int sleepTime = 0;
		int framesSkipped;
		
		while (running != false) {
			//Null on canvas.
			canvas = null;
			//Lock canvas to surfaceHolder to start drawing on it.
			canvas = this.surfaceHolder.lockCanvas();
			
			//Synchronize tasks.
			synchronized (surfaceHolder) {	
	            
				updateTime = System.currentTimeMillis();
				framesSkipped = 0;
				
				//Update gamePanel.
				game_screen.update();
				game_screen.onDraw(canvas);
				
				//Update time.
				timeDifference = System.currentTimeMillis() - updateTime;
				//Set sleep time.
				sleepTime = (int) (FRAME_PERIOD - timeDifference);
				
				if(sleepTime > 0) {
					//Positive, then app is going smoothly.
					//Put to sleep for saving energy.
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}		
				//Activating if lost some frames.
				while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
					//Catch up. Update without draw.
					game_screen.update();
					
					sleepTime += FRAME_PERIOD;
					framesSkipped++;
				}
			}	
			if (canvas != null) {
				surfaceHolder.unlockCanvasAndPost(canvas);
			}
		}
		Log.d("GAMELOOP", "Stan:" + this.getState());
		Log.d("GAMELOOP", "Zakonczono thread:" + this.getName());
	}
}

