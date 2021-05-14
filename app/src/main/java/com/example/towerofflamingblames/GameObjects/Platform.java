package com.example.towerofflamingblames.GameObjects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.towerofflamingblames.GameState;
import com.example.towerofflamingblames.GameSurface;
import com.example.towerofflamingblames.R;

import static java.lang.Math.abs;

public class Platform implements IGameObject{

    private final Bitmap leftPlatform;
    private final Bitmap middlePlatform;
    private final Bitmap rightPlatform;
    private final Rect rect;
    private final int number;   // liczba środkowych kostek platformy
    private final boolean movable;  // flaga wskazująca czy jest to ruchoma platforma
    private int movableX = GameState.MOVABLE_X; // prędkość porszunia się plaform

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
    public void update() {
        if (movable) {
            // jeśli jest to ruchoma platforma, zmieniaj jej położenie x
            // sprawdza, czy poruszanie platform zostalo zmienione w trakcie gry
            if (abs(movableX) != GameState.MOVABLE_X) {
                movableX /= movableX;   // dostajemy -1 albo 1
                movableX *= GameState.MOVABLE_X;
            }
            // platforma porusza się na całej długości ekranu
            if (rect.right + movableX >= GameState.SCREEN_WIDTH ||
                    rect.left + movableX <= 0) {
                movableX *= -1;
            }
            rect.left += movableX;
            rect.right += movableX;
        }
        // stopniowe opuszczanie platformy
        rect.top += GameState.MOVABLE_Y;
        rect.bottom += GameState.MOVABLE_Y;
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
