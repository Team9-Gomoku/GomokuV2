package edu.pdx.cs.cs554.gomoku;

/**
 * Created by melod on 2/25/2017.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
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
import android.widget.Toast;

public class BT_host extends Activity {

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
    private BT_service mConnection = null;


    private static final String TAG = "BT_Gomoku";
    private static final boolean D = true;

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

    private boolean is_game_start = false;
    private boolean is_in_turn = false;
    int size;

    /**
     * String buffer for outgoing messages
     */
    private StringBuffer mOutStringBuffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

//         If the adapter is null, then Bluetooth is not supported, in which case we will quit
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            this.finish();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(this, "Waiting for Client to join!", Toast.LENGTH_LONG).show();
        // Check whether BT is enabled

        // If BT is not on, request that it be enabled.
        // setupBTService() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mBTService == null) {
            setupBTService();
        }


    }

    @Override
    public void onResume() {
        System.out.println("BluetoothChatFragment::onResume is invoked.\n");

        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mBTService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mBTService.getState() == mBTService.STATE_NONE) {
                // Start the Bluetooth chat services
                mBTService.start();

                System.out.print("Waiting for guest to join.");
            }
        }

        System.out.println("BluetoothChatFragment::onResume is invoked.\n");

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


            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupBTService();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Toast.makeText(this, "bt_not_enabled_leaving",
                            Toast.LENGTH_SHORT).show();
                    this.finish();
                }
        }
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

                    startGameHost();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(context, msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void startGameHost() {

        // When connection is established: draw the board on host first
        // setContentView(R.layout.activity_main);
        Toast.makeText(context, "Game start with " + connectedDeviceName + "!", Toast.LENGTH_SHORT).show();

        boardSize = BluetoothActivity.boardSize;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenSizeX = size.x;
        screenSizeY = size.y;

        statusBarHeight = getStatusBarHeight();
//        Toast.makeText(context, "status bar height= " + statusBarHeight, Toast.LENGTH_SHORT).show();
        screenSizeY -= statusBarHeight;

        lineOffset = Math.round(((float) screenSizeX) / ((float) (boardSize + 1)));

        this.mode = BluetoothActivity.freestyle;
//        Toast.makeText(context, "freestyle= " + mode, Toast.LENGTH_SHORT).show();
        sa = new GameView(this, boardSize, screenSizeX, 0);
        sa.setBackgroundColor(Color.argb(255, 255, 250, 250));
        setContentView(sa);
        game = new GameController(boardSize, screenSizeX, mode);
        // send message (the size of board) to guest to start game on guest
        //    The format is: "[game_start] size"
//        String temp_msg = "[game_start] ";
//        System.out.println(temp_msg + Integer.toString(boardSize));
//        sendMessage(temp_msg + Integer.toString(boardSize)+this.mode);
        String temp_msg = "[game_start] " +
                "(" + Integer.toString(boardSize) + "," + this.mode + ")";
        sendMessage(temp_msg);
        // setup the related variables for host
        is_game_start = true;
        is_in_turn = true;

        Toast.makeText(context, "You are player BLACK. Its your turn! ", Toast.LENGTH_SHORT).show();


    }

    protected void handle_message_read(String str) {
        //This method will decode all the messages recieved from the client and if it is a move place the move
        //if it is a check for in then update score.

        String msg_type = str.substring(str.indexOf("[") + 1, str.indexOf("]"));

        if (msg_type.equals("remote_move")) {

            String str_i = str.substring(str.indexOf("(") + 1, str.indexOf(","));
            String str_j = str.substring(str.indexOf(",") + 1, str.indexOf(")"));
            int x1 = Integer.valueOf(str_i);
            int y1 = Integer.valueOf(str_j);

            game.setPlayerTileonline(x1, y1, "server");
            sa.placePiece(x1, y1, "server");

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
                builder.setMessage(game.getCurrentPlayerColor() + " won! Rematch?")
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


            // Toast.makeText(this, "connected"+message, Toast.LENGTH_SHORT).show();

        }

        if (msg_type.equals("is_winner")) {
            String str_i = str.substring(str.indexOf("(") + 1, str.indexOf(","));
            String str_j = str.substring(str.indexOf(",") + 1, str.indexOf(")"));
            blackScore = Integer.valueOf(str_i);
            whiteScore = Integer.valueOf(str_j);
            sa.updateScore(blackScore, whiteScore);


            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(game.getCurrentPlayerColor() + " won! Rematch?")
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
//        Toast.makeText(this, "connected" + message, Toast.LENGTH_SHORT).show();
        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mBTService.write(send);


        }
    }

    protected void acceptCharacter(byte character) {

    }


    private void setupBTService() {


        System.out.println("BT_host::setupBTService() is invoked.");

        context = getApplicationContext();
        mBTService = new BT_service(context, mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("Hi");
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
                            builder.setMessage(game.getCurrentPlayerColor() + " won! Rematch?")
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