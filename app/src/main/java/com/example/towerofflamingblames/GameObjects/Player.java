package com.example.towerofflamingblames.GameObjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;

import com.example.towerofflamingblames.GameState;
import com.example.towerofflamingblames.R;

public class Player implements IGameObject {

    private final Rect rect;
    private final Bitmap[] frames;
    private int currentBitmap = 0;
    private boolean canJump = false;
    private int coins = 0;
    private final Paint paint;
    private final Paint paintBooster;
    private boolean isBoosterCoinsActive = false;
    private boolean isBoosterHourglassActive = false;
    private boolean isBoosterHurricaneActive = false;
    private int diffCoinsFrequency;
    private int diffPlatformSpeedY;
    private float diffJumpVelocity;
    private float durationCoins = 0;
    private float durationHourglass = 0;
    private float durationHurricane = 0;
    private static class MyVector{
        public float x;
        public float y;
        public MyVector(){
            this.x = 0.0f;
            this.y = 0.0f;
        }
        public MyVector(float x, float y){
            this.x = x;
            this.y = y;
        }
    }
    private final MyVector vel = new MyVector();
    private final MyVector acc = new MyVector(0.0f, GameState.PLAYER_GRAVITY_VELOCITY);
    private final MyVector pos;

    public Player(Context context, int square_size){
        frames = new Bitmap[6];
        frames[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.stand_left);
        frames[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.stand_right);
        frames[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.jump_left);
        frames[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.jump_right);
        frames[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.fall_left);
        frames[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.fall_right);
        this.pos = new MyVector((int) (GameState.SCREEN_WIDTH / 2),
                (int) (GameState.SCREEN_HEIGHT /2 - 100 - square_size));
        this.rect = new Rect((int)pos.x,(int)pos.y,
                (int)pos.x+square_size,(int)pos.y+square_size);
        this.paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(80);
        this.paintBooster = new Paint();
        paintBooster.setColor(Color.YELLOW);
        paintBooster.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
        paintBooster.setTextSize(GameState.SCREEN_WIDTH / 10);
    }

    @Override
    public void update(float deltaTime) {
        boosterUpdate(deltaTime);
        int n = 1;
        boolean doExit = false; //gdy gracz już ma kolizję z podłożem to koniec "próbkowania"
        if(this.vel.y >= 0) {
            if (currentBitmap == 2)
                currentBitmap = 4;
            else if (currentBitmap == 3)
                currentBitmap = 5;
            n = (Math.abs((int) ((this.vel.y + this.acc.y * deltaTime + 0.5f * this.acc.y) * deltaTime))) / (GameState.PLATFORM_SIZE / 4) + 1;
            deltaTime /= n;
        }
        /**
         * Na potrzeby detekcji, jeśli gracz spada to jest "próbkowe" sprawdzanie kolizji - jeśli spada szybko,
         * dzięki temu mniejsza szansa na ominięcie sprawdzania kolizji z platformą (która ma wąską kolizję)
         */
        for (int k =0; k< n; k++) {
            this.vel.y += this.acc.y * deltaTime;
            int tmppos = (int) this.pos.y;
            this.pos.y += (int) ((this.vel.y + 0.5f * this.acc.y) * deltaTime);
            Log.d("difff", String.valueOf(tmppos - this.pos.y));
            this.vel.x *= 0.9;
            if (this.pos.x + this.vel.x * deltaTime >= 0
                    && this.getRect().right + this.vel.x * deltaTime <= GameState.SCREEN_WIDTH) {
                this.pos.x += (this.vel.x) * deltaTime;
            }

            if (Math.abs(this.vel.x) < 0.001f) {
                this.vel.x = 0.0f;
            }

            this.rect.set((int) this.pos.x, (int) this.pos.y, (int) this.pos.x + this.rect.width(),
                    (int) this.pos.y + this.rect.height());

            // iterowanie po platformach w celu sprawdzenia kolizji
            if (this.vel.y >= 0) {
                for (IGameObject object : GameState.platforms) {
                    if (Rect.intersects(this.rect, object.getRect())) {
                        if (this.getRect().bottom < object.getRect().top + GameState.PLATFORM_SIZE / 4) {
                            this.pos.y = object.getRect().top - getRect().height();
                            this.vel.y = 0;
                            canJump = true;
                            doExit = true;
                            if (currentBitmap == 4)
                                currentBitmap = 0;
                            else if (currentBitmap == 5)
                                currentBitmap = 1;
                        }
                    }
                }
            }

            // iterowanie po artefaktach w celu sprawdzenia kolizji
            for (int i = 0; i < GameState.artefacts.size(); i++) {
                if (Rect.intersects(this.rect, GameState.artefacts.get(i).getRect())) {
                    GameState.artefacts.get(i).action(this);
                    GameState.artefacts.remove(i);
                }
            }
            if(doExit) break;
        }

    }

