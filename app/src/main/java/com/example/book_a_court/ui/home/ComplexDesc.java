package com.example.book_a_court.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.book_a_court.R;

public class ComplexDesc extends AppCompatActivity {
    String complex_name,complex_uid;
    TextView complex_desc_name;
    TextView complex_desc_rating;
    RecyclerView complex_desc_recycler;
    RatingBar ratingStars;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complex_desc);

        complex_name = getIntent().getStringExtra("cName");
        complex_uid = getIntent().getStringExtra("uid");

        complex_desc_name = findViewById(R.id.complex_desc_name);
        complex_desc_rating = findViewById(R.id.complex_desc_rating);
        complex_desc_recycler = findViewById(R.id.complex_desc_recycler);

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
    }
}