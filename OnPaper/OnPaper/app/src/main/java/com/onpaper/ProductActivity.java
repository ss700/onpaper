package com.onpaper;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class ProductActivity extends Activity {

    public static final String SERVERIP = "10.126.0.174";
    public static final int SERVERPORT = 7474;
    WebView mWebView = null;
    int hasib=0;



    private String weburl ="http://10.126.0.174/screen.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_product);
        Intent iin= getIntent();
        Bundle b=iin.getExtras();

        hasib = (int) b.get("found");

        if(hasib==0)
        {
            weburl ="http://www.mcdonalds.com.hk/en/24-hours-mcdelivery.html";
        }
        else if (hasib==1)
        {
            weburl ="http://www2.hm.com/en_gb/productpage.0455363001.html";
        }
        else if (hasib==2)
        {
            weburl ="http://10.126.0.174/screen.png";
        }


        setContentView(R.layout.activity_itemdetails);
        mWebView = (WebView)findViewById(R.id.webview);

        mWebView.setWebViewClient(mWebViewClient);
        mWebView.loadUrl(weburl);



    }
    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
    }

    WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    };
    public void confirmPay(View view) {
        new Thread(new Client()).start();
    }

    DatagramSocket socket = null;
    String command;
    public class Client implements Runnable {
        public void run() {
            try {
                if(hasib==1)
                {
                    command = "mc";
                }
                else
                {
                    command ="nothing";
                }
                
                InetAddress serverAddress = InetAddress.getByName(SERVERIP);
                socket = new DatagramSocket(SERVERPORT);
                byte[] buf = command.getBytes();
                DatagramPacket packet = new DatagramPacket(buf, buf.length,
                        serverAddress, SERVERPORT);
                socket.send(packet);

                socket.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }


}
