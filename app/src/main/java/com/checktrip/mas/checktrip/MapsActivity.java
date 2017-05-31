package com.checktrip.mas.checktrip;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.Manifest;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import modules.DirectionFinder;
import modules.DirectionFinderListener;
import modules.Route;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        DirectionFinderListener {
    private GoogleMap mMap;
    private Button btnFindPath;
    private EditText etOrigin;
    private EditText etDestination;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;

    private LocationManager locManager;
    private Location location;
    private String bestProvider;
    private TextView time,go_date,TSAtime,btw,title2;
    private String depAirportAddress,deplocaltime,deptime;
    private String travelDuration = "0";
    private int diff_min;
    private int waittime;
    private boolean judge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        btnFindPath = (Button) findViewById(R.id.btnFindPath);
        etOrigin = (EditText) findViewById(R.id.etOrigin);
        etDestination = (EditText) findViewById(R.id.etDestination);
        time = (TextView)findViewById(R.id.time);
        go_date = (TextView) findViewById(R.id.go_date);
        TSAtime = (TextView)findViewById(R.id.TSAtime);
        btw = (TextView)findViewById(R.id.btw);
        title2 = (TextView) findViewById(R.id.title2);

        Intent intent = getIntent();
        String depcity = intent.getStringExtra("depcity");
        deplocaltime = intent.getStringExtra("deplocaltime");
        deptime = intent.getStringExtra("deptime");
        depAirportAddress = intent.getStringExtra("depAirportAddress");
        diff_min = Integer.parseInt(intent.getStringExtra("diff_min"));
        judge = intent.getExtras().getBoolean("judge");
        Log.i("1515151515",Integer.toString(diff_min));


        ((TextView) findViewById(R.id.etDestination)).setText(depAirportAddress);



        new tsaGet().execute(depcity);

        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });

    }
    private void initProvider() {
        //创建LocationManager对象

        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // List all providers:
        //List<String> providers = locManager.getAllProviders();
        Criteria criteria = new Criteria();
        bestProvider = locManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            location = locManager.getLastKnownLocation(bestProvider);
        } else {
            // Show rationale and request permission.
            Toast.makeText(this, "No GPS Signal!", Toast.LENGTH_SHORT).show();
        }
        System.out.println("经度："+location.getLatitude()+"  纬度：" + location.getLongitude());
    }
    private void sendRequest() {
        /*if (location == null) {
            String origin = etOrigin.getText().toString();
        } else {
            String origin = String.valueOf(location.getLatitude())+","+String.valueOf(location.getLongitude());
        }*/
        //String origin = etOrigin.getText().toString();
        /*if (origin.isEmpty()) {
            origin = etOrigin.getText().toString();
        }*/
        String origin = etOrigin.getText().toString();
        if (origin.equals("cl")||origin.equals("Current Location") ){
            origin = String.valueOf(location.getLatitude())+","+String.valueOf(location.getLongitude());
            etOrigin.setText("Current Location", TextView.BufferType.EDITABLE);
        }
        //String destination = etDestination.getText().toString();
        String destination = depAirportAddress;//need to replace
        if (origin.isEmpty()) {
            Toast.makeText(this, "Please enter origin address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            Toast.makeText(this, "Please enter destination address!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng gatech = new LatLng(33.775230, -84.396671);
        Marker gatechmaker = mMap.addMarker(new MarkerOptions().position(gatech).title("Georgia Tech"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gatech,12));
        gatechmaker.remove();
        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }*/
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            initProvider();
        } else {
            // Show rationale and request permission.
            Toast.makeText(this, "No GPS Signal!", Toast.LENGTH_SHORT).show();
        }
        //LatLng curloc = new LatLng(location.getLatitude(), location.getLongitude());
        //mMap.addMarker(new MarkerOptions().position(curloc).title("Current Location"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curloc,12));
        UiSettings mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
    }
    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            //The duration is the google map direction time for original to destination
            //text 直接显示时间，hour 和 min， value 为 second
            /*Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5:00"));
            Date currentLocalTime = cal.getTime();
            DateFormat date = new SimpleDateFormat("HH:mm a");
            // you can get seconds by adding  "...:ss" to it
            date.setTimeZone(TimeZone.getTimeZone("GMT-5:00"));

            String localTime = date.format(currentLocalTime);*/


            int dura = (int)Math.ceil(route.duration.value/60.0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = null;
            try {
                date = sdf.parse(deptime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long newtime2;

            if(judge){
                newtime2 = date.getTime() - (waittime)*60000 - dura*60000;

            }else{
                newtime2 = date.getTime() - dura*60000 - diff_min*60000;

            }


            String timeSequence = String.valueOf(getDate(newtime2, "yyyy-MM-dd HH:mm"));
            String[] parts = timeSequence.split(" ");



            time.setText(parts[1]);
            go_date.setText(parts[0]);
            btw.setText("(Based On The Location You Select)");


            System.out.println(getDate(newtime2, "yyyy-MM-dd HH:mm")+"\n\n\n"+"(Based On The Location You Select)");
            System.out.println(newtime2);
            travelDuration = String.valueOf(dura);
            ((TextView) findViewById(R.id.tvDuration)).setText(travelDuration+"mins");
            //((TextView) findViewById(R.id.tvDuration)).setText(localTime);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.GREEN).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }

    private class tsaGet extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {

            String urls = params[0].toString();
            String site = "http://apps.tsa.dhs.gov/MyTSAWebService/GetTSOWaitTimes.ashx?ap="+urls+"&output=json";
            Log.i("FlightStats", site.toString());

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(site);

                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer result = new StringBuffer();
                String line;
                while((line=reader.readLine())!=null){
                    result.append(line);
                }
                Log.i("FlightStats", result.toString());
                return result.toString();

            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(connection != null){
                    connection.disconnect();
                }
                try{
                    if(reader != null){
                        reader.close();
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try{
                JSONObject waitTimeList = new JSONObject(result);
                String waittimeIndex = waitTimeList.getJSONArray("WaitTimes").getJSONObject(0).getString("WaitTime");
                int i = Integer.parseInt(waittimeIndex);
                switch (i){
                    case 1:
                        waittime = 0;
                        TSAtime.setText("0 minutes");
                        break;
                    case 2:
                        waittime = 10;
                        TSAtime.setText("1-10 minutes");
                        break;
                    case 3:
                        waittime = 20;
                        TSAtime.setText("11-20 minutes");
                        break;
                    case 4:
                        waittime = 30;
                        TSAtime.setText("21-30 minutes");
                        break;
                    case 5:
                        waittime = 45;
                        TSAtime.setText("31-45 minutes");
                        break;
                    case 6:
                        waittime = 60;
                        TSAtime.setText("46-60 minutes");
                        break;
                    case 7:
                        waittime = 90;
                        TSAtime.setText("61-90 minutes");
                        break;
                    case 8:
                        waittime = 120;
                        TSAtime.setText("91-120 minutes");
                        break;
                    case 9:
                        waittime = 120;
                        TSAtime.setText("120+ minutes");
                        break;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date date = null;
                try {
                    date = sdf.parse(deptime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long newtime;
                if(judge){
                    newtime = date.getTime() - (waittime)*60000;

                }else{
                    newtime = date.getTime() - diff_min*60000;
                    title2.setText("Baggage Time:");
                }


                String timeSequence = String.valueOf(getDate(newtime, "yyyy-MM-dd HH:mm"));
                String[] parts = timeSequence.split(" ");



                time.setText(parts[1]);
                go_date.setText(parts[0]);
                System.out.println(getDate(newtime, "yyyy-MM-dd HH:mm")+"\n"+"(Without Considering Time You Spend On The Road)"+newtime);
                System.out.println(newtime);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Log.i("FlightStats", result);
        }


    }


    public String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }



}