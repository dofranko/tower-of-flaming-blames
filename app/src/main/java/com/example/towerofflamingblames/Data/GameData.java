package com.example.towerofflamingblames.Data;

public class GameData {
    public String name;
    public String date;
    public Long scores;

    public GameData() { }

    public GameData(String name, String date, Long scores) {
        this.name = name;
        this.date = date;
        this.scores = scores;
    }
}
