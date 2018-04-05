package com.example.luke.cs1631_semesterproj;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;


/**
 * Created by Luke on 2/25/2018.
 */

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){
        String TAG = "test";
        Log.e(TAG, "MESSAGE IS RECEIVED");
        Bundle b = intent.getExtras();

        if(b != null) {
            Object[] message = (Object[]) b.get("pdus");

            //look through the array and get the messages
            for (int i = 0; i < message.length; i++) {

                SmsMessage temp_message = SmsMessage.createFromPdu((byte[]) message[i]);
                String body = temp_message.getDisplayMessageBody();
                String from = temp_message.getOriginatingAddress();

                MainActivity ma = MainActivity.instance();
                try {
                    ma.handleSMS(body, from);
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (TransformerException e) {
                    e.printStackTrace();
                }


            }
        }
    }
}
