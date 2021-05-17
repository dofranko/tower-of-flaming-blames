package com.example.towerofflamingblames.GameStatistics;

// dane do wyświetlania w recycleView
public class StatisticsGame {

    public String email;
    public String score;
    public String name;
    public String date;

    public StatisticsGame(String email, String score, String name, String date) {
        this.email = email;
        this.score = score;
        this.name = name;
        this.date = date;
    }
}
