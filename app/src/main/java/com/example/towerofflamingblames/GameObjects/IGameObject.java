package com.example.towerofflamingblames.GameObjects;

import android.graphics.Canvas;
import android.graphics.Rect;

public interface IGameObject {
    void update(float deltaTime);
    void draw(Canvas canvas);
    void moveScene(int deltaY);
    Rect getRect();
}
