package com.dclxvi.vahid.uniproject1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.dclxvi.vahid.uniproject1.tcpConnection.TCPresive;
import com.dclxvi.vahid.uniproject1.tcpConnection.TCPsend;


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
    swchLED_Status.setOnCheckedChangeListener(
      new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
          Log.i("sendTread", "in main port: " + port + " ip: " + IP);

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
                      String msg ="\nshakhse : " + monitor.rfidtext + "ba idCard :" + monitor.rfidid + " vared shod";
                      new myalert(context,msg);
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

        }
      }
    }).start();
  }

  public static TextView getConnection() {
    return connectionStatus;
  }
}


//    class TCPsend extends AsyncTask<Void, Void, Void> {
//      String ip = "192.168.88.231";
//      String massage;
//      int port = 16662;
//
//
//      private Socket socket;
//      private PrintWriter pw;
//
//      @Override
//      protected Void doInBackground(Void... params) {
//        try {
//          socket = new Socket(ip, port);
//          pw = new PrintWriter(socket.getOutputStream());
//          pw.write(massage);
//          pw.flush();
//          pw.close();
//          socket.close();
//
//        } catch (UnknownHostException e) {
//          e.printStackTrace();
//        } catch (IOException e) {
//          e.printStackTrace();
//        }
//
//        return null;
//      }
//      public void start() {
//        this.execute();
//        Log.i("test", "THread start");
//      }
//
//      public TCPsend setIP(String IP) {
//        this.ip = IP;
//        return this;
//      }
//
//      public TCPsend setPort(int port) {
//        this.port = port;
//        return this;
//      }
//      public TCPsend setMessage(String massage) {
//        this.massage  = massage;
//        return this;
//      }
//
//  }

