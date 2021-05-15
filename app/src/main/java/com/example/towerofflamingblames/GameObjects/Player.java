package com.example.towerofflamingblames.GameObjects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.towerofflamingblames.GameState;
import com.example.towerofflamingblames.GameSurface;
import com.example.towerofflamingblames.R;

import java.util.ArrayList;

public class Player implements IGameObject {

    private final Rect rect;
    private final Bitmap image;
    private boolean canJump = false;
    private int coins = 0;
    private final Paint paint;
    private static class MyVector{
        public float x;
        public float y;
        public MyVector(){
            this.x = 0.0f;
            this.y = 0.0f;
        }
        public MyVector(float x, float y){
            this.x = x;
            this.y = y;
        }
    }
    private final MyVector vel = new MyVector();
    private final MyVector acc = new MyVector(0.0f, 0.8f);
    private final MyVector pos;

    public Player(Bitmap image, int square_size){
        this.image = image;
        this.pos = new MyVector((int) (GameState.SCREEN_WIDTH / 2 - square_size / 2),
                GameState.SCREEN_HEIGHT - square_size);
        this.rect = new Rect((int)pos.x,(int)pos.y,
                (int)pos.x+square_size,(int)pos.y+square_size);
        this.paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(80);
    }

    @Override
    public void update() {
        this.vel.y += this.acc.y;

        this.pos.y += (int) (this.vel.y + 0.5f * this.acc.y);

        this.vel.x *= 0.9;
        if (this.pos.x + this.vel.x >= 0 && this.getRect().right + this.vel.x <= GameState.SCREEN_WIDTH) {
            this.pos.x += (this.vel.x);
        }

        if (Math.abs(this.vel.x) < 0.001f) {
            this.vel.x = 0.0f;

        }
        if (pos.y > GameState.SCREEN_HEIGHT - rect.height()) {
            pos.y = GameState.SCREEN_HEIGHT - rect.height() - 500;
            canJump = true;
        }
        this.rect.set((int) this.pos.x, (int) this.pos.y, (int) this.pos.x + this.rect.width(),
                (int) this.pos.y + this.rect.height());

        // iterowanie po platformach w celu sprawdzenia kolizji
        for (IGameObject object : GameState.platforms) {
            if (Rect.intersects(this.rect, object.getRect())) {
                if (this.getRect().bottom < object.getRect().top + GameState.PLATFORM_SIZE / 4 &&
                        this.vel.y > 0) {
                    this.pos.y = object.getRect().top - getRect().height();
                    this.vel.y = 0;
                    canJump = true;
                }
            }
        }

        // iterowanie po artefaktach w celu sprawdzenia kolizji
        for (int i = 0; i < GameState.artefacts.size(); i++) {
            if (Rect.intersects(this.rect, GameState.artefacts.get(i).getRect())) {
                GameState.artefacts.get(i).action(this);
                GameState.artefacts.remove(i);
            }
        }
    }

    public void jump(){
        if(!canJump) return;
        this.vel.y = -20.0f;
        canJump = false;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, null, rect, null);
        canvas.drawText(String.valueOf(coins), 120, 80, paint);
    }

    @Override
    public Rect getRect() {
        return this.rect;
    }

    public void setVelocity(float x){
        this.vel.x = x;
    }

    public void addCoin() {
        coins += 1;
    }
}