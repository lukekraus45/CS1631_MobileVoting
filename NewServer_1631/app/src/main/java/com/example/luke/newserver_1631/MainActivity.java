package com.example.luke.newserver_1631;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.firebase.client.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    final String TAG = "";
    Button b;
    TextView tv;
    String value = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        b = (Button) findViewById(R.id.button);
        tv = (TextView) findViewById(R.id.textView2);

        FirebaseDatabase database;
        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);

        DatabaseReference myref = database.getReference();

        try{
            myref.addValueEventListener(new ValueEventListener(){

                @Override
                public void onDataChange(DataSnapshot dataSnapshot){
                    for(DataSnapshot item_snapshot : dataSnapshot.getChildren()) {

                        Voter user = item_snapshot.getValue(Voter.class);
                        user.code = item_snapshot.child("Code").getValue().toString();
                        user.number = item_snapshot.child("Number").getValue().toString();
                        user.vote = item_snapshot.child("Vote").getValue().toString();
                        value += "Code: " + user.code + "... Number: " + user.number + "... Vote: " + user.vote + "\n";
                    }
                    tv.setText(value);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }



            });


        }catch (Exception e){
            Log.e(TAG, e.toString());
        }


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.setEnabled(false);
                b.setText("Server Connected");

            }
        });





    }

}

class Voter {
    String number;
    String vote;
    String code;
}
