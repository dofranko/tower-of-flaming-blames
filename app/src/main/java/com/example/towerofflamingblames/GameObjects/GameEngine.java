package com.example.towerofflamingblames.GameObjects;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.example.towerofflamingblames.GameState;
import com.example.towerofflamingblames.GameSurface;
import com.example.towerofflamingblames.R;

import java.util.ArrayList;

import java.util.List;

public class GameEngine {
    private List<IGameObject> gameObjects = new ArrayList<>();
    private GameSurface context;
    private Background background;
    private Player player;

    public GameEngine(GameSurface context) {
        this.context = context;
        GameState.SCREEN_WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels;
        GameState.SCREEN_HEIGHT = Resources.getSystem().getDisplayMetrics().heightPixels;
        background = new Background(BitmapFactory.decodeResource(context.getResources(),
                R.drawable.background_flames));
        this.player = new Player(BitmapFactory.decodeResource(context.getResources(),
                R.drawable.player_temp), GameState.SCREEN_HEIGHT * GameState.PLAYER_HEIGHT_PERCENTAGE / 100);
        this.gameObjects.add(new Platform());
        GameState.platforms = this.gameObjects;
    }

    public void update(){
        background.update();
        player.update();
        for (IGameObject gameObject : gameObjects) {
            gameObject.update();
        }
    }

    public void draw(Canvas canvas){
        background.draw(canvas);
        player.draw(canvas);
        for (IGameObject gameObject : gameObjects) {
            gameObject.draw(canvas);
        }
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
