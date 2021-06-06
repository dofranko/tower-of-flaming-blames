package com.example.towerofflamingblames.GameObjects.Artefacts;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.towerofflamingblames.GameObjects.Generator;
import com.example.towerofflamingblames.GameObjects.Player;
import com.example.towerofflamingblames.GameState;

import java.util.ArrayList;

public abstract class AbstractArtefact implements IGameArtefact {

    private final ArrayList<Bitmap> bitmaps;
    private int currentBitmap = 0;
    private final Rect rect;

    public AbstractArtefact(int left, int top, ArrayList<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
        this.rect = new Rect(left, top, left + GameState.ARTEFACT_SIZE, top + GameState.ARTEFACT_SIZE);
    }

    @Override
    public void update(float deltaTime) {
        // stopniowe opuszczanie
        int temp = (int) (GameState.PLATFORM_MOVABLE_SPEED_Y * deltaTime);
        rect.top += temp;
        rect.bottom += temp;
        currentBitmap += 1;
        if (currentBitmap >= bitmaps.size()) {
            currentBitmap = 0;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap((Bitmap) bitmaps.get(currentBitmap), null, this.rect, null);
    }

    @Override
    public void moveScene(int deltaY) {
        rect.top += deltaY;
        rect.bottom += deltaY;
    }

    @Override
    public void action(Player player) {

    }

    @Override
    public Rect getRect() { return this.rect; }
}
