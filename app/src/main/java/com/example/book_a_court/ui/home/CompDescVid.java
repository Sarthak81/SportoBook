package com.example.book_a_court.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;

import com.example.book_a_court.R;
import com.example.book_a_court.ui.complexPages.gallery_images.ImageUploadInfo;
import com.example.book_a_court.ui.complexPages.gallery_video.RecyclerVideoAdapter;
import com.example.book_a_court.ui.complexPages.gallery_video.videoUploadInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CompDescVid extends AppCompatActivity {
    DatabaseReference databaseReference_vid;

    RecyclerView recyclerView_vid_1;

    RecyclerVideoAdapter adapter_vid_1;


    List< videoUploadInfo > list_vid_1 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp_desc_vid);

        String complex_uid= getIntent().getStringExtra("uid");
        recyclerView_vid_1=(RecyclerView) findViewById(R.id.recyclerView_video_1);
        recyclerView_vid_1.setHasFixedSize(true);
        recyclerView_vid_1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        databaseReference_vid= FirebaseDatabase.getInstance().getReference("Videos").child(complex_uid);

        databaseReference_vid.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    videoUploadInfo videoUploadinfo = postSnapshot.getValue(videoUploadInfo.class);
                    assert videoUploadinfo != null;
                    // Toast.makeText(getContext(), ""+videoUploadinfo.getVideoURL(), Toast.LENGTH_SHORT).show();
                    list_vid_1.add(videoUploadinfo);


                }

                adapter_vid_1 = new RecyclerVideoAdapter(getApplicationContext(), list_vid_1);

                recyclerView_vid_1.setAdapter(adapter_vid_1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }

        });


    }
}