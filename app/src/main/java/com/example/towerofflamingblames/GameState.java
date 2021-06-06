package com.example.towerofflamingblames;

import android.graphics.Bitmap;

import com.example.towerofflamingblames.GameObjects.Artefacts.IGameArtefact;
import com.example.towerofflamingblames.GameObjects.IGameObject;

import java.util.ArrayList;
import java.util.LinkedList;

public class GameState {

    public static void restart(int screen_width, int screen_height){
        SCREEN_WIDTH = screen_width;
        SCREEN_HEIGHT = screen_height;
        MAX_PLATFORM_LENGTH = SCREEN_WIDTH / PLATFORM_SIZE - 1;
        MIN_PLATFORM_LENGTH = SCREEN_WIDTH / PLATFORM_SIZE - 2;
        if(MAX_PLATFORM_LENGTH == 0){
            MAX_PLATFORM_LENGTH = 1;
        }
        if(MIN_PLATFORM_LENGTH == 0){
            MAX_PLATFORM_LENGTH = 1;
        }
        PLAYER_JUMP_VELOCITY = 26.5f;
        PLAYER_GRAVITY_VELOCITY = 0.4f;
        ACTUAL_DIFFICULTY  = 0;
        PLATFORM_MOVABLE_SPEED_Y = 2;
        PLATFORM_MOVABLE_SPEED_X = 3;
        PLATFORM_GAP_Y = (int)(SCREEN_HEIGHT / PLAYER_HEIGHT_PERCENTAGE * 1.8);
        PLATFORM_GAP_X = 100;
        MOVABLE_PLATFORMS_FREQUENCY  = 10;
        COINS_FREQUENCY = 5;
        artefacts  = new ArrayList<>();
        coins_since_difficulty_increased = 0;
        platforms  = new LinkedList<>();
    }
    //sbr - set by restart

    public static int SCREEN_WIDTH; //sbr
    public static int SCREEN_HEIGHT; //sbr
    /*
    ==========
    PLAYER
    =========
     */
    public final static int PLAYER_HEIGHT_PERCENTAGE = 10;
    public static float PLAYER_JUMP_VELOCITY; //sbr
    public static float PLAYER_GRAVITY_VELOCITY; //sbr

    /*
    ============
    GAME
    ===========
     */
    //aktualny poziom trudności gry w trakcie rozgrywki
    public static int ACTUAL_DIFFICULTY; //sbr
    public final static int MAX_DIFFICULTY = 5;
    public final static int INCREASE_DIFFICULTY_COINS_REQUIREMENT = 4;
    private static int coins_since_difficulty_increased = 0;
    public static void increaseDifficulty(){
        coins_since_difficulty_increased++;
        if(coins_since_difficulty_increased >= INCREASE_DIFFICULTY_COINS_REQUIREMENT) {
            coins_since_difficulty_increased = 0;
            if (ACTUAL_DIFFICULTY < MAX_DIFFICULTY) {
                ACTUAL_DIFFICULTY++;
                if(ACTUAL_DIFFICULTY == MAX_DIFFICULTY){
                    MIN_PLATFORM_LENGTH = 0;
                    MAX_PLATFORM_LENGTH = 2;
                }
                else{
                    int tmp_delta_min_platform = MIN_PLATFORM_LENGTH/MAX_DIFFICULTY;
                    if(tmp_delta_min_platform > 1){
                        MIN_PLATFORM_LENGTH -= tmp_delta_min_platform;
                    }
                    else{
                        MIN_PLATFORM_LENGTH -= 1;
                    }
                    if(MIN_PLATFORM_LENGTH < MIN_GLOBALLY_PLATFORM_LENGTH)
                        MIN_PLATFORM_LENGTH = MIN_GLOBALLY_PLATFORM_LENGTH;

                    int tmp_delta_max_platform = MAX_PLATFORM_LENGTH/MAX_DIFFICULTY;
                    if(tmp_delta_max_platform > 1){
                        MAX_PLATFORM_LENGTH -= tmp_delta_min_platform;
                    }
                    else{
                        MAX_PLATFORM_LENGTH -= 1;
                    }
                    if(MAX_PLATFORM_LENGTH < 2) MAX_PLATFORM_LENGTH = 2;
                }
                if ((int)(PLATFORM_MOVABLE_SPEED_X * 1.5) == PLATFORM_MOVABLE_SPEED_X){
                    PLATFORM_MOVABLE_SPEED_X++;
                }
                else{
                    PLATFORM_MOVABLE_SPEED_X *= 1.5;
                }
                if ((int)(PLATFORM_MOVABLE_SPEED_Y * 1.3) == PLATFORM_MOVABLE_SPEED_Y){
                    PLATFORM_MOVABLE_SPEED_Y++;
                }
                else{
                    PLATFORM_MOVABLE_SPEED_Y *= 1.3;
                }
                PLATFORM_GAP_X *= 1.3;
                if(PLATFORM_GAP_X > SCREEN_WIDTH /3){
                    PLATFORM_GAP_X = SCREEN_WIDTH /3;
                }
            }
            MOVABLE_PLATFORMS_FREQUENCY--;
            COINS_FREQUENCY++;
            if(MOVABLE_PLATFORMS_FREQUENCY < 1) MOVABLE_PLATFORMS_FREQUENCY = 1;
        }
    }

    /*
    ==========
    PLATFORMS
    ==========
     */
    // lista platfrom
    public static LinkedList<IGameObject> platforms; //sbr
    // wielkość pojedynczej kostki platformy
    public final static int PLATFORM_SIZE = 100;
    // wskazuje z ilu kostek maksymalnie może składać się platforma
    public static int MAX_PLATFORM_LENGTH; //sbr
    public static int MIN_PLATFORM_LENGTH; //sbr
    public final static int MIN_GLOBALLY_PLATFORM_LENGTH = 0;
    // płynne przesuwanie na boki oraz w dół platformy
    public static int PLATFORM_MOVABLE_SPEED_X; //sbr
    public static int PLATFORM_MOVABLE_SPEED_Y; //sbr
    // co ile plaform ma się pojawiać ruchoma platforma
    public static int MOVABLE_PLATFORMS_FREQUENCY; //sbr
    // odległość pomiędzy sąsiednimi platformami w szerokości
    public static int PLATFORM_GAP_X;
    // odległość pomiędzy sąsiednimi platformami w wysokości
    public static int PLATFORM_GAP_Y; //sbr

    // rozmiar różnych artefaktów w grze
    public final static int ARTEFACT_SIZE = 100;
    // lista artefaktów
    public static ArrayList<IGameArtefact> artefacts; //sbr
    // bitmapy artefaktów
    public static ArrayList<Bitmap> bitmapsCoin;
    public static ArrayList<Bitmap> bitmapsCoins;
    public static ArrayList<Bitmap> bitmapsHourglass;
    public static ArrayList<Bitmap> bitmapsHurricane;
    // częstotliwość występowania monet
    public static int COINS_FREQUENCY; //sbr

    // spowolnienie poruszania się "okna",gdy gracz jest powyżej połowy ekranu
    public final static float MOVE_SCENE_SLOW_RATIO = 0.03f;

    /*
    ==========
    CONTROLS
    ==========
     */
    //minimalna czułość sterowania przechylając ekran
    public final static float MIN_TILT_DEVICE_TO_MOVE = 0.3f;
}
