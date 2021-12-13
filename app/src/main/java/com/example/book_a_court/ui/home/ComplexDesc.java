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
import com.example.book_a_court.ui.complexPages.gallery_images.ImageUploadInfo;
import com.example.book_a_court.ui.complexPages.gallery_images.RecyclerViewAdapter;
import com.example.book_a_court.ui.complexPages.gallery_images.gallery_main;
import com.example.book_a_court.ui.complexPages.manage.ManageSportsAdapter;
import com.example.book_a_court.ui.complexPages.manage.Sport;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
    RecyclerViewAdapter adapter1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complex_desc);
        DatabaseReference databaseReference1;
        List< ImageUploadInfo > list = new ArrayList<>();
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
        complex_desc_recycler.setHasFixedSize(true);
        complex_desc_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_SHORT).show();

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

        //String l_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Setting up Firebase image upload folder path in databaseReference.
        // The path is already defined in gallery_main.
        databaseReference1 = FirebaseDatabase.getInstance().getReference(gallery_main.Database_Path).child(complex_uid);

       // databaseReference_vid=FirebaseDatabase.getInstance().getReference("Videos").child(l_uid);
        // Adding Add Value Event Listener to databaseReference.
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {

//                progressDialog.setMessage("Loading Images From Firebase.");
//                progressDialog.show();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    ImageUploadInfo imageUploadInfo = postSnapshot.getValue(ImageUploadInfo.class);

                    list.add(imageUploadInfo);
                    Toast.makeText(getApplicationContext(), ""+list.size(), Toast.LENGTH_SHORT).show();
                    // Toast.makeText(getApplicationContext(), ""+imageUploadInfo.getImageURL(), Toast.LENGTH_SHORT).show();
                  //  adapter1.notifyDataSetChanged();
                }

                adapter1 = new RecyclerViewAdapter(getApplicationContext(), list);

                complex_desc_recycler.setAdapter(adapter1);
                Toast.makeText(getApplicationContext(), ""+list.size(), Toast.LENGTH_SHORT).show();

                // Hiding the progress dialog.
//                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {

                // Hiding the progress dialog.
               // progressDialog.dismiss();

            }
        });











        priceList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        priceListAdapter = new PriceListAdapter(getApplicationContext(), sportArrayList);
        priceList.setAdapter(priceListAdapter);
    }
}