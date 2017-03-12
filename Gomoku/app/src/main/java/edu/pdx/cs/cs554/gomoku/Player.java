package edu.pdx.cs.cs554.gomoku;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o instanceof Player) {
            Player anotherPlayer = (Player) o;
            return name.equals(anotherPlayer.name) &&
                    score == anotherPlayer.score &&
                    isBlack == anotherPlayer.isBlack &&
                    scoreIncremented == anotherPlayer.scoreIncremented;
        }
        return false;
    }

    // TODO override hashCode()
}
