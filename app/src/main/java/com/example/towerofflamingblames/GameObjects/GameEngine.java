package com.example.towerofflamingblames.GameObjects;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.example.towerofflamingblames.GameState;
import com.example.towerofflamingblames.GameSurface;
import com.example.towerofflamingblames.R;

import java.util.Queue;

public class GameEngine {
    private Queue<IGameObject> gameObjects;
    private GameSurface context;
    private Background background;
    private Player player;
    private Generator generator;

    public GameEngine(GameSurface context) {
        this.context = context;
        GameState.SCREEN_WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels;
        GameState.SCREEN_HEIGHT = Resources.getSystem().getDisplayMetrics().heightPixels;
        background = new Background(BitmapFactory.decodeResource(context.getResources(),
                R.drawable.background_flames));
        this.player = new Player(BitmapFactory.decodeResource(context.getResources(),
                R.drawable.player_temp), GameState.SCREEN_HEIGHT * GameState.PLAYER_HEIGHT_PERCENTAGE / 100);
        this.generator = new Generator(context);
        this.gameObjects = generator.createObjects();
        GameState.platforms = this.gameObjects;
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
                if (event.getY() > GameState.SCREEN_HEIGHT / 2) {
                    player.jump();
                } else if (event.getX() < GameState.SCREEN_WIDTH / 2) {
                    player.setVelocity(-10);
                } else {
                    player.setVelocity(10);
                }
            }
        }
        return true;
    }
}
