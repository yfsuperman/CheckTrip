package com.checktrip.mas.checktrip;

//import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by FanYang on 11/2/16.
 */

public class pickupRecyclerAdapter extends RecyclerView.Adapter<pickupRecyclerAdapter.ViewHolder>{

    private Context context;
    private String userName;
    private ArrayList<UserFlightInfo> userflightinfo;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    public pickupRecyclerAdapter(Context context, ArrayList<UserFlightInfo> userflightinfo) {
        this.context = context;
        this.userflightinfo = userflightinfo;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pickup_cardview,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int i) {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        final String uid = user.getUid();
        final FirebaseDatabase ref = FirebaseDatabase.getInstance();
        DatabaseReference userNameData = ref.getReference("users/"+uid+"/name/");
        userNameData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userName = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        final DatabaseReference flightList2 = databaseReference.child("users/"+uid+"/pickups/");

        final UserFlightInfo uf2 = userflightinfo.get(i);
        holder.departureAirport.setText(uf2.getDepartureAirportFsCode());
        holder.arrivalAirport.setText(uf2.getArrivalAirportFsCode());
        holder.departureTime.setText(uf2.getDepartureYearMonthDay()+" "+uf2.getDepartureTime());
        holder.arrivalTime.setText(uf2.getArrivalYearMonthDay()+" "+uf2.getArrivalTime());
        holder.flightnumber.setText("Flight No. "+uf2.getAirlineCode().toUpperCase()+uf2.getFlightNumber());
        holder.Passenger_name.setText(uf2.getPassenger().toUpperCase().charAt(0)+"  ");




        //card view click event
        holder.mCardView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ShowAPI.class);
                intent.putExtra("date", uf2.getDate());
                intent.putExtra("flight", uf2.getFlightNumber());
                intent.putExtra("airline",uf2.getAirlineCode());
                intent.putExtra("bag",uf2.getWillCheckBag());
                intent.putExtra("judge",false);
                context.startActivity(intent);
                Log.i("bagbagbagbagbag", String.valueOf(uf2.getWillCheckBag()));
            }
        });

        //delete button click event
        holder.delete.setOnClickListener(new View.OnClickListener(){



            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,PickUpHomePage.class);
                String unque_key = uf2.getFlightId();
                Log.e("delete click",unque_key);
                Log.e("delete click",String.valueOf(flightList2.child(unque_key)));
                flightList2.child(unque_key).removeValue();
                context.startActivity(intent);
            }
        });


        //share button click event
        holder.share.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String unque_key = uf2.getFlightId();
                String text = "Hi, this is "+ userName +". Could you please help me pick up "+uf2.getPassenger()+"? Copy the link below and paste it to CheckTrip so that you can see the flight information. Thank you!";
                String urlink = String.valueOf(flightList2.child(unque_key))+".json";
//                Uri text_uri = Uri.parse(text);
//                Uri urlink_uri = Uri.parse(urlink);
//
//                ArrayList<Uri> list = new ArrayList<Uri>();
//                list.add(text_uri);
//                list.add(urlink_uri);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT,"Subject Here");
                intent.putExtra(Intent.EXTRA_TEXT,text).putExtra(Intent.EXTRA_TEXT,text+" "+urlink);


//                intent.putExtra(Intent.EXTRA_TEXT,String.valueOf(flightList.child(unque_key)));
                context.startActivity(Intent.createChooser(intent,"Shear Via"));
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.i("size", String.valueOf(userflightinfo.size()));
        return userflightinfo.size();
    }







    public static class ViewHolder extends RecyclerView.ViewHolder{

        CardView mCardView;
        TextView flightnumber,departureAirport,departureTime,arrivalAirport,arrivalTime;
        TextView Passenger_name;
        Button share,delete;

        public ViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.cv2);
            flightnumber = (TextView) itemView.findViewById(R.id.flightnumber);
            departureAirport = (TextView) itemView.findViewById(R.id.departureAirport);
            departureTime = (TextView) itemView.findViewById(R.id.departureTime);
            arrivalAirport = (TextView) itemView.findViewById(R.id.arrivalAirport);
            arrivalTime = (TextView) itemView.findViewById(R.id.arrivalTime);
            Passenger_name = (TextView) itemView.findViewById(R.id.Passener_name);
            share = (Button) itemView.findViewById(R.id.btn_share);
            delete = (Button) itemView.findViewById(R.id.btn_delete);


        }
    }


}
