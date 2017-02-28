package edu.pdx.cs.cs554.gomoku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;



import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {
    Button boffline,bonline,bai ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

   }

    public void showMenu(View view) {
        setContentView(R.layout.menu);
    }

    public void startGame(View view) {
        startGame(getGameType(), getGameMode(), getBoardSize());
    }

    public void playGame(View view) {
        playGame(getGameType(), getGameMode(), getBoardSize());
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

    public GameMode getGameMode() {
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

    public int getBoardSize() {
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




    public void startGame(GameType gameType, GameMode gameMode, int boardSize) {

        if (gameMode != GameMode.ONLINE) {
            setContentView(R.layout.activity_main);

            Board board = (Board) findViewById(R.id.board);
            board.setNumColumns(boardSize);
            board.setNumRows(boardSize);
            board.setGameType(gameType);
            board.setGameMode(gameMode);

            TimerView blackTimer = (TimerView) findViewById(R.id.timer_black);
            blackTimer.setPrefix("BLACK PLAYER ");
            blackTimer.start();

            TimerView whiteTimer = (TimerView) findViewById(R.id.timer_white);
            whiteTimer.setPrefix("WHITE PLAYER ");
        }

        else
        {

            setContentView(R.layout.client_activity_main);
            Intent myIntent = new Intent(MainActivity.this, ClientActivity.class);
           // myIntent.putExtra("key", value); //Optional parameters
            MainActivity.this.startActivity(myIntent);
          //  Intent intent = new Intent(getApplicationContext(), ClientActivity.class);

         //   startActivity(intent);
        }

        }


    public void playGame(GameType gameType, GameMode gameMode, int boardSize) {

        setContentView(R.layout.activity_main);

        Board board = (Board) findViewById(R.id.board);
        board.setNumColumns(boardSize);
        board.setNumRows(boardSize);
        board.setGameType(gameType);
        board.setGameMode(gameMode);

        TimerView blackTimer = (TimerView) findViewById(R.id.timer_black);
        blackTimer.setPrefix("BLACK PLAYER ");
        blackTimer.start();

        TimerView whiteTimer = (TimerView) findViewById(R.id.timer_white);
        whiteTimer.setPrefix("WHITE PLAYER ");


    }






}
