package com.example.towerofflamingblames.GameObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Platform implements IGameObject{

    private Rect rect;

    public Platform(){
        this.rect = new Rect(200, 600, 600, 620);
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.rgb(255,50,100));
        canvas.drawRect(rect, paint);
    }

    @Override
    public Rect getRect() {
        return this.rect;
    }
}
