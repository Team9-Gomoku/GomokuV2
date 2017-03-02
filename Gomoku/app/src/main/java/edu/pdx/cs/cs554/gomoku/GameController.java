package edu.pdx.cs.cs554.gomoku;

public class GameController {
    public enum PlayerColor {BLACK, WHITE, EMPTY};
    public static PlayerColor playerTurn;
    public int boardSize;
    private boolean freestyle;
    private int lineOffset;

    private PlayerColor[][] boardArray;

    public GameController(int sizeX, int screenSizeX, boolean freestyle) {
        playerTurn = PlayerColor.BLACK;
        this.freestyle = freestyle; //Mode spcifies if the game is freestyle (1) or standard (0)
        boardSize = sizeX;
        boardArray = new PlayerColor[sizeX][sizeX];
        for(int i = 0; i < sizeX; i++) {
            for(int j = 0; j < sizeX; j++) {
                boardArray[i][j] = PlayerColor.EMPTY;
            }
        }
        lineOffset = Math.round(((float)screenSizeX)/((float)(boardSize + 1)));
    }

    public int getLogicalCoordinate(float position) {
        int logicalPosition;

        if (Math.round(position/lineOffset) >= boardSize) {

            logicalPosition = boardSize - 1;
        }
        else {
            logicalPosition = Math.round(position/lineOffset) - 1;
        }
        if (logicalPosition > boardSize) {
            logicalPosition = boardSize;
        }
        else if (logicalPosition < 0) {
            logicalPosition = 0;
        }
        return logicalPosition;
    }

    public boolean isWinner(float xfloat, float yfloat, boolean AI) {
        int x;
        int y;
        if (AI == false) {
            x = getLogicalCoordinate(xfloat);
            y = getLogicalCoordinate(yfloat);
        }
        else {
            x = (int)xfloat;
            y = (int)yfloat;
        }

        if (freestyle == true) {
            return isWinnerFreestyle(x, y);
        }else if (BluetoothActivity.freestyle==true){
            return isWinnerFreestyle(x, y);
        }
        else {
            return isWinnerStandard(x, y);
        }

    }

    //check winner
    public boolean isWinnerStandard(int x, int y) {

        int count = 0;
        //check vertical win
        for (int row = 0; row < boardSize; row++) {
            if (boardArray[row][x] == playerTurn && count < 5)
                count++;
            else {
                count = 0;
            }
            if(count == 5 && ((row-count > 0 && boardArray[row-count][x] == PlayerColor.EMPTY) || (row+1 < boardSize && boardArray[row+1][x] == PlayerColor.EMPTY))) {
                if ((row-count > 0 && boardArray[row-count][x] != playerTurn) && (row+1 < boardSize && boardArray[row+1][x] != playerTurn)) {
                    return true;
                }
                else if (row-count == -1 && boardArray[row+1][x] != playerTurn) {
                    return true;
                }
                else if (row+1 == boardSize && boardArray[row-count][x] != playerTurn) {
                    return true;
                }
            }
        }

        count = 0;

        //check horizontal win
        for (int column = 0; column < boardSize; column++) {
            if (boardArray[y][column] == playerTurn && count < 5)
                count++;
            else{
                count = 0;
            }
            if(count == 5 && ((column-count > 0 && (boardArray[y][column-count] == PlayerColor.EMPTY)) || (column+1 < boardSize && boardArray[y][column+1] == PlayerColor.EMPTY))) {
                if ((column-count > 0 && (boardArray[y][column-count] != playerTurn)) && (column+1 < boardSize && boardArray[y][column+1] != playerTurn)) {
                    return true;
                }
                else if (column-count == -1 && boardArray[y][column+1] != playerTurn) {
                    return true;
                }
                else if (column+1 == boardSize && boardArray[y][column-count] != playerTurn){
                    return true;
                }
            }
        }

        //check diagonal win upperleft/lowerright
        int diagX;
        int diagY;
        for (int i = (5-1); i < boardSize; i++) {
            diagX = i;
            diagY = boardSize-1;
            count = 0;
            while (diagX >= 0 && diagY >= 0) {
                if (boardArray[diagY][diagX] == playerTurn) {
                    count++;
                    if (count == 5 && (((diagX+count < boardSize && diagY+count < boardSize) && boardArray[diagY+count][diagX+count] == PlayerColor.EMPTY) || ((diagX-1 >= 0 && diagY-1 >= 0) && boardArray[diagY-1][diagX-1] == PlayerColor.EMPTY))) {
                        if (((diagX+count < boardSize && diagY+count < boardSize) && boardArray[diagY+count][diagX+count] != playerTurn) && ((diagX-1 >= 0 && diagY-1 >= 0) && boardArray[diagY-1][diagX-1] != playerTurn)){
                            return true;
                        }
                    }
                }
                else {
                    count = 0;
                }
                diagX--;
                diagY--;
            }
            count = 0;
            diagY = i;
            diagX = boardSize-1;
            while (diagX >= 0 && diagY >= 0) {
                if (boardArray[diagY][diagX] == playerTurn) {
                    count++;
                    if (count == 5 && (((diagX+count < boardSize && diagY+count < boardSize) && boardArray[diagY+count][diagX+count] == PlayerColor.EMPTY) || ((diagX-1 >= 0 && diagY-1 >= 0) && boardArray[diagY-1][diagX-1] == PlayerColor.EMPTY))) {
                        if (((diagX+count < boardSize && diagY+count < boardSize) && boardArray[diagY+count][diagX+count] != playerTurn) && ((diagX-1 >= 0 && diagY-1 >= 0) && boardArray[diagY-1][diagX-1] != playerTurn)) {
                            return true;
                        }
                    }
                }
                else {
                    count = 0;
                }
                diagX--;
                diagY--;
            }
        }


        for (int i = (5-1); i < boardSize; i++) {
            diagY = i;
            diagX = 0;
            count = 0;
            while (diagX < boardSize && diagY >= 0) {
                if (boardArray[diagY][diagX] == playerTurn) {
                    count++;
                    if (count == 5 && (((diagX+1 < boardSize && diagY-1 >= 0) && boardArray[diagY-1][diagX+1] == PlayerColor.EMPTY) || ((diagX-count >= 0 && diagY+count < boardSize) && boardArray[diagY+count][diagX-count] == PlayerColor.EMPTY))) {
                        if (((diagX+1 < boardSize && diagY-1 >= 0) && boardArray[diagY-1][diagX+1] != playerTurn) && ((diagX-count >= 0 && diagY+count < boardSize) && boardArray[diagY+count][diagX-count] != playerTurn)) {
                            return true;
                        }
                    }
                }
                else {
                    count = 0;
                }
                diagX++;
                diagY--;
            }
            count = 0;
            diagX = boardSize - 1 - i;
            diagY = boardSize - 1;
            while (diagX < boardSize && diagY >= 0) {
                if (boardArray[diagY][diagX] == playerTurn) {
                    count++;
                    if (count == 5 && (((diagX+1 < boardSize && diagY-1 >= 0) && boardArray[diagY-1][diagX+1] == PlayerColor.EMPTY) || ((diagX-count >= 0 && diagY+count < boardSize) && boardArray[diagY+count][diagX-count] == PlayerColor.EMPTY))) {
                        return true;
                    }
                }
                else {
                    count = 0;
                }
                diagX++;
                diagY--;
            }
        }

        return false;
    }

