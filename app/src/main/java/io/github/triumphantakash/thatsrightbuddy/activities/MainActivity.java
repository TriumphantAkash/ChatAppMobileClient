package io.github.triumphantakash.thatsrightbuddy.activities;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import io.github.triumphantakash.thatsrightbuddy.R;
import io.github.triumphantakash.thatsrightbuddy.threads.ClientSocketReader;
import io.github.triumphantakash.thatsrightbuddy.threads.ClientSocketWriter;

public class MainActivity extends AppCompatActivity {

    Socket clientSocket;
    DataOutputStream outToServer;
    BufferedReader inFromServer;
    TextView myText, friendsText;
    Button sendButton;
    Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myText = (TextView)findViewById(R.id.text_my);
        friendsText = (TextView)findViewById(R.id.text_friends);
        sendButton = (Button)findViewById(R.id.button_send);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Looper.myLooper() == null) {
                    Looper.prepare();
                }
                mHandler = new Handler() {
                    public void handleMessage(String msg) {
                        friendsText.append(msg);
                    }
                };
            }
        });

        try {
            clientSocket = new Socket("192.168.0.26", 6969);

            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            ClientSocketWriter socketWriteThread = new ClientSocketWriter(outToServer);
            socketWriteThread.start();

            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            ClientSocketReader socketReadThread = new ClientSocketReader(inFromServer, mHandler);
            socketReadThread.start();

        }catch (IOException e){
            Toast.makeText(getApplicationContext(), "could not connect to the server", Toast.LENGTH_SHORT).show();
        }

    }

    //send data to Writer thread in onResume function and also
    @Override
    protected void onResume() {
        super.onResume();

    }



}
