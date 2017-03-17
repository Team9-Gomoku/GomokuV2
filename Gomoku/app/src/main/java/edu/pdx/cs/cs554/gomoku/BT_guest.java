package edu.pdx.cs.cs554.gomoku;

/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class BT_guest extends Activity {
    //initializations for game
    private GameView sa;
    private GameController game;
    public static int screenSizeX;
    public static int screenSizeY;
    public static int lineOffset;
    public static int boardSize;
    public boolean mode;
    public int statusBarHeight;
    public int blackScore = 0;
    public int whiteScore = 0;

    //Initializations for bluetooth
    private static final String TAG = "BT_Gomoku";
    private static final boolean D = true;

    int size = 10;
    float x1, y1;


    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_GAME_START = 6;

    protected Context context;
    private String connectedDeviceName;
    private BT_service mBTService;
    private BluetoothAdapter mBluetoothAdapter;

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    // Game status
    private static final int GAME_ST_NORMAL = 0;
    private static final int GAME_ST_WIN = 1;
    private static final int GAME_ST_TIE = 2;

    //Indicate whether game start
    private boolean is_game_start = false;
    private boolean is_in_turn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);


        //after enter the main activity of BT_guest, check whether BT is available
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            this.finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        //Check whether BT is enabled,If BT is not on, request that it be enabled.
        // setupBTService() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mBTService == null) {
            setupBTService();
        }

        // Button for guest to scan and connect to host will be handled by BT_DeviceListActivity.
        // This activity will return the host device MAC address

        Button button_guest_scan = (Button) findViewById(R.id.guest_scan);
        button_guest_scan.setVisibility(View.VISIBLE);
        button_guest_scan.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent serverIntent = new Intent(BT_guest.this, BT_DeviceListActivity.class);
                        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
                    }
                }
        );
    }

    public void onDestroy() {

        super.onDestroy();
        if (mBTService != null) {
            mBTService.stop();
        }
        finish();

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                System.out.println("BT_guest::onActivityResult() REQUEST_CONNECT_DEVICE_SECURE: ");
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    System.out.println("RESULT_OK\n");
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupBTService();
                } else {
                    // Bluetooth not enabled or an error occurred
                    Toast.makeText(this, "bt_not_enabled_leaving",
                            Toast.LENGTH_SHORT).show();
                    this.finish();
                }
        }
    }

    private void setupBTService() {

        context = getApplicationContext();
        mBTService = new BT_service(context, mHandler);
    }


    protected final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BT_service.STATE_CONNECTED:
                            //TODO: Tell the game that the connection was established
                            break;
                        case BT_service.STATE_CONNECTING:
                            //TODO: Tell the game that it's connecting
                            break;
                        case BT_service.STATE_LISTEN:
                        case BT_service.STATE_NONE:
                            //TODO: Tell the game that it's not connected
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;

                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    //TODO: Handle the message (i.e. wait for the ACK)
                    break;
                case MESSAGE_READ:
