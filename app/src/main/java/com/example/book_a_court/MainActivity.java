package com.example.book_a_court;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button login,reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.Login);
        reg = findViewById(R.id.Register);
    }

    public void login(View view){
        //intent to open login page
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }

    public void register(View view){
        //intent to open login page
        Intent intent = new Intent(this, register.class);
        startActivity(intent);
    }

}