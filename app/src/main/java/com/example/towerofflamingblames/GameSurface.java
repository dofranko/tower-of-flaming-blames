package com.example.towerofflamingblames;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.towerofflamingblames.GameObjects.IGameObject;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread gameThread;


    public GameSurface(Context context) {
        super(context);
        this.setFocusable(true);
        this.getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        this.gameThread = new GameThread(this, holder);
        this.gameThread.setRunning(true);
        this.gameThread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        boolean retry= true;
        while(retry) {
            try {
                this.gameThread.setRunning(false);
                this.gameThread.join();
            }catch(InterruptedException e)  {
                e.printStackTrace();
            }
            retry= true;
        }
    }

    /**
     * Wywołuje update() każdego przedmiotu
     */
    public void update(){
    }

    /**
     * Wywołuje draw() każdego przedmiotu
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        for(IGameObject gameObject : gameThread.gameObjects){
            gameObject.draw(canvas);
        }
    }

    /**
     * Przechwycenie eventów naciskania ekranu
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {


        return true;
    }
}
