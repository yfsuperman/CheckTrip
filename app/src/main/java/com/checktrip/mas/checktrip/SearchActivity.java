package com.checktrip.mas.checktrip;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import java.util.Calendar;


public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bSearch, etDate;
    private EditText etFlight,etAirline;
    private Calendar c;
    private CheckBox checkbox;


    private final int DATE_DIALOG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        c = Calendar.getInstance();

        etDate = (Button) findViewById(R.id.etDate);
        etDate.setOnClickListener(this);
        etFlight = (EditText) findViewById(R.id.etFlight);
        etAirline = (EditText) findViewById(R.id.etAirline);
        bSearch = (Button) findViewById(R.id.bSearch);
        bSearch.setOnClickListener(this);
        checkbox = (CheckBox) findViewById(R.id.checkbox);
    }








    //case action
    public void onClick(View v) {
        Boolean checkBag = checkbox.isChecked();
        String checkBag_string = "false";
        if(checkBag){
            checkBag_string = "true";
        }


        String date_string = etDate.getText().toString().trim();
        String flight_string = etFlight.getText().toString().trim();
        String airline_string = etAirline.getText().toString().trim();
        switch (v.getId()) {
            case R.id.bSearch:

                Intent intent = new Intent(SearchActivity.this, ShowAPI.class);
                intent.putExtra("date", date_string);
                intent.putExtra("flight", flight_string);
                intent.putExtra("airline", airline_string);
                intent.putExtra("bag",checkBag_string);
                startActivity(intent);

                break;

            case R.id.etDate:
                new DatePickerDialog(SearchActivity.this, new DatePickerDialog.OnDateSetListener(){

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                        etDate.setText(year+"/"+(monthOfYear+1)+"/"+dayOfMonth);

                    }
                },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();

                break;



        }
    }




}
