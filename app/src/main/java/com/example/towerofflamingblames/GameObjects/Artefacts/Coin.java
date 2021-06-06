package com.example.towerofflamingblames.GameObjects.Artefacts;

import com.example.towerofflamingblames.GameObjects.Generator;
import com.example.towerofflamingblames.GameObjects.Player;
import com.example.towerofflamingblames.GameState;
import com.example.towerofflamingblames.R;

public class Coin extends AbstractArtefact {

    public Coin(int left, int top) {
        super(left, top, GameState.bitmapsCoin);
    }

    @Override
    public void action(Player player) {
        player.addCoin();
        GameState.increaseDifficulty();
    }
}
