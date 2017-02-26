package edu.pdx.cs.cs554.gomoku;

/**
 * Created by melod on 2/25/2017.
 */


        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.graphics.Path;
        import android.os.SystemClock;
        import android.view.MotionEvent;
        import android.widget.Chronometer;
        import android.widget.RelativeLayout;


public class GameView extends RelativeLayout{

    Paint black;
    Paint white;
    Paint grid;
    Paint text;
    private Path circle;
    private Path p1;
    private Path p2;
    private int boardSize;
    private int screenWidth;
    private int lineOffset;
    private int pieceRadius;
    private int blackScore, whiteScore;
    private float textSize;
    private float canvasTextSize;
    private static Bitmap background;
    private int mode;
    public final Chronometer timeElapsed;

    public GameView(Context context, int boardSize, int screenSizeX, int mode) {
        super(context);

        if (background != null) {
            background.recycle();
        }

        this.mode = mode; //Used to determine if this is an AI game or player vs player (1 = AI, 0 = player vs player)
        screenWidth = screenSizeX;
        background = BitmapFactory.decodeResource(getResources(), R.drawable.wood);
        background.setHasAlpha(false);
        lineOffset = Math.round(((float) screenSizeX) / ((float) (boardSize + 1)));
        black = new Paint(Paint.ANTI_ALIAS_FLAG);
        white = new Paint(Paint.ANTI_ALIAS_FLAG);
        text = new Paint(Paint.ANTI_ALIAS_FLAG);

        textSize = getResources().getDimensionPixelSize(R.dimen.myFontSize);
        canvasTextSize = textSize * (context.getResources().getDisplayMetrics().densityDpi/160f);

        grid = new Paint(Paint.ANTI_ALIAS_FLAG);
        grid.setAntiAlias(true);
        grid.setStrokeWidth(2);
        grid.setColor(Color.BLACK);
        white.setColor(Color.WHITE);
        white.setAntiAlias(true);
        text.setColor(Color.BLACK);
        text.setTextSize(canvasTextSize);
        text.setAntiAlias(true);
        black.setColor(Color.BLACK);
        black.setAntiAlias(true);
        circle = new Path();
        p1 = new Path();
        p2 = new Path();
        this.boardSize = boardSize;
        pieceRadius = (lineOffset/4);
        timeElapsed  = new Chronometer(context);
        timeElapsed.setTextColor(Color.BLACK);
        timeElapsed.setTextSize(textSize);
        timeElapsed.setX(text.measureText("Turn Time: ") + lineOffset);
        timeElapsed.setY(screenWidth+15 + (text.getFontSpacing()*2) - getResources().getDimensionPixelSize(getResources().getIdentifier("status_bar_height", "dimen", "android")));
        this.addView(timeElapsed);

        timeElapsed.setBase(SystemClock.elapsedRealtime());
        timeElapsed.start();
    }

    public void reset() {
        timeElapsed.setBase(SystemClock.elapsedRealtime());
        timeElapsed.start();
        circle.reset();
        p1.reset();
        p2.reset();
        invalidate();
    }

    @Override
    public void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(background, 0, 0, null);

        drawGrid(canvas);

        if (GameController.playerTurn == GameController.PlayerColor.BLACK) {
            canvas.drawPath(circle, black);

        }
        else {
            canvas.drawPath(circle, white);
        }
        canvas.drawPath(p1, black);
        canvas.drawPath(p2, white);
        //showing scoreboard
        if (mode == 0) {  //If Not AI Mode
            canvas.drawText("Black Score: " + blackScore, lineOffset, screenWidth, text);
            canvas.drawText("White Score: " + whiteScore, lineOffset, screenWidth + text.getFontSpacing(), text);
        }
        else {
            canvas.drawText("Your Score: " + blackScore, lineOffset, screenWidth, text);
            canvas.drawText("AI Score: " + whiteScore, lineOffset, screenWidth + text.getFontSpacing(), text);
        }
        canvas.drawText("Turn Time: ", lineOffset, screenWidth + (text.getFontSpacing() * 2), text);
        canvas.drawText("Turn Color:", lineOffset, screenWidth + (text.getFontSpacing() * 3), text);
        if (GameController.playerTurn == GameController.PlayerColor.BLACK){
            canvas.drawCircle(text.measureText("Turn Color: ") + lineOffset + pieceRadius, screenWidth + (text.getFontSpacing() * 3) - pieceRadius, pieceRadius, black);
        } else if (GameController.playerTurn == GameController.PlayerColor.WHITE) {
            canvas.drawCircle(text.measureText("Turn Color: ") + lineOffset + pieceRadius, screenWidth + (text.getFontSpacing() * 3) - pieceRadius, pieceRadius, white);
        }
    }

    private void touch_down(float x, float y) {
        circle.addCircle(x, y, pieceRadius, Path.Direction.CW);
    }

    private void touch_move(float x, float y) {
        circle.reset();
        circle.addCircle(x, y, pieceRadius, Path.Direction.CW);
    }

    private void touch_up(float x, float y) {
        if (GameController.playerTurn == GameController.PlayerColor.BLACK) {
            p1.addPath(circle);
        }
        else {
            p2.addPath(circle);
        }
        circle.reset();
        invalidate();
        timeElapsed.setBase(SystemClock.elapsedRealtime());
        timeElapsed.start();
    }

    private void touch_up_online(float x, float y,String status) {
        if (status.equals("server")) {
            p2.addPath(circle);
        }
        else {
            p1.addPath(circle);
        }
        invalidate();
        circle.reset();
    }

    public void updateScore(int black, int white) {
        blackScore = black;
        whiteScore = white;
    }

    public boolean onTouchEvent(MotionEvent event, float x, float y) {
        x = getSnapCoordinate(x);
        y = getSnapCoordinate(y);

        //If within boundaries of the board grid
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_down(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up(x, y);
                invalidate();
                break;
        }

        return true;
    }

    public void placeAIPiece(float x, float y) {
        x = getSnapCoordinate(x);
        y = getSnapCoordinate(y);
        touch_down(x, y);
        touch_up(x, y);
    }
    public void placePiece(int x, int y,String status) {
        x = (x + 1) * lineOffset;
        y = (y + 1) * lineOffset;

        touch_down(x, y);
        touch_up_online(x, y, status);
    }

    public float getSnapCoordinate(float position) {
        float toReturn = Math.round(position/lineOffset) * lineOffset;
        if (toReturn < lineOffset) {
            toReturn = lineOffset;
        }
        else if (toReturn > (boardSize * lineOffset)) {
            toReturn = boardSize * lineOffset;
        }

        return toReturn;
    }

    private void drawGrid(Canvas canvas) {
        for(int i = 1; i <= boardSize; i++) {

            canvas.drawLine((i * lineOffset), lineOffset, (i * lineOffset), (lineOffset * (boardSize)), grid); //draw col
            canvas.drawLine(lineOffset, (i*lineOffset), (lineOffset * (boardSize)), (i*lineOffset), grid); //draw row
        }
    }
}





