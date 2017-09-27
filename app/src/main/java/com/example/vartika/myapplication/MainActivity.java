package com.example.vartika.myapplication;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private final int PORT_NUM = 5001;
    private final String IP_ADDRESS = "192.168.43.156";

    private Socket socket = null;
    private Handler handler;
    DataOutputStream dataOutputStream = null;
    DataInputStream dataInputStream = null;
    TextView textResponse;
    TextView ipAddress;
    TextView  textPort;
    Button buttonConnect, buttonClear, buttonSendData;
    EditText welcomeMsg;
    EditText passwordText;
    String msgToServer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipAddress = (TextView) findViewById(R.id.address);
        textPort = (TextView) findViewById(R.id.port);
        buttonConnect = (Button) findViewById(R.id.connect);
        buttonClear = (Button) findViewById(R.id.clear);
        buttonSendData = (Button) findViewById(R.id.send_data);
        textResponse = (TextView) findViewById(R.id.response);

        welcomeMsg = (EditText) findViewById(R.id.welcomemsg);
        passwordText = (EditText) findViewById(R.id.password);

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tMsg = welcomeMsg.getText().toString();
                String password = passwordText.getText().toString();
                ipAddress.setText(IP_ADDRESS);
                StringBuffer ssidName = new StringBuffer(30);
                ssidName.append(tMsg);
                Log.i(LOG_TAG, "ssidName string buffer, " + ssidName);
                StringBuffer leftSSID = new StringBuffer();
                if (ssidName.length() < 30) {
                    for (int i = ssidName.length() + 1; i < 30; i++) {
                        leftSSID.append('0');
                    }
                }
                StringBuffer passwordText = new StringBuffer(30);
                passwordText.append(password);
                StringBuffer leftPassword = new StringBuffer();
                if (passwordText.length() < 30) {
                    for (int i = passwordText.length() + 1; i < 30; i++) {
                        leftPassword.append('0');
                    }
                }

                Log.i(LOG_TAG, "password string buffer, " + passwordText.length());
                // main string contains ssid and password
                String totalString = ssidName + ";" + leftSSID + passwordText + ";" + leftPassword;
                Log.i(LOG_TAG, "totalString, " + totalString.length());
                if (totalString.equals("")) {
                    tMsg = null;
                    Toast.makeText(MainActivity.this, "No SSID and Password sent", Toast.LENGTH_SHORT).show();
                }
                if (tMsg.equals("")) {
                    tMsg = null;
                    Toast.makeText(MainActivity.this, "No Welcome Msg sent", Toast.LENGTH_SHORT).show();
                }

                if (!ipAddress.getText().toString().isEmpty() && !textPort.getText().toString().isEmpty()) {
                    MyClientTask myClientTask = new MyClientTask(ipAddress
                            .getText().toString(), Integer.parseInt(textPort
                            .getText().toString()),
                            totalString);
                    myClientTask.execute();
                } else {
                    Toast.makeText(MainActivity.this, "Please enter ip and port", Toast.LENGTH_SHORT).show();
                }
            }
        });


        buttonSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessageToServer();
            }
        });


        buttonClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                textResponse.setText("");
            }
        });
    }

    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;
        String response = "";

        MyClientTask(String addr, int port, String msgTo) {
            dstAddress = addr;
            dstPort = port;
            msgToServer = msgTo;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
                Log.i(LOG_TAG, "socket client, " + socket);
                try {
                    socket = new Socket(dstAddress, dstPort);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {

                }
                Log.i(LOG_TAG, "socket client, " + socket);
               if (socket != null) {
                   Log.i(LOG_TAG, "isSocketConnected, " + socket.isConnected());
               }

                return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            buttonConnect.setText("Connected");
            super.onPostExecute(result);
        }
    }

    private void sendMessageToServer() {
        final String[] response = {""};
        String messageServer = welcomeMsg.getText().toString();
        Log.i(LOG_TAG, "messageServer, " + messageServer.length());
        if (messageServer.equals("")) {
            messageServer = null;
            Toast.makeText(MainActivity.this, "No Welcome Msg sent", Toast.LENGTH_SHORT).show();
        }

        if (socket == null) {
            Toast.makeText(this, "NOT CONNECTED", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            dataOutputStream = new DataOutputStream(
                    socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());
            Log.i(LOG_TAG, "dataOutputStream, " + dataOutputStream);
            Log.i(LOG_TAG, "dataInputStream, " + dataInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            PrintWriter out =
                    new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
       BufferedReader in = null;
        try {
            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            Log.i(LOG_TAG, "BufferedReader In, " + in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (socket != null) {
            Log.i(LOG_TAG, "while sending data socket, " + socket.isConnected());
        }
        handler = new Handler();

        final BufferedReader finalIn = in;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (msgToServer != null) {
                    try {
                        Log.i(LOG_TAG, "message to server, " + msgToServer);
                        dataOutputStream.writeBytes(msgToServer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i(LOG_TAG, "dataOutputStream, " + dataOutputStream.size());
                    try {
                       String userInput = finalIn.readLine();
                        Log.i(LOG_TAG, "echo:, " + userInput);
                        response[0] = userInput;
                        Log.i(LOG_TAG, "response, " + response[0]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            // make operation on UI - on example
                            // on progress bar.
                            textResponse.setText(response[0]);
                        }
                    });
                }
            }
        }).start();


/*                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }*/

/*
            if (dataOutputStream != null) {
                try {
                    dataOutputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (dataInputStream != null) {
                try {
                    dataInputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
*/


        }
    }
