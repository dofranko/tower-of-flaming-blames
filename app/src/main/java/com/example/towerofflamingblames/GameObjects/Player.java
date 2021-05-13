package com.example.towerofflamingblames.GameObjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.example.towerofflamingblames.GameState;
import com.example.towerofflamingblames.R;

import java.io.Console;

public class Player implements IGameObject {

    private  Rect rect;
    private Bitmap image;
    private boolean canJump = false;
    private class MyVector{
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
    private MyVector vel = new MyVector();
    private MyVector acc = new MyVector(0.0f, 0.8f);
    private MyVector pos;

    public Player(Bitmap image, int square_size){
        this.image = image;
        this.pos = new MyVector((int)(GameState.SCREEN_WIDTH / 2 - square_size / 2),
                GameState.SCREEN_HEIGHT - square_size);
        this.rect = new Rect((int)pos.x,(int)pos.y,
                (int)pos.x+square_size,(int)pos.y+square_size);
    }

    @Override
    public void update() {
        this.vel.y += this.acc.y;

        this.pos.y += (int) (this.vel.y + 0.5f * this.acc.y);

        this.vel.x *= 0.9;

        this.pos.x += (this.vel.x);

        if (Math.abs(this.vel.x) < 0.001f) {
            this.vel.x = 0.0f;

        }
        if (pos.y > GameState.SCREEN_HEIGHT - rect.height()) {
            pos.y = GameState.SCREEN_HEIGHT - rect.height() - 500;
            canJump = true;
        }
        this.rect.set((int) this.pos.x, (int) this.pos.y, (int) this.pos.x + this.rect.width(),
                (int) this.pos.y + this.rect.height());
        for (IGameObject object : GameState.platforms) {
            if (Rect.intersects(getRect(), object.getRect())) {
                this.pos.y = object.getRect().top - getRect().height();
                this.rect.set((int) this.pos.x, (int) this.pos.y, (int) this.pos.x + this.rect.width(),
                        (int) this.pos.y + this.rect.height());
                this.vel.y = 0;
                canJump = true;
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
    }

    @Override
    public Rect getRect() {
        return this.rect;
    }

    public void setVelocity(float x){
        this.vel.x = x;
    }
}
