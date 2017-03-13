package edu.pdx.cs.cs554.gomoku;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.TextView;

import static edu.pdx.cs.cs554.gomoku.Constants.ONE_MINUTE_IN_MILLISECONDS;
import static edu.pdx.cs.cs554.gomoku.Constants.PLAYER_TIME_LIMIT_IN_MILLISECONDS;

public class TimerView extends TextView {

    private String prefix;

    private PlayerTimer playerTimer;

    private long remainingMilliseconds = PLAYER_TIME_LIMIT_IN_MILLISECONDS + 1;

    private boolean oneMinutePerRoundMode = false;

    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void start() {
        if (oneMinutePerRoundMode) {
            remainingMilliseconds = ONE_MINUTE_IN_MILLISECONDS;
            playerTimer = null;
        }
        if (playerTimer == null) {
            playerTimer = new PlayerTimer(remainingMilliseconds, 1000);
        }
        playerTimer.start();
    }

    public void pause() {
        if (playerTimer != null) {
            playerTimer.stop();
        }
        playerTimer = null;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    class PlayerTimer extends CountDownTimer {

        private boolean shouldStop = false;

        public PlayerTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (shouldStop) {
                TimerView.this.remainingMilliseconds = millisUntilFinished;
                cancel();
            }
            long minutes = millisUntilFinished / 1000 / 60;
            long seconds = millisUntilFinished / 1000 % 60;
            TimerView.this.setText(prefix + String.format("%02d:%02d", minutes, seconds));
        }

        @Override
        public void onFinish() {
            TimerView.this.setText(prefix + "00:00");
            if (oneMinutePerRoundMode) {
                String msg = prefix + "has timed out!  Click BACK TO MENU.";
                Board board = ((Board) ((MainActivity) getContext()).findViewById(R.id.board));
                board.showWinningMessage(msg);
                if (board.getGameMode().equals(GameMode.OFFLINE)) {
                    SharedPreferences.Editor editor = ((MainActivity) getContext())
                            .getPreferences(Context.MODE_PRIVATE).edit();
                    Player winner;
                    if (TimerView.this.prefix.startsWith("BLACK")) {
                        winner = board.getWhitePlayer();
                    } else {
                        winner = board.getBlackPlayer();
                    }
                    winner.incrementScore();
                    editor.putInt(winner.getName(), winner.getScore());
                    editor.commit();
                }
            } else {
                oneMinutePerRoundMode = true;
                TimerView.this.start();
            }
        }

        public void stop() {
            shouldStop = true;
        }
    }
}
