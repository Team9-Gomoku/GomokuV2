package edu.pdx.cs.cs554.gomoku;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by boss on 2/20/17.
 */

public class Offline extends GridAdapter {
    public Offline(Context context) {
        super(context, null);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            //This block of code will round up the to the board intersection position.
            float xPosition = (float) event.getX()/cellWidth;
            float yPosition = (float) event.getY()/cellHeight;
            int column = (int)(Math.round(xPosition)) -1;
            int row = (int)(Math.round(yPosition))-1;

            //If position is out of the grid
            if (column < 0 || row < 0) {
                return false;
            }

            if (column >= numColumns || row >= numRows) {
                return false;
            }

            if (winner)
                return false;

            //If position is already placed by other stone
            if(cellChecked[column][row] != null) {
                return false;
            }

            //Alternate the stone color
            if (activePlayer == 0){
                cellChecked[column][row] = "WHITE";
                activePlayer = 1;
                ((TimerView) ((MainActivity) getContext()).findViewById(R.id.timer_white)).pause();
                if (!winner) {
                    ((TimerView) ((MainActivity) getContext()).findViewById(R.id.timer_black)).start();
                }
            } else {
                cellChecked[column][row] = "BLACK";
                activePlayer = 0;
                ((TimerView) ((MainActivity) getContext()).findViewById(R.id.timer_black)).pause();
                if (!winner) {
                    ((TimerView) ((MainActivity) getContext()).findViewById(R.id.timer_white)).start();
                }
            }


            Log.i("INFO", cellChecked[column][row] + ": "+ String.valueOf(column) + " , " + String.valueOf(row));
            invalidate();
        }

        return true;
    }
}
