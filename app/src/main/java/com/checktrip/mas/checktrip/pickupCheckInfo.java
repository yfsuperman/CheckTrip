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


public class pickupCheckInfo extends AppCompatActivity implements FlightStatsCallback, View.OnClickListener {

    private TextView depAirport,depInfo,depGate,depTime,depDate;
    private TextView arrAirport,arrInfo,arrGate,arrTime,arrDate;
    private Button saveFlight, logout, checkTime;
    private Toolbar toolbar;
    private String arrtime_temp,arrlocaltime,arrcity;
    private String userName;
    private long diff_min;
    private boolean bag;

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //defining a database reference
    private DatabaseReference databaseReference;



    private FlightStatsService service;
    private String arrivalAirportAddress, arrivalAirportFsCode, arrivalGate, arrivalYearMonthDay, arrivalTerminal,arrivalTime;
    private String departureAirportAddress, departureAirportFsCode,departureGate,departureYearMonthDay, departureTerminal, departureTime;

    //private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_api);


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
        depDate = (TextView) findViewById(R.id.depDate);
        arrAirport = (TextView)findViewById(R.id.arrAirport);
        arrInfo = (TextView)findViewById(R.id.arrInfo);
        arrGate = (TextView)findViewById(R.id.arrGate);
        arrTime = (TextView)findViewById(R.id.arrTime);
        arrDate = (TextView) findViewById(R.id.arrDate);

        saveFlight = (Button) findViewById(R.id.saveFlight);
        logout = (Button) findViewById(R.id.logout);
        saveFlight.setOnClickListener(this);
        logout.setOnClickListener(this);
        checkTime = (Button) findViewById(R.id.checkTime);
        checkTime.setOnClickListener(this);

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        //getting the database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = user.getUid();

        //get user name
        final FirebaseDatabase ref = FirebaseDatabase.getInstance();
        DatabaseReference userNameData = ref.getReference("users/"+uid+"/name/");
        userNameData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userName = dataSnapshot.getValue(String.class);
                Log.i("hhhhhhhhhhhhhhhhh",userName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        service = new FlightStatsService(this);
//        dialog = new ProgressDialog(this);
//        dialog.setMessage("Loading...");
//        dialog.show();


        Intent intent = getIntent();
        String data_input = intent.getStringExtra("date");
        String flight_input = intent.getStringExtra("flight");
        String airline_input = intent.getStringExtra("airline");
        bag = intent.getExtras().getBoolean("bag");


        service.refreshFlight(data_input,flight_input,airline_input);
    }

