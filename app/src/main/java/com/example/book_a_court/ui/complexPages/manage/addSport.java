package com.example.book_a_court.ui.complexPages.manage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.book_a_court.Profile;
import com.example.book_a_court.R;
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
    String currentUserId;
    Button save_court;
    public static int i_d=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sport);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;

        currentUserId = user.getUid();
        documentReference = db.collection("sports").document(currentUserId).collection("court").document("court "+(++i_d));

        court_name=findViewById(R.id.edit_name_sport);
        court_no=findViewById(R.id.edit_number_sport);
        court_price=findViewById(R.id.edit_price_sport);
        save_court=findViewById(R.id.court_save);

        String name= court_name.getText().toString();
        String price= court_price.getText().toString();
        String number= court_no.getText().toString();

        save_court.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(name.length()>0 && price.length()>0 && number.length()>0){
                    Map< String, Object > court_details = new HashMap<>();
                    court_details.put("court_name",name);
                    court_details.put("court_price",price);
                    court_details.put("court_number",number);

                    documentReference.update(court_details).addOnSuccessListener(new OnSuccessListener< Void >() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(addSport.this, "Court Successfully Created", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(addSport.this, "Couldnt Create New Court", Toast.LENGTH_SHORT).show();


                        }
                    });
                }
            }
        });




    }
}