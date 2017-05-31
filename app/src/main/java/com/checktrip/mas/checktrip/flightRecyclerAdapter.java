package com.checktrip.mas.checktrip;

//import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by FanYang on 11/2/16.
 */

public class flightRecyclerAdapter extends RecyclerView.Adapter<flightRecyclerAdapter.ViewHolder>{

    private Context context;
    private String userName;
    private ArrayList<UserFlightInfo> userflightinfo;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    public flightRecyclerAdapter(Context context, ArrayList<UserFlightInfo> userflightinfo) {
        this.context = context;
        this.userflightinfo = userflightinfo;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.flight_cardview,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {

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



        final DatabaseReference flightList = databaseReference.child("users/"+uid+"/flights/");

        final UserFlightInfo uf = userflightinfo.get(i);
        holder.departureAirport.setText(uf.getDepartureAirportFsCode());
        holder.arrivalAirport.setText(uf.getArrivalAirportFsCode());
        holder.departureTime.setText(uf.getDepartureYearMonthDay()+" "+uf.getDepartureTime());
        holder.arrivalTime.setText(uf.getArrivalYearMonthDay()+" "+uf.getArrivalTime());
        holder.flightnumber.setText("Flight No. "+uf.getAirlineCode().toUpperCase()+uf.getFlightNumber());
        holder.terminal.setText("Terminal: "+uf.getDepartureTerminal());
        holder.gate.setText("Gate: "+uf.getDepartureGate());



        //card view click event
        holder.mCardView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ShowAPI.class);
                intent.putExtra("date", uf.getDate());
                intent.putExtra("flight", uf.getFlightNumber());
                intent.putExtra("airline",uf.getAirlineCode());
                intent.putExtra("judge",true);
                context.startActivity(intent);
            }
        });

        //delete button click event
        holder.delete.setOnClickListener(new View.OnClickListener(){



            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,UserHomePage.class);
                String unque_key = uf.getFlightId();
                Log.e("delete click",unque_key);
                Log.e("delete click",String.valueOf(flightList.child(unque_key)));
                flightList.child(unque_key).removeValue();
                context.startActivity(intent);
            }
        });


        //share button click event
        holder.share.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String unque_key = uf.getFlightId();
                String instruction = "Hi, this is "+userName+". Copy the link below and paste it to CheckTrip so that you can see my flight information. Thank you! ";
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT,"Subject Here");
                intent.putExtra(Intent.EXTRA_TEXT, instruction + String.valueOf(flightList.child(unque_key))+".json");

//                intent.putExtra(Intent.EXTRA_TEXT, "Hi, this is "+userName+". Copy the link below and paste it to CheckTrip so that you can see my flight information. Thank you!"+"\n\n"+String.valueOf(flightList.child(unque_key)));
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
        TextView terminal,gate;
        Button share,delete;

        public ViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.cv);
            flightnumber = (TextView) itemView.findViewById(R.id.flightnumber);
            departureAirport = (TextView) itemView.findViewById(R.id.departureAirport);
            departureTime = (TextView) itemView.findViewById(R.id.departureTime);
            arrivalAirport = (TextView) itemView.findViewById(R.id.arrivalAirport);
            arrivalTime = (TextView) itemView.findViewById(R.id.arrivalTime);
            terminal = (TextView) itemView.findViewById(R.id.terminal);
            gate = (TextView) itemView.findViewById(R.id.gate);
            share = (Button) itemView.findViewById(R.id.btn_share);
            delete = (Button) itemView.findViewById(R.id.btn_delete);


        }
    }


}
