package edu.pdx.cs.cs554.gomoku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridView = new GridView(this);
        gridView.setNumColumns(10);
        gridView.setNumRows(10);

        setContentView(gridView);
    }
}
