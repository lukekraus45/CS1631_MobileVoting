package com.example.luke.cs1631_semesterproj;

import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    final int HIGHEST_POSTER_ID = 50; //change this val to the number of posters there are
    HashMap<String, Integer> voteList = new HashMap<String, Integer>();
    int[] voteCounter = new int[HIGHEST_POSTER_ID];
    private String TAG = "TEST";
    private static MainActivity ma;
    boolean acceptTexts = false;
    boolean debug = false;

    //added for specific posters
    private ArrayList<Integer> posterIDs = new ArrayList<>();

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
        final TextView tv = (TextView) findViewById(R.id.textView);

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

                //set up arraylist of valid poster IDs
                posterIDs.add(2);
                posterIDs.add(5);
                posterIDs.add(7);
                posterIDs.add(8);
                posterIDs.add(9);
                posterIDs.add(10);

                //setup TextMessage Feature
                try {
                    SmsManager sms = SmsManager.getDefault();
                    //change to admin's phone number
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
                acceptTexts = false;
                ArrayList<Integer> indexList = new ArrayList<Integer>();
                int max_votes = 0;
                StringBuilder results = new StringBuilder();
                for(int i = 0; i < HIGHEST_POSTER_ID; i++) {
                    if(voteCounter[i] > max_votes){
                        max_votes = voteCounter[i];
                        indexList = new ArrayList<Integer>();
                        indexList.add(i+1);
                    }else if(voteCounter[i] == max_votes){
                        indexList.add(voteCounter[i]);
                    }
                    results.append("Poster " + (i+1) + ": " + voteCounter[i] + "\n");
                }




                if(indexList.size() == 1){
                    results.append("\n\n\nPoster Number " + indexList.get(0) + " wins with " + max_votes + " votes!");
                    Log.e(TAG, "Poster Number " + indexList.get(0) + " wins with " + max_votes + " votes!");
                }else{
                    for(int i = 0; i < indexList.size(); i++){
                        results.append("Poster " + indexList.get(i) + " tied for first receiving " + max_votes + " votes\n");
                    }
                }
                tv.setText(results.toString());

                try { //text the winning poster ID to the admin
                    SmsManager sms = SmsManager.getDefault();
                    //change to admin's phone number
                    sms.sendTextMessage("8143350802", null, results.toString(), null, null);

                }catch (Exception e){
                    Log.e(TAG, e.toString());
                }

            }
        });
    }
    public void handleSMS(String message, String from){
        if(!debug){
            Log.e(TAG, "MESSAGE RECEIVED FROM " + from + " CONTENT " + message);
            from = from.substring(1);//remove + at beginning
            int vote_number = -1;
            if(message.equals("Text Messaging Opened")){
                //do nothing...first message
            }else if(message.length() <= 2){//assuming votes are not going to be longer than 2 characters
                try{
                    vote_number = Integer.parseInt(message);
                }catch(Exception e){
                    Log.e(TAG, "Exception "  + e);
                }

                if (voteList.containsKey(from)) { //invalid vote: duplicate voter
                    Log.e(TAG, "SOME ERROR OCCURED WITH THE VOTE: DUPLICATE COTE");
                    acknowlegde(from, "You have already voted before. This vote will not be counted.");
                } else if(vote_number > 0 && vote_number <= HIGHEST_POSTER_ID && posterIDs.contains(vote_number)){ //valid vote
                    voteCounter[vote_number-1] = voteCounter[vote_number-1] + 1;//increase the counter
                    voteList.put(from, vote_number);
                    String acceptVote = String.format("You voted for number " + vote_number + ". Thanks! You will not be able to vote again.");
                    acknowlegde(from, acceptVote);
                }else{ //invalid vote: not a valid poster ID
                    Log.e(TAG, "SOME ERROR OCCURED WITH THE VOTE: INVALID POSTER ID");
                    //send msg 711 sayingi its an invalid vote (the candidate ID does not exist)
                    acknowlegde(from, "This is an invalid vote. Please vote for a valid poster ID.");
                }

            } else {
                Log.e(TAG, "AN ERROR OCCURED: TOO MANY CHARACTERS OR MAYBE SOMETHING ELSE");
                acknowlegde(from, "This is an invalid vote attempt.");
            }

        }else{
            Log.e(TAG, "MESSAGE RECEIVED FROM " + from + " CONTENT " + message);
            from = from.substring(1);//remove + at beginning
            int vote_number = -1;
            if(message.equals("Text Messaging Opened")){
                //do nothing...first message
            }else if(message.length() <= 2){//assuming votes are not going to be longer than 2 characters
                try{
                    vote_number = Integer.parseInt(message);
                }catch(Exception e){
                    Log.e(TAG, "Exception "  + e);
                }

                if(vote_number > 0 && vote_number <= HIGHEST_POSTER_ID){ //valid vote
                    voteCounter[vote_number-1] = voteCounter[vote_number-1] + 1;//increase the counter
                    voteList.put(from, vote_number);
                    String acceptVote = String.format("You voted for number " + vote_number + ". Thanks! You will not be able to vote again.");
                    acknowlegde(from, acceptVote);
                }else{ //invalid vote: not a valid poster ID
                    Log.e(TAG, "SOME ERROR OCCURED WITH THE VOTE: INVALID POSTER ID");
                    //send msg 711 sayingi its an invalid vote (the candidate ID does not exist)
                    acknowlegde(from, "This is an invalid vote. Please vote for a valid poster ID.");
                }

            }
        }




    }

    private void acknowlegde (String recipient, String ackMsg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(recipient, null, ackMsg, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

