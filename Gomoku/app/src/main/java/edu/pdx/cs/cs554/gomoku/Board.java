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
    protected GameType gameType = GameType.STANDARD;
    private Paint blackPaint = new Paint();
    private Paint whitePaint = new Paint();
    protected String[][] cellChecked;
    private GameMode gameMode = GameMode.AI;


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

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
        Log.i("INFO", "Game type is set to " + this.gameType);
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
        Log.i("INFO", "Game mode is set to " + this.gameMode);
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
            //Log.i("INFO", column + "," + 0);
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
        winner =  checkHorizontal("WHITE") ||
                checkHorizontal("BLACK") ||
                checkVertical("WHITE") ||
                checkVertical("BLACK") ||
                checkLeftDiagonal("WHITE") ||
                checkLeftDiagonal("BLACK") ||
                checkRightDiagonal("WHITE") ||
                checkRightDiagonal("BLACK");

        if (winner) {
            ((TimerView) ((MainActivity) getContext()).findViewById(R.id.timer_black)).pause();
            ((TimerView) ((MainActivity) getContext()).findViewById(R.id.timer_white)).pause();
        }

        return winner;
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
                        if (gameType.equals(GameType.FREESTYLE) && isNotBlockedEnd(column+1, row, playerColor)) {
                            Log.i("INFO", playerColor + " IS THE WINNER in freestyle game type");
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
                        if (gameType.equals(GameType.FREESTYLE) && isNotBlockedEnd(column, row+1, playerColor)) {
                            Log.i("INFO", playerColor + " IS THE WINNER in freestyle game type");
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
        for( int k = 0 ; k < numColumns * 2 ; k++ ) {
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

                            if (gameType.equals(GameType.FREESTYLE) && isNotBlockedEnd(column+1, row-1, playerColor)) {
                                Log.i("INFO", playerColor + " IS THE WINNER in freestyle game type");
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
                    //Log.i("INFO", column + "," + row);
                    score = 0;

                }
            }
            if(isWinner)
                break;
        }
        return isWinner;
    }

    private boolean checkLeftDiagonal(String playerColor) {
        int score = 0;
        boolean isWinner = false;

        //number of reverse diagonal
        int k = numRows + numColumns - 1;
        int row = numRows - k;
        for(int i =numRows-1; i>=row; i--){
            int tmpRow = i;
            int tmpCol= 0;
            while(tmpRow<numRows && tmpCol<numColumns){
                if(tmpRow<0){
                    tmpCol++;
                    tmpRow++;
                    continue;
                }else{
                    if (cellChecked[tmpCol][tmpRow] == playerColor && score < 5) {
                        score++;
                        //Log.i("INFO", "SCORE: " + score);
                        if(score == 5){
                            //CHECK if there's NO 6 in a row AND
                            // (left ends is NULL OR right ends is NULL)
                            // checks XOOOOO   or  OOOOOX or OOOOO
                            if(!isNotBlockedEnd(tmpCol+1, tmpRow+1, playerColor) &&
                                    (isNotBlockedEnd(tmpCol-5, tmpRow-5) || isNotBlockedEnd(tmpCol+1, tmpRow+1))) {
                                Log.i("INFO", playerColor + " IS THE WINNER");
                                isWinner = true;
                                break;
                            }

                            // checks OOOOOO (6x) or more
                            if (gameType.equals(GameType.FREESTYLE) && isNotBlockedEnd(tmpCol+1, tmpRow+1, playerColor)) {
                                Log.i("INFO", playerColor + " IS THE WINNER in freestyle game type");
                                isWinner = true;
                                break;
                            }
                            score = 0;
                            isWinner = false;
                        }
                    } else {
                        score = 0;
                        isWinner = false;
                    }

                    //Log.i("INFO", tmpCol + "," + tmpRow);
                    tmpCol++;
                    tmpRow++;

                }
            }
            if(isWinner)
                break;
            score = 0;
            //Log.i("INFO", "SCORE: " + score);
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
        findWinner();
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

            if(gameMode.equals(GameMode.OFFLINE)){
                OfflineMode(column, row);
            } else if (gameMode.equals(GameMode.AI)) {
                AIMode(column, row);
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
        Log.i("INFO", cellChecked[column][row] + ": "+ column + " , " + row);
    }






    // ================================== AI MODE =============================================
    //TODO

    private void AIMode(int column, int row) {
        //Alternate the stone color
        cellChecked[column][row] = "WHITE";
        Log.i("INFO", cellChecked[column][row] + ": "+ column + " , " + row);
        if(findWinner()) {
            return;
        }
        computerMove(column, row);
    }


    private void computerMove(int column, int row) {

        int [] cellToPlace = null;

        cellToPlace = ComputerWinningStep("BLACK");
        if(cellToPlace != null) {
            Log.i("INFO", "COMPUTER WINING STEP DETECTED");
            cellChecked[cellToPlace[0]][cellToPlace[1]] = "BLACK";
            return;
        }

        cellToPlace = threatSequences();

        if(cellToPlace != null) {
            Log.i("INFO", "THREAT STEP DETECTED");
            cellChecked[cellToPlace[0]][cellToPlace[1]] = "BLACK";
        } else if (cellToPlace == null) {
            cellToPlace = NoThreatMove(column, row);
            cellChecked[cellToPlace[0]][cellToPlace[1]] = "BLACK";
        }
    }

    private int [] ComputerWinningStep(String playerColor) {
        int [] computerMove;

        computerMove = checkComputerWinningStepHorizontal(playerColor);

        if(computerMove == null) {
            computerMove = checkComputerWinningStepVertical(playerColor);
        }

        if(computerMove == null) {
            computerMove = checkComputerWinningStepRightDiagonal(playerColor);
        }

        if(computerMove == null) {
            computerMove = checkComputerWinningStepLeftDiagonal(playerColor);
        }

        return computerMove;
    }


    private int [] checkComputerWinningStepHorizontal(String playerColor) {
        for (int row = 0; row < numRows; row++) {
            int score = 0;
            for (int column = 0; column < numColumns; column++) {

                if (cellChecked[column][row] == playerColor && score < 4) {
                    score++;

                    if (score == 2) {
                        // O O _ O O -> O O O O O
                        if(isNotBlockedEnd(column - 2, row, null) &&
                                isNotBlockedEnd(column - 3, row, playerColor) &&
                                isNotBlockedEnd(column - 4, row, playerColor)) {
                            return new int [] {column - 2, row};
                        }
                    }

                    if (score == 3) {
                        // O O O _ O -> O O O O O
                        // O _ O O O -> O O O O O
                        // O O O _ O -> O O O O O
                        // O _ O O O -> O O O O O
                        if (isNotBlockedEnd(column + 1, row, null) &&
                                isNotBlockedEnd(column + 2, row, playerColor)) {
                            return new int [] {column + 1, row};
                        } else if (isNotBlockedEnd(column - 3, row, null) &&
                                isNotBlockedEnd(column - 4, row, playerColor)) {
                            return new int [] {column - 3, row};
                        } else if (isNotBlockedEnd(column + 1, row, null) &&
                                isNotBlockedEnd(column + 2, row, playerColor)) {
                            return new int[] {column + 1, row};
                        }
                    }

                    if (score == 4) {
                        // (_|B) O O O O _ -> O O O O O
                        // _ O O O O B-> O O O O O
                        if(isNotBlockedEnd(column + 1, row, null)) {
                            return new int [] {column + 1, row};
                        } else if (isNotBlockedEnd(column - 4, row, null)) {
                            return new int [] {column - 4, row};
                        }
                    }
                } else {
                    score = 0;
                }
            }
        }
        return null;
    }

    private int [] checkComputerWinningStepVertical(String playerColor) {
        for (int column = 0; column < numColumns; column++) {
            int score = 0;
            for (int row = 0; row < numRows; row++) {

                if (cellChecked[column][row] == playerColor && score < 4) {
                    score++;

                    if (score == 2) {
                        // O O _ O O -> O O O O O
                        if(isNotBlockedEnd(column, row - 2, null) &&
                                isNotBlockedEnd(column, row - 3, playerColor) &&
                                isNotBlockedEnd(column, row - 4, playerColor)) {
                            return new int [] {column, row - 2};
                        }
                    }

                    if (score == 3) {
                        // O O O _ O -> O O O O O
                        // O _ O O O -> O O O O O
                        // O O O _ O -> O O O O O
                        // O _ O O O -> O O O O O
                        if (isNotBlockedEnd(column, row + 1, null) &&
                                isNotBlockedEnd(column, row + 2, playerColor)) {
                            return new int [] {column, row + 1};
                        } else if (isNotBlockedEnd(column, row - 3, null) &&
                                isNotBlockedEnd(column, row - 4, playerColor)) {
                            return new int [] {column, row - 3};
                        } else if (isNotBlockedEnd(column + 1, row + 1, null) &&
                                isNotBlockedEnd(column, row + 2, playerColor)) {
                            return new int[] {column, row + 1};
                        }
                    }

                    if (score == 4) {
                        // (_|B) O O O O _ -> O O O O O
                        // _ O O O O B-> O O O O O
                        if(isNotBlockedEnd(column, row + 1, null)) {
                            return new int [] {column, row + 1};
                        } else if (isNotBlockedEnd(column, row - 4, null)) {
                            return new int [] {column, row - 4};
                        }
                    }
                } else {
                    score = 0;
                }
            }
        }
        return null;
    }

    private int [] checkComputerWinningStepRightDiagonal(String playerColor) {
        for( int k = 0 ; k < numColumns * 2 ; k++ ) {
            int score = 0;
            for( int column = 0 ; column <= k ; column++ ) {
                int row = k - column;
                if( row < numColumns && column < numColumns ) {
                    //cellChecked[column][row] = "BLACK";
                    if (cellChecked[column][row] == playerColor && score < 5) {
                        score++;

                        if (score == 2) {
                            // O O _ O O -> O O X O O
                            if(isNotBlockedEnd(column - 2, row + 2, null) &&
                                    isNotBlockedEnd(column - 3, row + 3, playerColor) &&
                                    isNotBlockedEnd(column - 4, row + 4, playerColor)) {
                                return new int [] {column - 2, row + 2};
                            }
                        }

                        if (score == 3) {
                            // O O O _ O -> O O O X O
                            // O _ O O O -> O X O O O
                            // X O O O _ O
                            // O _ O O O X
                            if (isNotBlockedEnd(column + 1, row - 1, null) &&
                                    isNotBlockedEnd(column + 2, row - 2, playerColor)) {
                                return new int [] {column + 1, row - 1};
                            } else if (isNotBlockedEnd(column - 3, row + 3, null) &&
                                    isNotBlockedEnd(column - 4, row + 4, playerColor)) {
                                return new int [] {column - 3, row + 3};
                            } else if (isNotBlockedEnd(column + 1, row - 1, null) &&
                                    isNotBlockedEnd(column + 2, row - 2, playerColor)) {
                                return new int[] {column + 1, row - 1};
                            } else if (isNotBlockedEnd(column - 3, row + 3, null) &&
                                    isNotBlockedEnd(column - 4, row + 4, playerColor)) {
                                return new int[] {column - 3, row + 3};
                            }
                        }

                        if (score == 4) {
                            // (_|B|X) O O O O _  ->  (_|B|X) O O O O X
                            // X O O O O (_|B|X)  ->  O O O O O (_|B|X)
                            if (isNotBlockedEnd(column + 1, row - 1)) {
                                return new int[] {column + 1, row - 1};
                            } else if (isNotBlockedEnd(column - 4, row + 4)) {
                                return new int[] {column - 4, row + 4};
                            }
                        }

                    } else {
                        score = 0;
                    }
                }
            }
            score = 0;
        }
        return null;
    }

    private int [] checkComputerWinningStepLeftDiagonal(String playerColor) {
        int score = 0;

        //number of reverse diagonal
        int k = numRows + numColumns - 1;
        int row = numRows - k;
        for(int i =numRows-1; i>=row; i--){
            int tmpRow = i;
            int tmpCol= 0;
            while(tmpRow<numRows && tmpCol<numColumns){
                if(tmpRow<0){
                    tmpCol++;
                    tmpRow++;
                    continue;
                }else{
                    if (cellChecked[tmpCol][tmpRow] == playerColor && score < 5) {
                        score++;


                        if (score == 2) {
                            // O O _ O O -> O O X O O
                            if(isNotBlockedEnd(tmpCol - 2, tmpRow - 2, null) &&
                                    isNotBlockedEnd(tmpCol - 3, tmpRow - 3, playerColor) &&
                                    isNotBlockedEnd(tmpCol - 4, tmpRow - 4, playerColor)) {
                                return new int [] {tmpCol - 2, tmpRow - 2};
                            }
                        }

                        if (score == 3) {
                            // O O O _ O -> O O O X O
                            // O _ O O O -> O X O O O
                            // X O O O _ O
                            // O _ O O O X
                            if (isNotBlockedEnd(tmpCol + 1, tmpRow + 1, null) &&
                                    isNotBlockedEnd(tmpCol + 2, tmpRow + 2, playerColor)) {
                                return new int [] {tmpCol + 1, tmpRow + 1};
                            } else if (isNotBlockedEnd(tmpCol - 3, tmpRow - 3, null) &&
                                    isNotBlockedEnd(tmpCol - 4, tmpRow - 4, playerColor)) {
                                return new int [] {tmpCol - 3, tmpRow - 3};
                            } else if (isNotBlockedEnd(tmpCol + 1, tmpRow + 1, null) &&
                                    isNotBlockedEnd(tmpCol + 2, tmpRow + 2, playerColor)) {
                                return new int[] {tmpCol + 1, tmpRow + 1};
                            } else if (isNotBlockedEnd(tmpCol - 3, tmpRow - 3, null) &&
                                    isNotBlockedEnd(tmpCol - 4, tmpRow - 4, playerColor)) {
                                return new int[] {tmpCol - 3, tmpRow - 3};
                            }
                        }


                        if (score == 4) {
                            // (_|B|X) O O O O _  ->  (_|B|X) O O O O X
                            // X O O O O (_|B|X)  ->  O O O O O (_|B|X)
                            if (isNotBlockedEnd(tmpCol + 1, tmpRow + 1)) {
                                return new int[] {tmpCol + 1, tmpRow + 1};
                            } else if (isNotBlockedEnd(tmpCol - 4, tmpRow - 4)) {
                                return new int[] {tmpCol - 4, tmpRow - 4};
                            }
                        }

                    } else {
                        score = 0;
                    }
                    tmpCol++;
                    tmpRow++;

                }
            }
            score = 0;
        }
        return null;
    }






    private int [] NoThreatMove(int column, int row) {

        if(isNotBlockedEnd(column + 1, row, null)) {
            return new int [] {column + 1, row};
        } else if (isNotBlockedEnd(column, row + 1, null)) {
            return new int [] {column, row + 1};
        } else if(isNotBlockedEnd(column + 1, row + 1, null)) {
            return new int [] {column + 1, row + 1};
        } else if (isNotBlockedEnd(column - 1, row + 1, null)) {
            return new int [] {column - 1, row + 1};
        } else if(isNotBlockedEnd(column - 1, row, null)) {
            return new int [] {column - 1, row};
        } else if (isNotBlockedEnd(column - 1, row - 1, null)) {
            return new int [] {column - 1, row - 1};
        } else if (isNotBlockedEnd(column, row - 1, null)) {
            return new int [] {column, row - 1};
        } else if (isNotBlockedEnd(column + 1, row - 1, null)) {
            return new int [] {column + 1, row - 1};
        }
        return null;
    }

    private int [] threatSequences () {
        int [] computerMove = null;

        computerMove = checkThreatHorizontal("WHITE");

        if(computerMove == null) {
            computerMove = checkThreatVertical("WHITE");
        }

        if(computerMove == null) {
            computerMove = checkThreatRightDiagonal("WHITE");
        }

        if(computerMove == null) {
            computerMove = checkThreatLeftDiagonal("WHITE");
        }
        return computerMove;
    }

    private int [] checkThreatHorizontal(String playerColor) {
        for (int row = 0; row < numRows; row++) {
            int score = 0;
            for (int column = 0; column < numColumns; column++) {

                if (cellChecked[column][row] == playerColor && score < 4) {
                    score++;

                    if (score == 2) {
                        // (_|O) O _ O O _  -> (_|O) O X O O _
                        if(isNotBlockedEnd(column + 1, row, null) &&
                                isNotBlockedEnd(column - 2, row, null)  &&
                                isNotBlockedEnd(column - 3, row, playerColor) &&
                                (isNotBlockedEnd(column - 4, row, null) ||
                                        isNotBlockedEnd(column - 4, row, playerColor))) {
                            return new int [] {column - 2, row};
                        }

                        // _ O O _ O (_|X) -> _ O O X O (_|O)
                        if(isNotBlockedEnd(column - 2, row, null) &&
                                isNotBlockedEnd(column + 1, row, null)  &&
                                isNotBlockedEnd(column + 2, row, playerColor) &&
                                (isNotBlockedEnd(column + 3, row, null) ||
                                        isNotBlockedEnd(column + 3, row, playerColor))) {
                            return new int [] {column + 1, row};
                        }
                        // O O _ O O -> O O X O O
                        if(isNotBlockedEnd(column - 2, row, null) &&
                                isNotBlockedEnd(column - 3, row, playerColor) &&
                                isNotBlockedEnd(column - 4, row, playerColor)) {
                            return new int [] {column - 2, row};
                        }
                    }

                    if (score == 3) {
                        // O O O _ O -> O O O X O
                        // O _ O O O -> O X O O O
                        // B _ O O O _   ->  B _ O O O X _
                        // _ _ O O O _ _ ->  _ _ O O O X _
                        // X O O O _ O
                        // O _ O O O X
                        if (isNotBlockedEnd(column + 1, row, null) &&
                                isNotBlockedEnd(column + 2, row, playerColor)) {
                            return new int [] {column + 1, row};
                        } else if (isNotBlockedEnd(column - 3, row, null) &&
                                isNotBlockedEnd(column - 4, row, playerColor)) {
                            return new int [] {column - 3, row};
                        } else if (isNotBlockedEnd(column + 1, row, null) &&
                                isNotBlockedEnd(column - 3, row, null) &&
                                !isNotBlockedEnd(column - 4, row, null)) {
                            return new int[] {column + 1, row};
                        } else if (isNotBlockedEnd(column + 1, row, null) &&
                                isNotBlockedEnd(column - 3, row, null) &&
                                !isNotBlockedEnd(column + 2, row, null)) {
                            return new int[] {column - 3, row};
                        } else if (isNotBlockedEnd(column + 1, row, null) &&
                                isNotBlockedEnd(column - 3, row, null)) {
                            return new int[] {column + 1, row};
                        } else if (isNotBlockedEnd(column + 1, row, null) &&
                                isNotBlockedEnd(column + 2, row, playerColor)) {
                            return new int[] {column + 1, row};
                        } else if (isNotBlockedEnd(column - 3, row, null) &&
                                isNotBlockedEnd(column - 4, row, playerColor)) {
                            return new int[] {column - 3, row};
                        }
                    }

                    if (score == 4) {
                        // (_|B|X) O O O O _  ->  (_|B|X) O O O O X
                        // X O O O O (_|B|X)  ->  O O O O O (_|B|X)
                        if (isNotBlockedEnd(column + 1, row)) {
                            return new int[] {column + 1, row};
                        } else if (isNotBlockedEnd(column - 4, row)) {
                            return new int[] {column - 4, row};
                        }
                    }

                } else {
                    score = 0;
                }
            }
        }
        return null;
    }

    private int [] checkThreatVertical(String playerColor) {
        for (int column = 0; column < numColumns; column++) {
            int score = 0;
            for (int row = 0; row < numRows; row++) {

                if (cellChecked[column][row] == playerColor && score < 4) {
                    score++;

                    if (score == 2) {
                        // (_|O) O _ O O _  -> (_|O) O X O O _
                        if(isNotBlockedEnd(column, row + 1, null) &&
                                isNotBlockedEnd(column, row - 2, null)  &&
                                isNotBlockedEnd(column, row - 3, playerColor) &&
                                (isNotBlockedEnd(column, row - 4, null) ||
                                        isNotBlockedEnd(column, row - 4, playerColor))) {
                            return new int [] {column, row - 2};
                        }

                        // _ O O _ O (_|X) -> _ O O X O (_|O)
                        if(isNotBlockedEnd(column, row - 2, null) &&
                                isNotBlockedEnd(column, row + 1, null)  &&
                                isNotBlockedEnd(column, row + 2, playerColor) &&
                                (isNotBlockedEnd(column, row + 3 , null) ||
                                        isNotBlockedEnd(column, row + 3, playerColor))) {
                            return new int [] {column, row + 1};
                        }
                        // O O _ O O -> O O X O O
                        if(isNotBlockedEnd(column, row - 2, null) &&
                                isNotBlockedEnd(column, row - 3, playerColor) &&
                                isNotBlockedEnd(column, row - 4, playerColor)) {
                            return new int [] {column, row - 2};
                        }
                    }

                    if (score == 3) {
                        // O O O _ O -> O O O X O
                        // O _ O O O -> O X O O O
                        // B _ O O O _   ->  B _ O O O X _
                        // _ _ O O O _ _ ->  _ _ O O O X _
                        // X O O O _ O
                        // O _ O O O X
                        if (isNotBlockedEnd(column, row + 1, null) &&
                                isNotBlockedEnd(column, row + 2, playerColor)) {
                            return new int [] {column, row + 1};
                        } else if (isNotBlockedEnd(column, row - 3, null) &&
                                isNotBlockedEnd(column, row - 4, playerColor)) {
                            return new int [] {column, row - 3};
                        } else if (isNotBlockedEnd(column, row + 1, null) &&
                                isNotBlockedEnd(column, row - 3, null) &&
                                !isNotBlockedEnd(column, row - 4, null)) {
                            return new int[] {column, row + 1};
                        } else if (isNotBlockedEnd(column, row + 1, null) &&
                                isNotBlockedEnd(column, row - 3, null) &&
                                !isNotBlockedEnd(column, row + 2, null)) {
                            return new int[] {column, row - 3};
                        } else if (isNotBlockedEnd(column, row + 1, null) &&
                                isNotBlockedEnd(column, row - 3, null)) {
                            return new int[] {column, row + 1};
                        } else if (isNotBlockedEnd(column, row + 1, null) &&
                                isNotBlockedEnd(column, row + 2, playerColor)) {
                            return new int[] {column, row + 1};
                        } else if (isNotBlockedEnd(column, row - 3, null) &&
                                isNotBlockedEnd(column, row - 4, playerColor)) {
                            return new int[] {column, row - 3};
                        }
                    }


                    if (score == 4) {
                        // (_|B|X) O O O O _  ->  (_|B|X) O O O O X
                        // X O O O O (_|B|X)  ->  O O O O O (_|B|X)
                        if (isNotBlockedEnd(column, row + 1)) {
                            return new int[] {column, row + 1};
                        } else if (isNotBlockedEnd(column, row - 4)) {
                            return new int[] {column, row - 4};
                        }
                    }
                } else {
                    score = 0;
                }
            }
        }
        return null;
    }


    //Check right diagonal ↗↗↗↗↗↗
    private int [] checkThreatRightDiagonal(String playerColor) {
        for( int k = 0 ; k < numColumns * 2 ; k++ ) {
            int score = 0;
            for( int column = 0 ; column <= k ; column++ ) {
                int row = k - column;
                if( row < numColumns && column < numColumns ) {
                    //cellChecked[column][row] = "BLACK";
                    if (cellChecked[column][row] == playerColor && score < 5) {
                        score++;

                        //Log.i("INFO", "SCORE: " + score);
                        if (score == 2) {
                            // (_|O) O _ O O _  -> (_|O) O X O O _
                            if(isNotBlockedEnd(column + 1, row - 1, null) &&
                                    isNotBlockedEnd(column - 2, row + 2, null)  &&
                                    isNotBlockedEnd(column - 3, row + 3, playerColor) &&
                                    (isNotBlockedEnd(column - 4, row + 4, null) ||
                                            isNotBlockedEnd(column - 4, row + 4, playerColor))) {
                                return new int [] {column - 2, row + 2};
                            }

                            // _ O O _ O (_|X) -> _ O O X O (_|O)
                            if(isNotBlockedEnd(column - 2, row + 2, null) &&
                                    isNotBlockedEnd(column + 1, row - 1, null)  &&
                                    isNotBlockedEnd(column + 2, row - 2, playerColor) &&
                                    (isNotBlockedEnd(column + 3, row - 3 , null) ||
                                            isNotBlockedEnd(column + 3, row - 3, playerColor))) {
                                return new int [] {column + 1, row - 1};
                            }


                            // O O _ O O -> O O X O O
                            if(isNotBlockedEnd(column - 2, row + 2, null) &&
                                    isNotBlockedEnd(column - 3, row + 3, playerColor) &&
                                    isNotBlockedEnd(column - 4, row + 4, playerColor)) {
                                return new int [] {column - 2, row + 2};
                            }
                        }

                        if (score == 3) {
                            // O O O _ O -> O O O X O
                            // O _ O O O -> O X O O O
                            // B _ O O O _   ->  B _ O O O X _
                            // _ O O O _ B   ->  _ X O O O _ B
                            // _ _ O O O _ _ ->  _ _ O O O X _
                            // X O O O _ O
                            // O _ O O O X
                            if (isNotBlockedEnd(column + 1, row - 1, null) &&
                                    isNotBlockedEnd(column + 2, row - 2, playerColor)) {
                                return new int [] {column + 1, row - 1};
                            } else if (isNotBlockedEnd(column - 3, row + 3, null) &&
                                    isNotBlockedEnd(column - 4, row + 4, playerColor)) {
                                return new int [] {column - 3, row + 3};
                            } else if (isNotBlockedEnd(column + 1, row - 1, null) &&
                                    isNotBlockedEnd(column - 3, row + 3, null) &&
                                    !isNotBlockedEnd(column - 4, row + 4, null)) {
                                return new int[] {column + 1, row - 1};
                            } else if (isNotBlockedEnd(column + 1, row - 1, null) &&
                                    isNotBlockedEnd(column - 3, row + 3, null) &&
                                    !isNotBlockedEnd(column + 2, row - 2, null)) {
                                return new int[] {column - 3, row + 3};
                            } else if (isNotBlockedEnd(column + 1, row - 1, null) &&
                                    isNotBlockedEnd(column - 3, row + 3, null)) {
                                return new int[] {column + 1, row - 1};
                            } else if (isNotBlockedEnd(column + 1, row - 1, null) &&
                                    isNotBlockedEnd(column + 2, row - 2, playerColor)) {
                                return new int[] {column + 1, row - 1};
                            } else if (isNotBlockedEnd(column - 3, row + 3, null) &&
                                    isNotBlockedEnd(column - 4, row + 4, playerColor)) {
                                return new int[] {column - 3, row + 3};
                            }
                        }

                        if (score == 4) {
                            // (_|B|X) O O O O _  ->  (_|B|X) O O O O X
                            // X O O O O (_|B|X)  ->  O O O O O (_|B|X)
                            if (isNotBlockedEnd(column + 1, row - 1)) {
                                return new int[] {column + 1, row - 1};
                            } else if (isNotBlockedEnd(column - 4, row + 4)) {
                                return new int[] {column - 4, row + 4};
                            }
                        }

                    } else {
                        score = 0;
                    }
                    //Log.i("INFO", column + "," +  row);
                }
            }
            score = 0;
            //Log.i("INFO", "SCORE: " + score);
        }
        return null;
    }

    private int [] checkThreatLeftDiagonal(String playerColor) {
        int score = 0;

        //number of reverse diagonal
        int k = numRows + numColumns - 1;
        int row = numRows - k;
        for(int i =numRows-1; i>=row; i--){
            int tmpRow = i;
            int tmpCol= 0;
            while(tmpRow<numRows && tmpCol<numColumns){
                if(tmpRow<0){
                    tmpCol++;
                    tmpRow++;
                    continue;
                }else{
                    if (cellChecked[tmpCol][tmpRow] == playerColor && score < 5) {
                        score++;


                        if (score == 2) {
                            // (_|O) O _ O O _  -> (_|O) O X O O _
                            if(isNotBlockedEnd(tmpCol + 1, tmpRow + 1, null) &&
                                    isNotBlockedEnd(tmpCol - 2, tmpRow - 2, null)  &&
                                    isNotBlockedEnd(tmpCol - 3, tmpRow - 3, playerColor) &&
                                    (isNotBlockedEnd(tmpCol - 4, tmpRow - 4, null) ||
                                            isNotBlockedEnd(tmpCol - 4, tmpRow - 4, playerColor))) {
                                return new int [] {tmpCol - 2, tmpRow - 2};
                            }

                            // _ O O _ O (_|X) -> _ O O X O (_|O)
                            if(isNotBlockedEnd(tmpCol - 2, tmpRow - 2, null) &&
                                    isNotBlockedEnd(tmpCol + 1, tmpRow + 1, null)  &&
                                    isNotBlockedEnd(tmpCol + 2, tmpRow + 2, playerColor) &&
                                    (isNotBlockedEnd(tmpCol + 3, tmpRow + 3, null) ||
                                            isNotBlockedEnd(tmpCol + 3, tmpRow + 3, playerColor))) {
                                return new int [] {tmpCol + 1, tmpRow + 1};
                            }
                            // O O _ O O -> O O X O O
                            if(isNotBlockedEnd(tmpCol - 2, tmpRow - 2, null) &&
                                    isNotBlockedEnd(tmpCol - 3, tmpRow - 3, playerColor) &&
                                    isNotBlockedEnd(tmpCol - 4, tmpRow - 4, playerColor)) {
                                return new int [] {tmpCol - 2, tmpRow - 2};
                            }
                        }

                        if (score == 3) {
                            // O O O _ O -> O O O X O
                            // O _ O O O -> O X O O O
                            // B _ O O O _   ->  B _ O O O X _
                            // _ _ O O O _ _ ->  _ _ O O O X _
                            // X O O O _ O
                            // O _ O O O X
                            if (isNotBlockedEnd(tmpCol + 1, tmpRow + 1, null) &&
                                    isNotBlockedEnd(tmpCol + 2, tmpRow + 2, playerColor)) {
                                return new int [] {tmpCol + 1, tmpRow + 1};
                            } else if (isNotBlockedEnd(tmpCol - 3, tmpRow - 3, null) &&
                                    isNotBlockedEnd(tmpCol - 4, tmpRow - 4, playerColor)) {
                                return new int [] {tmpCol - 3, tmpRow - 3};
                            } else if (isNotBlockedEnd(tmpCol + 1, tmpRow + 1, null) &&
                                    isNotBlockedEnd(tmpCol - 3, tmpRow - 3, null) &&
                                    !isNotBlockedEnd(tmpCol - 4, tmpRow - 4, null)) {
                                return new int[] {tmpCol + 1, tmpRow + 1};
                            } else if (isNotBlockedEnd(tmpCol + 1, tmpRow + 1, null) &&
                                    isNotBlockedEnd(tmpCol - 3, tmpRow - 3, null) &&
                                    !isNotBlockedEnd(tmpCol + 2, tmpRow + 2, null)) {
                                return new int[] {tmpCol - 3, tmpRow - 3};
                            } else if (isNotBlockedEnd(tmpCol + 1, tmpRow + 1, null) &&
                                    isNotBlockedEnd(tmpCol - 3, tmpRow - 3, null)) {
                                return new int[] {tmpCol + 1, tmpRow + 1};
                            } else if (isNotBlockedEnd(tmpCol + 1, tmpRow + 1, null) &&
                                    isNotBlockedEnd(tmpCol + 2, tmpRow + 2, playerColor)) {
                                return new int[] {tmpCol + 1, tmpRow + 1};
                            } else if (isNotBlockedEnd(tmpCol - 3, tmpRow - 3, null) &&
                                    isNotBlockedEnd(tmpCol - 4, tmpRow - 4, playerColor)) {
                                return new int[] {tmpCol - 3, tmpRow - 3};
                            }
                        }


                        if (score == 4) {
                            // (_|B|X) O O O O _  ->  (_|B|X) O O O O X
                            // X O O O O (_|B|X)  ->  O O O O O (_|B|X)
                            if (isNotBlockedEnd(tmpCol + 1, tmpRow + 1)) {
                                return new int[] {tmpCol + 1, tmpRow + 1};
                            } else if (isNotBlockedEnd(tmpCol - 4, tmpRow - 4)) {
                                return new int[] {tmpCol - 4, tmpRow - 4};
                            }
                        }

                    } else {
                        score = 0;
                    }

                    //Log.i("INFO", tmpCol + "," + tmpRow);
                    tmpCol++;
                    tmpRow++;

                }
            }
            score = 0;
            //Log.i("INFO", "SCORE: " + score);
        }
        return null;
    }



    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
