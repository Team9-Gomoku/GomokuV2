package edu.pdx.cs.cs554.gomoku;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
    }

    public void showMenu(View view) {
        setContentView(R.layout.menu);
    }

    public void startGame(View view) {
        GameMode gameMode = getGameMode();
        Editable blackPlayerName = ((EditText) findViewById(R.id.black_player)).getText();
        Editable whitePlayerName = ((EditText) findViewById(R.id.white_player)).getText();
        if (gameMode.equals(GameMode.OFFLINE) && blackPlayerName.toString().trim().isEmpty()) {
            blackPlayerName.insert(0, "can't be empty");
            return;
        }
        if (gameMode.equals(GameMode.OFFLINE) && whitePlayerName.toString().trim().isEmpty()) {
            whitePlayerName.insert(0, "can't be empty");
            return;
        }
        if (gameMode.equals(GameMode.AI) && blackPlayerName.toString().trim().isEmpty()) {
            blackPlayerName.insert(0, "can't be empty");
            return;
        }
        startGame(getGameType(), gameMode, getBoardSize(),
            blackPlayerName.toString().trim(), whitePlayerName.toString().trim());
    }

    private GameType getGameType() {
        RadioGroup gameTypeButtons = (RadioGroup) findViewById(R.id.game_type_buttons);
        switch (gameTypeButtons.getCheckedRadioButtonId()) {
            case R.id.standard:
                return GameType.STANDARD;
            case R.id.freestyle:
                return GameType.FREESTYLE;
        }
        throw new IllegalStateException("This cannot happen!");
    }

    private GameMode getGameMode() {
        RadioGroup gameModeButtons = (RadioGroup) findViewById(R.id.mode_buttons);
        switch (gameModeButtons.getCheckedRadioButtonId()) {
            case R.id.offline:
                return GameMode.OFFLINE;
            case R.id.ai:
                return GameMode.AI;
            case R.id.online:
                return GameMode.ONLINE;
        }
        throw new IllegalStateException("This cannot happen!");
    }

    private int getBoardSize() {
        RadioGroup boardSizeButtons = (RadioGroup) findViewById(R.id.board_size_buttons);
        switch (boardSizeButtons.getCheckedRadioButtonId()) {
            case R.id.ten:
                return 11;
            case R.id.fifteen:
                return 16;
            case R.id.twenty:
                return 21;
        }
        throw new IllegalStateException("This cannot happen!");
    }

    private void startGame(GameType gameType, GameMode gameMode, int boardSize,
        String blackPlayerName, String whitePlayerName) {
        setContentView(R.layout.activity_main);

        Board board = (Board) findViewById(R.id.board);
        board.setNumColumns(boardSize);
        board.setNumRows(boardSize);
        board.setGameType(gameType);
        board.setGameMode(gameMode);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        int blackPlayerScore = sharedPref.getInt(blackPlayerName, 0);
        int whitePlayerScore = sharedPref.getInt(whitePlayerName, 0);

        Log.i("INFO", "Creating black player with score: " + blackPlayerScore);
        Player bp = new Player(blackPlayerName, blackPlayerScore, true);
        board.setBlackPlayer(bp);
        Log.i("INFO", "Creating white player with score: " + whitePlayerScore);
        board.setWhitePlayer(new Player(whitePlayerName, whitePlayerScore, false));
        board.setActivePlayer(bp);

        TimerView blackTimer = (TimerView) findViewById(R.id.timer_black);
        blackTimer.setPrefix("BLACK PLAYER ");
        blackTimer.setVisibility(View.VISIBLE);
        blackTimer.start();

        TimerView whiteTimer = (TimerView) findViewById(R.id.timer_white);
        if (gameMode.equals(GameMode.AI)) {
            whiteTimer.setPrefix("COMPUTER     ");
            whiteTimer.setText(whiteTimer.getPrefix() + String.format("%02d:%02d", 10, 00));
        } else {
            whiteTimer.setPrefix("WHITE PLAYER ");
        }
        whiteTimer.setVisibility(View.VISIBLE);
    }

    public void showScores(View view) {
        setContentView(R.layout.activity_scores);
        GridView scores = (GridView) findViewById(R.id.scores);
        scores.setAdapter(new ScoreAdapter((Activity) scores.getContext()));
    }

    public void hideWhitePlayer(View view) {
        EditText whitePlayerName = (EditText) findViewById(R.id.white_player);
        TextView whitePlayerLabel = (TextView) findViewById(R.id.white_player_label);
        whitePlayerName.setVisibility(View.INVISIBLE);
        whitePlayerLabel.setVisibility(View.INVISIBLE);
    }

    public void showWhitePlayer(View view) {
        EditText whitePlayerName = (EditText) findViewById(R.id.white_player);
        TextView whitePlayerLabel = (TextView) findViewById(R.id.white_player_label);
        whitePlayerName.setVisibility(View.VISIBLE);
        whitePlayerLabel.setVisibility(View.VISIBLE);
    }
}
