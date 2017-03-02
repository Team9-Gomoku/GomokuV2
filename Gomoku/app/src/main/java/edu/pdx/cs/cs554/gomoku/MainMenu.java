package edu.pdx.cs.cs554.gomoku;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;


public class MainMenu extends Activity {

    static int boardSize;
    static boolean freestyle = false;
    Button boffline,bonline,bai ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        boffline = (Button) findViewById(R.id.bOffline);
        boffline.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                //call game activity
                Context mContext = getApplicationContext();
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.spinner, null);

                final String array_spinner[];
                array_spinner = new String[3];

                array_spinner[0] = "10x10";
                array_spinner[1] = "15x15";
                array_spinner[2] = "20x20";

                final Spinner s = (Spinner) layout.findViewById(R.id.Spinner01);

                ArrayAdapter adapter = new ArrayAdapter(MainMenu.this, android.R.layout.simple_spinner_item, array_spinner);

                s.setAdapter(adapter);

                final AlertDialog.Builder builder = new AlertDialog.Builder(MainMenu.this);
                builder.setCancelable(false)
                        .setView(layout);

                builder.setMessage("Select a board size:");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RadioButton freestyle = (RadioButton) findViewById(R.id.freestyle);
                        if (freestyle.isChecked() == true) {
                            MainMenu.freestyle = true;
                        }
                        switch (s.getSelectedItem().toString()) {
                            case "10x10":
                                MainMenu.boardSize = 10;
                                break;
                            case "15x15":
                                MainMenu.boardSize = 15;
                                break;
                            case "20x20":
                                MainMenu.boardSize = 20;
                                break;
                        }

                        Intent intent = new Intent("android.intent.action.DISPLAY");
                        startActivity(intent);
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });


        bonline = (Button) findViewById(R.id.bOnline);
        bonline.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //call bluetooth screen

                Intent intent = new Intent(v.getContext(), BluetoothActivity.class);
                startActivity(intent);
            }
        });


        bai = (Button) findViewById(R.id.bAi);
        bai.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                Context mContext = getApplicationContext();
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.spinner, null);

                final String array_spinner[];
                array_spinner = new String[3];

                array_spinner[0] = "10x10";
                array_spinner[1] = "15x15";
                array_spinner[2] = "20x20";

                final Spinner s = (Spinner) layout.findViewById(R.id.Spinner01);

                ArrayAdapter adapter = new ArrayAdapter(MainMenu.this, android.R.layout.simple_spinner_item, array_spinner);

                s.setAdapter(adapter);

                final AlertDialog.Builder builder = new AlertDialog.Builder(MainMenu.this);
                builder.setCancelable(false)
                        .setView(layout);
                builder.setMessage("Select a board size:");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RadioButton freestyle = (RadioButton) findViewById(R.id.freestyle);
                        if (freestyle.isChecked() == true) {
                            MainMenu.freestyle = true;
                        }
                        switch (s.getSelectedItem().toString()) {
                            case "10x10":
                                MainMenu.boardSize = 10;
                                break;
                            case "15x15":
                                MainMenu.boardSize = 15;
                                break;
                            case "20x20":
                                MainMenu.boardSize = 20;
                                break;
                        }
                        Intent intent = new Intent("android.intent.action.DISPLAY_AI");
                        startActivity(intent);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });


    }


}
