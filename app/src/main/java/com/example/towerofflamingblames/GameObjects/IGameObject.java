package com.example.towerofflamingblames.GameObjects;

import android.graphics.Canvas;
import android.graphics.Rect;

public interface IGameObject {
    void update();
    void draw(Canvas canvas);
    Rect getRect();
}
