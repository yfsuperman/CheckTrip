package com.checktrip.mas.checktrip;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Handler;



public class UserHomePage extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    //private Toolbar toolbar;
    private Button logout, pickup;
    private FloatingActionButton fab;
    private ArrayList<UserFlightInfo> mflight = new ArrayList<>();
    private flightRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private SwipeRefreshLayout swipeLayout;
    private boolean isRefresh = true;

    private Toolbar toolbar;

    public UserHomePage() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_page);



        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
//        //加载颜色是循环播放的，只要没有完成刷新就会一直循环，color1>color2>color3>color4
//        swipeLayout.setColorScheme(android.R.color.white,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light, android.R.color.holo_red_light);


        //set toolbar
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){
//
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
////                switch (item.getItemId()) {
////                    case R.id.logout:
////                        firebaseAuth.signOut();
////                        //closing activity
////                        finish();
////                        //starting login activity
////                        startActivity(new Intent(UserHomePage.this, LoginActivity.class));
////                }
//                return true;
//            }
//        });
//
//        toolbar.inflateMenu(R.menu.menu);


        //Floating Button
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserHomePage.this,SearchActivity.class);
                startActivity(intent);
            }
        });

        //Logout Button
        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                //closing activity
                finish();
                //starting login activity
                startActivity(new Intent(UserHomePage.this, LoginActivity.class));
            }
        });


        //Pick Up button
        pickup = (Button) findViewById(R.id.pickup);
        pickup.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //starting login activity
                startActivity(new Intent(UserHomePage.this, PickUpHomePage.class));
            }
        });








        //getting the database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //getting current user
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        String uid = user.getUid();

        final DatabaseReference flightList = databaseReference.child("users/"+uid+"/flights/");
//        DatabaseReference key = flightList;
        Log.i("key", String.valueOf(flightList));

//        UserFlightInfo uf = new UserFlightInfo();
//        uf.setArrPlace("a");
//        uf.setDepPlace("a");
//        uf.setUnique_key("a");
//        uf.setFlightDate("a");
//        uf.setFlightNo("a");
//        mflight.add(uf);
//
//        UserFlightInfo uf2 = new UserFlightInfo();
//        uf2.setArrPlace("b");
//        uf2.setDepPlace("b");
//        uf2.setUnique_key("b");
//        uf2.setFlightDate("b");
//        uf2.setFlightNo("b");
//        mflight.add(uf2);



//        flightList.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                for(DataSnapshot data : dataSnapshot.getChildren()){
//                    UserFlightInfo date_uf = data.getValue(UserFlightInfo.class);
//                    UserFlightInfo uf = new UserFlightInfo();
//                    uf.setArrPlace(date_uf.getArrPlace());
//                    uf.setDepPlace(date_uf.getDepPlace());
//                    uf.setUnique_key(date_uf.getUnique_key());
//                    uf.setFlightDate(date_uf.getFlightDate());
//                    uf.setFlightNo(date_uf.getFlightNo());
//
//
//                    Log.i("uf", String.valueOf(uf));
//                    Log.i("data",String.valueOf(data));
//                    mflight.add(uf);
//                }
//
//
////                final String key = dataSnapshot.getKey();
////                UserFlightInfo uf = dataSnapshot.getValue(UserFlightInfo.class);
////                String kkey = uf.getUnique_key();
////                //DataSnapshot ref = dataSnapshot.child(key);
////                //UserFlightInfo uf = ref.getValue(UserFlightInfo.class);
////                Log.i("kkey", String.valueOf(kkey));
////                Log.i("uf", String.valueOf(uf));
////                mflight.put(key,uf);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        flightList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserFlightInfo uf = dataSnapshot.getValue(UserFlightInfo.class);
                mflight.add(uf);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                UserFlightInfo uf = dataSnapshot.getValue(UserFlightInfo.class);
                mflight.add(uf);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                UserFlightInfo uf = dataSnapshot.getValue(UserFlightInfo.class);
                mflight.remove(uf);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                UserFlightInfo uf = dataSnapshot.getValue(UserFlightInfo.class);
                mflight.add(uf);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.i("mflight", String.valueOf(mflight));
        //mflight = { "key":"-KW3deIeeRDAEFbnbfur"; "value": {"unique_key":"-KW3deIeeRDAEFbnbfur"; "depPlace":"ATL - Atlanta， GA， US"; "arrPlace":"SEA - Seattle， WA， US"; "flightNo":"dl2329"; "flightDate":"2016/11/9";} ;};

        mRecyclerView = (RecyclerView) findViewById(R.id.messageList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(UserHomePage.this));
        mAdapter = new flightRecyclerAdapter(this, mflight);
        mRecyclerView.setAdapter(mAdapter);



    }

    @Override
    public void onStart(){
        super.onStart();
        mAdapter.notifyDataSetChanged();


    }

    @Override
    public void onResume(){
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onRefresh() {
        if(!isRefresh){ isRefresh = true;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    swipeLayout.setRefreshing(false);
                    mAdapter.notifyDataSetChanged();
                }
            }, 1000); }

    }
}
