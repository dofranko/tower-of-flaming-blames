package com.example.towerofflamingblames.GameObjects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.gifdecoder.StandardGifDecoder;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.towerofflamingblames.GameObjects.Artefacts.Coin;
import com.example.towerofflamingblames.GameObjects.Artefacts.Coins;
import com.example.towerofflamingblames.GameObjects.Artefacts.Hourglass;
import com.example.towerofflamingblames.GameObjects.Artefacts.Hurricane;
import com.example.towerofflamingblames.GameState;
import com.example.towerofflamingblames.GameSurface;
import com.example.towerofflamingblames.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;

public class Generator implements IGameObject {

    public final GameSurface context;
    private final Bitmap imageCoin;
    private final Rect rectCoin = new Rect(10, 10, 100 ,100);
    // odlicza, kiedy umieścić ruchomą platformę
    private int counterPlatforms = 0;
    // odlicza, kiedy umieścić monety
    private int counterCoins = 0;

    public Generator(GameSurface context) {
        this.context = context;
        this.imageCoin = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin0);

    }

    public void createObjects() {
        // na samym początku wygenerowanie bitmap
        createBitmaps();
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
        // co ileś na platformie będzie znajdować się:
        addCoin();
        if (GameState.COINS_FREQUENCY / 2 == counterCoins) {
            int i = (int) (Math.random() * 9);
            int[] positions = getPositionOfArtefact();
            if (i == 0)
                GameState.artefacts.add(new Coins(positions[0], positions[1]));
            else if (i == 4)
                GameState.artefacts.add(new Hourglass(positions[0], positions[1]));
            else if (i == 8)
                GameState.artefacts.add(new Hurricane(positions[0], positions[1]));
        }
    }

    // dodawanie monet na platformach
    private void addCoin() {
        counterCoins += 1;
        if (counterCoins >= GameState.COINS_FREQUENCY) {
            counterCoins = 0;
            int[] positions = getPositionOfArtefact();
            GameState.artefacts.add(new Coin(positions[0], positions[1]));
        }
    }

    private int[] getPositionOfArtefact() {
        int left = GameState.platforms.getLast().getRect().left;
        int right = GameState.platforms.getLast().getRect().right;
        int artefactLeft = (right - left) / 2 + left - GameState.ARTEFACT_SIZE / 2;
        int top = GameState.platforms.getLast().getRect().top;
        int artefactTop = top - GameState.ARTEFACT_SIZE;
        return new int[]{artefactLeft, artefactTop};
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

    // jednorazowe stworzenie bitmap
    public void createBitmaps() {
        GameState.bitmapsCoin = generateBitmapsFromGIF(R.drawable.coin);
        GameState.bitmapsCoins = generateBitmapsFromGIF(R.drawable.coins);
        GameState.bitmapsHourglass = generateBitmapsFromGIF(R.drawable.hourglass);
        GameState.bitmapsHurricane = generateBitmapsFromGIF(R.drawable.hurricane);
    }

    // metoda służąca do przkonwertowania GIFa na serię bitmap z użyciem biblioteki Glide
    private ArrayList<Bitmap> generateBitmapsFromGIF(int gif) {
        ArrayList<Bitmap> bitmaps = new ArrayList();
        Glide.with(context.getContext())
                .asGif()
                .load(gif)
                .into(new CustomTarget<GifDrawable>() {
                    @Override
                    public void onResourceReady(@NonNull GifDrawable resource, @Nullable Transition<? super GifDrawable> transition) {
                        try {
                            Object GifState = resource.getConstantState();
                            Field frameLoader = GifState.getClass().getDeclaredField("frameLoader");
                            frameLoader.setAccessible(true);
                            Object gifFrameLoader = frameLoader.get(GifState);
                            assert gifFrameLoader != null;
                            Field gifDecoder = gifFrameLoader.getClass().getDeclaredField("gifDecoder");
                            gifDecoder.setAccessible(true);
                            StandardGifDecoder standardGifDecoder = (StandardGifDecoder) gifDecoder.get(gifFrameLoader);
                            for (int i = 0; i < Objects.requireNonNull(standardGifDecoder).getFrameCount(); i++) {
                                standardGifDecoder.advance();
                                bitmaps.add(standardGifDecoder.getNextFrame());
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    @Override
                    public void onLoadCleared(@Nullable @org.jetbrains.annotations.Nullable Drawable placeholder) { }
                });
        return bitmaps;
    }
}
