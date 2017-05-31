package com.checktrip.mas.checktrip;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class UserAreaActivity extends AppCompatActivity implements View.OnClickListener {

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //defining a database reference
    private DatabaseReference databaseReference;

    //view objects
    private TextView Useremail;
    private EditText Name, Address;
    private Button buttonSave;
    private Button Continue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        //getting the database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();


        //if the user is not logged in
        //that means current user will return null
        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }

        //getting current user
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //initializing views
        Useremail = (TextView) findViewById(R.id.tvUseremail);
        Address = (EditText) findViewById(R.id.etAddress);
        Name = (EditText) findViewById(R.id.etName);
        buttonSave = (Button) findViewById(R.id.bSave);
        Continue = (Button) findViewById(R.id.bContinue);

        //displaying logged in user name
        Useremail.setText("Welcome!"+user.getEmail());

        //adding listener to button
        buttonSave.setOnClickListener(this);
        Continue.setOnClickListener(this);

    }
    private void saveUserInformation() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //Getting values from database
        String name = Name.getText().toString().trim();
        String add = Address.getText().toString().trim();
        String emai = user.getEmail();
        UserFlightInfo flightInfo = null;
        UserFlightInfo pickupInfo = null;

        //getting the current logged in user
        Log.i("firecheck", String.valueOf(user));
        String ud = user.getUid();

        //creating a userinformation object
        UserInformation userInformation = new UserInformation(emai, ud, name, add, flightInfo, pickupInfo);

        databaseReference.child("users").child(ud).setValue(userInformation);

        //displaying a success toast
        Toast.makeText(this, "Information Saved...", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        if(view == buttonSave){
            saveUserInformation();
        }
        //if logout is pressed
        if(view == Continue){
            //logging out the user
            //firebaseAuth.signOut();
            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }
    }


}