    @Override
    public void serviceSuccess(JSONObject flightStatuses) {
//        dialog.hide();

        try {
//            final String depairport = flightStatuses.getJSONObject("appendix").getJSONArray("airports").getJSONObject(1).getString("cityCode")+" - "+flightStatuses.getJSONObject("appendix").getJSONArray("airports").getJSONObject(1).getString("city")+"， "+flightStatuses.getJSONObject("appendix").getJSONArray("airports").getJSONObject(1).getString("stateCode")+"， "+flightStatuses.getJSONObject("appendix").getJSONArray("airports").getJSONObject(1).getString("countryCode");
//            final String arrairport = flightStatuses.getJSONObject("appendix").getJSONArray("airports").getJSONObject(0).getString("cityCode")+" - "+flightStatuses.getJSONObject("appendix").getJSONArray("airports").getJSONObject(0).getString("city")+"， "+flightStatuses.getJSONObject("appendix").getJSONArray("airports").getJSONObject(0).getString("stateCode")+"， "+flightStatuses.getJSONObject("appendix").getJSONArray("airports").getJSONObject(0).getString("countryCode");
//            String depinfo = flightStatuses.getJSONObject("appendix").getJSONArray("airports").getJSONObject(1).getString("name");
//            String arrinfo = flightStatuses.getJSONObject("appendix").getJSONArray("airports").getJSONObject(0).getString("name");
//            //String depgate = flightStatuses
//            //String arrgate = flightStatuses
            arrtime_temp = flightStatuses.getJSONArray("flightStatuses").getJSONObject(1).getJSONObject("departureDate").getString("dateLocal");
            String depdate = arrtime_temp.substring(0,10);
            String deptime = arrtime_temp.substring(11,16);
            arrtime_temp = depdate+" "+deptime;
//            String arrtime_temp = flightStatuses.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("arrivalDate").getString("dateLocal");
//            String arrdate = arrtime_temp.substring(0,10);
//            String arrtime = arrtime_temp.substring(11,16);
            arrlocaltime = flightStatuses.getJSONObject("appendix").getJSONArray("airports").getJSONObject(0).getString("localTime");
            arrcity = flightStatuses.getJSONObject("appendix").getJSONArray("airports").getJSONObject(0).getString("fs");
            arrlocaltime = arrlocaltime.substring(0,10)+" "+arrlocaltime.substring(11,16);


            arrivalAirportFsCode = flightStatuses.getJSONObject("appendix").getJSONArray("airports").getJSONObject(0).getString("fs");
            arrivalYearMonthDay = flightStatuses.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("arrivalDate").getString("dateLocal").substring(0,10);
            arrivalTime = flightStatuses.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("arrivalDate").getString("dateLocal").substring(11,16);
            arrivalAirportAddress = flightStatuses.getJSONObject("appendix").getJSONArray("airports").getJSONObject(0).getString("name");
            arrivalTerminal = flightStatuses.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("airportResources").getString("arrivalTerminal");
            arrivalGate = flightStatuses.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("airportResources").getString("arrivalGate");

            departureAirportFsCode = flightStatuses.getJSONObject("appendix").getJSONArray("airports").getJSONObject(1).getString("fs");
            departureYearMonthDay = flightStatuses.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("departureDate").getString("dateLocal").substring(0,10);
            departureTime = flightStatuses.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("departureDate").getString("dateLocal").substring(11,16);
            departureAirportAddress = flightStatuses.getJSONObject("appendix").getJSONArray("airports").getJSONObject(1).getString("name");
            departureTerminal = flightStatuses.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("airportResources").getString("departureTerminal");
            departureGate = flightStatuses.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("airportResources").getString("departureGate");

            depAirport.setText(departureAirportFsCode);
            arrAirport.setText(arrivalAirportFsCode);
            depInfo.setText(departureAirportAddress);
            arrInfo.setText(arrivalAirportAddress);
            depGate.setText("Gate: "+departureGate+"    "+ "Terminal: "+departureTerminal);
            arrGate.setText("Gate: "+arrivalGate+"    "+ "Terminal: "+arrivalTerminal);
            depTime.setText(departureTime);
            depDate.setText(departureYearMonthDay);
            arrTime.setText(arrivalTime);
            arrDate.setText(arrivalYearMonthDay);

            Log.i("check",departureGate.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }





    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
    }

    public void onClick (View view) {

        String depAirportAddress = depInfo.getText().toString().trim();





        if (view == saveFlight) {
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
        if(view == checkTime){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            try
            {
                java.util.Date d1 = df.parse(arrtime_temp);
                java.util.Date d2=df.parse(arrlocaltime);
                long diff = d2.getTime() - d1.getTime();
                diff_min = diff / (1000 * 60);
                if(bag){
                    diff_min -= 15;
                }
                Log.i("1",arrtime_temp);
                Log.i("2",arrlocaltime);
                Log.i("diff_min", String.valueOf(diff_min));
            }
            catch (Exception e)
            {
            }


            Intent intent = new Intent(pickupCheckInfo.this, MapsActivity.class);
            intent.putExtra("deptime", arrtime_temp);
            intent.putExtra("deplocaltime", arrlocaltime);
            intent.putExtra("depcity",arrcity);
            intent.putExtra("depAirportAddress",depAirportAddress);
            intent.putExtra("diff_min",String.valueOf(diff_min));
            startActivity(intent);
        }
    }
}

