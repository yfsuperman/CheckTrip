package com.checktrip.mas.checktrip;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class tsaTime extends AppCompatActivity {

    private TextView time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tsa_time);

        time = (TextView)findViewById(R.id.time);
        Intent intent = getIntent();
        String depcity = intent.getStringExtra("depcity");
        String deplocaltime = intent.getStringExtra("deplocaltime");
        String deptime = intent.getStringExtra("deptime");
        String depAirportAddress = intent.getStringExtra("depAirportAddress");


        new tsaGet().execute(depcity);
    }


    private class tsaGet extends AsyncTask<String,String,String>{

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
                int waittime = 0;
                int i = Integer.parseInt(waittimeIndex);
                switch (i){
                    case 1:
                        waittime = 0;
                        break;
                    case 2:
                        waittime = 10;
                        break;
                    case 3:
                        waittime = 20;
                        break;
                    case 4:
                        waittime = 30;
                        break;
                    case 5:
                        waittime = 45;
                        break;
                    case 6:
                        waittime = 60;
                        break;
                    case 7:
                        waittime = 90;
                        break;
                    case 8:
                        waittime = 120;
                        break;
                    case 9:
                        waittime = 120;
                        break;
                }



                time.setText(String.valueOf(waittime));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Log.i("FlightStats", result);
        }


    }


}
