package be.ecam.student09024.shooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.util.Log;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix;

public class MainGamePanel extends SurfaceView implements SurfaceHolder.Callback
{
    private ThreadLoop thread;
    private Position position;
    private Bitmap bitmap;

    // Class Name TAG for debug
    private static final String TAG = MainGamePanel.class.getSimpleName();

    public MainGamePanel(Context context)
    {
        super(context);
        // Get Events for this class
        getHolder().addCallback(this);
        // Redim Image (110x110)
        bitmap = bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.player), 110, 110, true);
        // Matrix to rotate
        Matrix mat = new Matrix();
        mat.postRotate(90);
        // Rotated bitmap
        bitmap = bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
        // create position and load bitmap
        position = new Position(bitmap, 800, 800);
        // create the game loop thread
        thread = new ThreadLoop(getHolder(), this);
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

     @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        // Start thread
        thread.setRunning(true);
        thread.start();
    }

     @Override
     public void surfaceDestroyed(SurfaceHolder holder)
     {
         // Destroy thread
         boolean retry = true;
    	 while (retry)
         {
        	  try
              {
                 thread.join();
            	 retry = false;
              }
              catch (InterruptedException e)
              {

              }
         }
     }

     @Override
     public boolean onTouchEvent(MotionEvent event)
     {
         if (event.getAction() == MotionEvent.ACTION_DOWN)
         {
             	   // delegating event handling to the droid
             	   position.handleActionDown((int)event.getX(), (int)event.getY());

             	   // check if in the lower part of the screen we exit
             	   if (event.getY() > getHeight() - 50)
                   {
                 	    thread.setRunning(false);
                 	    ((Activity)getContext()).finish();
                   }
                   else
                   {
                	 Log.d(TAG, "Coords: x=" + event.getX() + ",y=" + event.getY());
                   }
          }
         if (event.getAction() == MotionEvent.ACTION_MOVE)
         {
         	   // the gestures
         	   if (position.isTouched())
               {
                   Log.d(TAG, "coucou");
             	    // the droid was picked up and is being dragged
                   position.setX((int)event.getX());
                   position.setY((int)event.getY());
             	   }
         	  }
         if (event.getAction() == MotionEvent.ACTION_UP)
         {
         	   // touch was released
         	   if (position.isTouched()) {
                   position.setTouched(false);
             	   }
         	  }
        	  return true;
     }

     @Override
     protected void onDraw(Canvas canvas)
     {
         // Draw image ressource into screen
         //canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.player), 150, 150, null);
         // Set all black
         canvas.drawColor(Color.BLACK);
         // reDraw position of player
         position.draw(canvas);
     }

}
