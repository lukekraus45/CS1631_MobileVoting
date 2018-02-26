package com.example.luke.cs1631_semesterproj;

import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {



    private String TAG = "TEST";
    private static MainActivity ma;
    boolean acceptTexts = false;

    public static  MainActivity instance(){
        return ma;
    }

    @Override
    public void onStart(){
        super.onStart();
        ma = this;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button b1 = (Button) findViewById(R.id.button);
        final Button b2 = (Button) findViewById(R.id.button2);
        final Button b3 = (Button) findViewById(R.id.button3);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "BUTTON 1 PRESSED!!!!!!!!!!!!!!!!!!!");
                b1.setEnabled(false);
                b1.setText("Server Connected");

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "BUTTON 2 PRESSED!!!!!!!!!!!!!!!!!!!");
                b2.setEnabled(false);
                b2.setText("Text Messages Able to be Received");
                acceptTexts = true;

                //setup TextMessage Feature
                try {
                    SmsManager sms = SmsManager.getDefault();
                    //change to admin number
                    sms.sendTextMessage("8143350802", null, "Text Messaging Opened", null, null);

                }catch (Exception e){
                    Log.e(TAG, e.toString());
                }
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "BUTTON 3 PRESSED!!!!!!!!!!!!!!!!!!!");
                b3.setEnabled(false);
                b3.setText("Final Results Sent...See Below");

            }
        });
    }
    public void handleSMS(String message, String from){

        Log.e(TAG, "MESSAGE RECEIVED FROM " + from + " CONTENT " + message);


    }

}

