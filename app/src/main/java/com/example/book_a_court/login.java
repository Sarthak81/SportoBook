package com.example.book_a_court;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class login extends AppCompatActivity {
    EditText mEmail, mPassword;
    CardView mLoginBtn;
    Button forgotTextLink, complex, person, googleSign, googleOut;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    GoogleSignInClient mGoogleSignInClient;
    public static final String TAG = "TAG";
    private final static int RC_SIGN_IN = 123;
    String userID;
    String Email_P,Name,Phone;
    int exist=0;
    int activeUser = 0;     // 0 - complex owner    and   1-person

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user =fAuth.getCurrentUser();
        if(user!=null){
            Intent intent =new Intent(getApplicationContext(),Profile.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmail = findViewById(R.id.loginMail);
        mPassword = findViewById(R.id.loginPass);
       googleSign = findViewById(R.id.googleSign);
//       googleOut = findViewById(R.id.googleOut);
        mLoginBtn = findViewById(R.id.cardView);
        complex = findViewById(R.id.complex);
        person = findViewById(R.id.person);
        forgotTextLink = findViewById(R.id.button);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar2);

        person.setOnClickListener(new View.OnClickListener()
        {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick (View v){
            activeUser = 1;
            person.setBackgroundColor(android.R.color.transparent);
            complex.setBackgroundColor(Color.parseColor("#3700B3"));
        }
        });
        complex.setOnClickListener(new View.OnClickListener()
        {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick (View v){
            activeUser = 0;
            complex.setBackgroundColor(android.R.color.transparent);
            person.setBackgroundColor(Color.parseColor("#3700B3"));
        }
        });


        createRequest();
        googleSign.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                signIn();
            }
        });


        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();


                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Required.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is Required.");
                    return;
                }

                if (password.length() < 6) {
                    mPassword.setError("Password Must be >= 6 Characters");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                if (activeUser == 0) {            // for complex owner authentication
                    fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener< AuthResult >() {
                        @Override
                        public void onComplete(@NonNull Task< AuthResult > task) {
                            if (task.isSuccessful()) {
                                //Toast.makeText(login.this, "Complex Logged in Successfully", Toast.LENGTH_SHORT).show();
                                checkAccessLevel(Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).getUid());

                                //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            } else {
                                Toast.makeText(login.this, "Error ! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }

                        }
                    });
//                    Toast.makeText(login.this, "Complex Logged in", Toast.LENGTH_SHORT).show();
                } else if (activeUser == 1) {       // for person authentication
                    fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener< AuthResult >() {
                        @Override
                        public void onComplete(@NonNull Task< AuthResult > task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(login.this, "Person Logged in Successfully", Toast.LENGTH_SHORT).show();
                                checkAccessLevel(Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).getUid());
                                //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            } else {
                                Toast.makeText(login.this, "Error ! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }

                        }
                    });
                }
            }
        });
        forgotTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your Email To Received Reset Link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener< Void >() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(login.this, "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(login.this, "Error ! Reset Link is Not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });

                passwordResetDialog.create().show();

            }
        });


    }



    private void checkAccessLevel(String uid) {
        DocumentReference df = fStore.collection("users").document(uid);

        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                Log.d("TAG", "OnSuccess:" + documentSnapshot.getData());
                if(activeUser==0) {
                    if (documentSnapshot.getString("IsAdmin") != null) {
                        startActivity(new Intent(getApplicationContext(), navCom.class));
                        finish();
                    }
                    else
                        Toast.makeText(login.this, "Not Authenticated User", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                }if(activeUser==1) {
                    if (documentSnapshot.getString("IsUser") != null) {
                        //Toast.makeText(getApplicationContext(), "id exists", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), navPer.class));

                        finish();
                        progressBar.setVisibility(View.GONE);
                    }
                    else
                        Toast.makeText(login.this, "Not Authenticated User", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                }

            }


        });
    }


    private void createRequest(){

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        progressBar.setVisibility(View.GONE);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                // Google Sign In failed, update UI appropriately
                //Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        fAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser fuser = fAuth.getCurrentUser();

                            userID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
                            Email_P = Objects.requireNonNull(fAuth.getCurrentUser()).getEmail().trim();
                            //Phone=Objects.requireNonNull(fAuth.getCurrentUser()).getPhoneNumber();
                            Name=Objects.requireNonNull(fAuth.getCurrentUser()).getDisplayName();



                            checkEmailExistsOrNot(Email_P);

                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(login.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
   void checkEmailExistsOrNot(String email) {
       DocumentReference documentReference = fStore.collection("users").document(userID);
       fAuth.fetchSignInMethodsForEmail(email)
               .addOnCompleteListener(new OnCompleteListener< SignInMethodQueryResult >() {
                   @Override
                   public void onComplete(@NonNull Task< SignInMethodQueryResult > task) {
                       if (task.isSuccessful()) {
                           boolean check = !task.getResult().getSignInMethods().isEmpty();
                           if (!check) {
                               if(activeUser==1){
                                   Map<String,Object> user = new HashMap<>();
                                   user.put("email",Email_P);
                                   //user.put("phone",Phone);
                                   user.put("fName",Name);
                                   user.put("IsUser","1");
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

                                   Intent intent =new Intent(getApplicationContext(),navPer.class);
                                   startActivity(intent);}

                               else {
                                   Map<String,Object> user = new HashMap<>();
                                   user.put("email",Email_P);
                                   //user.put("phone",Phone);
                                   user.put("fName",Name);
                                   user.put("IsAdmin","1");
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

                                   Intent intent =new Intent(getApplicationContext(),navCom.class);
                                   startActivity(intent);

                               }


                           } else {
                               checkAccessLevel(userID);
                              // Toast.makeText(getApplicationContext(), "email already exist", Toast.LENGTH_LONG).show();
//                               Intent intent =new Intent(getApplicationContext(),MainActivity.class);
//                               startActivity(intent);
                           }
                       }
                   }
               });
   }

}

