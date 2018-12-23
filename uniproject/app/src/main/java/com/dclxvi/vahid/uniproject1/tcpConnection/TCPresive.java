package com.dclxvi.vahid.uniproject1.tcpConnection;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.dclxvi.vahid.uniproject1.MainActivity;
import com.dclxvi.vahid.uniproject1.monitor;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class TCPresive implements Runnable {
  private ServerSocket serverSocket;
//  private TextView txtConnection;


  private int serverPort;
  private Handler handler;
  private Context context;

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
        Socket client = serverSocket.accept();

        handler.post(new Runnable() {
          @Override
          public void run() {
//            Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
            MainActivity.getConnection().setText("connected");
          }
        });
        DataInputStream in = new DataInputStream(client.getInputStream());
        String monitorSrt = "";
        Log.i("socketR", "before message is " + monitorSrt);
        while ((monitorSrt = in.readLine()) != null) {

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
            if (str.contains("humidity")) {
              String[] spliteSS = str.split(":");
              synchronized (monitor.humidity) {
                monitor.humidity = spliteSS[1];
              }
              Log.i("socketR", "humidity set im monitor");
            }
            if (str.contains("cpu%")) {
              String[] spliteSS = str.split(":");
              synchronized (monitor.cpuUsage) {
                monitor.cpuUsage = spliteSS[1];
              }
              Log.i("socketR", "cpuUsage set im monitor");
            }
            if (str.contains("cpuTemp")) {
              String[] spliteSS = str.split(":");
              synchronized (monitor.cpuTemp) {
                monitor.cpuTemp = spliteSS[1];
              }
              Log.i("socketR", "cpuTemp set im monitor");
            }
            if (str.contains("photocell")) {
              String[] spliteSS = str.split(":");
              synchronized (monitor.photocell) {
                monitor.photocell = spliteSS[1];
              }
              Log.i("socketR", "Photocell set im monitor");
            }
            if (str.contains("rfidtext")) {
              String[] spliteSS = str.split(":");
              synchronized (monitor.rfidtext) {
                monitor.rfidtext = spliteSS[1];
              }
              Log.i("socketR", "rfid set im monitor");
            }
            if (str.contains("rfidid")) {
              String[] spliteSS = str.split(":");
              synchronized (monitor.rfidid) {
                monitor.rfidid = spliteSS[1];
              }
              Log.i("socketR", "rfid set im monitor");
            }

          }
          in.close();
          client.close();
          serverSocket.close();
          try {
            Thread.sleep(100);
          } catch (Exception e) {
            Log.i("MYserver", e.toString());
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
      Log.i("server", e.toString());
    }
  }
}
