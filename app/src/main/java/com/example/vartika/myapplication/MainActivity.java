package com.example.vartika.myapplication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private Socket socket = null;
    TextView textResponse;
    EditText editTextAddress, editTextPort;
    Button buttonConnect, buttonClear, buttonSendData;
    EditText welcomeMsg;

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


/*
        buttonSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessageToServer();
            }
        });
*/

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
        String msgToServer;

        MyClientTask(String addr, int port, String msgTo) {
            dstAddress = addr;
            dstPort = port;
            msgToServer = msgTo;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            socket = null;
            DataOutputStream dataOutputStream = null;
            DataInputStream dataInputStream = null;

            try {
                Log.i(LOG_TAG, "socket client, " + socket);
                socket = new Socket(dstAddress, dstPort);
                Log.i(LOG_TAG, "socket client, " + socket);
                Log.i(LOG_TAG, "isSocketConnected, " + socket.isConnected());
                // sendMessageToServer();

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
                Log.i(LOG_TAG, "echo:, "  + userInput);
                response = userInput;
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
                    Log.i(LOG_TAG, "response, " + userInput);

/*
                try {
                    while (true) {
                        System.out.println(dataInputStream.readUTF());
                        response = dataInputStream.readUTF();
                    }
                } catch (EOFException e) {
                    e.printStackTrace();
                }
*/

             //   char responseChar = dataInputStream.readChar();
               //   Log.i(LOG_TAG, "response Char, " + responseChar);
                // response = dataInputStream.readChar();


/*
                while (dataInputStream.available() > 0) {
                String k = dataInputStream.readUTF();
                    Log.i(LOG_TAG, "k value, " + k);
                }
*/

            } catch (IOException e) {
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (Exception e) {
                e.printStackTrace();
                response = "IOException: " + e.toString();
            } finally {
/*
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
*/

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
            }
        }

            @Override
            protected void onPostExecute (Void result){
                buttonConnect.setText("Connected");
                 textResponse.setText(response);
                super.onPostExecute(result);
            }
        }

        private void sendMessageToServer() {
            String messageServer = welcomeMsg.getText().toString();
            if (messageServer.equals("")) {
                messageServer = null;
                Toast.makeText(MainActivity.this, "No Welcome Msg sent", Toast.LENGTH_SHORT).show();
            }
            //Send the message to the server
            OutputStream os = null;
            try {
                os = socket.getOutputStream();
                Log.i(LOG_TAG, "os, " + os);
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert os != null;
            OutputStreamWriter osw = new OutputStreamWriter(os);
            Log.i(LOG_TAG, "osw," + osw);
            BufferedWriter bw = new BufferedWriter(osw);
            String number = "2";
            String sendMessage = number + "\n";
            try {
                bw.write(sendMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
/*        try {
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        }
    }



