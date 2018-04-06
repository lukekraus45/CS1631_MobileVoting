package com.example.luke.cs1631_semesterproj;

import android.app.FragmentTransaction;
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

public class MainActivity extends AppCompatActivity {

    final int HIGHEST_POSTER_ID = 50; //change this val to the number of posters there are
    HashMap<String, Integer> voteList = new HashMap<String, Integer>();
    int[] voteCounter = new int[HIGHEST_POSTER_ID];
    private String TAG = "TEST";
    private static MainActivity ma;
    boolean acceptTexts = false;
    boolean debug = true;
    boolean testScript = true;
    int messageCount = 0;
    FirebaseDatabase database;

    //added for specific posters
    private ArrayList<Integer> posterIDs = new ArrayList<>();

    public static MainActivity instance() {
        return ma;
    }

    @Override
    public void onStart() {
        super.onStart();
        ma = this;

    }

    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        tv = (TextView) findViewById(R.id.password);
        Button b = (Button) findViewById(R.id.button4);
        Log.e(TAG, "THIS " + this);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.e(TAG, tv.getText().toString());
                if (Integer.parseInt(tv.getText().toString()) == 1234) {
                    Intent getDrawScreenIntent = new Intent(ma, ServerActivity.class);
                    startActivity(getDrawScreenIntent);
                    Log.e(TAG, "WORKS");
                }


            }
        });


    }


}
