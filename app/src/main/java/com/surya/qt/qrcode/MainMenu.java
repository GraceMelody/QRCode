package com.surya.qt.qrcode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
            Toast toast = Toast.makeText(getApplicationContext(),
                    scanContent+"Promise doesn't run.", Toast.LENGTH_LONG);
            toast.show();


            Promise p = PromiseInternet.Fetch("https://httpbin.org/post", scanContent);

            class DoSomething implements IDelegate {
                public void execute (String data)
                {
                    Toast toast = Toast.makeText(getApplicationContext(),data,Toast.LENGTH_LONG);
                    toast.show();
                }
            }

            p.then(new DoSomething());

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
