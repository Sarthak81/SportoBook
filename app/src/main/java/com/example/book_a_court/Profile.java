package com.example.book_a_court;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Profile extends AppCompatActivity {

    Button logout, save;
    ImageView image,bbtn;
    Uri imageUri, getImageUri;
    UploadTask uploadTask;
    StorageReference storageReference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    private static final int PICK_IMAGE = 1;
    String currentUserId;
    //FirebaseAuth fAuth=FirebaseAuth.getInstance();
    public static final String TAG = "TAG";
    TextView textName,textEmail,textPhone,editName,editEmail,editPhone,doneName,donePhone;
    EditText editPerName,editPerEmail,editPerPhone;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        logout = findViewById(R.id.logout);
        image = findViewById(R.id.pp);
        save = findViewById(R.id.ppedit);
        textName = findViewById(R.id.textName);
        textEmail = findViewById(R.id.textEmail);
textPhone=findViewById(R.id.textPhone);
        //editEmail = findViewById(R.id.editEmail);
        editPhone = findViewById(R.id.editPhone);
        editName = findViewById(R.id.editName);
        doneName = findViewById(R.id.doneNAME);
        donePhone =findViewById(R.id.donePhone);

        editPerName = findViewById(R.id.editPerName);
        editPerEmail = findViewById(R.id.editPerEmail);
        editPerPhone = findViewById(R.id.editPerPhone);
    bbtn=findViewById(R.id.userProfBack);
        //textEmail.setText("hello");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;

        currentUserId = user.getUid();
        documentReference = db.collection("users").document(currentUserId);
        //getImageUri=db.collection("users").document()
        storageReference = FirebaseStorage.getInstance().getReference("User Pps");
        // databaseReference = database.getReference("users");

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        bbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), navPer.class);
                startActivity(intent);
            }
        });

//        editEmail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                textEmail.setVisibility(View.GONE);
//                editEmail.setVisibility(View.VISIBLE);
//                mail_edit_kardo_bro();
//                editEmail.setVisibility(View.GONE);
//                textEmail.setVisibility(View.VISIBLE);
//
//            }
//        });
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textName.setVisibility(View.GONE);
                editName.setVisibility(View.GONE);
                editPerName.setVisibility(View.VISIBLE);
                 doneName.setVisibility(View.VISIBLE);
//                editPerName.setVisibility(View.GONE);
//                textName.setVisibility(View.VISIBLE);
            }
        });
        doneName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_edit_kardo_bro();
                textName.setVisibility(View.VISIBLE);
                editName.setVisibility(View.VISIBLE);
                editPerName.setVisibility(View.GONE);
                doneName.setVisibility(View.GONE);
//                editPerName.setVisibility(View.GONE);
//                textName.setVisibility(View.VISIBLE);
            }
        });

        editPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textPhone.setVisibility(View.GONE);
                editPhone.setVisibility(View.GONE);
                editPerPhone.setVisibility(View.VISIBLE);
                donePhone.setVisibility(View.VISIBLE);
//                editPerName.setVisibility(View.GONE);
//                textName.setVisibility(View.VISIBLE);
            }
        });
        donePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone_edit_kardo_bro();
                textPhone.setVisibility(View.VISIBLE);
                editPhone.setVisibility(View.VISIBLE);
                editPerPhone.setVisibility(View.GONE);
                donePhone.setVisibility(View.GONE);

            }
        });



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });

//        GoogleSignInAccount signInAccount= GoogleSignIn.getLastSignedInAccount(this);
//        if(signInAccount!=null){
//            name.setText(signInAccount.getDisplayName())
//        }
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //startActivityForResult(intent, PICK_IMAGE);
                CropImage.activity().start(Profile.this);
            }
        });

    }

