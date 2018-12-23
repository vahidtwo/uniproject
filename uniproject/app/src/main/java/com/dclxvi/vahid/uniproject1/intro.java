package com.dclxvi.vahid.uniproject1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class intro extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_intro);
    Button btn_startSocketProgram = findViewById(R.id.btn_startSocketProgram);
    btn_startSocketProgram.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Intent intent = new Intent(intro.this, socketPrograming.class);
        intro.this.startActivity(intent);
        finish();
        Log.i("test","introfinish");
      }

    });
  }

}
