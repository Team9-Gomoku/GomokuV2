package edu.pdx.cs.cs554.gomoku;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

public class ScoreAdapter extends BaseAdapter {

    private final Map.Entry<String, Integer>[] scoresByPlayerName;
    private final Activity activity;

    public ScoreAdapter(Activity activity) {
        this.activity = activity;
        this.scoresByPlayerName = activity.getPreferences(Context.MODE_PRIVATE)
                .getAll().entrySet().toArray(new Map.Entry[0]);
        Arrays.sort(scoresByPlayerName, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
    }

    @Override
    public int getCount() {
        return (scoresByPlayerName.length + 1) * 3;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv = new TextView(activity);
        if (position == 0) {
            tv.setText("RANK");
            tv.setTypeface(Typeface.DEFAULT_BOLD);
        } else if (position == 1) {
            tv.setText("NAME");
            tv.setGravity(Gravity.RIGHT);
            tv.setTypeface(Typeface.DEFAULT_BOLD);
        } else if (position == 2) {
            tv.setText("SCORE");
            tv.setGravity(Gravity.RIGHT);
            tv.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            int index = (position - 3) / 3;
            Map.Entry playerScore = scoresByPlayerName[index];
            switch (position % 3) {
                case 0:
                    tv.setText(String.valueOf(index+1));
                    break;
                case 1:
                    tv.setText(playerScore.getKey().toString());
                    tv.setGravity(Gravity.RIGHT);
                    break;
                case 2:
                    tv.setText(playerScore.getValue().toString());
                    tv.setGravity(Gravity.RIGHT);
                    break;
                default:
                    throw new IllegalStateException("This cannot happen!");
            }
        }
        return tv;
    }
}
