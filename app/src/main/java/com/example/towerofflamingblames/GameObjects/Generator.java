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
    // ostatnia platforma, która wyznacza lokalizację następnej
    private IGameObject lastPlatform;

    public Generator(GameSurface context){
        this.context = context;
    }

    public Queue<IGameObject> createObjects() {
        Queue<IGameObject> objects = new LinkedList<>();
        // podłoga wieży
        lastPlatform = new Platform(-50, GameState.SCREEN_HEIGHT - 100, 10, false, context);
        objects.add(lastPlatform);
        // dodanie widocznych na ekranie platform
        int lastTop = lastPlatform.getRect().top;
        while (lastTop > 0) {
            addPlatform(objects);
            lastTop = lastPlatform.getRect().top;
        }
        return objects;
    }

    public void update() {
        // pierwsza dodana do listy platforma
        IGameObject head = GameState.platforms.peek();
        if (head != null) {
            Rect peekRect = head.getRect();
            // jeśli jest poniżej dolnego ekranu usuń ją i dodaj nową na górze
            if (peekRect.top >= GameState.SCREEN_HEIGHT) {
                GameState.platforms.remove();
                addPlatform(GameState.platforms);
            }
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
        int left = lastPlatform.getRect().left;
        // jeśli będzie poza ekranem zmieniamy stronę przesuwania platform
        // prawa ściana
        if (left + (2 + number) * GameState.PLATFORM_SIZE + GameState.PLATFORM_GAP > GameState.SCREEN_WIDTH) {
            GameState.PLATFORM_GAP *= -1;
            left = GameState.SCREEN_WIDTH - (2 + number) * GameState.PLATFORM_SIZE;
        } else if (left + GameState.PLATFORM_GAP < 0) {     // lewa ściana
            GameState.PLATFORM_GAP *= -1;
            left = 0;
        } else {    // brak kolizji
            left += GameState.PLATFORM_GAP;
        }
        // obliczanie wysokości następnej platformy
        int top = lastPlatform.getRect().top - GameState.SCREEN_HEIGHT / GameState.PLAYER_HEIGHT_PERCENTAGE;
        lastPlatform = new Platform(left, top, number, movable, context);
        objects.add(lastPlatform);
    }
}