//    private void mail_edit_kardo_bro() {
//        String email_edited = editEmail.getText().toString().trim();
//        Map<String,Object> edMail = new HashMap<>();
//        edMail.put("fname",email_edited);
//        documentReference.update(edMail).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Toast.makeText(Profile.this, "Email Successfully Changed", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(Profile.this, "Email Change Failed", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE ) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                assert result != null;
                imageUri = result.getUri();
                Picasso.get().load(imageUri).into(image);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error" + e, Toast.LENGTH_SHORT).show();
        }

    }

    private String getFileExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }

    private void uploadData() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("SAVING CHANGES");

        if (imageUri != null) {
            pd.show();
            final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getFileExt(imageUri));
            uploadTask = reference.putFile(imageUri);

            //userID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
            //DocumentReference documentReference = db.collection("users").document(userID);


            Task< Uri > urlTask = uploadTask.continueWithTask(new Continuation< UploadTask.TaskSnapshot, Task< Uri > >() {
                @Override
                public Task< Uri > then(@NonNull Task< UploadTask.TaskSnapshot > task) throws Exception {

                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());
                    } else {
//                       uploadTask.addOnProgressListener(new OnProgressListener< UploadTask.TaskSnapshot >() {
//                           @Override
//                           public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
//                               double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                              pd.setMessage("Uploaded "+progress+"%");
//                           }
//                       });

                        return reference.getDownloadUrl();

                    }

                }


            }).addOnCompleteListener(new OnCompleteListener< Uri >() {
                @Override
                public void onComplete(@NonNull Task< Uri > task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        //Toast.makeText(Profile.this, "" + downloadUri.toString(), Toast.LENGTH_SHORT).show();
                        Map< String, Object > profile = new HashMap<>();
                        profile.put("url", downloadUri.toString());
                       // profile.put("fname", p_e);
                        // databaseReference.child(currentUserId).setValue(image);
                        documentReference.update(profile)
                                .addOnSuccessListener(new OnSuccessListener< Void >() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: user Profile Update for user " + currentUserId);
                                        pd.dismiss();
                                        Handler handler = new Handler();
                                        //Toast.makeText(Profile.this, "" + downloadUri.toString(), Toast.LENGTH_SHORT).show();

                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(Profile.this, Profile.class);
                                                startActivity(intent);

                                            }
                                        }, 2000);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: " + e.toString());
                            }
                        });


                    }

                }
            });

        }

        else {
            Toast.makeText(this, "Image  not selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        documentReference.addSnapshotListener(new EventListener< DocumentSnapshot >() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {


                assert snapshot != null;
                textPhone.setText(Objects.requireNonNull(snapshot.get("phone")).toString());
                textName.setText(snapshot.getString("fname"));
                //if(textEmail.setText("")!=null;)
                textEmail.setText(snapshot.getString("email"));

//                Toast.makeText(Profile.this, ""+snapshot.get("url"), Toast.LENGTH_SHORT).show();
                //
//                try {
//                    Glide.with(getApplicationContext()).load((Objects.requireNonNull(snapshot.get("url"))).toString()).into(image);
//                }
//                catch (Exception e){
//                    Toast.makeText(Profile.this, "No image", Toast.LENGTH_SHORT).show();
//                }

           }

//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
        });


    }
    private void phone_edit_kardo_bro() {
        String phone_edited = editPerPhone.getText().toString().trim();
        if(phone_edited.length() > 0){
        Map< String, Object > edPhone = new HashMap<>();
        edPhone.put("phone", phone_edited);
        documentReference.update(edPhone).addOnSuccessListener(new OnSuccessListener< Void >() {
            @Override
            public void onSuccess(Void aVoid) {

                textPhone.setText(phone_edited);
                Toast.makeText(Profile.this, "Phone no Successfully Changed", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Profile.this, " Phone no Change Failed", Toast.LENGTH_SHORT).show();


            }
        });

    }
        else{
            Toast.makeText(Profile.this, "Please enter valid phone number", Toast.LENGTH_SHORT).show();
            textPhone.setText(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber());

        }
    }
    private void name_edit_kardo_bro() {
        String name_edited = editPerName.getText().toString().trim();
        if (name_edited.length() > 0){
            Map< String, Object > edMail = new HashMap<>();

        edMail.put("fname", name_edited);
        documentReference.update(edMail).addOnSuccessListener(new OnSuccessListener< Void >() {
            @Override
            public void onSuccess(Void aVoid) {

                    textName.setText(name_edited);
                    Toast.makeText(Profile.this, "Name Successfully Changed", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Profile.this, "Name Change Failed", Toast.LENGTH_SHORT).show();

            }
        });
    }
        else{
            Toast.makeText(Profile.this, "Please Enter Valid Name", Toast.LENGTH_SHORT).show();
            textName.setText(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName());
        }
    }
}
