package com.example.book_a_court;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class register extends AppCompatActivity {
    LinearLayout linearPer,linearCom;
    Button Complex,Person,Register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Complex = findViewById(R.id.complex);
        Person = findViewById(R.id.person);
        linearCom = findViewById(R.id.linearcom);
        linearPer = findViewById(R.id.linearper);
        Register = findViewById(R.id.Register);
        linearPer.setAlpha(0);

        Complex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearPer.setAlpha(0);
                linearCom.setAlpha(1);
            }
        });
        Person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearCom.setAlpha(0);
                linearPer.setAlpha(1);
            }
        });
    }
}