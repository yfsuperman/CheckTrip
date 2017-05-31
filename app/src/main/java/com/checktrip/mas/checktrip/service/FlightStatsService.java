package com.checktrip.mas.checktrip.service;

import android.os.AsyncTask;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import static java.lang.Math.log;

/**
 * Created by FanYang on 10/19/16.
 */

public class FlightStatsService {
    private FlightStatsCallback callback;
    private String date;
    private String flightNo, airline;
    private Exception error;

    public FlightStatsService(FlightStatsCallback callback){
        this.callback = callback;
    }

    public String getDate(){
        return date;
    }
    public String getFlightNo(){
        return flightNo;
    }


    public void refreshFlight(String l, String m, String n){
        this.date = l;
        this.flightNo = m;
        this.airline = n;
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... Param) {



                String info =airline+"/"+flightNo+"/dep/"+date+"?appId=98a92df2&appKey=3644b00bd9356ea1a4c95f65554d23e2&utc=false";
                String site = "https://api.flightstats.com/flex/flightstatus/rest/v2/json/flight/status/" + info;
                Log.i("FlightStats", site.toString());


                try {
                    URL url = new URL(site);

                    URLConnection connection = url.openConnection();

                    InputStream inputStream = connection.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while((line=reader.readLine())!=null){
                        result.append(line);
                    }
                    //Log.i("FlightStats", result.toString());
                    return result.toString();

                } catch (Exception e) {
                    error = e;
                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {

                if(s==null && error!=null){
                    callback.serviceFailure(error);
                    return;
                }

                try {
                    JSONObject data = new JSONObject(s);

                    JSONObject flightStatus = data;



                    if(flightStatus==null){
                        callback.serviceFailure(new FlightNumberException("Sorry, no such flight"));
                        return;
                    }



                    Log.i("FlightStats", flightStatus.toString());
                    //FlightStatuses ff = new FlightStatuses();
                    //ff=flightStatus.getJSONObject(0).getString("departureAirportFsCode");

                    callback.serviceSuccess(flightStatus);
                } catch (JSONException e) {
                    callback.serviceFailure(e);
                }

            }
        }.execute(date,flightNo);
    }

    public class FlightNumberException extends Exception{
        public FlightNumberException (String s){
            super (s);
        }
    }

}
