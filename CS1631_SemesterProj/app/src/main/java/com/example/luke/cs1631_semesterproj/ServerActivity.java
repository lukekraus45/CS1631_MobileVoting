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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
                b2.setText("Text Messages Able to be Received");
                acceptTexts = true;

                //set up arraylist of valid poster IDs


                //setup TextMessage Feature
                try {
                    SmsManager sms = SmsManager.getDefault();
                    //change to admin's phone number
                    sms.sendTextMessage("8143350802", null, "Text Messaging Opened", null, null);

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
                        while ((line = reader.readLine()) != null) {
                            InputStream temp_is = am.open(line);
                            BufferedReader br = new BufferedReader(new InputStreamReader(temp_is));

                            String xmlLine = null;

                            while ((xmlLine = br.readLine()) != null) {
                                xmlStringBuilder.append(xmlLine + "\n");
                            }

                            br.close();
                            temp_is.close();
                        }
                        tv.setText(xmlStringBuilder.toString());
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
                }

            }
        });
    }
    public void handleSMS(String message, String from) throws ParserConfigurationException, TransformerException {

        //701 = success
        //702 = duplicate vote
        //703 = invalid vote
        messageCount++;
        DatabaseReference ref = database.getReference("Message" + messageCount);

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
                ref.child("Code").setValue("Valid");

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
    private void toXml(String Phonenumber, int error_code, String user_vote) throws ParserConfigurationException, TransformerException {
        try {

            File path = this.getFilesDir();
            File file = new File(path,"test.xml");
            DocumentBuilderFactory dbFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            // root element
            Element rootElement = doc.createElement("cars");
            doc.appendChild(rootElement);

            // supercars element
            Element supercar = doc.createElement("supercars");
            rootElement.appendChild(supercar);

            // setting attribute to element
            Attr attr = doc.createAttribute("company");
            attr.setValue("Ferrari");
            supercar.setAttributeNode(attr);

            // carname element
            Element carname = doc.createElement("carname");
            Attr attrType = doc.createAttribute("type");
            attrType.setValue("formula one");
            carname.setAttributeNode(attrType);
            carname.appendChild(doc.createTextNode("Ferrari 101"));
            supercar.appendChild(carname);

            Element carname1 = doc.createElement("carname");
            Attr attrType1 = doc.createAttribute("type");
            attrType1.setValue("sports");
            carname1.setAttributeNode(attrType1);
            carname1.appendChild(doc.createTextNode("Ferrari 202"));
            supercar.appendChild(carname1);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);


            Log.e(TAG, "HERE");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
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

