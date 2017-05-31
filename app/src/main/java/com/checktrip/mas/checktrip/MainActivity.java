package com.checktrip.mas.checktrip;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity{
    private final int SPLASH_DISPLAY_LENGHT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent mainIntent = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(mainIntent);
                MainActivity.this.finish();
            }

        }, SPLASH_DISPLAY_LENGHT);



    }




}
