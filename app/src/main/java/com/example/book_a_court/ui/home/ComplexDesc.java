package com.example.book_a_court.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.book_a_court.R;
import com.example.book_a_court.ui.complexPages.gallery_images.ImageUploadInfo;
import com.example.book_a_court.ui.complexPages.gallery_images.RecyclerViewAdapter;
import com.example.book_a_court.ui.complexPages.gallery_images.gallery_main;
import com.example.book_a_court.ui.complexPages.gallery_video.videoUploadInfo;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ComplexDesc extends AppCompatActivity {
    String complex_name,complex_uid,sport_name;
    TextView complex_desc_name,time1,time2;
    TextView complex_desc_rating,avail;
    RecyclerView complex_desc_recycler,priceList;
    RatingBar ratingStars;
    FirebaseFirestore db;
    Button bookBtn;
    ArrayList<Sport> sportArrayList;
    PriceListAdapter priceListAdapter;
    RecyclerViewAdapter adapter1;
    int myRating = 0;
    Button ratebtn;
    CalendarView calendarView;
    int d,m,y,hr1,hr2;
    ArrayList<String> spinnerList;
    float avgrating =0;
    int size;
    String[] spinnerArr;
    ArrayAdapter<String> adapter;
    Spinner dropdown;

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
        avail = findViewById(R.id.avail);
        time1 = findViewById(R.id.time1);
        time2 = findViewById(R.id.time2);
        priceList = findViewById(R.id.price_list);
        bookBtn = findViewById(R.id.bookbtn);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        sportArrayList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        complex_desc_recycler.setHasFixedSize(true);
        complex_desc_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        spinnerList = new ArrayList<>();


        db.collection("sports").document(complex_uid).collection("court")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot snapshot : task.getResult()){
                            Sport sport = new Sport(snapshot.getString("court_name"),snapshot.get("court_price").toString(),snapshot.get("court_number").toString());
                            Log.d("newestTag", "onSuccess: " + (snapshot.getString("court_name")+snapshot.get("court_price").toString()+snapshot.get("court_number").toString()));
                            sportArrayList.add(sport);
                            spinnerList.add(sport.getName());
                        }
                        size = spinnerList.size();
                        spinnerArr = new String[size];
                        for(int i=0;i<size;i++){
                            spinnerArr[i]=spinnerList.get(i);
                        }
                        dropdown = findViewById(R.id.spinner);
                        adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,spinnerArr);
                        dropdown.setAdapter(adapter);
                        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                sport_name = parent.getSelectedItem().toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        priceListAdapter.notifyDataSetChanged();
                    }
                });

        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                d=dayOfMonth;
                m=month;
                y = year;
            }
        });

        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),complex_fare.class);
                hr1 = Integer.parseInt(time1.getText().toString());
                hr2 = Integer.parseInt(time2.getText().toString());
                intent.putExtra("cName",complex_name);
                intent.putExtra("uid",complex_uid);
                intent.putExtra("day",d);
                intent.putExtra("month",m);
                intent.putExtra("year",y);
                intent.putExtra("hr1",hr1);
                intent.putExtra("hr2",hr2);
                intent.putExtra("sport_name",sport_name);
                startActivity(intent);
            }
        });

        //fetching availability
//        db.collection("bookings").document(complex_uid).collection(String.valueOf(d+'/'+m+'/'+y))
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful()){
//                            boolean flag=true;
//                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
//                                if(documentSnapshot.get("day").toString()==String.valueOf(time.getD()) &&
//                                        documentSnapshot.get("month").toString()==String.valueOf(time.getM()) &&
//                                        documentSnapshot.get("year").toString()==String.valueOf(time.getD()) ){
//                                    int tmp1 = Integer.parseInt(documentSnapshot.get("hr1").toString());
//                                    int tmp2 = Integer.parseInt(documentSnapshot.get("hr2").toString());
//                                    if((tmp1>=hr1 && tmp1<hr2) || (tmp2>hr1 && tmp2<=hr2)){
//                                        avail.setText("Unavailable");
//                                        avail.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
//                                        flag = false;
//                                        break;
//                                    }
//                                }
//                            }
//                            if(flag){
//                                avail.setText("Available");
//                                avail.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
//                            }
//                        }
//                    }
//                });


        complex_desc_name.setText(complex_name);
        ratingStars = findViewById(R.id.ratingBar2);
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mRatingBarCh = rootRef.child("ratings").child(complex_uid);

        mRatingBarCh.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float total=0,num=0;
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    total = total+Float.parseFloat(postSnapshot.getValue().toString());
                    num++;
                }
                    avgrating= total/num;
                complex_desc_rating.setText(String.valueOf(avgrating) + "/5");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ratingStars.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                int rating = (int) v;
                String message = null;
                myRating  =(int) ratingBar.getRating();
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
                Toast.makeText(ComplexDesc.this,message, Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.ratebutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = ratingStars.getRating();
                mRatingBarCh.child(user.getUid()).setValue(String.valueOf(rating));
            }
        });

        databaseReference1 = FirebaseDatabase.getInstance().getReference(gallery_main.Database_Path).child(complex_uid);

        // Adding Add Value Event Listener to databaseReference.

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    ImageUploadInfo imageUploadInfo = postSnapshot.getValue(ImageUploadInfo.class);

                    list.add(imageUploadInfo);
//                    adapter1.notifyDataSetChanged();
                }

                adapter1 = new RecyclerViewAdapter(getApplicationContext(), list);

                complex_desc_recycler.setAdapter(adapter1);
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {

            }
        });











        priceList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        priceListAdapter = new PriceListAdapter(getApplicationContext(), sportArrayList);
        priceList.setAdapter(priceListAdapter);
    }
}