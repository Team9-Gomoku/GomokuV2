package edu.pdx.cs.cs554.gomoku;

import android.util.Log;

import static edu.pdx.cs.cs554.gomoku.Board.cellChecked;


public class AI {
    int numRows, numColumns;

    public AI(int numRows, int numColumns) {
        this.numRows = numRows;
        this.numColumns = numColumns;
    }

    public void computerMove(int column, int row) {

        int[] cellToPlace = null;

        cellToPlace = ComputerWinningStep("WHITE");
        if (cellToPlace != null) {
            Log.i("INFO", "COMPUTER WINING STEP DETECTED");
            cellChecked[cellToPlace[0]][cellToPlace[1]] = "WHITE";
            return;
        }

        cellToPlace = threatSequences();

        if (cellToPlace != null) {
            Log.i("INFO", "THREAT STEP DETECTED");
            cellChecked[cellToPlace[0]][cellToPlace[1]] = "WHITE";
        } else if (cellToPlace == null) {
            cellToPlace = NoThreatMove(column, row);
            cellChecked[cellToPlace[0]][cellToPlace[1]] = "WHITE";
        }





    }

    //Check if the end is blocked
    private boolean isNotBlockedEnd(int column, int row, String playerColor) {
        return (cellChecked[column][row]) == playerColor;
    }

    private boolean isNotBlockedEnd(int column, int row) {
        return (cellChecked[column][row]) == null;
    }

    private int[] ComputerWinningStep(String playerColor) {
        int[] computerMove;

        computerMove = checkComputerWinningStepHorizontal(playerColor);

        if (computerMove == null) {
            computerMove = checkComputerWinningStepVertical(playerColor);
        }

        if (computerMove == null) {
            computerMove = checkComputerWinningStepRightDiagonal(playerColor);
        }

        if (computerMove == null) {
            computerMove = checkComputerWinningStepLeftDiagonal(playerColor);
        }

        return computerMove;
    }


