package edu.pdx.cs.cs554.gomoku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
    }

    public void showMenu(View view) {
        setContentView(R.layout.menu);
    }

    public void createBoard10(View view) {
        createBoard(11);
    }

    public void createBoard15(View view) {
        createBoard(16);
    }

    public void createBoard20(View view) {
        createBoard(21);
    }

    private void createBoard(int size) {
        setContentView(R.layout.activity_main);

        Board board = (Board) findViewById(R.id.board);
        board.setNumColumns(size);
        board.setNumRows(size);
        board.setGameType(GameType.FREESTYLE);

        TimerView blackTimer = (TimerView) findViewById(R.id.timer_black);
        blackTimer.setPrefix("BLACK PLAYER ");
        blackTimer.start();

        TimerView whiteTimer = (TimerView) findViewById(R.id.timer_white);
        whiteTimer.setPrefix("WHITE PLAYER ");
    }
}