    private void boosterUpdate(float deltaTime) {
        if (isBoosterCoinsActive) {
            durationCoins += deltaTime;
            if (durationCoins > 1000) {
                isBoosterCoinsActive = false;
                GameState.COINS_FREQUENCY += this.diffCoinsFrequency;
                durationCoins = 0;
            }
        }
        if (isBoosterHourglassActive) {
            durationHourglass += deltaTime;
            if (durationHourglass > 500) {
                isBoosterHourglassActive = false;
                GameState.PLATFORM_MOVABLE_SPEED_Y += this.diffPlatformSpeedY;
                durationHourglass = 0;
            }
        }
        if (isBoosterHurricaneActive) {
            durationHurricane += deltaTime;
            if (durationHurricane > 500) {
                isBoosterHurricaneActive = false;
                GameState.PLAYER_JUMP_VELOCITY -= this.diffJumpVelocity;
                durationHurricane = 0;
            }
        }
    }


    public void updateFallingPosition(float deltaTime){
        int temp = (int) (GameState.PLATFORM_MOVABLE_SPEED_Y * deltaTime);
        this.pos.y += temp;
        this.rect.top += temp;
        this.rect.bottom += temp;
    }

    public void jump() {
        if(!canJump) return;
        if (currentBitmap == 0)
            currentBitmap = 2;
        else
            currentBitmap = 3;
        this.vel.y = -GameState.PLAYER_JUMP_VELOCITY;
        canJump = false;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(frames[currentBitmap], null, rect, null);
        canvas.drawText(String.valueOf(coins), 120, 80, paint);
        if (isBoosterCoinsActive && durationCoins < 100) {
            paintBooster.setAlpha(500 - (int) (durationCoins) * 2);
            canvas.drawText("More artefacts!", (int) (GameState.SCREEN_WIDTH / 10 * 2), GameState.SCREEN_HEIGHT - 200, paintBooster);
        }
        if (isBoosterHourglassActive && durationHourglass < 100) {
            paintBooster.setAlpha(500 - (int) (durationHourglass) * 2);
            canvas.drawText("More time!", (int) (GameState.SCREEN_WIDTH / 10 * 3), GameState.SCREEN_HEIGHT - 200, paintBooster);
        }
        if (isBoosterHurricaneActive && durationHurricane < 100) {
            paintBooster.setAlpha(500 - (int) (durationHurricane) * 2);
            canvas.drawText("Higher jumps!", (int) (GameState.SCREEN_WIDTH / 10 * 2), GameState.SCREEN_HEIGHT - 200, paintBooster);
        }
    }

    @Override
    public void moveScene(int deltaY) {
        rect.top += deltaY;
        rect.bottom += deltaY;
        pos.y += deltaY;
    }

    @Override
    public Rect getRect() {
        return this.rect;
    }

    public int getCoins() {
        return this.coins;
    }

    public void setHorizontalVelocity(float x) {
        if (x < 0.1 && x > -0.1) return;
        this.vel.x = x;
        if (x < 0) {
            if (canJump)
                currentBitmap = 0;
            else if (this.vel.y > 0)
                currentBitmap = 4;
            else
                currentBitmap = 2;
        } else {
            if (canJump)
                currentBitmap = 1;
            else if (this.vel.y > 0)
                currentBitmap = 5;
            else
                currentBitmap = 3;
        }
    }

    // ustawia boostery
    // jeżeli ten sam rodzaj boosteru będzie się nakładał, zresetuj duration
    public void setBooster(String name) {
        if (name.equals("coins")) {
            if (this.isBoosterCoinsActive)
                this.durationCoins = 0;
            else {
                int previousCoinsFrequency = GameState.COINS_FREQUENCY;
                GameState.COINS_FREQUENCY *= 0.8;
                this.diffCoinsFrequency = previousCoinsFrequency - GameState.COINS_FREQUENCY;
                this.isBoosterCoinsActive = true;
            }
        }
        else if (name.equals("hourglass")) {
            if (this.isBoosterHourglassActive)
                this.durationHourglass = 0;
            else {
                int previousPlatformSpeedY = GameState.PLATFORM_MOVABLE_SPEED_Y;
                GameState.PLATFORM_MOVABLE_SPEED_Y *= 0.8;
                this.diffPlatformSpeedY = previousPlatformSpeedY - GameState.PLATFORM_MOVABLE_SPEED_Y;
                this.isBoosterHourglassActive = true;
            }
        }
        else if (name.equals("hurricane")) {
            if (this.isBoosterHurricaneActive) {
                this.durationHurricane = 0;
            } else {
                float previousJumpVelocity = GameState.PLAYER_JUMP_VELOCITY;
                GameState.PLAYER_JUMP_VELOCITY *= 1.2;
                this.diffJumpVelocity = GameState.PLAYER_JUMP_VELOCITY - previousJumpVelocity;
                this.isBoosterHurricaneActive = true;
            }
        }
    }

    public void addCoin() {
        coins += 1;
    }
}
