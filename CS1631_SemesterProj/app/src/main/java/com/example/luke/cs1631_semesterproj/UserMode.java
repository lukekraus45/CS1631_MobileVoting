package com.example.luke.cs1631_semesterproj;

import android.content.Intent;
import android.content.res.AssetManager;
import android.nfc.Tag;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.TimerTask;

public class UserMode extends AppCompatActivity {

    final int HIGHEST_POSTER_ID = 50; //change this val to the number of posters there are
    HashMap<String, Integer> voteList = new HashMap<String, Integer>();
    int[] voteCounter = new int[HIGHEST_POSTER_ID];
    private String TAG = "TEST";
    private static ServerActivity sa;
    boolean acceptTexts = false;
    boolean debug = true;
    boolean testScript = true;
    int messageCount = 0;
    FirebaseDatabase database;

    //added for specific posters
    private ArrayList<Integer> posterIDs = new ArrayList<>();


    @Override
    public void onStart(){
        super.onStart();
        //sa = new ServerActivity();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usermode);
        Intent activityCalled = getIntent();
        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        final Button b1 = (Button) findViewById(R.id.button6);
        final EditText phoneNumber = (EditText) findViewById(R.id.editText);
        final EditText vote = (EditText) findViewById(R.id.editText2);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    b1.setEnabled(false);

                    handleSMS(vote.getText().toString(), phoneNumber.getText().toString());

                    //SmsManager smsManager = SmsManager.getDefault();
                    //smsManager.sendTextMessage("8143350802", null, vote.getText().toString(), null, null);
                    b1.setText("Vote Submitted!");

                }catch (Exception e){
                    Log.e(TAG, "EXCEPTION " + e.toString());
                }

            }
        });




    }


    public void handleSMS(String message, String from) throws ParserConfigurationException, TransformerException {

        //701 = success
        //702 = duplicate vote
        //703 = invalid vote
        DatabaseReference myref = database.getReference("Counter");

        try{
            myref.addValueEventListener(new ValueEventListener(){

                @Override
                public void onDataChange(DataSnapshot dataSnapshot){
                    for(DataSnapshot item_snapshot : dataSnapshot.getChildren()) {

                        String value = item_snapshot.getValue(String.class);
                        messageCount = Integer.parseInt(value);


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }



            });


            myref.setValue(messageCount);
        }catch (Exception e){
            Log.e(TAG, e.toString());
        }

        DatabaseReference ref = database.getReference("Message" + messageCount);
        messageCount++;



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



                    ref.child("Number").setValue(from);
                    ref.child("Vote").setValue(message);
                    ref.child("Code").setValue("Invalid: Duplicate");

                } else if(vote_number > 0 && vote_number <= HIGHEST_POSTER_ID){ //valid vote
                    voteCounter[vote_number-1] = voteCounter[vote_number-1] + 1;//increase the counter
                    voteList.put(from, vote_number);
                    String acceptVote = String.format("You voted for number " + vote_number + ". Thanks! You will not be able to vote again.");

                    ref.child("Number").setValue(from);
                    ref.child("Vote").setValue(message);
                    ref.child("Code").setValue("Valid");

                }else{ //invalid vote: not a valid poster ID
                    Log.e(TAG, "SOME ERROR OCCURED WITH THE VOTE: INVALID POSTER ID");
                    //send msg 711 sayingi its an invalid vote (the candidate ID does not exist)

                    ref.child("Number").setValue(from);
                    ref.child("Vote").setValue(message);
                    ref.child("Code").setValue("Invalid: invalid poster ID");

                }

            } else {
                Log.e(TAG, "AN ERROR OCCURED: TOO MANY CHARACTERS OR MAYBE SOMETHING ELSE");

                ref.child("Number").setValue(from);
                ref.child("Vote").setValue(message);
                ref.child("Code").setValue("Invalid");

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

                    ref.child("Number").setValue(from);
                    ref.child("Vote").setValue(message);
                    ref.child("Code").setValue("Valid");


                }else{ //invalid vote: not a valid poster ID
                    Log.e(TAG, "SOME ERROR OCCURED WITH THE VOTE: INVALID POSTER ID");
                    //send msg 711 sayingi its an invalid vote (the candidate ID does not exist)

                    ref.child("Number").setValue(from);
                    ref.child("Vote").setValue(message);
                    ref.child("Code").setValue("Invalid");
                }

            }
        }




    }

}

