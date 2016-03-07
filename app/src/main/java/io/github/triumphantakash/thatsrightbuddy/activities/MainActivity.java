package io.github.triumphantakash.thatsrightbuddy.activities;

import android.os.Handler;
import android.os.Message;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {

    public DataOutputStream outToServer;
    public BufferedReader inFromServer;
    public TextView myText, friendsText;
    public Button sendButton;
    public Handler mHandler;
    static String myPassableText = "WELCOME";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myText = (TextView)findViewById(R.id.text_my);
        friendsText = (TextView)findViewById(R.id.text_friends);
        sendButton = (Button)findViewById(R.id.button_send);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPassableText = "FRIEND : " + myText.getText() + "";
                myText.setText("");
            }
        });

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Looper.myLooper() == null) {
                    Looper.prepare();
                }
                mHandler = new Handler() {
                    public void handleMessage(Message msg) {
                        friendsText.append(msg.getData().getString("msg"));
                    }
                };
            }
        });

      ManagerThread managerThread = new ManagerThread();
        managerThread.start();
//        try {
//            managerThread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }

    //send data to Writer thread in onResume function and also
    @Override
    protected void onResume() {
        super.onResume();

    }

    //reads from user input and write it to server stream
    public class ClientSocketWriter extends Thread{
        DataOutputStream outToServer;
       // BufferedReader messageBufferedReader;
        public ClientSocketWriter(DataOutputStream outToServer) {
            this.outToServer = outToServer;
        }
        public void run(){
            while(true){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //  messageBufferedReader = new BufferedReader( new InputStreamReader(message));
                try {
                    //   message = messageBufferedReader.readLine();
                    if(!myPassableText.equals("")) {
                       // System.out.print("you: ");
                        outToServer.writeBytes(myPassableText + "\n");
                        myPassableText = "";
                    }

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public class ManagerThread extends Thread{

        public void run() {
            Looper.prepare();
            Socket clientSocket = null;
            try {
                clientSocket = new Socket("192.168.0.26", 6970);
                outToServer = new DataOutputStream(clientSocket.getOutputStream());
                ClientSocketWriter socketWriteThread = new ClientSocketWriter(outToServer);
                socketWriteThread.start();

                inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                ClientSocketReader socketReadThread = new ClientSocketReader(inFromServer, mHandler, getApplicationContext());
                socketReadThread.start();

                socketWriteThread.join();
                socketReadThread.join();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "server is not up", Toast.LENGTH_LONG).show();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "server is not up", Toast.LENGTH_LONG).show();
            }
        }
    }

}
