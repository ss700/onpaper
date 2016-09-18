package com.onpaper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class ItemDetails2Activity extends Activity {

    TextView tv1;
    Button bt1;
    EditText et1;
    Time t;
    private static final int UDP_SERVER_PORT = 7474;

    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String msgString = (String)msg.obj;
            t.setToNow();
            tv1.setText(String.valueOf(t.hour)+":"+
                    String.valueOf(t.minute)+":"+
                    String.valueOf(t.second)+"=>"+msgString+"\n"+tv1.getText().toString());
        }
    };

    AtomicBoolean isRunning=new AtomicBoolean(false);

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemdetails2);
        tv1=(TextView) findViewById(R.id.TextView01);
        bt1=(Button) findViewById(R.id.button1);
        et1=(EditText) findViewById(R.id.editText1);
        t=new Time();
    }

    public void bt1_OnClick(View view)
    {
        try {
            String sendData = et1.getText().toString();
            InetAddress serverAddr = InetAddress.getByName("10.126.0.174");
            DatagramPacket dp;
            dp = new DatagramPacket(sendData.getBytes(), sendData.length(), serverAddr, UDP_SERVER_PORT);
            try {
                ds.send(dp);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    DatagramSocket ds = null;
    public void onStart() {
        super.onStart();
        tv1.setText("START...");

        Thread background=new Thread(new Runnable() {
            public void run() {
                try {
                    String data;
                    byte[] recevieData = new byte[1024];
                    DatagramPacket dp = new DatagramPacket(recevieData, recevieData.length);

                    ds = new DatagramSocket(UDP_SERVER_PORT);

                    for (;isRunning.get();) {
                        Thread.sleep(100);
                        ds.receive(dp);
                        data = new String(recevieData, 0, dp.getLength());
                        handler.sendMessage(handler.obtainMessage(1,data));
                    }
                }
                catch (Throwable t) {
                    // just end the background thread
                }
            }
        });

        isRunning.set(true);
        background.start();
    }

    public void onStop() {
        super.onStop();
        isRunning.set(false);
    }
}