package com.dclxvi.vahid.uniproject1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class socketPrograming extends AppCompatActivity {

  public static int port;
  public static String ip;

  private EditText edt_port;
  private EditText edt_ip;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_socket_programing);
    Button btn_monitoring = findViewById(R.id.btn_monitoring);
    edt_port = (EditText) findViewById(R.id.edt_port);
    edt_ip = (EditText) findViewById(R.id.edt_ip);

    btn_monitoring.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        ip = edt_ip.getText().toString();
        Log.i("test","ip"+ip);

        port = Integer.valueOf(edt_port.getText().toString());


        Intent intent = new Intent(socketPrograming.this, MainActivity.class);
        intent.putExtra("port",port);
        intent.putExtra("ip",ip);

        startActivity(intent);
        finish();

        Log.w("test","port: "+port+" and ip: "+ip);

      }

    });
  }
}
