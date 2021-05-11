package com.example.towerofflamingblames;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import com.example.towerofflamingblames.GameObjects.Background;
import com.example.towerofflamingblames.GameObjects.IGameObject;

import java.util.LinkedList;
import java.util.List;

public class GameThread extends Thread {

    private boolean running;
    private GameSurface gameSurface;
    private SurfaceHolder surfaceHolder;

    private final int MAX_FPS = 30;

    public GameThread(GameSurface gameSurface, SurfaceHolder surfaceHolder) {
        this.gameSurface = gameSurface;
        this.surfaceHolder = surfaceHolder;
    }

    @Override
    public void run() {
        long startTime;
        long targetWaitTimeMilis = 1000/MAX_FPS;

        while (running) {
            //Updating and drawing game
            startTime = System.nanoTime();
            Canvas canvas = null;
            try {
                // Get Canvas from Holder and lock it.
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (canvas) {
                    this.gameSurface.update();
                    this.gameSurface.draw(canvas);
                }
            }
            catch (Exception e) {
                Log.d("Thread.canvas", e.getMessage());
            }
            finally {
                if (canvas != null) {
                    // Unlock Canvas.
                    this.surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

            // Calculating wait time
            long endTime = System.nanoTime();
            long waitTime = (endTime - startTime) / 1000000; // (Change nanoseconds to milliseconds)
            waitTime = targetWaitTimeMilis - waitTime;
            try {
                if(waitTime>0)
                    this.sleep(waitTime);
            }
            catch (InterruptedException e) {
                Log.d("Thread.sleep", e.getMessage());
            }
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
