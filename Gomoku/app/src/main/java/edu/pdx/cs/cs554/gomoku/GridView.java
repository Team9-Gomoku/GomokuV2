package edu.pdx.cs.cs554.gomoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class GridView extends View {
    private int numColumns, numRows;
    private int cellWidth, cellHeight;
    private Paint blackPaint = new Paint();
    private Paint greyPaint = new Paint();
    private String[][] cellChecked;

    //Player 1 (WHITE) , if activePlayer = 0
    //Player 2 (BLACK) , if activePlayer = 1
    private int activePlayer = 0;

    public GridView(Context context) {
        this(context, null);
    }

    public GridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        blackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        greyPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        greyPaint.setColor(Color.GRAY);
    }

    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
        calculateDimensions();
    }

    public int getNumColumns() {
        return numColumns;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
        calculateDimensions();
    }

    public int getNumRows() {
        return numRows;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateDimensions();
    }

    private void calculateDimensions() {
        if (numColumns < 1 || numRows < 1) {
            return;
        }

        cellWidth = getWidth() / (numColumns + 1);
        cellHeight = cellWidth;

        cellChecked = new String[numColumns][numRows];
        //Log.i("INFO", "numCOlumns = " + numColumns + "\t " + "numRows = " + numRows);

        invalidate();
    }

    private boolean findWinner() {
        checkHorizontal("WHITE");
        checkHorizontal("BLACK");
        checkVertical("WHITE");
        checkVertical("BLACK");
        return checkRightDiagonal("WHITE");
    }

    private boolean checkHorizontal(String playerColor) {
        int score = 0;
        boolean isWinner = false;
        for (int row = 1; row < numRows; row++) {
            for (int column = 1; column < numColumns; column++) {
                if (cellChecked[column][row] == playerColor && score < 5) {
                    score++;
                    //Log.i("INFO", String.valueOf(score));
                } else if(score == 5 &&
                        ((cellChecked[column][row] == null) || (cellChecked[column-6][row] == null))) {
                    Log.i("INFO", playerColor + " IS THE WINNER");
                    isWinner = true;
                    break;
                } else{
                    isWinner = false;
                    score = 0;
                }
            }
            if(isWinner)
                break;
        }
        return isWinner;
    }

    private boolean checkVertical(String playerColor) {
        int score = 0;
        boolean isWinner = false;
        for (int column = 1; column < numColumns; column++) {
            for (int row = 1; row < numRows; row++) {
                if (cellChecked[column][row] == playerColor && score < 5) {
                    score++;
                    //Log.i("INFO", String.valueOf(score));
                } else if(score == 5 &&
                        ((cellChecked[column][row] == null) || (cellChecked[column][row-6] == null))) {
                    Log.i("INFO", playerColor + " IS THE WINNER");
                    isWinner = true;
                    break;
                } else{
                    isWinner = false;
                    score = 0;
                }
            }
            if(isWinner)
                break;
        }
        return isWinner;
    }

    private boolean checkRightDiagonal(String playerColor) {
        int score = 0;
        boolean isWinner = false;
        for( int k = 1 ; k < numColumns * 2 ; k++ ) {
            for( int column = 1 ; column <= k ; column++ ) {
                int row = k - column + 1;
                if( row < numColumns && column < numColumns ) {
                    if (cellChecked[column][row] == playerColor && score < 5) {
                        score++;
                    } else if (score == 5) {
                        Log.i("INFO", playerColor + " IS THE WINNER");
                        isWinner = true;
                        break;
                    } else{
                        isWinner = false;
                        score = 0;
                    }
                }
            }
            if(isWinner)
                break;
        }
        return isWinner;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        if (numColumns == 0 || numRows == 0) {
            return;
        }

        int width = getWidth();
        int height = getHeight();

        //This block will draw the grid based on the number of columns and rows.
        for (int i = 1; i < numColumns + 1; i++) {
            canvas.drawLine(i * cellWidth, cellHeight, i * cellWidth, numRows * cellHeight, blackPaint);
        }

        for (int i = 1; i < numRows + 1; i++) {
            canvas.drawLine(cellWidth, i * cellHeight, numRows * cellWidth, i * cellHeight, blackPaint);

        }

        //This block will draw the stone on the board
        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                if (cellChecked[i][j] != null) {
                    if (cellChecked[i][j] == "WHITE"){
                        canvas.drawCircle((i) *cellWidth, (j)*cellHeight, cellWidth/3, greyPaint);
                    } else {
                        canvas.drawCircle((i)*cellWidth, (j)*cellHeight, cellWidth/3, blackPaint);
                    }
                }
            }
        }

        findWinner();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            //This block of code will round up the to the board intersection position.
            float xPosition = (float) event.getX()/cellWidth;
            float yPosition = (float) event.getY()/cellHeight;
            int column = (int)(Math.round(xPosition));
            int row = (int)(Math.round(yPosition));

            /*DEBUG
            Log.d("DEBUG", "column: " + xPosition);
            Log.d("DEBUG", "row: " + yPosition);
            Log.d("DEBUG", "column: " + column);
            Log.d("DEBUG", "row: " + row);
            */

            //If position is out of the grid
            if (column <= 1 || row <= 1) {
                return false;
            }
            if (column >= numColumns || row >= numRows) {
                return false;
            }

            //If position is already placed by other stone
            if(cellChecked[column][row] != null) {
                return false;
            }

            //Alternate the stone color
            if (activePlayer == 0){
                cellChecked[column][row] = "WHITE";
                activePlayer = 1;
            } else {
                cellChecked[column][row] = "BLACK";
                activePlayer = 0;
            }


            Log.i("INFO", cellChecked[column][row] + ": "+ String.valueOf(column) + " , " + String.valueOf(row));
            invalidate();
        }

        return true;
    }
}
