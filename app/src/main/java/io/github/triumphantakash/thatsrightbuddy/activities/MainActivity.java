package io.github.triumphantakash.thatsrightbuddy.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    //sample comment from devMayank branch
    String sentence;
    String modifiedSentence;
    BufferedReader inFromUser;
    Socket clientSocket;
    DataOutputStream outToServer;
    BufferedReader inFromServer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            clientSocket = new Socket("192.168.0.26", 6969);

            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            ClientSocketWriter socketWriteThread = new ClientSocketWriter(outToServer);
            socketWriteThread.start();

            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            ClientSocketReader socketReadThread = new ClientSocketReader(inFromServer);
            socketReadThread.start();
        }catch (IOException e){
            Toast.makeText(getApplicationContext(), "could not connect to the server", Toast.LENGTH_SHORT).show();
        }
    }
}
