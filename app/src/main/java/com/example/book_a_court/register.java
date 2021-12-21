package com.example.book_a_court;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;



public class register extends AppCompatActivity {
    LinearLayout linearPer,linearCom;
    Button Complex,Person,regComBtn,regPerBtn;
    public static final String TAG = "TAG";
    EditText mFullName,mEmail,mPassword,mPhone;
    EditText comName,comAddress,comEmail,comPass,comPhone;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;



    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Complex = findViewById(R.id.complex);
        Person = findViewById(R.id.person);
        linearCom = findViewById(R.id.linearcom);
        linearPer = findViewById(R.id.linearper);
        regComBtn = findViewById(R.id.regcombtn);
        regPerBtn = findViewById(R.id.regperbtn);

        comName = findViewById(R.id.comName);
        comAddress = findViewById(R.id.comAddress);
        comEmail = findViewById(R.id.comEmail);
        comPass = findViewById(R.id.comPassword);
        comPhone = findViewById(R.id.comPhone);

        mFullName   = findViewById(R.id.perName);
        mEmail      = findViewById(R.id.perEmail);
        mPassword   = findViewById(R.id.perPass);
        mPhone      = findViewById(R.id.perPhone);

        linearPer.setVisibility(View.INVISIBLE);
        regPerBtn.setVisibility(View.INVISIBLE);
//        Complex.setBackgroundColor(android.R.color.transparent);
        Person.setBackgroundColor(android.R.color.transparent);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        Complex.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @SuppressLint({"ResourceAsColor", "UseCompatLoadingForDrawables"})
            @Override
            public void onClick(View v) {
                Complex.setBackground(getResources().getDrawable(R.drawable.item_button_back));
                Person.setBackgroundColor(android.R.color.transparent);
                regPerBtn.setVisibility(View.INVISIBLE);
                linearPer.setVisibility(View.INVISIBLE);
                linearCom.setVisibility(View.VISIBLE);
                regComBtn.setVisibility(View.VISIBLE);
            }
        });
        Person.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                Person.setBackground(getResources().getDrawable(R.drawable.item_button_back));
                Complex.setBackgroundColor(android.R.color.transparent);
                regComBtn.setVisibility(View.INVISIBLE);
                linearCom.setVisibility(View.INVISIBLE);
                linearPer.setVisibility(View.VISIBLE);
                regPerBtn.setVisibility(View.VISIBLE);
            }
        });



        regComBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = comEmail.getText().toString().trim();
                String password = comPass.getText().toString().trim();
                String fullName = comName.getText().toString();
                String phone    = comPhone.getText().toString();
                String address = comAddress.getText().toString();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }
                if(TextUtils.isEmpty(phone)){
                    mEmail.setError("Phone no. is Required.");
                    return;
                }
                if(TextUtils.isEmpty(fullName)){
                    mEmail.setError("Complex Name is Required.");
                    return;
                }
                if(TextUtils.isEmpty(address)){
                    mEmail.setError("Address is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("Password Must be >= 6 Characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // register the user in firebase

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            // send verification link

                            FirebaseUser fuser = fAuth.getCurrentUser();
                            assert fuser != null;
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(register.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                                }
                            });

                            Toast.makeText(register.this, "User Created.", Toast.LENGTH_SHORT).show();
                            userID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("fName",fullName);
                            user.put("email",email);
                            user.put("phone",phone);
                            user.put("address",address);
                            user.put("IsAdmin","1");
                           // user.put("url","");


                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });
                            progressBar.setVisibility(View.GONE);
                          startActivity(new Intent(getApplicationContext(),MainActivity.class));

                        }else {
                            Toast.makeText(register.this, "Error ! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
                if(fAuth.getCurrentUser() != null){
                    startActivity(new Intent(getApplicationContext(),login.class));
                    finish();
                }
            }
        });

        regPerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String email = mEmail.getText().toString().trim();
                  String password = mPassword.getText().toString().trim();
                 String fullName = mFullName.getText().toString();
                 String phone    = mPhone.getText().toString();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("Password Must be >= 6 Characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // register the user in firebase

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            // send verification link

                            FirebaseUser fuser = fAuth.getCurrentUser();
                            assert fuser != null;
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(register.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                                }
                            });


                            Toast.makeText(register.this, "User Created.", Toast.LENGTH_SHORT).show();
                            userID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("fName",fullName);
                            user.put("email",email);
                            user.put("phone",phone);
                            user.put("IsUser","1");
                            //user.put("url","");

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });
//                            startActivity(new Intent(getApplicationContext(),MainActivity.class));

                        }else {
                            Toast.makeText(register.this, "Error ! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
                if(fAuth.getCurrentUser() != null){
                    startActivity(new Intent(getApplicationContext(),login.class));
                    finish();
                }
            }
        });




    }
}