package edu.pdx.cs.cs554.gomoku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.RadioButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);


    }

    public void nextMessage(View view) {

        setContentView(R.layout.menu);

    }

    public void resetMethod(View view) {

        setContentView(R.layout.menu);

    }
/*
    public void setStandard(View view) {

        GridView gridView = (GridView) findViewById(R.id.gridview);
     //   gridView.setEmptyView(new GridAdapter(this));

        GridAdapter gridView2 = (GridAdapter) findViewById(R.id.grid_view);
       // gridView1.setNumColumns(11);
       // gridView1.setNumRows(11);
        gridView2.setMode("standard");


    }

    public void setFreestyle(View view) {


        GridView gridView = (GridView) findViewById(R.id.gridview);
       // gridView.setEmptyView(new GridAdapter(this));

        GridAdapter gridView3 = (GridAdapter) findViewById(R.id.grid_view);
        // gridView1.setNumColumns(11);
        // gridView1.setNumRows(11);
        gridView3.setMode("freestyle");

    }
*/

  //  public void playGame(View view) {

      //  RadioButton rb = (RadioButton) findViewById(R.id.ten);

 //   }


    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {

        setContentView(R.layout.activity_main);
        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setEmptyView(new GridAdapter(this));

        GridAdapter gridView1 = (GridAdapter) findViewById(R.id.grid_view);
        gridView1.setNumColumns(11);
        gridView1.setNumRows(11);
        gridView1.setMode("freestyle");

     //   RadioButton gridViewformode = (RadioButton) findViewById(R.id.freestyle);
       // gridViewformode.(new GridAdapter(this));



        TimerView blackTimer = (TimerView) findViewById(R.id.timer_black);
        blackTimer.setPrefix("PLAYER 1 ");
        blackTimer.start();

        TimerView whiteTimer = (TimerView) findViewById(R.id.timer_white);
        whiteTimer.setPrefix("PLAYER 2 ");

    }

    public void sendMessage2(View view) {

        setContentView(R.layout.activity_main);
        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setEmptyView(new GridAdapter(this));

        GridAdapter gridView1 = (GridAdapter) findViewById(R.id.grid_view);
        gridView1.setNumColumns(21);
        gridView1.setNumRows(21);
       gridView1.setMode("freestyle");

        TimerView blackTimer = (TimerView) findViewById(R.id.timer_black);
        blackTimer.setPrefix("PLAYER 1 ");
        blackTimer.start();

        TimerView whiteTimer = (TimerView) findViewById(R.id.timer_white);
        whiteTimer.setPrefix("PLAYER 2 ");

    }


    public void sendMessage3(View view) {

        setContentView(R.layout.activity_main);
        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setEmptyView(new GridAdapter(this));

        GridAdapter gridView1 = (GridAdapter) findViewById(R.id.grid_view);
        gridView1.setNumColumns(16);
        gridView1.setNumRows(16);
       gridView1.setMode("freestyle");

        TimerView blackTimer = (TimerView) findViewById(R.id.timer_black);
        blackTimer.setPrefix("PLAYER 1 ");
        blackTimer.start();

        TimerView whiteTimer = (TimerView) findViewById(R.id.timer_white);
        whiteTimer.setPrefix("PLAYER 2 ");

    }

}
