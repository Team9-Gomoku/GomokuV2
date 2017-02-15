package edu.pdx.cs.cs554.gomoku;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.TextView;

public class TimerView extends TextView {

    private static final int PLAYER_TIME_LIMIT_IN_MILLISECONDS = 10 * 60 * 1000;    // 10 minutes

    private String prefix;

    private PlayerTimer playerTimer;

    private long remainingMilliseconds = PLAYER_TIME_LIMIT_IN_MILLISECONDS + 1;

    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void start() {
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
            TimerView.this.setText(TimerView.this.getPrefix() + String.format("%02d:%02d", minutes, seconds));
        }

        @Override
        public void onFinish() {
            TimerView.this.setText(TimerView.this.getPrefix() + String.format("%02d:%02d", 0, 0));
        }

        public void stop() {
            shouldStop = true;
        }
    }
}
