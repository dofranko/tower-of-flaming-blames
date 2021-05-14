package com.example.towerofflamingblames;

import com.example.towerofflamingblames.GameObjects.IGameObject;

import java.util.Queue;

public class GameState {

    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static Queue<IGameObject> platforms;
    public final static int PLAYER_HEIGHT_PERCENTAGE = 10;
    // wielkość pojedynczej kostki platformy
    public static int PLATFORM_SIZE = 100;
    // wskazuje z ilu kostek maksymalnie może składać się platforma
    public static int MAX_PLATFORM_LENGTH = 5;
    // płynne przesuwanie na boki oraz w dół platformy
    public static int MOVABLE_X = 5;
    public static int MOVABLE_Y = 5;
    // co ile plaform ma się pojawiać ruchoma platforma
    public static int COUNTER_MOVABLE_PLATFORMS = 5;
    // odległość pomiędzy sąsiednimi platformami w szerokości
    public static int PLATFORM_GAP = 100;
}
