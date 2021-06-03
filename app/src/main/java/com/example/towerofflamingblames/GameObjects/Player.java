package com.example.towerofflamingblames.GameObjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.example.towerofflamingblames.GameState;

public class Player implements IGameObject {

    private final Rect rect;
    private final Bitmap image;
    private boolean canJump = false;
    private int coins = 0;
    private final Paint paint;
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

    public Player(Bitmap image, int square_size){
        this.image = image;
        this.pos = new MyVector((int) (GameState.SCREEN_WIDTH / 2),
                (int) (GameState.SCREEN_HEIGHT /2 - 100 - square_size));
        this.rect = new Rect((int)pos.x,(int)pos.y,
                (int)pos.x+square_size,(int)pos.y+square_size);
        this.paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(80);
    }

    @Override
    public void update(float deltaTime) {
        int n = 1;
        boolean doExit = false; //gdy gracz już ma kolizję z podłożem to koniec "próbkowania"
        if(this.vel.y >= 0) {
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


    public void updateFallingPosition(float deltaTime){
        int temp = (int) (GameState.PLATFORM_MOVABLE_SPEED_Y * deltaTime);
        this.pos.y += temp;
        this.rect.top += temp;
        this.rect.bottom += temp;
    }

    public void jump(){
        if(!canJump) return;
        this.vel.y = -GameState.PLAYER_JUMP_VELOCITY;
        canJump = false;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, null, rect, null);
        canvas.drawText(String.valueOf(coins), 120, 80, paint);
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

    public void setHorizontalVelocity(float x){
        this.vel.x = x;
    }

    public void addCoin() {
        coins += 1;
    }
}
