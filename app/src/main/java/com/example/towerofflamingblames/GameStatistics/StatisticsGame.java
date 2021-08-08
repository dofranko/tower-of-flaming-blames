package com.example.towerofflamingblames.GameStatistics;

// dane do wyświetlania w recycleView
public class StatisticsGame {

    public String leftUp;
    public String rightUp;
    public String leftDown;
    public String rightDown;

    public StatisticsGame() { }

    public StatisticsGame(String leftUp, String rightUp, String leftDown, String rightDown) {
        this.leftUp = leftUp;
        this.rightUp = rightUp;
        this.leftDown = leftDown;
        this.rightDown = rightDown;
    }
}
