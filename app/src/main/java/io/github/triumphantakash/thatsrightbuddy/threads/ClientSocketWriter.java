package io.github.triumphantakash.thatsrightbuddy.threads;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by akash on 3/5/16.
 */
//reads from user input and write it to server stream
public class ClientSocketWriter extends Thread{
    DataOutputStream outToServer;
    BufferedReader messageBufferedReader;
    String message;
    public ClientSocketWriter(DataOutputStream outToServer) {
        this.outToServer = outToServer;
    }
    public void run(){
        while(true){
            System.out.print("you: ");
            messageBufferedReader = new BufferedReader( new InputStreamReader(System.in));
            try {
                message = messageBufferedReader.readLine();
                outToServer.writeBytes(message+"\n");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
