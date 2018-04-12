package com.example.luke.cs1631_semesterproj;

import android.content.Intent;
import android.content.res.AssetManager;
import android.nfc.Tag;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class ServerActivity extends AppCompatActivity {

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
    Integer[][] dataResults;

    //added for specific posters
    private ArrayList<Integer> posterIDs = new ArrayList<>();

    public static  ServerActivity instance(){
        return sa;
    }

    @Override
    public void onStart(){
        super.onStart();
        sa = this;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent activityCalled = getIntent();
        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
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
                b2.setText("Messages Able to be Received");
                acceptTexts = true;

                //set up arraylist of valid poster IDs


                //setup TextMessage Feature
                try {
                    SmsManager sms = SmsManager.getDefault();
                    //change to admin's phone number
                    //sms.sendTextMessage("8143350802", null, "Text Messaging Opened", null, null);

                }catch (Exception e){
                    Log.e(TAG, e.toString());
                }
                //test file
                if(testScript) {
                    AssetManager am = view.getContext().getAssets();
                    StringBuilder xmlStringBuilder = new StringBuilder();
                    try {
                        //go through all of the files and send them
                        InputStream is = am.open("test.txt");
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                        String line = null;
                        /*while ((line = reader.readLine()) != null) {
                            InputStream temp_is = am.open(line);
                            BufferedReader br = new BufferedReader(new InputStreamReader(temp_is));

                            String xmlLine = null;

                            while ((xmlLine = br.readLine()) != null) {
                                xmlStringBuilder.append(xmlLine + "\n");
                            }

                            br.close();
                            temp_is.close();
                        }
                        tv.setText(xmlStringBuilder.toString());*/
                        Log.e(TAG, "READ LINE " + reader.readLine());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                ArrayList<Integer> indexList_second = new ArrayList<Integer>();
                ArrayList<Integer> indexList_third = new ArrayList<Integer>();


/*                int max_votes = 0;
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

                int winning_vote = max_votes;
                max_votes = 0;
                for(int i = 0; i < HIGHEST_POSTER_ID; i++) {
                    if(voteCounter[i] > max_votes && voteCounter[i] != winning_vote){
                        max_votes = voteCounter[i];
                        indexList_second = new ArrayList<Integer>();
                        indexList_second.add(i+1);
                    }else if(voteCounter[i] == max_votes){
                        indexList_second.add(voteCounter[i]);
                    }
                    //results.append("Poster " + (i+1) + ": " + voteCounter[i] + "\n");
                }

                int seond_place = max_votes;
                max_votes = 0;
                for(int i = 0; i < HIGHEST_POSTER_ID; i++) {
                    if(voteCounter[i] > max_votes && voteCounter[i] != winning_vote && voteCounter[i] != seond_place){
                        max_votes = voteCounter[i];
                        indexList_third = new ArrayList<Integer>();
                        indexList_third.add(i+1);
                    }else if(voteCounter[i] == max_votes){
                        indexList_third.add(voteCounter[i]);
                    }
                    //results.append("Poster " + (i+1) + ": " + voteCounter[i] + "\n");
                }




                if(indexList.size() == 1){
                    results.append("\n\n\nPoster Number " + indexList.get(0) + " wins with " + winning_vote + " votes!");
                    Log.e(TAG, "Poster Number " + indexList.get(0) + " wins with " + winning_vote + " votes!");
                }else{
                    for(int i = 0; i < indexList.size(); i++){
                        results.append("Poster " + indexList.get(i) + " tied for first receiving " + winning_vote + " votes\n");
                    }
                }
                if(indexList_second.size() == 1){
                    results.append("\n\n\nPoster Number " + indexList_second.get(0) + " comes in second with  with " + seond_place + " votes!");
                    Log.e(TAG, "Poster Number " + indexList_second.get(0) + " wins with " + seond_place + " votes!");
                }else{
                    for(int i = 0; i < indexList_second.size(); i++){
                        results.append("Poster " + indexList_second.get(i) + " tied for second receiving " + seond_place + " votes\n");
                    }
                }
                if(indexList_third.size() == 1){
                    results.append("\n\n\nPoster Number " + indexList_third.get(0) + " comes in third with " + max_votes + " votes!");
                    Log.e(TAG, "Poster Number " + indexList_third.get(0) + " comes in third with " + max_votes + " votes!");
                }else{
                    for(int i = 0; i < indexList_third.size(); i++){
                        results.append("Poster " + indexList_third.get(i) + " tied for third receiving " + max_votes + " votes\n");
                    }
                }


                tv.setText(results.toString());

                try { //text the winning poster ID to the admin
                    SmsManager sms = SmsManager.getDefault();
                    //change to admin's phone number
                    sms.sendTextMessage("8143350802", null, results.toString(), null, null);

                }catch (Exception e){
                    Log.e(TAG, e.toString());
                }*/



                //initialize results array
                dataResults = new Integer[100][2];
                for (int i = 0; i < 100; i++) {
                    dataResults[i][0] = i;
                    dataResults[i][1] = 0;
                }

                DatabaseReference myref = database.getReference();

                try{
                    myref.addValueEventListener(new ValueEventListener(){

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot item_snapshot : dataSnapshot.getChildren()) {

                                Voter user = item_snapshot.getValue(Voter.class);
                                user.code = item_snapshot.child("Code").getValue().toString();
                                //user.number = item_snapshot.child("Number").getValue().toString();
                                user.vote = item_snapshot.child("Vote").getValue().toString();
                                String temp = user.code;

                                if (temp.equalsIgnoreCase("Valid")) {
                                    dataResults[Integer.parseInt(user.vote)][1] += 1;
                                }

                            }

                            //java.util.Arrays.sort(dataResults, java.util.Collections.reverseOrder());

                            Arrays.sort(dataResults, new java.util.Comparator<Integer[]>() {
                                @Override
                                //arguments to this method represent the arrays to be sorted
                                public int compare(Integer[] o1, Integer[] o2) {
                                    //get the item ids which are at index 0 of the array
                                    Integer posterNumOne = o1[0];
                                    Integer posterNumTwo = o2[0];
                                    // sort on item id
                                    return posterNumTwo.compareTo(posterNumOne);
                                }
                            });

                            // sort array on quantity(second column)
                            Arrays.sort(dataResults, new java.util.Comparator<Integer[]>() {
                                @Override
                                public int compare(Integer[] o1, Integer[] o2) {
                                    Integer voteCountOne = o1[1];
                                    Integer voteCountTwo = o2[1];
                                    // reverse sort on quantity
                                    return voteCountTwo.compareTo(voteCountOne);
                                }
                            });




                            String resultString = "";
                            resultString = "1st: Poster#" + Integer.toString(dataResults[0][0]) + "(" + Integer.toString(dataResults[0][1]) + ")" +
                                    "\n2nd: Poster#" + Integer.toString(dataResults[1][0]) + "(" + Integer.toString(dataResults[1][1]) + ")" +
                                    "\n3rd: Poster# " + Integer.toString(dataResults[2][0]) + "(" + Integer.toString(dataResults[2][1]) + ")";
                            
                            tv.setText(resultString);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }



                    });


                }catch (Exception e){
                    Log.e(TAG, e.toString());
                }


            }
        });
    }
    public void handleSMS(String message, String from) throws ParserConfigurationException, TransformerException {

        //701 = success
        //702 = duplicate vote
        //703 = invalid vote

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


                    acknowlegde(from, "You have already voted before. This vote will not be counted.");

                    ref.child("Number").setValue(from);
                    ref.child("Vote").setValue(message);
                    ref.child("Code").setValue("Invalid: Duplicate");

                } else if(vote_number > 0 && vote_number <= HIGHEST_POSTER_ID){ //valid vote
                    voteCounter[vote_number-1] = voteCounter[vote_number-1] + 1;//increase the counter
                    voteList.put(from, vote_number);
                    String acceptVote = String.format("You voted for number " + vote_number + ". Thanks! You will not be able to vote again.");
                    acknowlegde(from, acceptVote);

                    ref.child("Number").setValue(from);
                    ref.child("Vote").setValue(message);
                    ref.child("Code").setValue("Valid");

                }else{ //invalid vote: not a valid poster ID
                    Log.e(TAG, "SOME ERROR OCCURED WITH THE VOTE: INVALID POSTER ID");
                    //send msg 711 sayingi its an invalid vote (the candidate ID does not exist)
                    acknowlegde(from, "This is an invalid vote. Please vote for a valid poster ID.");

                    ref.child("Number").setValue(from);
                    ref.child("Vote").setValue(message);
                    ref.child("Code").setValue("Invalid: invalid poster ID");

                }

            } else {
                Log.e(TAG, "AN ERROR OCCURED: TOO MANY CHARACTERS OR MAYBE SOMETHING ELSE");
                acknowlegde(from, "This is an invalid vote attempt.");

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
                    acknowlegde(from, acceptVote);

                    ref.child("Number").setValue(from);
                    ref.child("Vote").setValue(message);
                    ref.child("Code").setValue("Valid");


                }else{ //invalid vote: not a valid poster ID
                    Log.e(TAG, "SOME ERROR OCCURED WITH THE VOTE: INVALID POSTER ID");
                    //send msg 711 sayingi its an invalid vote (the candidate ID does not exist)
                    acknowlegde(from, "This is an invalid vote. Please vote for a valid poster ID.");

                    ref.child("Number").setValue(from);
                    ref.child("Vote").setValue(message);
                    ref.child("Code").setValue("Invalid");
                }

            }
        }




    }


    private void acknowlegde (String recipient, String ackMsg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            //smsManager.sendTextMessage(recipient, null, ackMsg, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

