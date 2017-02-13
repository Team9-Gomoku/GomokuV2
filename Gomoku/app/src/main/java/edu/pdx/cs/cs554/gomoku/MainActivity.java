package edu.pdx.cs.cs554.gomoku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridView = (GridView) findViewById(R.id.grid_view);
        gridView.setNumColumns(10);
        gridView.setNumRows(10);
        gridView.setMode("standard");

        TimerView blackTimer = (TimerView) findViewById(R.id.timer_black);
        blackTimer.setPrefix("BLACK PLAYER ");
        blackTimer.start();

        TimerView whiteTimer = (TimerView) findViewById(R.id.timer_white);
        whiteTimer.setPrefix("WHITE PLAYER ");
    }
}
