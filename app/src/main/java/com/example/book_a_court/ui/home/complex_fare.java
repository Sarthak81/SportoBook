package com.example.book_a_court.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.book_a_court.R;

public class complex_fare extends AppCompatActivity {
    String complexName,uid,sportName,sportPrice;
    int d,m,y,hr1,hr2;
    TextView complex_name,fare,sport_name,duration;
    Button pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complex_fare);

        complexName = getIntent().getStringExtra("cName");
        uid = getIntent().getStringExtra("uid");
        sportName = getIntent().getStringExtra("sport_name");
        sportPrice = getIntent().getStringExtra("sport_price");
        d = getIntent().getIntExtra("day",0);
        m = getIntent().getIntExtra("month",0);
        y = getIntent().getIntExtra("year",0);
        hr1 = getIntent().getIntExtra("hr1",0);
        hr2 = getIntent().getIntExtra("hr2",0);

        complex_name = findViewById(R.id.fare_name);
        fare = findViewById(R.id.fare_fare);
        sport_name = findViewById(R.id.fare_sport);
        duration = findViewById(R.id.fare_duration);
        pay = findViewById(R.id.proceed_to_pay);

        int price = Integer.parseInt(sportPrice);
        Toast.makeText(this, ""+price+" "+complexName+" "+sportName, Toast.LENGTH_SHORT).show();
        complex_name.setText(complexName);
        sport_name.setText(sportName);
        fare.setText(String.valueOf((hr2-hr1)*price));
        duration.setText(String.valueOf(hr2-hr1));
    }
}