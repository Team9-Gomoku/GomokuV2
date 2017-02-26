package edu.pdx.cs.cs554.gomoku;

/**
 * Created by melod on 2/25/2017.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.Set;

public class BluetoothActivity extends Activity {
    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
    ListView lv;
    Button button_host,button_guest;
    static int boardSize;
    static boolean freestyle = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        button_host= (Button) findViewById(R.id.button_host);
        button_host.setOnClickListener(new View.OnClickListener() {

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

                ArrayAdapter adapter = new ArrayAdapter(BluetoothActivity.this, android.R.layout.simple_spinner_item, array_spinner);

                s.setAdapter(adapter);

                final AlertDialog.Builder builder = new AlertDialog.Builder(BluetoothActivity.this);
                builder.setCancelable(false)
                        .setView(layout);
                builder.setMessage("Select a board size:");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RadioButton freestyle = (RadioButton) findViewById(R.id.freestyle1);
                        if (freestyle.isChecked() == true) {
                            BluetoothActivity.freestyle = true;
                        }

                        switch (s.getSelectedItem().toString()) {
                            case "10x10":
                                BluetoothActivity.boardSize = 10;
                                break;
                            case "15x15":
                                BluetoothActivity.boardSize = 15;
                                break;
                            case "20x20":
                                BluetoothActivity.boardSize = 20;
                                break;
                        }

                        Intent mainIntent = new Intent(getApplicationContext(), BT_host.class);
                        startActivity(mainIntent);
                        finish();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        button_guest= (Button) findViewById(R.id.button_join);
        button_guest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //call bluetooth screen
                Intent mainIntent = new Intent(BluetoothActivity.this, BT_guest.class);
                BluetoothActivity.this.startActivity(mainIntent);
                finish();

            }
        });



    }



}
