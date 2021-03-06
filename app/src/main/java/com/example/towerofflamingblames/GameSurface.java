package com.example.towerofflamingblames;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.towerofflamingblames.GameObjects.GameEngine;
import com.example.towerofflamingblames.GameObjects.IGameObject;

import static android.content.Context.SENSOR_SERVICE;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread gameThread;
    private GameEngine gameEngine;
    private final GameActivity context;

    public GameSurface(GameActivity context) {
        super(context);
        this.context = context;
        this.setFocusable(true);
        this.getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        this.gameThread = new GameThread(this, holder);
        this.gameEngine = new GameEngine(this, this.context);
        this.gameThread.setRunning(true);
        this.gameThread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                this.gameThread.setRunning(false);
                this.gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
            GameState.platforms.clear();
            GameState.artefacts.clear();
            SensorManager senman =(SensorManager) this.getContext().getSystemService(SENSOR_SERVICE);
            senman.unregisterListener(gameEngine);
        }
    }

    /**
     * Wywołuje update() silnika gry
     */
    public void update(float deltaTime) {
        this.gameEngine.update(deltaTime > 0 ? deltaTime : 1);
    }

    /**
     * Wywołuje draw() silnika gry
     * @param canvas kanwas
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        this.gameEngine.draw(canvas);
    }

    /**
     * Przechwycenie eventów naciskania ekranu i przekazanie do silnika
     * @param event event ekranu
     * @return czy obsłużono
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.gameEngine.handleEvent(event);
    }
}
