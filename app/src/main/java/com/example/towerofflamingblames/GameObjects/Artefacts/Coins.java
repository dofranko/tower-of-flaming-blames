package com.example.towerofflamingblames.GameObjects.Artefacts;

import com.example.towerofflamingblames.GameObjects.Generator;
import com.example.towerofflamingblames.GameObjects.Player;
import com.example.towerofflamingblames.GameState;
import com.example.towerofflamingblames.R;

public class Coins extends AbstractArtefact {

    public Coins(int left, int top) {
        super(left, top, GameState.bitmapsCoins);
    }

    @Override
    public void action(Player player) {
        player.setBooster("coins");
    }
}