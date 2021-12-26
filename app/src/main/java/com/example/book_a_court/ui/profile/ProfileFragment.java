package com.example.book_a_court.ui.profile;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.book_a_court.MainActivity;
import com.example.book_a_court.Profile;
import com.example.book_a_court.R;
import com.example.book_a_court.navCom;
import com.example.book_a_court.navPer;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    Button logout, save;
    ImageView image, bbtn;
    Uri imageUri, getImageUri;
    UploadTask uploadTask;
    StorageReference storageReference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    private static final int PICK_IMAGE = 1;
    String currentUserId;
    FirebaseFirestore fStore;
    public static final String TAG = "TAG";
    TextView textName, textEmail, textPhone, editName, editEmail, editPhone, doneName, donePhone;
    EditText editPerName, editPerEmail, editPerPhone;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.activity_profile, container, false);
        logout = (Button) root.findViewById(R.id.logout);
        image = (ImageView) root.findViewById(R.id.pp);
        save = (Button) root.findViewById(R.id.ppedit);
        textName = (TextView) root.findViewById(R.id.textName);
        textEmail = (TextView)root.findViewById(R.id.textEmail);
        textPhone = (TextView)root.findViewById(R.id.textPhone);
        //editEmail = findViewById(R.id.editEmail);
        editPhone = root.findViewById(R.id.editPhone);
        editName = root.findViewById(R.id.editName);
        doneName = root.findViewById(R.id.doneNAME);
        donePhone = root.findViewById(R.id.donePhone);

        editPerName = (EditText) root.findViewById(R.id.editPerName);
//        editPerEmail = findViewById(R.id.editPerEmail);
        editPerPhone = (EditText)root.findViewById(R.id.editPerPhone);
        //textEmail.setText("hello");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
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
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

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
                Intent intent = CropImage.activity()
                        .getIntent(getContext());

                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }

        });



        return root;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE ) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                assert result != null;
                imageUri = result.getUri();
                Picasso.get().load(imageUri).into(image);
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error" + e, Toast.LENGTH_SHORT).show();
        }

    }




    private String getFileExt(Uri uri) {
        ContentResolver contentResolver = requireActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }

    private void uploadData() {
        final ProgressDialog pd = new ProgressDialog(getContext());
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
                                                Intent intent = new Intent(getContext(),navPer.class);
                                                startActivity(intent);

                                            }
                                        }, 500);
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

        } else {
            Toast.makeText(getContext(), "Image  not selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
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
                if (snapshot.get("url") != null)
                    Glide.with(getContext()).load((Objects.requireNonNull(snapshot.get("url"))).toString()).into(image);

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
        if (phone_edited.length() > 0) {
            Map< String, Object > edPhone = new HashMap<>();
            edPhone.put("phone", phone_edited);
            documentReference.update(edPhone).addOnSuccessListener(new OnSuccessListener< Void >() {
                @Override
                public void onSuccess(Void aVoid) {

                    textPhone.setText(phone_edited);
                    Toast.makeText(getContext(), "Phone no Successfully Changed", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), " Phone no Change Failed", Toast.LENGTH_SHORT).show();


                }
            });

        } else {
            Toast.makeText(getContext(), "Please enter valid phone number", Toast.LENGTH_SHORT).show();
            textPhone.setText(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber());

        }
    }

    private void name_edit_kardo_bro() {
        String name_edited = editPerName.getText().toString().trim();
        if (name_edited.length() > 0) {
            Map< String, Object > edMail = new HashMap<>();

            edMail.put("fname", name_edited);
            documentReference.update(edMail).addOnSuccessListener(new OnSuccessListener< Void >() {
                @Override
                public void onSuccess(Void aVoid) {

                    textName.setText(name_edited);
                    Toast.makeText(getContext(), "Name Successfully Changed", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Name Change Failed", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(getContext(), "Please Enter Valid Name", Toast.LENGTH_SHORT).show();
            textName.setText(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName());
        }


        // View root = inflater.inflate(R.layout.activity_profile, container, false);

    }
}