package com.example.book_a_court;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.book_a_court.ui.home.BookingData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.sql.Time;
import java.util.Objects;

//implements payment feature

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {
    int fare,d,m,y,hr1,hr2;
    String complexName,userPhone,userEmail,sportName,uid,sportPrice,userName;
    FirebaseAuth fauth;
    FirebaseDatabase db;
    DatabaseReference personReference,complexReference;
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_payment);
        Checkout.preload(getApplicationContext());
        fare = getIntent().getIntExtra("fare",0);
        complexName = getIntent().getStringExtra("complex_name");
        uid = getIntent().getStringExtra("uid");
        sportName = getIntent().getStringExtra("sport_name");
        sportPrice = getIntent().getStringExtra("sport_price");
        d = getIntent().getIntExtra("day",0);
        m = getIntent().getIntExtra("month",0);
        y = getIntent().getIntExtra("year",0);
        hr1 = getIntent().getIntExtra("hr1",0);
        hr2 = getIntent().getIntExtra("hr2",0);

        fauth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        documentReference = FirebaseFirestore.getInstance().collection("users").document(Objects.requireNonNull(fauth.getUid()));
        personReference = db.getReference().child("PersonBookings").child(Objects.requireNonNull(fauth.getUid()));
        complexReference = db.getReference().child("ComplexBookings").child(uid);

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                userName = value.getString("fName");
            }
        });
        startPayment();
    }

    //Func for initiating portal
    public void startPayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_6bRRkpCfQh2t0B");

//        checkout.setImage(R.drawable.logo);

        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();

            options.put("name", complexName);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onPaymentSuccess(String s) {
//        Toast.makeText(this, "Successful" + s, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),PaymentStatus.class);
        intent.putExtra("status","Payment Successful");
        intent.putExtra("payId",s);

        BookingData bookingDataPerson = new BookingData(d,m,y,hr1,hr2,fare,java.time.LocalDate.now().toString(),
                java.time.LocalTime.now().toString(),sportName,complexName);
        personReference.setValue(bookingDataPerson);

        BookingData bookingDataComplex = new BookingData(d,m,y,hr1,hr2,fare,java.time.LocalDate.now().toString(),
                java.time.LocalTime.now().toString(),sportName,userName);
        complexReference.setValue(bookingDataComplex);

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