//                    Toast.makeText(context, "inside message read", Toast.LENGTH_SHORT).show();
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    handle_message_read(readMessage);
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    connectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(context, "Connected to "
                            + connectedDeviceName, Toast.LENGTH_SHORT).show();

                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(context, msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    //
    protected void handle_message_read(String str) {


        String msg_type = str.substring(str.indexOf("[") + 1, str.indexOf("]"));

        System.out.println("BT_guest::handle_message_read(): msg_type = " + msg_type);

        // message to start game
        if (msg_type.equals("game_start")) {
           // String str_size = str.substring(str.indexOf(" ") + 1);
            String str_size = str.substring(str.indexOf("(") + 1, str.indexOf(","));
            String str_mode = str.substring(str.indexOf(",") + 1, str.indexOf(")"));
//            Toast.makeText(context, "mode=" + str_mode, Toast.LENGTH_SHORT).show();
            size = Integer.parseInt(str_size);
            this.mode = Boolean.valueOf(str_mode);
//            if(str_mode.equals(true)){
//                this.mode =true;
//            Toast.makeText(context, "this.mode=" + this.mode, Toast.LENGTH_SHORT).show();}
//            else
//                this.mode =false;
//            Toast.makeText(context, "this.mode=" + this.mode, Toast.LENGTH_SHORT).show();
            startGameGuest();
        } else if (msg_type.equals("remote_move")) {

            String str_i = str.substring(str.indexOf("(") + 1, str.indexOf(","));
            String str_j = str.substring(str.indexOf(",") + 1, str.indexOf(")"));
            int x1 = Integer.parseInt(str_i);
            int y1 = Integer.parseInt(str_j);

//            Toast.makeText(context, "x1=" + x1 + " y1=" + y1, Toast.LENGTH_SHORT).show();
            //TODO: got the move from host and update the grid

            game.setPlayerTileonline(x1, y1, "client");
            sa.placePiece(x1, y1, "client");
            if (game.isWinner(x1, y1, true)) {
                //get score
                if (game.getCurrentPlayerColor() == "BLACK") {
                    blackScore += 1;
                } else {
                    whiteScore += 1;
                }
                String winner_msg = "[is_winner] " +
                        "(" + Integer.toString(blackScore) + "," + Integer.toString(whiteScore) + ")";
                sendMessage(winner_msg);
                sa.updateScore(blackScore, whiteScore);
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
             //   builder.setMessage(game.getCurrentPlayerColor() + " won! Rematch?")
                builder.setMessage( "Black Player won! Rematch?")
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        })
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                sa.reset();
                                game = new GameController(boardSize, screenSizeX, mode);
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }


            game.switchPlayer();
            is_in_turn = true;
//            Toast.makeText(this, "Your turn!", Toast.LENGTH_SHORT).show();

        } else if (msg_type.equals("is_winner")) {
            String str_i = str.substring(str.indexOf("(") + 1, str.indexOf(","));
            String str_j = str.substring(str.indexOf(",") + 1, str.indexOf(")"));
            blackScore = Integer.valueOf(str_i);
            whiteScore = Integer.valueOf(str_j);
            sa.updateScore(blackScore, whiteScore);
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
          //  builder.setMessage(game.getCurrentPlayerColor() + " won! Rematch?")
            builder.setMessage("Black Player won! Rematch?")
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            sa.reset();
                            game = new GameController(boardSize, screenSizeX, mode);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }
    }

    private void startGameGuest() {

        // write code to make move and draw board

        setContentView(R.layout.activity_main);
        boardSize = size;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenSizeX = size.x;
        screenSizeY = size.y;

        statusBarHeight = getStatusBarHeight();
//        Toast.makeText(context, "status bar height= " + statusBarHeight, Toast.LENGTH_SHORT).show();
        screenSizeY -= statusBarHeight;


        lineOffset = Math.round(((float) screenSizeX) / ((float) (this.boardSize + 1)));
//        Toast.makeText(context, "lineoffset= " + lineOffset, Toast.LENGTH_SHORT).show();
        //this.mode = BluetoothActivity.freestyle;
        sa = new GameView(this, boardSize, screenSizeX, 0);
        sa.setBackgroundResource(R.drawable.wood);
     //   sa.setBackgroundColor(Color.TRANSPARENT);
        setContentView(sa);

        game = new GameController(boardSize, screenSizeX, mode);


        //set is_game_start = true and set is_in_turn = false, as host always starts the game
        is_game_start = true;
        is_in_turn = false;
        Toast.makeText(context, "You are player WHITE.", Toast.LENGTH_SHORT).show();




    }


    /**
     * Establish connection with other divice
     *
     * @param data   An {@link Intent} with {@link BT_DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(BT_DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);


        mBTService.connect(device, secure);
    }


    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mBTService.getState() != mBTService.STATE_CONNECTED) {
            Toast.makeText(this, "not_connected", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mBTService.write(send);


        }
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
        if (is_in_turn == true) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:

                    if (game.setPlayerTile(x, y) == true) {
                        sa.onTouchEvent(event, x, y);

                        int i = game.getLogicalCoordinate(x);
                        int j = game.getLogicalCoordinate(y);
                        String location_msg = "[remote_move] " +
                                "(" + i + "," + j + ")";
                        sendMessage(location_msg);

                        if (game.isWinner(x, y, false)) {
                            //get score
                            if (game.getCurrentPlayerColor() == "BLACK") {
                                blackScore += 1;
                            } else {
                                whiteScore += 1;
                            }
                            String winner_msg = "[is_winner] " +
                                    "(" + Integer.toString(blackScore) + "," + Integer.toString(whiteScore) + ")";
                            sendMessage(winner_msg);
                            sa.updateScore(blackScore, whiteScore);

                            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                         //   builder.setMessage(game.getCurrentPlayerColor() + " won! Rematch?")
                            builder.setMessage("Black Player won! Rematch?")
                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            finish();
                                        }
                                    })
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            sa.reset();
                                            game = new GameController(boardSize, screenSizeX, mode);
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();

                        } else {
                            //give out turn and sending turn change message
                            is_in_turn = false;
                            //      msg fmt: [switch_turn]
//                        String switch_turn_msg = "[switch_turn]";
//                        sendMessage(switch_turn_msg);
                            game.switchPlayer();
                        }
                    }
                    break;
                default:
                    if (game.isTaken(x, y) == false) {
                        sa.onTouchEvent(event, x, y);


                    }
            }
        }


        return true;
    }


}
