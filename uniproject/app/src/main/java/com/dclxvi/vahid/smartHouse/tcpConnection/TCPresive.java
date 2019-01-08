package com.dclxvi.vahid.smartHouse.tcpConnection;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.dclxvi.vahid.smartHouse.MainActivity;
import com.dclxvi.vahid.smartHouse.monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class TCPresive implements Runnable {
  private ServerSocket serverSocket;

  private int serverPort;
  private Handler handler;
  private Context context;
  private BufferedReader in;
  private Socket client;

  //constractor
  public TCPresive(int serverPort, Context context, Handler handler) {
    this.handler = handler;
    this.context = context;
    this.serverPort = serverPort;
  }

  @Override
  public void run() {
    try {
      while (true) {
        serverSocket = new ServerSocket(serverPort);
        client = serverSocket.accept();
        Log.i("buffertest", client.getInetAddress() + "");

        handler.post(new Runnable() {
          @Override
          public void run() {
            MainActivity.getConnection().setText("connected");
          }
        });
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
//        DataInputStream in = new DataInputStream(client.getInputStream());
        String monitorSrt = "";
        Log.i("buffertest", "before message is " + monitorSrt);
        while ((monitorSrt = in.readLine()) != null && monitorSrt.length() > 0) {
          Log.i("buffertest", "arfter message is " + monitorSrt);

          String[] splitS;
          if (monitorSrt.contains(",")) {
            splitS = monitorSrt.split(",");
          } else {
            splitS = new String[]{monitorSrt};
          }
          for (String str : splitS) {

            if (str.contains("temp")) {
              String[] spliteSS = str.split(":");
              synchronized (monitor.temp) {
                monitor.temp = spliteSS[1];
              }
            }
            if (str.contains("connectionStatus")) {
              String[] spliteSS = str.split(":");
              synchronized (monitor.lampStatus) {
                monitor.lampStatus = spliteSS[1];
              }
            }
            if (str.contains("humidity")) {
              String[] spliteSS = str.split(":");
              synchronized (monitor.humidity) {
                monitor.humidity = spliteSS[1];
              }
              Log.i("test", "humidity set im monitor");
            }
            if (str.contains("cpu%")) {
              String[] spliteSS = str.split(":");
              synchronized (monitor.cpuUsage) {
                monitor.cpuUsage = spliteSS[1];
              }
              Log.i("test", "cpuUsage set im monitor");
            }
            if (str.contains("cpuTemp")) {
              String[] spliteSS = str.split(":");
              synchronized (monitor.cpuTemp) {
                monitor.cpuTemp = spliteSS[1];
              }
              Log.i("test", "cpuTemp set im monitor");
            }
            if (str.contains("photocell")) {
              String[] spliteSS = str.split(":");
              synchronized (monitor.photocell) {
                monitor.photocell = spliteSS[1];
              }
              Log.i("test", "Photocell set im monitor");
            }
            if (str.contains("rfidtext")) {
              String[] spliteSS = str.split(":");
              synchronized (monitor.rfidtext) {
                monitor.rfidtext = spliteSS[1];
              }
              Log.i("test", "rfid set im monitor");
            }
            if (str.contains("rfidid")) {
              String[] spliteSS = str.split(":");
              synchronized (monitor.rfidid) {
                monitor.rfidid = spliteSS[1];
              }
              Log.i("test", "rfid set im monitor");
            }

          }
          try {
            Thread.sleep(100);
          } catch (Exception e) {
            Log.i("buffertest", e.toString());
          }
        }

        in.close();
        client.close();
        serverSocket.close();
        Log.i("buffertest", "all is close in normal");

      }
    } catch (IOException e) {
      e.printStackTrace();
      Log.i("buffertest", e.toString());
      try {
        in.close();
        client.close();
        serverSocket.close();
        Log.i("buffertest", "all is close");
      } catch (IOException eb) {
      Log.i("buffertest", eb.toString());
        e.printStackTrace();
      }
    }
  }
}
