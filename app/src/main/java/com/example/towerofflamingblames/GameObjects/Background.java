package com.example.towerofflamingblames.GameObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Background implements  IGameObject{
    private Rect rectangle;

    public Background(){
        rectangle = new Rect();
        rectangle.set(100,100,300,300);
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.rgb(255,50,100));
        canvas.drawRect(rectangle, paint);
    }

    @Override
    public void update() {

    }
}
