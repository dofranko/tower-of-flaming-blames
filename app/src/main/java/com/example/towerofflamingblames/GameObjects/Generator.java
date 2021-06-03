package com.example.towerofflamingblames.GameObjects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.towerofflamingblames.GameState;
import com.example.towerofflamingblames.GameSurface;
import com.example.towerofflamingblames.R;

public class Generator implements IGameObject {

    private final GameSurface context;
    private final Bitmap imageCoin;
    private final Rect rectCoin = new Rect(10, 10, 100 ,100);
    // odlicza, kiedy umieścić ruchomą platformę
    private int counterPlatforms = 0;
    // odlicza, kiedy umieścić monetę
    private int counterCoins = 0;


    public Generator(GameSurface context) {
        this.context = context;
        this.imageCoin = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin0);

    }

    public void createObjects() {
        int tmp_top = GameState.SCREEN_HEIGHT/2 + GameState.PLATFORM_GAP_Y;
        while (tmp_top < GameState.SCREEN_HEIGHT) {
            GameState.platforms.addFirst(new Platform(GameState.SCREEN_WIDTH/2, tmp_top, 1, false, context));
            tmp_top += GameState.PLATFORM_GAP_Y;
        }
        // podłoga wieży
        GameState.platforms.add(new Platform(-50, GameState.SCREEN_HEIGHT /2, 10, false, context));
        // dodanie pierwszego schodka
        int left = GameState.SCREEN_WIDTH / 2 - GameState.PLATFORM_SIZE * 2;
        int top = GameState.platforms.getLast().getRect().top - GameState.PLATFORM_GAP_Y;
        GameState.platforms.add(new Platform(left, top, 3, false, context));
        // dodanie widocznych na ekranie platform
        while (GameState.platforms.getLast().getRect().top > 0) {
            addPlatform();
        }
    }

    private void addPlatform() {
        // domyślnie platformy są nieruchome
        boolean movable = false;
        // co ileś platform tworzymy platformę ruchomą
        counterPlatforms += 1;
        if (counterPlatforms == GameState.MOVABLE_PLATFORMS_FREQUENCY) {
            counterPlatforms = 0;
            movable = true;
        }
        // losujemy długość platformy z dozwolonego zakresu
        int number = (int)(GameState.MIN_PLATFORM_LENGTH
                +  Math.random() * (GameState.MAX_PLATFORM_LENGTH - GameState.MIN_PLATFORM_LENGTH));
        int left = GameState.platforms.getLast().getRect().left;
        // jeśli będzie poza ekranem zmieniamy stronę przesuwania platform
        // prawa ściana
        if (left + (2 + number) * GameState.PLATFORM_SIZE + GameState.PLATFORM_GAP_X > GameState.SCREEN_WIDTH) {
            GameState.PLATFORM_GAP_X *= -1;
            left = GameState.SCREEN_WIDTH - (2 + number) * GameState.PLATFORM_SIZE;
        } else if (left + GameState.PLATFORM_GAP_X < 0) {     // lewa ściana
            GameState.PLATFORM_GAP_X *= -1;
            left = 0;
        } else {    // brak kolizji
            left += GameState.PLATFORM_GAP_X;
        }
        // obliczanie wysokości następnej platformy
        int top = GameState.platforms.getLast().getRect().top - GameState.PLATFORM_GAP_Y;
        GameState.platforms.add(new Platform(left, top, number, movable, context));
        // co ileś na platformie będzie znajdować się moneta
        addCoin();
    }

    // dodawanie monet na platformach
    private void addCoin() {
        counterCoins += 1;
        if (counterCoins == GameState.COINS_FREQUENCY) {
            counterCoins = 0;
            int left = GameState.platforms.getLast().getRect().left;
            int right = GameState.platforms.getLast().getRect().right;
            int coinLeft = right - left - GameState.ARTEFACT_SIZE / 2;
            int top = GameState.platforms.getLast().getRect().top;
            int coinTop = top - GameState.ARTEFACT_SIZE - 20;
            GameState.artefacts.add(new Coin(coinLeft, coinTop, context));
        }
    }

    public void update(float deltaTime) {
        // pierwsza dodana do listy platforma
        IGameObject lowestPlatform = GameState.platforms.getFirst();
        if (lowestPlatform != null) {
            Rect lowestPlatformRect = lowestPlatform.getRect();
            // jeśli jest poniżej dolnego ekranu usuń ją i dodaj nową na górze
            if (lowestPlatformRect.top >= GameState.SCREEN_HEIGHT) {
                GameState.platforms.removeFirst();
                addPlatform();
            }
        }
        // usuwanie niezebranego artefaktu
        if (GameState.artefacts.size() > 0) {
            IGameObject lowestArtefact = GameState.artefacts.get(0);
            Rect lowestArtefactRect = lowestArtefact.getRect();
            if (lowestArtefactRect.top >= GameState.SCREEN_HEIGHT) {
                GameState.artefacts.remove(0);
            }
        }
        for (IGameObject gameObject : GameState.platforms) {
            gameObject.update(deltaTime);
        }
        for (IGameObject gameObject : GameState.artefacts) {
            gameObject.update(deltaTime);
        }
    }

    public void moveScene(int deltaY){
        for (IGameObject gameObject : GameState.platforms) {
            gameObject.moveScene(deltaY);
        }
        for (IGameObject gameObject : GameState.artefacts) {
            gameObject.moveScene(deltaY);
        }
    }

    @Override
    public Rect getRect() {
        return null;
    }

    public void draw(Canvas canvas) {
        for (IGameObject gameObject : GameState.platforms) {
            gameObject.draw(canvas);
        }
        for (IGameObject gameObject : GameState.artefacts) {
            gameObject.draw(canvas);
        }
        // rysuje ikonkę monety przy obecnej liczbie monet gracza
        canvas.drawBitmap(imageCoin, null, rectCoin, null);
    }
}
