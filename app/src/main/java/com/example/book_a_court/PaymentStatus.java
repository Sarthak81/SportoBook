package com.example.book_a_court;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class PaymentStatus extends AppCompatActivity {
    TextView paymentStatus,paymentID;
    String status,id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_status);

        paymentID = findViewById(R.id.payment_id);
        paymentStatus = findViewById(R.id.payment_status);

        status = getIntent().getStringExtra("status");
        id = getIntent().getStringExtra("payId");

        if(status.equals("Payment Successful")){
            paymentStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        }
        else{
            paymentStatus.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }

        paymentStatus.setText(status);
        paymentID.setText("Transaction ID : "+id);
    }
}