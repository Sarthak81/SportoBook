package com.example.book_a_court.ui.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_a_court.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ChatFragment extends Fragment {
    RecyclerView recyclerView;
    ChatUserAdapter chatUserAdapter;
    FirebaseDatabase database;
    FirebaseFirestore fStore;
    FirebaseAuth auth;
    ArrayList<String> userNames ;
    String[] nameString = {"Raj","Rahul","Anjali","Anonymous","Aparichit","Dhoni","Kohli","Rohit","Mahi","Bumrah","Bhuvi"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_chat, container, false);
        userNames = new ArrayList<>();
        for(String name : nameString){
            userNames.add(name);
        }
        recyclerView = root.findViewById(R.id.chatRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatUserAdapter = new ChatUserAdapter(getContext(),userNames);
        recyclerView.setAdapter(chatUserAdapter);

        return root;
    }

}