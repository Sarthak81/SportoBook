package com.example.book_a_court.ui.complexPages.manage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.book_a_court.R;
import com.example.book_a_court.navCom;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class addSport extends AppCompatActivity {
    EditText court_price,court_name,court_no;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    String currentUserId,name;
    int price,number;
    Button save_court;
    ImageView back_addSport;
    public static int i_d=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sport);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        currentUserId = user.getUid();



        court_name=findViewById(R.id.edit_name_sport);
        court_no=findViewById(R.id.edit_number_sport);
        court_price=findViewById(R.id.edit_price_sport);
        save_court=findViewById(R.id.court_save);
        back_addSport=findViewById(R.id.back_addSp);


        back_addSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(), navCom.class);
                startActivity(intent);
            }
        });


        save_court.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name= court_name.getText().toString();
                try {
                    price= Integer.parseInt(String.valueOf(court_price.getText()));
                }
                catch (NumberFormatException e){
                    Toast.makeText(addSport.this, "Enter Valid Price", Toast.LENGTH_SHORT).show();
                }
                try {
                    number= Integer.parseInt(String.valueOf(court_no.getText()));
                }
                catch (NumberFormatException e){
                    Toast.makeText(addSport.this, "Enter Valid Court No", Toast.LENGTH_SHORT).show();
                }


                documentReference = db.collection("sports").document(currentUserId).collection("court").document("court_"+name);

                if(name.length()>0 && price>=0 && number>=0){


                Map< String, Object > court_details = new HashMap<>();
                    court_details.put("court_name",name);
                    court_details.put("court_price",String.valueOf(price));
                    court_details.put("court_number",String.valueOf(number));


                    documentReference.set(court_details).addOnSuccessListener(new OnSuccessListener< Void >() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(addSport.this, "Court Successfully Created", Toast.LENGTH_SHORT).show();
                            Intent intent =new Intent(getApplicationContext(), navCom.class);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(addSport.this, "Couldn't Create New Court", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    Toast.makeText(addSport.this, "Please Fill Out All Above Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }
}