    public boolean isWinnerFreestyle(int x, int y) {

        int count = 0;
        boolean isWinner = false;
        //check vertical win
        for (int row = 0; row < boardSize; row++) {
            if (boardArray[row][x] == playerTurn && count < 5)
                count++;
            else
                count = 0;
            if(count >= 5) {
                return true;
            }
        }

        count = 0;

        //check horizontal win
        for (int column = 0; column < boardSize; column++) {
            if (boardArray[y][column] == playerTurn && count < 5)
                count++;
            else
                count = 0;
            if(count >= 5) {
                return true;
            }
        }

        count = 0;

        //check diagonal win upper-left/lower-right
        int diagX = x;
        int diagY = y;
        while (diagX >= 0 && diagY >= 0) {
            if (boardArray[diagY][diagX] == playerTurn && count < 5)
                count++;
            else {
                break;
            }
            if (count >= 5) {
                return true;
            }
            diagX--;
            diagY--;
        }

        diagX = x + 1;
        diagY = y + 1;
        while (diagX < boardSize && diagY < boardSize) {
            if (boardArray[diagY][diagX] == playerTurn && count < 5)
                count++;
            else {
                break;
            }
            if (count >= 5) {
                return true;
            }
            diagX++;
            diagY++;
        }

        count = 0;

        //check diagonal win upper-right/lower-left
        diagX = x;
        diagY = y;
        while(diagX < boardSize && diagY >= 0) {
            if (boardArray[diagY][diagX] == playerTurn && count < 5)
                count++;
            else {
                break;
            }
            if (count >= 5) {
                return true;
            }
            diagX++;
            diagY--;
        }

        diagX = x - 1;
        diagY = y + 1;
        while(diagX >= 0 && diagY < boardSize) {
            if (boardArray[diagY][diagX] == playerTurn && count < 5)
                count++;
            else {
                break;
            }
            if (count >= 5) {
                return true;
            }
            diagX--;
            diagY++;
        }

        return false;
    }

    public boolean setPlayerTile(float xfloat, float yfloat) {
        int x = getLogicalCoordinate(xfloat);
        int y = getLogicalCoordinate(yfloat);

        if (boardArray[y][x] == PlayerColor.EMPTY) {
            boardArray[y][x] = playerTurn;
            return true;
        }
        else {
            return false;
        }
    }


    public boolean setAITile(int x, int y) {
        boardArray[y][x] = playerTurn;
        return true;
    }

    public boolean isTaken (float xfloat, float yfloat) {
        int x = getLogicalCoordinate(xfloat);
        int y = getLogicalCoordinate(yfloat);

        if (boardArray[y][x] != PlayerColor.EMPTY) {
            return true;
        }
        else {
            return false;
        }
    }

    public void switchPlayer() {
        if (playerTurn == PlayerColor.BLACK) {
            playerTurn = PlayerColor.WHITE;
        } else {
            playerTurn = PlayerColor.BLACK;
        }
    }

    public java.lang.String getCurrentPlayerColor() {
        return playerTurn.toString();
    }
    public boolean setPlayerTileonline(int x, int y,String status) {

        if (boardArray[y][x] == PlayerColor.EMPTY && status.equals("server")) {
            boardArray[y][x] = PlayerColor.WHITE;
            return true;
        }else if(boardArray[y][x] == PlayerColor.EMPTY && status.equals("client")){
            boardArray[y][x] = PlayerColor.BLACK;
            return true;
        }
        else {
            return false;
        }
    }


}



