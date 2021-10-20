package com.example.book_a_court.ui.complexPages.gallery_video;



import android.app.ProgressDialog;

import android.content.ContentResolver;

import android.content.Intent;

import android.net.Uri;

import android.os.Bundle;

import android.view.View;

import android.webkit.MimeTypeMap;

import android.widget.Button;

import android.widget.Toast;
import com.example.book_a_court.R;


import androidx.annotation.NonNull;

import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnFailureListener;

import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.storage.FirebaseStorage;

import com.google.firebase.storage.OnProgressListener;

import com.google.firebase.storage.StorageReference;

import com.google.firebase.storage.UploadTask;



import java.util.HashMap;



public class video_upload extends AppCompatActivity {



    Button uploadv;

    ProgressDialog progressDialog;
    String user_id;
final static String data_path="Videos";

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_upload);



        // initialise layout
       user_id= FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        uploadv = findViewById(R.id.uploadv);

        uploadv.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                // Code for showing progressDialog while uploading

                progressDialog = new ProgressDialog(video_upload.this);

                choosevideo();

            }

        });

    }



    // choose a video from phone storage

    private void choosevideo() {

        Intent intent = new Intent();

        intent.setType("video/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(intent, 5);

    }



    Uri videouri;



    // startActivityForResult is used to receive the result, which is the selected video.

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 5 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            videouri = data.getData();

            progressDialog.setTitle("Uploading...");

            progressDialog.show();

            uploadvideo();

        }

    }



    private String getfiletype(Uri videouri) {

        ContentResolver r = getContentResolver();

        // get the file type ,in this case its mp4

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(r.getType(videouri));

    }



    private void uploadvideo() {

        if (videouri != null) {

            // save the selected video in Firebase storage

            final StorageReference reference = FirebaseStorage.getInstance().getReference("Videos/" + System.currentTimeMillis() + "." + getfiletype(videouri));

            reference.putFile(videouri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override

                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                    while (!uriTask.isSuccessful()) ;

                    // get the link of video

                    String downloadUri = uriTask.getResult().toString();

                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Videos").child(user_id);

                    HashMap<String, String> map = new HashMap<>();

                    map.put("videolink", downloadUri);

                    reference1.setValue(map);
                    Toast.makeText(video_upload.this, ""+downloadUri, Toast.LENGTH_SHORT).show();
                    //ImageUploadInfo imageUploadInfo = new ImageUploadInfo(TempImageName, profileImageUrl.toString());
  videoUploadInfo videoUploadinfo =new videoUploadInfo(downloadUri);
  //videoUploadInfo videoUploadinfo= new videoUploadInfo(set)
                    // Video uploaded successfully
                   String videoUploadId = reference1.push().getKey();
////
////                                    // Adding image upload id s child element into databaseReference.
            reference1.child(videoUploadId).setValue(videoUploadinfo);

                    // Dismiss dialog

                    progressDialog.dismiss();

                    Toast.makeText(video_upload.this, "Video Uploaded!!", Toast.LENGTH_SHORT).show();

                }

            }).addOnFailureListener(new OnFailureListener() {

                @Override

                public void onFailure(@NonNull Exception e) {

                    // Error, Image not uploaded

                    progressDialog.dismiss();

                    Toast.makeText(video_upload.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                // Progress Listener for loading

                // percentage on the dialog box

                @Override

                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    // show the progress bar

                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());

                    progressDialog.setMessage("Uploaded " + (int) progress + "%");

                }

            });

        }

    }
}