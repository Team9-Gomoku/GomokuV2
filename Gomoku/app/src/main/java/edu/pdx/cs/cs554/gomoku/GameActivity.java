package edu.pdx.cs.cs554.gomoku;

/**
 * Created by melod on 2/25/2017.
 */


        import android.app.Activity;
        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.graphics.Color;
        import android.graphics.Point;
        import android.os.Bundle;
        import android.view.Display;
        import android.view.MotionEvent;
        import android.widget.Toast;


public class GameActivity extends Activity {

    private GameView sa;
    private GameController game;
    public static int screenSizeX;
    public static int screenSizeY;
    public static int lineOffset;
    public static int boardSize;
    boolean freestyle;
    public int statusBarHeight;
    public int blackScore = 0;
    public int whiteScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_online);

        boardSize = MainMenu.boardSize;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenSizeX = size.x;
        screenSizeY = size.y;

        statusBarHeight = getStatusBarHeight();

        screenSizeY -= statusBarHeight;

        lineOffset = Math.round(((float)screenSizeX)/((float)(this.boardSize + 1)));

        this.freestyle = MainMenu.freestyle;
        Toast.makeText(getApplicationContext(), "game activity freestyle= " + MainMenu.freestyle, Toast.LENGTH_SHORT).show();
        sa = new GameView(this, boardSize, screenSizeX, 0);
        sa.setBackgroundColor(Color.argb(255, 255, 250, 250));
        //RelativeLayout layout = new RelativeLayout(this);
        //layout.addView(sa);
        //layout.addView(sa.timeElapsed);
        setContentView(sa);
        game = new GameController(boardSize, screenSizeX, freestyle);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = (event.getY() - statusBarHeight);

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:

                if (game.setPlayerTile(x, y) == true) {
                    sa.onTouchEvent(event, x, y);
                    if (game.isWinner(x, y, false)) {
                        //get score
                        if (game.getCurrentPlayerColor() == "BLACK"){
                            blackScore += 1;
                        }else {
                            whiteScore += 1;
                        }

                        sa.updateScore(blackScore, whiteScore);

                        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage(game.getCurrentPlayerColor() + " won! Rematch?")
                                .setCancelable(false)
                                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                    }
                                })
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        sa.reset();
                                        game = new GameController(boardSize, screenSizeX, freestyle);
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else {
                        game.switchPlayer();
                    }
                }
                break;
            default:
                if (game.isTaken(x, y) == false) {
                    sa.onTouchEvent(event, x, y);
                }
        }

        return true;
    }

}

