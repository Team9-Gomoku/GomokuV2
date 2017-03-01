package edu.pdx.cs.cs554.gomoku;

public class Player {
    private final String name;
    private int score;
    private final boolean isBlack;
    private boolean scoreIncremented = false;

    public Player(String name, int score, boolean isBlack) {
        this.name = name;
        this.score = score;
        this.isBlack = isBlack;
    }

    public boolean isWhite() {
        return !isBlack;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void incrementScore() {
        if (!scoreIncremented) {
            score++;
        }
        scoreIncremented = true;
    }

}
