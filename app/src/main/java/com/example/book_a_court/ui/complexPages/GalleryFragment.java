package com.example.book_a_court.ui.complexPages;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_a_court.R;
import com.example.book_a_court.ui.complexPages.gallery_images.ImageUploadInfo;
import com.example.book_a_court.ui.complexPages.gallery_video.videoUploadInfo;
import com.example.book_a_court.ui.complexPages.gallery_video.RecyclerVideoAdapter;
import com.example.book_a_court.ui.complexPages.gallery_images.RecyclerViewAdapter;
import com.example.book_a_court.ui.complexPages.gallery_images.gallery_main;
import com.example.book_a_court.ui.complexPages.manage.video_upload;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    DatabaseReference databaseReference,databaseReference_vid;
    Button add_vid,add_img;
    // Creating RecyclerView.
    RecyclerView recyclerView,recyclerView_vid;

    // Creating RecyclerView.Adapter.
    RecyclerView.Adapter adapter;
    RecyclerVideoAdapter adapter_vid;

    // Creating Progress dialog
    ProgressDialog progressDialog;

    // Creating List of ImageUploadInfo class.
    List< ImageUploadInfo > list = new ArrayList<>();
    List< videoUploadInfo > list_vid = new ArrayList<>();



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
//        //final TextView textView = root.findViewById(R.id.text_gallery);
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView_vid=(RecyclerView) root.findViewById(R.id.recyclerView_video);
        add_vid=root.findViewById(R.id.video_add);
        add_img=root.findViewById(R.id.image_add);

        // Setting RecyclerView size true.
        recyclerView.setHasFixedSize(true);
        recyclerView_vid.setHasFixedSize(true);


        // Setting RecyclerView layout as LinearLayout.
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView_vid.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Assign activity this to progress dialog.
        progressDialog = new ProgressDialog(getContext());

        // Setting up message in Progress dialog.
        // Showing progress dialog.
        add_vid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), video_upload.class);
                startActivity(intent);
            }
        });
        add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), gallery_main.class);
                startActivity(intent);
            }
        });
        String l_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Setting up Firebase image upload folder path in databaseReference.
        // The path is already defined in gallery_main.
        databaseReference = FirebaseDatabase.getInstance().getReference(gallery_main.Database_Path).child(l_uid);

        databaseReference_vid=FirebaseDatabase.getInstance().getReference("Videos").child(l_uid);
        // Adding Add Value Event Listener to databaseReference.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                progressDialog.setMessage("Loading Images From Firebase.");
                progressDialog.show();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    ImageUploadInfo imageUploadInfo = postSnapshot.getValue(ImageUploadInfo.class);

                    list.add(imageUploadInfo);
                   // Toast.makeText(getContext(), ""+imageUploadInfo.getImageURL(), Toast.LENGTH_SHORT).show();

                }

                adapter = new RecyclerViewAdapter(getActivity(), list);

                recyclerView.setAdapter(adapter);

                // Hiding the progress dialog.
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                // Hiding the progress dialog.
                progressDialog.dismiss();

            }
        });

        databaseReference_vid.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                progressDialog.setMessage("Loading Images From Firebase.");
                progressDialog.show();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    videoUploadInfo videoUploadinfo = postSnapshot.getValue(videoUploadInfo.class);
                    assert videoUploadinfo != null;
                   // Toast.makeText(getContext(), ""+videoUploadinfo.getVideoURL(), Toast.LENGTH_SHORT).show();
                    list_vid.add(videoUploadinfo);


                }

                adapter_vid = new RecyclerVideoAdapter(getActivity(), list_vid);

                recyclerView_vid.setAdapter(adapter_vid);

                // Hiding the progress dialog.
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Hiding the progress dialog.
                progressDialog.dismiss();

            }
        });
        return root;
    }
}