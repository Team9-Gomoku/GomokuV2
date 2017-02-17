package edu.pdx.cs.cs554.gomoku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

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
        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setEmptyView(new GridAdapter(this));

        GridAdapter gridView1 = (GridAdapter) findViewById(R.id.grid_view);
        gridView1.setNumColumns(size);
        gridView1.setNumRows(size);
        gridView1.setMode(GameMode.FREESTYLE);

        TimerView blackTimer = (TimerView) findViewById(R.id.timer_black);
        blackTimer.setPrefix("BLACK PLAYER ");
        blackTimer.start();

        TimerView whiteTimer = (TimerView) findViewById(R.id.timer_white);
        whiteTimer.setPrefix("WHITE PLAYER ");

    }
}
