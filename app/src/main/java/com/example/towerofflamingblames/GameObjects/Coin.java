package com.example.towerofflamingblames.GameObjects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.towerofflamingblames.GameState;
import com.example.towerofflamingblames.GameSurface;
import com.example.towerofflamingblames.R;

public class Coin implements IGameArtefact {

    private final Bitmap[] frames;
    private int currentBitmap = 0;
    private final Rect rect;
    private int changingTime = 0;

    public Coin(int left, int top, GameSurface context) {
        frames = new Bitmap[6];
        frames[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin0);
        frames[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin1);
        frames[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin2);
        frames[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin3);
        frames[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin4);
        frames[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin5);
        this.rect = new Rect(left, top, left + GameState.ARTEFACT_SIZE, top + GameState.ARTEFACT_SIZE);
    }

    @Override
    public void update(float deltaTime) {
        // stopniowe opuszczanie monet
        int temp = (int) (GameState.MOVABLE_Y * deltaTime);
        rect.top += temp;
        rect.bottom += temp;
        // co trzy wywołania funkcji zmieniaj zdjęcie (TODO można dodać do tego delta time)
        changingTime += 1;
        if (changingTime == 3) {
            currentBitmap += 1;
            if (currentBitmap > 5) {
                currentBitmap = 0;
            }
            changingTime = 0;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(frames[currentBitmap], null, this.rect, null);
    }

    @Override
    public void action(Player player) {
        player.addCoin();
    }

    @Override
    public Rect getRect() { return this.rect; }
}
