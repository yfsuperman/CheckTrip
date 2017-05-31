package com.checktrip.mas.checktrip;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.checktrip.mas.checktrip.service.FlightStatsCallback;
import com.checktrip.mas.checktrip.service.FlightStatsService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

import android.support.v7.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.text.SimpleDateFormat;


public class pickupShowAPI extends AppCompatActivity implements View.OnClickListener {

    private TextView depAirport,depInfo,depGate,depTime,depDate;
    private TextView arrAirport,arrInfo,arrGate,arrTime,arrDate;
    private TextView checkName;
    private Button saveFlight, logout;
    private Toolbar toolbar;
    private String arrtime_temp,arrlocaltime,arrcity;
    private String userName,data_input,flight_input,airline_input;
    private String airlineCode, flightId, flightNumber, linkToFlight, passenger, tsa, date;
    private String willCheckBag;
    private UserFlightInfo pickupUF;
    private long diff_min;

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //defining a database reference
    private DatabaseReference databaseReference;

    private DatabaseReference pickupData;



    private FlightStatsService service;
    private String arrivalAirportAddress, arrivalAirportFsCode, arrivalGate, arrivalYearMonthDay, arrivalTerminal,arrivalTime;
    private String departureAirportAddress, departureAirportFsCode,departureGate,departureYearMonthDay, departureTerminal, departureTime;
    private String bag;
    //private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_show_api);


        //        //set toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("CheckTrip");
        setSupportActionBar(toolbar);

//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){
//
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.logout:
//                        firebaseAuth.signOut();
//                        //closing activity
//                        finish();
//                        //starting login activity
//                        startActivity(new Intent(ShowAPI.this, LoginActivity.class));
//                }
//                return true;
//            }
//        });

//        toolbar.inflateMenu(R.menu.menu);

        depAirport = (TextView)findViewById(R.id.depAirport);
        depInfo = (TextView)findViewById(R.id.depInfo);
        depGate = (TextView)findViewById(R.id.depGate);
        depTime = (TextView)findViewById(R.id.depTime);
        arrAirport = (TextView)findViewById(R.id.arrAirport);
        arrInfo = (TextView)findViewById(R.id.arrInfo);
        arrGate = (TextView)findViewById(R.id.arrGate);
        arrTime = (TextView)findViewById(R.id.arrTime);
        depDate = (TextView) findViewById(R.id.depDate);
        arrDate = (TextView) findViewById(R.id.arrDate);
        checkName = (TextView) findViewById(R.id.checkName);

        saveFlight = (Button) findViewById(R.id.saveFlight);
        logout = (Button) findViewById(R.id.logout);
        saveFlight.setOnClickListener(this);
        logout.setOnClickListener(this);

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        //getting the database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = user.getUid();

        //get user name
        final FirebaseDatabase ref = FirebaseDatabase.getInstance();





//        dialog = new ProgressDialog(this);
//        dialog.setMessage("Loading...");
//        dialog.show();


        Intent intent = getIntent();
        String link_temp = intent.getStringExtra("link");
        String linkUrl_temp = link_temp.replaceAll("https://checktrip-8ea3a.firebaseio.com/","");
        String linkUrl = linkUrl_temp.replaceAll(".json","");
        Log.i("bbbbbbbbbbbbbbbb",linkUrl);

        pickupData = ref.getReference(linkUrl);
        Log.i("bbbbbbbbbbbbbbbcccc", String.valueOf(pickupData));
        pickupData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pickupUF = dataSnapshot.getValue(UserFlightInfo.class);
                arrivalAirportFsCode = pickupUF.getArrivalAirportFsCode();
                arrivalYearMonthDay = pickupUF.getArrivalYearMonthDay();
                arrivalTime = pickupUF.getArrivalTime();
                arrivalAirportAddress = pickupUF.getArrivalAirportAddress();
                arrivalTerminal = pickupUF.getArrivalTerminal();
                arrivalGate = pickupUF.getArrivalGate();
                departureAirportFsCode = pickupUF.getDepartureAirportFsCode();
                departureYearMonthDay = pickupUF.getDepartureYearMonthDay();
                departureTime = pickupUF.getDepartureTime();
                departureAirportAddress = pickupUF.getDepartureAirportAddress();
                departureTerminal = pickupUF.getDepartureTerminal();
                departureGate = pickupUF.getDepartureGate();
                date = pickupUF.getDate();
                airlineCode = pickupUF.getAirlineCode();
                passenger = pickupUF.getPassenger();
                flightNumber = pickupUF.getFlightNumber();
                bag = pickupUF.getWillCheckBag();




