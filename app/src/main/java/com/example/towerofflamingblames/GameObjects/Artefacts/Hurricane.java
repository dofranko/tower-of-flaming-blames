package com.example.towerofflamingblames.GameObjects.Artefacts;

import com.example.towerofflamingblames.GameObjects.Generator;
import com.example.towerofflamingblames.GameObjects.Player;
import com.example.towerofflamingblames.GameState;
import com.example.towerofflamingblames.R;

public class Hurricane extends AbstractArtefact implements IGameArtefact {

    public Hurricane(int left, int top) {
        super(left, top, GameState.bitmapsHurricane);
    }

    @Override
    public void action(Player player) {
        player.setBooster("hurricane");
    }
}