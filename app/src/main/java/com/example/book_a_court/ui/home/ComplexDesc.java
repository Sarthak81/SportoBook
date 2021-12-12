package com.example.book_a_court.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.book_a_court.R;
import com.example.book_a_court.ui.complexPages.manage.ManageSportsAdapter;
import com.example.book_a_court.ui.complexPages.manage.Sport;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ComplexDesc extends AppCompatActivity {
    String complex_name,complex_uid;
    TextView complex_desc_name;
    TextView complex_desc_rating;
    RecyclerView complex_desc_recycler,priceList;
    RatingBar ratingStars;
    FirebaseFirestore db;
    Button bookBtn;
    ArrayList<Sport> sportArrayList;
    PriceListAdapter priceListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complex_desc);

        complex_name = getIntent().getStringExtra("cName");
        complex_uid = getIntent().getStringExtra("uid");

        complex_desc_name = findViewById(R.id.complex_desc_name);
        complex_desc_rating = findViewById(R.id.complex_desc_rating);
        complex_desc_recycler = findViewById(R.id.complex_desc_recycler);
        priceList = findViewById(R.id.price_list);
        bookBtn = findViewById(R.id.bookbtn);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        sportArrayList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        db.collection("sports").document(complex_uid).collection("court")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot snapshot : task.getResult()){
                            Sport sport = new Sport(snapshot.getString("court_name"),snapshot.get("court_price").toString(),snapshot.get("court_number").toString());
                            Log.d("newestTag", "onSuccess: " + (snapshot.getString("court_name")+snapshot.get("court_price").toString()+snapshot.get("court_number").toString()));
                            sportArrayList.add(sport);
                        }
                        priceListAdapter.notifyDataSetChanged();
                    }
                });

        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),complex_avail.class);
                intent.putExtra("cName",complex_name);
                intent.putExtra("uid",complex_uid);
                startActivity(intent);
            }
        });

        complex_desc_name.setText(complex_name);
        ratingStars = findViewById(R.id.ratingBar2);
        ratingStars.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                int rating = (int) v;
                String message = null;
                switch(rating){
                    case 1:
                        message="We apologize for inconvenience!";
                        break;
                    case 2:
                        message="Sorry to hear that!";
                        break;
                    case 3:
                        message="Good Enough!";
                        break;
                    case 4:
                        message="Great!, Thank you!";
                        break;
                    case 5:
                        message="Awesome! You are the best!";
                        break;
                }

            }
        });

        priceList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        priceListAdapter = new PriceListAdapter(getApplicationContext(), sportArrayList);
        priceList.setAdapter(priceListAdapter);
    }
}