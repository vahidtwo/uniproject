package com.dclxvi.vahid.uniproject1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class myalert {
  Context contexts;
  public myalert(final Context contexts, String msg){
    this.contexts=contexts;
    AlertDialog.Builder alert = new AlertDialog.Builder(contexts);
    alert.setTitle("some One Enter");
    alert.setMessage(msg);
    alert.setCancelable(false);
    alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
      }
    });
    alert.create().show();

  }
}
