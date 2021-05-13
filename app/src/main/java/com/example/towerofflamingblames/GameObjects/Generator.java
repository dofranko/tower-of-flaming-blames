package com.example.towerofflamingblames.GameObjects;

import android.graphics.Rect;

import com.example.towerofflamingblames.GameState;
import com.example.towerofflamingblames.GameSurface;

import java.util.LinkedList;
import java.util.Queue;

public class Generator {

    private final GameSurface context;
    // odlicza dodane platformy, ponieważ co któraś będzie ruchoma
    private int counterPlatforms = 0;
    // lastLeft i lastTop są to pozycje ostatniej dodanej platformy względem której dodamy nową
    private int lastLeft = GameState.SCREEN_WIDTH / 2;
    private int lastTop = GameState.SCREEN_HEIGHT;

    public Generator(GameSurface context){
        this.context = context;
    }

    public Queue<IGameObject> createObjects() {
        Queue<IGameObject> objects = new LinkedList<>();
        // podłoga wieży
        objects.add(new Platform(-50, GameState.SCREEN_HEIGHT - 50, 10, false, context));
        // dodanie widocznych na ekranie platform
        while (lastTop > 0) {
            lastTop -= (GameState.SCREEN_HEIGHT  / GameState.PLAYER_HEIGHT_PERCENTAGE);
            addPlatform(objects);
        }
        // na tej wysokości będą się pojawiać kolejno nowe pokazujące się platformy
        lastTop -= (GameState.SCREEN_HEIGHT  / GameState.PLAYER_HEIGHT_PERCENTAGE);
        return objects;
    }

    public void update() {
        // pierwsza dodana do listy platforma
        IGameObject head = GameState.platforms.peek();
        Rect peekRect = head.getRect();
        // jeśli jest poniżej dolnego ekranu usuń ją i dodaj nową na górze
        if (peekRect.top >= GameState.SCREEN_HEIGHT) {
            GameState.platforms.remove();
            addPlatform(GameState.platforms);
        }
    }

    private void addPlatform(Queue<IGameObject> objects) {
        boolean movable = false;
        counterPlatforms += 1;
        // co ileś platform tworzymy platformę ruchomą
        if (counterPlatforms == GameState.COUNTER_MOVABLE_PLATFORMS) {
            counterPlatforms = 0;
            movable = true;
        }
        // losujemy jej długość z dozwolonego zakresu
        int number = (int)(Math.random() * GameState.MAX_PLATFORM_LENGTH);
        // jeśli będzie poza ekranem zmieniamy stronę przesuwania platform
        if (lastLeft + (2 + number) * GameState.PLATFORM_SIZE > GameState.SCREEN_WIDTH || lastLeft < 0) {
            GameState.PLATFORM_GAP *= -1;
        }
        lastLeft += GameState.PLATFORM_GAP;
        objects.add(new Platform(lastLeft, lastTop, number, movable, context));
    }
}
