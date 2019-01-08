package com.dclxvi.vahid.smartHouse;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.dclxvi.vahid.smartHouse.tcpConnection.TCPresive;
import com.dclxvi.vahid.smartHouse.tcpConnection.TCPsend;


public class MainActivity extends AppCompatActivity {

  private static TextView connectionStatus;
  TextView txtTemp;
  TextView txtHumidity;
  TextView txtPhotoCell;
  TextView txtPwmLED;
  TextView txtRaspberryCpu;
  TextView txtWho;
  TextView txtRaspberryCpuTemp;

  Handler handler;
  Context context;

  Switch swchLED_Status;

  Intent intent;

  int port = 16662;
  float photocellInt = 0;

  String IP = "192.168.88.225";
  private String temp = "";
  private String lampStatus = "False";
  private String humidity = "";
  private String cpu_tmp = "";
  private String photocell = "";
  private String cpu = "";
  private String rfidname = "";
  private String rfidid = "";

  private Thread serverThread;

  @Override
  protected void onDestroy() {
    super.onDestroy();

    new TCPsend().setIP(IP).setMessage("end").setPort(port).start();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    handler = new Handler();
    context = getApplicationContext();

    txtTemp = findViewById(R.id.txtTemp);
    connectionStatus = findViewById(R.id.connectionStatus);
    txtHumidity = findViewById(R.id.txtHumidity);
    txtPhotoCell = findViewById(R.id.txtPhotoCell);
    txtPwmLED = findViewById(R.id.txtPwmLED);
    txtWho = findViewById(R.id.txtWho);
    txtRaspberryCpu = findViewById(R.id.txtCpu);
    txtRaspberryCpuTemp = findViewById(R.id.txtRaspberryCpuTemp);
    swchLED_Status = findViewById(R.id.swchLED);


    intent = getIntent();

    if (intent.hasExtra("port") && intent.hasExtra("ip")) {
      port = intent.getIntExtra("port", 0);
      IP = intent.getStringExtra("ip");
    }
    Log.i("test", "TCPS starting");

    //starting resive Thread
    TCPresive tcpResive = new TCPresive(port, context, handler);
    serverThread = new Thread(tcpResive);
    serverThread.start();

    Log.i("test", "TCPS started");
    Log.i("test", "TCPc starting");
    new TCPsend().setIP(IP).setMessage("getstate").setPort(port).start();
    Log.i("test", "getstatus1");

    swchLED_Status.setOnCheckedChangeListener(
      new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
          Log.i("test", "in main port: " + port + " ip: " + IP);

          new TCPsend().setIP(IP).setMessage(isChecked).setPort(port).start();
          if (isChecked)
            findViewById(R.id.imglam).setBackgroundResource(R.drawable.lamon);
          else
            findViewById(R.id.imglam).setBackgroundResource(R.drawable.lamoff);

          Log.i("test", "TCPc started");

        }
      }

    );
    // spliting and sett text View

    new Thread(new Runnable() {
      @Override
      public void run() {

        while (true) {
          if (
            !photocell.equals(monitor.photocell)
              || !temp.equals(monitor.temp)
              || !humidity.equals(monitor.humidity)
              || !cpu_tmp.equals(monitor.cpuTemp)
              || !cpu.equals(monitor.cpuUsage)
              || !rfidname.equals(monitor.rfidtext)
              || !rfidid.equals(monitor.rfidid)
            ) {

            photocellInt = Float.parseFloat(monitor.photocell);
            if (photocellInt > 1000)
              photocellInt = 100;
            else {
              photocellInt -= 3;
              photocellInt /= 100;
              if (photocellInt < 0)
                photocellInt = 0;
            }
            handler.post(new Runnable() {
              @Override
              public void run() {
                synchronized (monitor.photocell) {
                  if (!photocell.equals(monitor.photocell)) {
                    txtPhotoCell.setText(monitor.photocell + "");
                    txtPwmLED.setText(photocellInt + "%");
                  }
                  photocell = monitor.photocell;
                }
                synchronized (monitor.temp) {
                  if (!temp.equals(monitor.temp))
                    txtTemp.setText(monitor.temp + "");
                  temp = monitor.temp;
                }

                synchronized (monitor.rfidtext) {
                  synchronized (monitor.rfidid) {
                    if (!rfidname.equals(monitor.rfidtext) && !monitor.rfidtext.equals("no One")) {
                      String tempulate = String.valueOf(txtWho.getText());
                      String msg = "\nshakhse : " + monitor.rfidtext + "ba idCard :" + monitor.rfidid + " vared shod";
                      new myalert(context, msg);
                      txtWho.setText(tempulate + msg);
                      rfidname = monitor.rfidtext;
                      rfidid = monitor.rfidid;
                    }
                  }
                }
                synchronized (monitor.humidity) {
                  if (!humidity.equals(monitor.humidity))
                    txtHumidity.setText(monitor.humidity + "");
                  humidity = monitor.humidity;
                }
                synchronized (monitor.cpuUsage) {
                  if (!cpu.equals(monitor.cpuUsage))
                    txtRaspberryCpu.setText(monitor.cpuUsage + "");
                  cpu = monitor.cpuUsage;
                }
                synchronized (monitor.cpuTemp) {
                  if (!cpu_tmp.equals(monitor.cpuTemp))
                    txtRaspberryCpuTemp.setText(monitor.cpuTemp + "");
                  cpu_tmp = monitor.cpuTemp;
                }
              }
            });

          }
          try {
            Thread.sleep(50);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    }).start();
    new Thread(new Runnable() {
      @Override
      public void run() {
        while (true) {
          if (!lampStatus.equals(monitor.lampStatus)) {
            synchronized (monitor.lampStatus) {
              if (!lampStatus.equals(monitor.lampStatus)) {
                handler.post(new Runnable() {
                  @Override
                  public void run() {

                    if (lampStatus.equals("False"))
                      swchLED_Status.setChecked(true);
                    else
                      swchLED_Status.setChecked(false);
                  }
                });

                lampStatus = monitor.lampStatus;
              }
            }
          }
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          Log.i("test", "getstatus");
          new TCPsend().setIP(IP).setMessage("getstate").setPort(port).start();
          Log.i("test", "getstate");

        }
      }
    }).start();
  }

  public static TextView getConnection() {
    return connectionStatus;
  }
}