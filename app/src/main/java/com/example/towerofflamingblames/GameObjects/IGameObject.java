package com.example.towerofflamingblames.GameObjects;

import android.graphics.Canvas;
import android.graphics.Rect;

public interface IGameObject {
    void update(float deltaTime);
    void draw(Canvas canvas);
    Rect getRect();
}
