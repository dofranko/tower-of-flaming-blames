package com.example.towerofflamingblames.GameObjects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.towerofflamingblames.GameState;
import com.example.towerofflamingblames.GameSurface;
import com.example.towerofflamingblames.R;

import static java.lang.Math.abs;

public class Platform implements IGameObject {

    private final Bitmap leftPlatform;
    private final Bitmap middlePlatform;
    private final Bitmap rightPlatform;
    private final Rect rect;
    private final int number;   // liczba środkowych kostek platformy
    private final boolean movable;  // flaga wskazująca czy jest to ruchoma platforma
    private final float speedMultiplier = 1.0f; //TODO (np random) modyfikator lokalny prędkości poruszania się platformy
    private int direction = 1; //kierunek poruszania, 1 -> w prawo, -1 w lewo

    public Platform(int left, int top, int number, boolean movable, GameSurface context) {
        // konwersja grafiki z png na bitmap
        // platforma zaczyna się leftPlatform, póżniej dana liczba middlePlatform i kończy się rightPlatform
        leftPlatform = BitmapFactory.decodeResource(context.getResources(), R.drawable.left_platform);
        middlePlatform = BitmapFactory.decodeResource(context.getResources(), R.drawable.middle_platform);
        rightPlatform = BitmapFactory.decodeResource(context.getResources(), R.drawable.right_platform);
        // obszar, który zajmuję platforma jako całość
        this.rect = new Rect(left, top, left + (2 + number) * GameState.PLATFORM_SIZE, top + GameState.PLATFORM_SIZE);
        // liczba środkowych kostek platformy
        this.number = number;
        // wskazuje, czy to jest ruchoma platforma
        this.movable = movable;
    }

    @Override
    public void update(float deltaTime) {
        if (movable) {
            int temp = (int) (GameState.MOVABLE_X * deltaTime * direction * speedMultiplier);;
            rect.left += temp;
            rect.right += temp;
            // platforma porusza się na całej długości ekranu
            if ((rect.right >= GameState.SCREEN_WIDTH && direction == 1) || (rect.left<= 0 && direction == -1)) {
                direction *= -1;
            }
        }
        // stopniowe opuszczanie platformy
        int temp = (int) (GameState.MOVABLE_Y * deltaTime);
        rect.top += temp;
        rect.bottom += temp;
    }

    @Override
    public void draw(Canvas canvas) {
        // rysowanie pierwszej kostki leftPlatform
        Rect tmpRect = new Rect(rect.left, rect.top, rect.left + GameState.PLATFORM_SIZE, rect.bottom);
        canvas.drawBitmap(leftPlatform, null, tmpRect, null);
        // rysowanie środkowych kostek middlePlatform
        for (int i = 0; i < number; i++) {
            tmpRect.left += GameState.PLATFORM_SIZE;
            tmpRect.right += GameState.PLATFORM_SIZE;
            canvas.drawBitmap(middlePlatform, null, tmpRect, null);
        }
        // rysowanie ostatniej kostki rightPlatform
        tmpRect.left += GameState.PLATFORM_SIZE;
        tmpRect.right += GameState.PLATFORM_SIZE;
        canvas.drawBitmap(rightPlatform, null, tmpRect, null);
    }

    @Override
    public Rect getRect() {
        return this.rect;
    }
}
