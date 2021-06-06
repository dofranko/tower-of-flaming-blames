package com.example.towerofflamingblames.GameObjects.Artefacts;

import com.example.towerofflamingblames.GameObjects.IGameObject;
import com.example.towerofflamingblames.GameObjects.Player;

public interface IGameArtefact extends IGameObject {
    void action(Player player);
}