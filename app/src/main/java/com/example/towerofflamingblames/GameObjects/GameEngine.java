package com.example.towerofflamingblames.GameObjects;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.MotionEvent;

import com.example.towerofflamingblames.GameState;
import com.example.towerofflamingblames.GameSurface;
import com.example.towerofflamingblames.R;

import java.util.Queue;

import static android.content.Context.SENSOR_SERVICE;

public class GameEngine implements SensorEventListener {
    private final Queue<IGameObject> gameObjects;
    private final Background background;
    private final Player player;
    private final Generator generator;

    public GameEngine(GameSurface context) {
        GameState.SCREEN_WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels;
        GameState.SCREEN_HEIGHT = Resources.getSystem().getDisplayMetrics().heightPixels;
        background = new Background(BitmapFactory.decodeResource(context.getResources(),
                R.drawable.background_flames));
        this.player = new Player(BitmapFactory.decodeResource(context.getResources(),
                R.drawable.player_temp), GameState.SCREEN_HEIGHT * GameState.PLAYER_HEIGHT_PERCENTAGE / 100);
        this.generator = new Generator(context);
        this.gameObjects = generator.createObjects();
        GameState.platforms = this.gameObjects;
        // tworzenie reagowania na przechylenia telefonu
        SensorManager sensorManager = (SensorManager) context.getContext().getSystemService(SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    public void update() {
        background.update();
        player.update();
        for (IGameObject gameObject : gameObjects) {
            gameObject.update();
        }
        generator.update();
    }

    public void draw(Canvas canvas) {
        background.draw(canvas);
        for (IGameObject gameObject : gameObjects) {
            gameObject.draw(canvas);
        }
        player.draw(canvas);
    }

    public boolean handleEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE: {
                if (event.getY() > (float) GameState.SCREEN_HEIGHT / 2) {
                    player.jump();
                } else if (event.getX() < (float) GameState.SCREEN_WIDTH / 2) {
                    player.setVelocity(-15);
                } else {
                    player.setVelocity(15);
                }
            }
        }
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // dla wartości [-2,2] nie reagujemy, by telefon nie był taki czuły na przechylenia
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if (event.values[0] > 2) {
                player.setVelocity(-15);
            } else if (event.values[0] < -2) {
                player.setVelocity(15);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