                depAirport.setText(departureAirportFsCode);
                arrAirport.setText(arrivalAirportFsCode);
                depInfo.setText(departureAirportAddress);
                arrInfo.setText(arrivalAirportAddress);
                depGate.setText(departureGate);
                arrGate.setText(arrivalGate);
                depTime.setText(departureTime);
                arrTime.setText(arrivalTime);
                depDate.setText(departureYearMonthDay);
                arrDate.setText(arrivalYearMonthDay);
                checkName.setText("Traveler: "+passenger);
                saveFlight.setText("Ready to pick up *"+passenger+"* ?");
////                System.out.print(pickupUF);
//                data_input = dataSnapshot.getValue(String.class);
                Log.i("hhhhhhhhhhh", String.valueOf(pickupUF));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    private void saveFlightInformation() {
        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String ud = user.getUid();

        //getting the database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();
        String uniqueKey = databaseReference.child("flight").push().getKey();






        databaseReference.child("users").child(ud).child("pickups").child(uniqueKey).child("flightId").setValue(uniqueKey);
        databaseReference.child("users").child(ud).child("pickups").child(uniqueKey).child("airlineCode").setValue(airlineCode);
        databaseReference.child("users").child(ud).child("pickups").child(uniqueKey).child("flightNumber").setValue(flightNumber);
        databaseReference.child("users").child(ud).child("pickups").child(uniqueKey).child("date").setValue(date);
        databaseReference.child("users").child(ud).child("pickups").child(uniqueKey).child("passenger").setValue(passenger);
        databaseReference.child("users").child(ud).child("pickups").child(uniqueKey).child("linkToFlight").setValue(String.valueOf(databaseReference.child("users").child(ud).child("flights").child(uniqueKey)));
        databaseReference.child("users").child(ud).child("pickups").child(uniqueKey).child("willCheckBag").setValue(bag);

        databaseReference.child("users").child(ud).child("pickups").child(uniqueKey).child("arrivalAirportFsCode").setValue(arrivalAirportFsCode);
        databaseReference.child("users").child(ud).child("pickups").child(uniqueKey).child("arrivalAirportAddress").setValue(arrivalAirportAddress);
        databaseReference.child("users").child(ud).child("pickups").child(uniqueKey).child("arrivalGate").setValue(arrivalGate);
        databaseReference.child("users").child(ud).child("pickups").child(uniqueKey).child("arrivalTerminal").setValue(arrivalTerminal);
        databaseReference.child("users").child(ud).child("pickups").child(uniqueKey).child("arrivalYearMonthDay").setValue(arrivalYearMonthDay);
        databaseReference.child("users").child(ud).child("pickups").child(uniqueKey).child("arrivalTime").setValue(arrivalTime);

        databaseReference.child("users").child(ud).child("pickups").child(uniqueKey).child("departureAirportFsCode").setValue(departureAirportFsCode);
        databaseReference.child("users").child(ud).child("pickups").child(uniqueKey).child("departureAirportAddress").setValue(departureAirportAddress);
        databaseReference.child("users").child(ud).child("pickups").child(uniqueKey).child("departureGate").setValue(departureGate);
        databaseReference.child("users").child(ud).child("pickups").child(uniqueKey).child("departureTerminal").setValue(departureTerminal);
        databaseReference.child("users").child(ud).child("pickups").child(uniqueKey).child("departureYearMonthDay").setValue(departureYearMonthDay);
        databaseReference.child("users").child(ud).child("pickups").child(uniqueKey).child("departureTime").setValue(departureTime);



        //displaying a success toast
        Toast.makeText(this, "Information Saved...", Toast.LENGTH_LONG).show();
    }





    public void onClick (View view) {

        String depAirportAddress = depInfo.getText().toString().trim();





        if (view == saveFlight) {
            saveFlightInformation();
            startActivity(new Intent(this,PickUpHomePage.class));

        }
        if(view == logout){
            //logging out the user
            firebaseAuth.signOut();
            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }

    }
}

