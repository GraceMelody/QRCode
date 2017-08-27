package com.surya.qt.qrcode;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.plasmagrass.promiseinternet.IDelegate;
import com.plasmagrass.promiseinternet.Promise;
import com.plasmagrass.promiseinternet.PromiseInternet;

public class MainMenu extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Button scanBtn = (Button)findViewById(R.id.scanBtn);
        scanBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            //Toast toast = Toast.makeText(getApplicationContext(),
            //        scanContent+"Promise doesn't run.", Toast.LENGTH_LONG);
            //toast.show();

            String jsonstr = "{\"id\": \"" +  scanContent +"\"}";

            Promise p = PromiseInternet.Fetch("http://192.168.1.109:3000/checkin", jsonstr);

            final Context mContext = this;
            p.then(new IDelegate() {
                @Override
                public void execute(String s) {

                    String nama = s;
                    String text = "Welcome, " + nama;

                    AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                    alert
                            .setMessage(text)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            });

            /*
            PromiseInternet.Fetch("https://httpbin.org/post", scanContent)
                    .then(new IDelegate() {
                        @Override
                        public void execute(String s) {
                            Toast toast = Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });
                    */

        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
