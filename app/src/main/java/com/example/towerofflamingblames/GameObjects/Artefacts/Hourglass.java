package com.example.towerofflamingblames.GameObjects.Artefacts;

import android.util.Log;

import com.example.towerofflamingblames.GameObjects.Generator;
import com.example.towerofflamingblames.GameObjects.Player;
import com.example.towerofflamingblames.GameState;
import com.example.towerofflamingblames.R;

public class Hourglass extends AbstractArtefact {

    public Hourglass(int left, int top) {
        super(left, top, GameState.bitmapsHourglass);
    }

    @Override
    public void action(Player player) {
        player.setBooster("hourglass");
    }
}
