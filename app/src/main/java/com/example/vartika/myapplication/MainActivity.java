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
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private Socket socket = null;
    private Handler handler;
    DataOutputStream dataOutputStream = null;
    DataInputStream dataInputStream = null;
    TextView textResponse;
    EditText editTextAddress, editTextPort;
    Button buttonConnect, buttonClear, buttonSendData;
    EditText welcomeMsg;
    String msgToServer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextAddress = (EditText) findViewById(R.id.address);
        editTextPort = (EditText) findViewById(R.id.port);
        buttonConnect = (Button) findViewById(R.id.connect);
        buttonClear = (Button) findViewById(R.id.clear);
        buttonSendData = (Button) findViewById(R.id.send_data);
        textResponse = (TextView) findViewById(R.id.response);

        welcomeMsg = (EditText) findViewById(R.id.welcomemsg);

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tMsg = welcomeMsg.getText().toString();
                if (tMsg.equals("")) {
                    tMsg = null;
                    Toast.makeText(MainActivity.this, "No Welcome Msg sent", Toast.LENGTH_SHORT).show();
                }

                if (!editTextAddress.getText().toString().isEmpty() && !editTextPort.getText().toString().isEmpty()) {
                    MyClientTask myClientTask = new MyClientTask(editTextAddress
                            .getText().toString(), Integer.parseInt(editTextPort
                            .getText().toString()),
                            tMsg);
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
            try {
                Log.i(LOG_TAG, "socket client, " + socket);
                try {
                    socket = new Socket(dstAddress, dstPort);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i(LOG_TAG, "socket client, " + socket);
                Log.i(LOG_TAG, "isSocketConnected, " + socket.isConnected());

/*
                dataOutputStream = new DataOutputStream(
                        socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());

                PrintWriter out =
                        new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(socket.getInputStream()));
                BufferedReader stdIn =
                        new BufferedReader(
                                new InputStreamReader(System.in));

                if (msgToServer != null) {
                    dataOutputStream.writeUTF(msgToServer);
                    Log.i(LOG_TAG, "dataOutputStream, " + dataOutputStream.size());
                }

                String userInput;
                userInput = in.readLine();
                Log.i(LOG_TAG, "echo:, " + userInput);
                response = userInput;
*/
/*
                while ((userInput = stdIn.readLine()) != null) {
                    out.println(userInput);
                    System.out.println("echo: " + in.readLine());
                }
*/
                //    dataOutputStream.flush();
                //  dataOutputStream.close();
                //  int b = dataInputStream.read();
                //Log.i(LOG_TAG, "b , " + b);
                //  String k = dataInputStream.readUTF();
                // Log.i(LOG_TAG, "k value, " + k);
//                    response = dataInputStream.readUTF();
                //  Log.i(LOG_TAG, "response, " + userInput);

/*
                try {
                    while (true) {
                        System.out.println(dataInputStream.readUTF());
                        response = dataInputStream.readUTF();
                    }
                } catch (EOFException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (Exception e) {
                e.printStackTrace();
                response = "IOException: " + e.toString();
            } finally {
*/
/*
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
*//*

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
                return null;
*/

            }finally {

            }
        return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            buttonConnect.setText("Connected");
            //  textResponse.setText(response);
            super.onPostExecute(result);
        }
    }

    private void sendMessageToServer() {
        final String[] response = {""};
        String messageServer = welcomeMsg.getText().toString();
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

        Log.i(LOG_TAG, "while sending data socket, " + socket.isConnected());
        handler = new Handler();

        final BufferedReader finalIn = in;
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Perform long-running task here
                // (like audio buffering).
                // you may want to update some progress
                // bar every second, so use handler:
                if (msgToServer != null) {
                    try {
                        dataOutputStream.writeUTF(msgToServer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i(LOG_TAG, "dataOutputStream, " + dataOutputStream.size());
                    String userInput;
                    try {
                        userInput = finalIn.readLine();
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

/*
        if (msgToServer != null) {
            try {
                dataOutputStream.writeUTF(msgToServer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i(LOG_TAG, "dataOutputStream, " + dataOutputStream.size());
        }

        String userInput;
        try {
            userInput = in.readLine();
            Log.i(LOG_TAG, "echo:, " + userInput);
            response = userInput;
            Log.i(LOG_TAG, "response, " + response);
            textResponse.setText(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
*/

      /*  try {
            while (true) {
                System.out.println(dataInputStream.readUTF());
                response = dataInputStream.readUTF();
            }
        } catch (EOFException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
        } catch (Exception e) {
            e.printStackTrace();
            response = "IOException: " + e.toString();
        } finally {
*//*
                if (socket != null) {
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
