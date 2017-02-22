package edu.pdx.cs.cs554.gomoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class Board extends View {
    protected int numColumns, numRows;
    protected int cellWidth, cellHeight;
    protected boolean winner = false;
    protected GameMode mode = GameMode.STANDARD;
    private Paint blackPaint = new Paint();
    private Paint whitePaint = new Paint();
    protected String[][] cellChecked;
    private String mode2 = "OFFLINE2";
    //private String mode2 = "AI";


    //Player 1 (WHITE) , if activePlayer = 0
    //Player 2 (BLACK) , if activePlayer = 1
    protected int activePlayer = 1;

    public Board(Context context) {
        this(context, null);
    }

    public Board(Context context, AttributeSet attrs) {
        super(context, attrs);
        blackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        blackPaint.setStrokeWidth(8);
        whitePaint.setColor(Color.WHITE);
    }

    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
        calculateDimensions();
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
        calculateDimensions();
    }

    public void setMode(GameMode mode) {
        this.mode = mode;
        Log.i("INFO", "mode is set to " + this.mode);
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
        blockEdgeBoard();
        invalidate();
    }

    private void resetBoard() {
        cellChecked = new String[numColumns][numRows];
        blockEdgeBoard();
        invalidate();
    }

    //Put invisible stones on the edge of the board
    //So black and white cannot be placed on board edge
    private void blockEdgeBoard() {
        for(int column = 0; column < numColumns; column++) {
            //Log.i("INFO", String.valueOf(column) + "," + String.valueOf(0));
            cellChecked[column][0] = "BLANK";
            cellChecked[column][numRows-1] = "BLANK";
        }

        for(int row = 0; row < numRows; row++) {
            cellChecked[0][row] = "BLANK";
            cellChecked[numColumns-1][row] = "BLANK";
        }
    }

    //=========CHECK WINNER=============
    private boolean findWinner() {
        return checkHorizontal("WHITE") ||
                checkHorizontal("BLACK") ||
                checkVertical("WHITE") ||
                checkVertical("BLACK") ||
                checkLeftDiagonal("WHITE") ||
                checkLeftDiagonal("BLACK") ||
                checkRightDiagonal("WHITE") ||
                checkRightDiagonal("BLACK");
    }

    //Check if the end is blocked
    private boolean isNotBlockedEnd(int column, int row, String playerColor) {
        return (cellChecked[column][row]) == playerColor;
    }

    private boolean isNotBlockedEnd(int column, int row) {
        return (cellChecked[column][row]) == null;
    }

    //Find Winner by doing horizontal check.
    //Return true if found a winner
    private boolean checkHorizontal(String playerColor) {
        boolean isWinner = false;
        for (int row = 0; row < numRows; row++) {
            int score = 0;
            for (int column = 0; column < numColumns; column++) {

                if (cellChecked[column][row] == playerColor && score < 5) {
                    score++;

                    //Log.i("INFO", "SCORE:" + score);
                    //Found 5 in a row

                    if(score == 5) {

                        //CHECK if there's NO 6 in a row AND
                        // (left ends is NULL OR right ends is NULL)
                        // checks XOOOOO   or  OOOOOX or OOOOO
                        if(!isNotBlockedEnd(column+1, row, playerColor) &&
                                (isNotBlockedEnd(column-5, row) || isNotBlockedEnd(column+1, row))) {
                            Log.i("INFO", playerColor + " IS THE WINNER");
                            isWinner = true;
                            break;
                        }

                        // checks OOOOOO (6x) or more
                        if (mode.equals(GameMode.FREESTYLE) && isNotBlockedEnd(column+1, row, playerColor)) {
                            Log.i("INFO", playerColor + " IS THE WINNER in freestyle mode");
                            isWinner = true;
                            break;
                        }

                        //Blocked end at both side
                        isWinner = false;
                        score = 0;
                    }
                    continue;
                }
                isWinner = false;
                score = 0;
            }

            if(isWinner)
                break;
        }
        return isWinner;
    }

    private boolean checkVertical(String playerColor) {
        boolean isWinner = false;
        for (int column = 0; column < numColumns; column++) {
            int score = 0;
            for (int row = 0; row < numRows; row++) {

                if (cellChecked[column][row] == playerColor && score < 5) {
                    score++;

                    //Log.i("INFO", "SCORE:" + score);
                    //Found 5 in a row

                    if(score == 5) {

                        //CHECK if there's NO 6 in a row AND
                        // (left ends is NULL OR right ends is NULL)
                        // checks XOOOOO   or  OOOOOX or OOOOO
                        if(!isNotBlockedEnd(column, row+1, playerColor) &&
                                (isNotBlockedEnd(column, row-5) || isNotBlockedEnd(column, row+1))) {
                            Log.i("INFO", playerColor + " IS THE WINNER");
                            isWinner = true;
                            break;
                        }

                        // checks OOOOOO (6x) or more
                        if (mode.equals(GameMode.FREESTYLE) && isNotBlockedEnd(column, row+1, playerColor)) {
                            Log.i("INFO", playerColor + " IS THE WINNER in freestyle mode");
                            isWinner = true;
                            break;
                        }

                        //Blocked end at both side
                        isWinner = false;
                        score = 0;
                    }
                    continue;
                }
                isWinner = false;
                score = 0;
            }

            if(isWinner)
                break;
        }
        return isWinner;
    }

    //Check right diagonal ↗↗↗↗↗↗
    private boolean checkRightDiagonal(String playerColor) {
        boolean isWinner = false;
        for( int k = 0 ; k < numColumns ; k++ ) {
            int score = 0;
            for( int column = 0 ; column <= k ; column++ ) {
                int row = k - column;
                if( row < numColumns && column < numColumns ) {
                    //cellChecked[column][row] = "BLACK";
                    if (cellChecked[column][row] == playerColor && score < 5) {
                        score++;

                        if(score == 5) {
                            //CHECK if there's NO 6 in a row AND
                            // (left ends is NULL OR right ends is NULL)
                            // checks XOOOOO   or  OOOOOX or OOOOO
                            if(!isNotBlockedEnd(column+1, row+1, playerColor) &&
                                    (isNotBlockedEnd(column-5, row+5) || isNotBlockedEnd(column+1, row+1))) {
                                Log.i("INFO", playerColor + " IS THE WINNER");
                                isWinner = true;
                                break;
                            }

                            // checks OOOOOO (6x) or more

                            if (mode.equals(GameMode.FREESTYLE) && isNotBlockedEnd(column+1, row-1, playerColor)) {
                                Log.i("INFO", playerColor + " IS THE WINNER in freestyle mode");
                                isWinner = true;
                                break;
                            }

                            //Blocked end at both side
                            isWinner = false;
                            score = 0;
                        }
                        continue;
                    }
                    isWinner = false;
                    score = 0;
                    //Log.i("INFO", String.valueOf(column) + "," + String.valueOf(row));
                }
            }
            if(isWinner)
                break;
        }
        return isWinner;
    }

    //TODO NEED TO REFACTOR THIS LATER ON
    private boolean checkLeftDiagonal(String playerColor) {
        boolean isWinner = false;
        int score = 0;

        int i,j;

        //Check Bottom half
        for(i=numRows-1; i>=0; i--){
            int row = i;
            int column= 0;
            score = 0;
            while(row<numRows){
                //Log.i("INFO", String.valueOf(column) + "," + String.valueOf(row));
                if( row < numRows && column < numColumns ) {
                    if (cellChecked[column][row] == playerColor && score < 5) {
                        score++;
                        //Log.i("INFO", "SCORE: " + String.valueOf(score));
                        if(score == 5){
                            //CHECK if there's NO 6 in a row AND
                            // (left ends is NULL OR right ends is NULL)
                            // checks XOOOOO   or  OOOOOX or OOOOO
                            if(!isNotBlockedEnd(column+1, row+1, playerColor) &&
                                    (isNotBlockedEnd(column-5, row-5) || isNotBlockedEnd(column+1, row+1))) {
                                Log.i("INFO", playerColor + " IS THE WINNER");
                                isWinner = true;
                                break;
                            }

                            // checks OOOOOO (6x) or more
                            if (mode.equals(GameMode.FREESTYLE) && isNotBlockedEnd(column+1, row+1, playerColor)) {
                                Log.i("INFO", playerColor + " IS THE WINNER in freestyle mode");
                                isWinner = true;
                                break;
                            }
                            score = 0;
                            isWinner = false;
                        }
                    }   else {
                        score = 0;
                        isWinner = false;
                    }
                }
                column++;
                row++;
            }
            if(isWinner)
                break;
        }
        if(isWinner) {
            return true;
        }


        //Check the upper half
        for(i=1; i<numColumns; i++){
            int column = i;
            int row = 0;
            while(column<numColumns){
                if( row < numRows && column < numColumns ) {
                    if (cellChecked[column][row] == playerColor && score < 5) {
                        score++;
                        if(score == 5){
                            //CHECK if there's NO 6 in a row AND
                            // (left ends is NULL OR right ends is NULL)
                            // checks XOOOOO   or  OOOOOX or OOOOO
                            if(!isNotBlockedEnd(column+1, row+1, playerColor) &&
                                    (isNotBlockedEnd(column-5, row-5) || isNotBlockedEnd(column+1, row+1))) {
                                Log.i("INFO", playerColor + " IS THE WINNER");
                                isWinner = true;
                                break;
                            }

                            // checks OOOOOO (6x) or more
                            if (mode.equals(GameMode.FREESTYLE) && isNotBlockedEnd(column+1, row+1, playerColor)) {
                                Log.i("INFO", playerColor + " IS THE WINNER in freestyle mode");
                                isWinner = true;
                                break;
                            }
                            score = 0;
                            isWinner = false;
                        }
                    }   else {
                        score = 0;
                        isWinner = false;
                    }
                }
                column++;
                row++;
            }
            if(isWinner)
                break;
        }

        return isWinner;
    }

    private void drawBoard(Canvas canvas) {
        if (numColumns == 0 || numRows == 0) {
            return;
        }

        for (int i = 1; i < numColumns + 1; i++) {
            canvas.drawLine(i * cellWidth, cellHeight, i * cellWidth, numRows * cellHeight, blackPaint);
            canvas.drawLine(cellWidth, i * cellHeight, numRows * cellWidth, i * cellHeight, blackPaint);
        }
    }

    private void drawStone(Canvas canvas) {
        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                if (cellChecked[i][j] != null) {
                    if (cellChecked[i][j] == "WHITE"){
                        canvas.drawCircle((i+1) *cellWidth, (j+1)*cellHeight, cellWidth/3, whitePaint);
                    } else if (cellChecked[i][j] == "BLACK") {
                        canvas.drawCircle((i+1)*cellWidth, (j+1)*cellHeight, cellWidth/3, blackPaint);
                    }
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        drawBoard(canvas);
        drawStone(canvas);
        winner = findWinner();
        if (winner) {
            ((TimerView) ((MainActivity) getContext()).findViewById(R.id.timer_black)).pause();
            ((TimerView) ((MainActivity) getContext()).findViewById(R.id.timer_white)).pause();
        }
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

            if(mode2.equals("OFFLINE")){
                OfflineMode(column, row);
            }
            invalidate();
        }
        return true;
    }

    private void OfflineMode(int column, int row) {
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
    }

    /*
    private void AIMode(int column, int row) {

    }
     */

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}