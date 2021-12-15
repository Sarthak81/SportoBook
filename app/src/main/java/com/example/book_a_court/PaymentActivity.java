package com.example.book_a_court;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.Objects;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {
    int fare;
    String complex_name,userPhone,userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_payment);
        Checkout.preload(getApplicationContext());
        fare = getIntent().getIntExtra("fare",0);
        complex_name = getIntent().getStringExtra("complex_name");

        startPayment();
    }

    public void startPayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_6bRRkpCfQh2t0B");

//        checkout.setImage(R.drawable.logo);

        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();

            options.put("name", complex_name);
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", fare*100);//pass amount in currency subunits
            options.put("prefill.email", userEmail);
            options.put("prefill.contact",userPhone);
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
//        Toast.makeText(this, "Successful" + s, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),PaymentStatus.class);
        intent.putExtra("status","Payment Successful");
        intent.putExtra("payId",s);
        startActivity(intent);
    }

    @Override
    public void onPaymentError(int i, String s) {
//        Toast.makeText(this, "Error cause by" + s, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),PaymentStatus.class);
        intent.putExtra("status","Payment Failed");
        intent.putExtra("payId",s);
        startActivity(intent);
    }
}