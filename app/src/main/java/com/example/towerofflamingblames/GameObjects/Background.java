package com.example.towerofflamingblames.GameObjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;

public class Background implements  IGameObject{
    private Rect rect;
    private Bitmap image;

    public Background(Bitmap image){
        this.image = image;
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
        rect = new Rect();
        rect.set(0,0,width,height);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, null, rect, null);
    }

    @Override
    public void update() {}

    @Override
    public Rect getRect() {
        return this.rect;
    }

}
