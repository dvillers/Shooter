package be.ecam.student09024.shooter;

import android.view.SurfaceHolder;
import android.util.Log;
import android.graphics.Canvas;

public class ThreadLoop extends Thread
{
    // flag to hold game state
    private boolean running;
    private SurfaceHolder surfaceHolder;
    private MainGamePanel gamePanel;
    private static final String TAG = ThreadLoop.class.getSimpleName();
    private Position position;

    public ThreadLoop(SurfaceHolder surfaceHolder, MainGamePanel gamePanel)
    {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    public void setRunning(boolean running)
    {
        this.running = running;
    }

    @Override
    public void run()
    {
        // Canvas on which we will draw our image.
        // The canvas is the surface’s bitmap onto which we can draw and we can edit its pixels
        Canvas canvas;
        long tickCount = 0L;
        Log.d(TAG, "Starting game loop");
    	  while (running)
          {
              canvas = null;
              tickCount++;
              // try locking the canvas for exclusive pixel editing on the surface
              // On every execution of the game loop we get hold of the canvas and we pass it to the game panel to draw on it.
              	   try {
              	    canvas = this.surfaceHolder.lockCanvas();
              	    synchronized (surfaceHolder)
                    {
                  	     // update game state
                  	     // draws the canvas on the panel
                  	     this.gamePanel.onDraw(canvas);
                        //this.position.draw(canvas);
                  	    }
              	   } finally {
              	    // in case of an exception the surface is not left in
              	    // an inconsistent state
              	    if (canvas != null) {
                  	     surfaceHolder.unlockCanvasAndPost(canvas);
                  	    }
              	   } // end finally
          }
        Log.d(TAG, "Game loop executed " + tickCount + " times");
   	 }
}
