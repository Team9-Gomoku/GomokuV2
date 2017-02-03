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
    private Paint blackPen = new Paint();
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
        blackPen.setStyle(Paint.Style.STROKE);
        greyPaint.setColor(Color.GRAY);
    }

    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns + 1;
        calculateDimensions();
    }

    public int getNumColumns() {
        return numColumns;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows + 1;
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

        cellWidth = getWidth() / numColumns;
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

        for (int i = 1; i < numColumns; i++) {
            for (int j = 1; j < numRows; j++) {
                if (cellChecked[i][j] != null) {
                    if (cellChecked[i][j] == "WHITE"){
                        canvas.drawCircle(i *cellWidth, j*cellHeight, cellWidth/3, greyPaint);
                    } else {
                        canvas.drawCircle(i*cellWidth, j*cellHeight, cellWidth/3, blackPaint);
                    }
                }
            }
        }

        for (int i = 1; i < numColumns; i++) {
            canvas.drawLine(i * cellWidth, cellHeight, i * cellWidth, (numRows-1) * cellHeight, blackPaint);
        }

        for (int i = 1; i < numRows; i++) {
            canvas.drawLine(cellWidth, i * cellHeight, (numRows-1) * cellWidth, i * cellHeight, blackPaint);

        }

        findWinner();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float xPosition = (float) event.getX()/cellWidth;
            float yPosition = (float) event.getY()/cellHeight;
            int column = (int)(Math.round(xPosition));
            int row = (int)(Math.round(yPosition));
            if (column >= numColumns || row >= numRows) {
                return false;
            }

            if(cellChecked[column][row] != null) {
                return false;
            }
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
