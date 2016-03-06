package io.github.triumphantakash.thatsrightbuddy.threads;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by akash on 3/5/16.
 */
//reads from the server and display messages on user's screen
public class ClientSocketReader extends Thread{
    BufferedReader inFromServer;
    Message msg;
    String message;
    Handler referenceHandler;
    Bundle bundle;
    public ClientSocketReader(BufferedReader inFromServer, Handler handler) {
        this.inFromServer = inFromServer;
        this.referenceHandler = handler;
    }
    public void run(){
        Looper.prepare();

        try {
            System.out.println("client reader thread is up and running");
            while(true){
                Thread.sleep(1000);
                msg = Message.obtain();
                message = inFromServer.readLine();	//a new message is arrived
                bundle = new Bundle();
                bundle.putString("msg" ,message+"\n");
                msg.setData(bundle);
                referenceHandler.sendMessage(msg);
                Log.i("****TAG****", "SERVER: " + message + "\n");
            }
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Looper.loop();
    }
}