    private int[] checkComputerWinningStepHorizontal(String playerColor) {
        for (int row = 0; row < numRows; row++) {
            int score = 0;
            for (int column = 0; column < numColumns; column++) {

                if (cellChecked[column][row] == playerColor && score < 4) {
                    score++;

                    if (score == 2) {
                        // O O _ O O -> O O O O O
                        if (isNotBlockedEnd(column - 2, row, null) &&
                                isNotBlockedEnd(column - 3, row, playerColor) &&
                                isNotBlockedEnd(column - 4, row, playerColor)) {
                            return new int[] {column - 2, row};
                        }
                    }

                    if (score == 3) {
                        // O O O _ O -> O O O O O
                        // O _ O O O -> O O O O O
                        // O O O _ O -> O O O O O
                        // O _ O O O -> O O O O O
                        if (isNotBlockedEnd(column + 1, row, null) &&
                                isNotBlockedEnd(column + 2, row, playerColor)) {
                            return new int[] {column + 1, row};
                        } else if (isNotBlockedEnd(column - 3, row, null) &&
                                isNotBlockedEnd(column - 4, row, playerColor)) {
                            return new int[] {column - 3, row};
                        } else if (isNotBlockedEnd(column + 1, row, null) &&
                                isNotBlockedEnd(column + 2, row, playerColor)) {
                            return new int[] {column + 1, row};
                        }
                    }

                    if (score == 4) {
                        // (_|B) O O O O _ -> O O O O O
                        // _ O O O O B-> O O O O O
                        if (isNotBlockedEnd(column + 1, row, null)) {
                            return new int[] {column + 1, row};
                        } else if (isNotBlockedEnd(column - 4, row, null)) {
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

    private int[] checkComputerWinningStepVertical(String playerColor) {
        for (int column = 0; column < numColumns; column++) {
            int score = 0;
            for (int row = 0; row < numRows; row++) {

                if (cellChecked[column][row] == playerColor && score < 4) {
                    score++;

                    if (score == 2) {
                        // O O _ O O -> O O O O O
                        if (isNotBlockedEnd(column, row - 2, null) &&
                                isNotBlockedEnd(column, row - 3, playerColor) &&
                                isNotBlockedEnd(column, row - 4, playerColor)) {
                            return new int[] {column, row - 2};
                        }
                    }

                    if (score == 3) {
                        // O O O _ O -> O O O O O
                        // O _ O O O -> O O O O O
                        // O O O _ O -> O O O O O
                        // O _ O O O -> O O O O O
                        if (isNotBlockedEnd(column, row + 1, null) &&
                                isNotBlockedEnd(column, row + 2, playerColor)) {
                            return new int[] {column, row + 1};
                        } else if (isNotBlockedEnd(column, row - 3, null) &&
                                isNotBlockedEnd(column, row - 4, playerColor)) {
                            return new int[] {column, row - 3};
                        } else if (isNotBlockedEnd(column + 1, row + 1, null) &&
                                isNotBlockedEnd(column, row + 2, playerColor)) {
                            return new int[] {column, row + 1};
                        }
                    }

                    if (score == 4) {
                        // (_|B) O O O O _ -> O O O O O
                        // _ O O O O B-> O O O O O
                        if (isNotBlockedEnd(column, row + 1, null)) {
                            return new int[] {column, row + 1};
                        } else if (isNotBlockedEnd(column, row - 4, null)) {
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

    private int[] checkComputerWinningStepRightDiagonal(String playerColor) {
        for( int k = 0 ; k < numColumns * 2 ; k++ ) {
            int score = 0;
            for( int column = 0 ; column <= k ; column++ ) {
                int row = k - column;
                if ( row < numColumns && column < numColumns ) {
                    //cellChecked[column][row] = "BLACK";
                    if (cellChecked[column][row] == playerColor && score < 5) {
                        score++;

                        if (score == 2) {
                            // O O _ O O -> O O X O O
                            if (isNotBlockedEnd(column - 2, row + 2, null) &&
                                    isNotBlockedEnd(column - 3, row + 3, playerColor) &&
                                    isNotBlockedEnd(column - 4, row + 4, playerColor)) {
                                return new int[] {column - 2, row + 2};
                            }
                        }

                        if (score == 3) {
                            // O O O _ O -> O O O X O
                            // O _ O O O -> O X O O O
                            // X O O O _ O
                            // O _ O O O X
                            if (isNotBlockedEnd(column + 1, row - 1, null) &&
                                    isNotBlockedEnd(column + 2, row - 2, playerColor)) {
                                return new int[] {column + 1, row - 1};
                            } else if (isNotBlockedEnd(column - 3, row + 3, null) &&
                                    isNotBlockedEnd(column - 4, row + 4, playerColor)) {
                                return new int[] {column - 3, row + 3};
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

    private int[] checkComputerWinningStepLeftDiagonal(String playerColor) {
        int score = 0;

        //number of reverse diagonal
        int k = numRows + numColumns - 1;
        int row = numRows - k;
        for(int i =numRows-1; i>=row; i--) {
            int tmpRow = i;
            int tmpCol= 0;
            while(tmpRow<numRows && tmpCol<numColumns) {
                if (tmpRow<0) {
                    tmpCol++;
                    tmpRow++;
                    continue;
                } else {
                    if (cellChecked[tmpCol][tmpRow] == playerColor && score < 5) {
                        score++;


                        if (score == 2) {
                            // O O _ O O -> O O X O O
                            if (isNotBlockedEnd(tmpCol - 2, tmpRow - 2, null) &&
                                    isNotBlockedEnd(tmpCol - 3, tmpRow - 3, playerColor) &&
                                    isNotBlockedEnd(tmpCol - 4, tmpRow - 4, playerColor)) {
                                return new int[] {tmpCol - 2, tmpRow - 2};
                            }
                        }

                        if (score == 3) {
                            // O O O _ O -> O O O X O
                            // O _ O O O -> O X O O O
                            // X O O O _ O
                            // O _ O O O X
                            if (isNotBlockedEnd(tmpCol + 1, tmpRow + 1, null) &&
                                    isNotBlockedEnd(tmpCol + 2, tmpRow + 2, playerColor)) {
                                return new int[] {tmpCol + 1, tmpRow + 1};
                            } else if (isNotBlockedEnd(tmpCol - 3, tmpRow - 3, null) &&
                                    isNotBlockedEnd(tmpCol - 4, tmpRow - 4, playerColor)) {
                                return new int[] {tmpCol - 3, tmpRow - 3};
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






    private int[] NoThreatMove(int column, int row) {

        if (isNotBlockedEnd(column + 1, row, null)) {
            return new int[] {column + 1, row};
        } else if (isNotBlockedEnd(column, row + 1, null)) {
            return new int[] {column, row + 1};
        } else if (isNotBlockedEnd(column + 1, row + 1, null)) {
            return new int[] {column + 1, row + 1};
        } else if (isNotBlockedEnd(column - 1, row + 1, null)) {
            return new int[] {column - 1, row + 1};
        } else if (isNotBlockedEnd(column - 1, row, null)) {
            return new int[] {column - 1, row};
        } else if (isNotBlockedEnd(column - 1, row - 1, null)) {
            return new int[] {column - 1, row - 1};
        } else if (isNotBlockedEnd(column, row - 1, null)) {
            return new int[] {column, row - 1};
        } else if (isNotBlockedEnd(column + 1, row - 1, null)) {
            return new int[] {column + 1, row - 1};
        }
        return null;
    }

    private int[] threatSequences () {
        int[] computerMove = null;

        computerMove = checkThreatHorizontal("BLACK");

        if (computerMove == null) {
            computerMove = checkThreatVertical("BLACK");
        }

        if (computerMove == null) {
            computerMove = checkThreatRightDiagonal("BLACK");
        }

        if (computerMove == null) {
            computerMove = checkThreatLeftDiagonal("BLACK");
        }
        return computerMove;
    }

    private int[] checkThreatHorizontal(String playerColor) {
        for (int row = 0; row < numRows; row++) {
            int score = 0;
            for (int column = 0; column < numColumns; column++) {

                if (cellChecked[column][row] == playerColor && score < 4) {
                    score++;

                    if (score == 2) {
                        // (_|O) O _ O O _  -> (_|O) O X O O _
                        if (isNotBlockedEnd(column + 1, row, null) &&
                                isNotBlockedEnd(column - 2, row, null)  &&
                                isNotBlockedEnd(column - 3, row, playerColor) &&
                                (isNotBlockedEnd(column - 4, row, null) ||
                                        isNotBlockedEnd(column - 4, row, playerColor))) {
                            return new int[] {column - 2, row};
                        }

                        // _ O O _ O (_|X) -> _ O O X O (_|O)
                        if (isNotBlockedEnd(column - 2, row, null) &&
                                isNotBlockedEnd(column + 1, row, null)  &&
                                isNotBlockedEnd(column + 2, row, playerColor) &&
                                (isNotBlockedEnd(column + 3, row, null) ||
                                        isNotBlockedEnd(column + 3, row, playerColor))) {
                            return new int[] {column + 1, row};
                        }
                        // O O _ O O -> O O X O O
                        if (isNotBlockedEnd(column - 2, row, null) &&
                                isNotBlockedEnd(column - 3, row, playerColor) &&
                                isNotBlockedEnd(column - 4, row, playerColor)) {
                            return new int[] {column - 2, row};
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
                            return new int[] {column + 1, row};
                        } else if (isNotBlockedEnd(column - 3, row, null) &&
                                isNotBlockedEnd(column - 4, row, playerColor)) {
                            return new int[] {column - 3, row};
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

    private int[] checkThreatVertical(String playerColor) {
        for (int column = 0; column < numColumns; column++) {
            int score = 0;
            for (int row = 0; row < numRows; row++) {

                if (cellChecked[column][row] == playerColor && score < 4) {
                    score++;

                    if (score == 2) {
                        // (_|O) O _ O O _  -> (_|O) O X O O _
                        if (isNotBlockedEnd(column, row + 1, null) &&
                                isNotBlockedEnd(column, row - 2, null)  &&
                                isNotBlockedEnd(column, row - 3, playerColor) &&
                                (isNotBlockedEnd(column, row - 4, null) ||
                                        isNotBlockedEnd(column, row - 4, playerColor))) {
                            return new int[] {column, row - 2};
                        }

                        // _ O O _ O (_|X) -> _ O O X O (_|O)
                        if (isNotBlockedEnd(column, row - 2, null) &&
                                isNotBlockedEnd(column, row + 1, null)  &&
                                isNotBlockedEnd(column, row + 2, playerColor) &&
                                (isNotBlockedEnd(column, row + 3 , null) ||
                                        isNotBlockedEnd(column, row + 3, playerColor))) {
                            return new int[] {column, row + 1};
                        }
                        // O O _ O O -> O O X O O
                        if (isNotBlockedEnd(column, row - 2, null) &&
                                isNotBlockedEnd(column, row - 3, playerColor) &&
                                isNotBlockedEnd(column, row - 4, playerColor)) {
                            return new int[] {column, row - 2};
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
                            return new int[] {column, row + 1};
                        } else if (isNotBlockedEnd(column, row - 3, null) &&
                                isNotBlockedEnd(column, row - 4, playerColor)) {
                            return new int[] {column, row - 3};
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
    private int[] checkThreatRightDiagonal(String playerColor) {
        for( int k = 0 ; k < numColumns * 2 ; k++ ) {
            int score = 0;
            for( int column = 0 ; column <= k ; column++ ) {
                int row = k - column;
                if ( row < numColumns && column < numColumns ) {
                    //cellChecked[column][row] = "BLACK";
                    if (cellChecked[column][row] == playerColor && score < 5) {
                        score++;

                        //Log.i("INFO", "SCORE: " + score);
                        if (score == 2) {
                            // (_|O) O _ O O _  -> (_|O) O X O O _
                            if (isNotBlockedEnd(column + 1, row - 1, null) &&
                                    isNotBlockedEnd(column - 2, row + 2, null)  &&
                                    isNotBlockedEnd(column - 3, row + 3, playerColor) &&
                                    (isNotBlockedEnd(column - 4, row + 4, null) ||
                                            isNotBlockedEnd(column - 4, row + 4, playerColor))) {
                                return new int[] {column - 2, row + 2};
                            }

                            // _ O O _ O (_|X) -> _ O O X O (_|O)
                            if (isNotBlockedEnd(column - 2, row + 2, null) &&
                                    isNotBlockedEnd(column + 1, row - 1, null)  &&
                                    isNotBlockedEnd(column + 2, row - 2, playerColor) &&
                                    (isNotBlockedEnd(column + 3, row - 3 , null) ||
                                            isNotBlockedEnd(column + 3, row - 3, playerColor))) {
                                return new int[] {column + 1, row - 1};
                            }


                            // O O _ O O -> O O X O O
                            if (isNotBlockedEnd(column - 2, row + 2, null) &&
                                    isNotBlockedEnd(column - 3, row + 3, playerColor) &&
                                    isNotBlockedEnd(column - 4, row + 4, playerColor)) {
                                return new int[] {column - 2, row + 2};
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
                                return new int[] {column + 1, row - 1};
                            } else if (isNotBlockedEnd(column - 3, row + 3, null) &&
                                    isNotBlockedEnd(column - 4, row + 4, playerColor)) {
                                return new int[] {column - 3, row + 3};
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

    private int[] checkThreatLeftDiagonal(String playerColor) {
        int score = 0;

        //number of reverse diagonal
        int k = numRows + numColumns - 1;
        int row = numRows - k;
        for(int i =numRows-1; i>=row; i--) {
            int tmpRow = i;
            int tmpCol= 0;
            while(tmpRow<numRows && tmpCol<numColumns) {
                if (tmpRow<0) {
                    tmpCol++;
                    tmpRow++;
                    continue;
                }else{
                    if (cellChecked[tmpCol][tmpRow] == playerColor && score < 5) {
                        score++;


                        if (score == 2) {
                            // (_|O) O _ O O _  -> (_|O) O X O O _
                            if (isNotBlockedEnd(tmpCol + 1, tmpRow + 1, null) &&
                                    isNotBlockedEnd(tmpCol - 2, tmpRow - 2, null)  &&
                                    isNotBlockedEnd(tmpCol - 3, tmpRow - 3, playerColor) &&
                                    (isNotBlockedEnd(tmpCol - 4, tmpRow - 4, null) ||
                                            isNotBlockedEnd(tmpCol - 4, tmpRow - 4, playerColor))) {
                                return new int[] {tmpCol - 2, tmpRow - 2};
                            }

                            // _ O O _ O (_|X) -> _ O O X O (_|O)
                            if (isNotBlockedEnd(tmpCol - 2, tmpRow - 2, null) &&
                                    isNotBlockedEnd(tmpCol + 1, tmpRow + 1, null)  &&
                                    isNotBlockedEnd(tmpCol + 2, tmpRow + 2, playerColor) &&
                                    (isNotBlockedEnd(tmpCol + 3, tmpRow + 3, null) ||
                                            isNotBlockedEnd(tmpCol + 3, tmpRow + 3, playerColor))) {
                                return new int[] {tmpCol + 1, tmpRow + 1};
                            }
                            // O O _ O O -> O O X O O
                            if (isNotBlockedEnd(tmpCol - 2, tmpRow - 2, null) &&
                                    isNotBlockedEnd(tmpCol - 3, tmpRow - 3, playerColor) &&
                                    isNotBlockedEnd(tmpCol - 4, tmpRow - 4, playerColor)) {
                                return new int[] {tmpCol - 2, tmpRow - 2};
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
                                return new int[] {tmpCol + 1, tmpRow + 1};
                            } else if (isNotBlockedEnd(tmpCol - 3, tmpRow - 3, null) &&
                                    isNotBlockedEnd(tmpCol - 4, tmpRow - 4, playerColor)) {
                                return new int[] {tmpCol - 3, tmpRow - 3};
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
}
