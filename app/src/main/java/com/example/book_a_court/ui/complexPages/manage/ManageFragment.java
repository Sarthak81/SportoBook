package com.example.book_a_court.ui.complexPages.manage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_a_court.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ManageFragment extends Fragment {
    ManageSportsAdapter manageSportsAdapter;
    ArrayList<Sport> sportArrayList;
    Button addNew;
    RecyclerView manageRecycler;
    FirebaseFirestore db;
    String currentUserId;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_manage, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUserId = user.getUid();
        sportArrayList = new ArrayList<>();
        addNew = root.findViewById(R.id.addNew);
        manageRecycler = root.findViewById(R.id.manageRecycler);
        db = FirebaseFirestore.getInstance();


        //addnew button fn below
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), addSport.class);
                startActivity(intent);
            }
        });

        db.collection("sports").document(currentUserId).collection("court")
        .get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot snapshot : task.getResult()){
                    Sport sport = new Sport(snapshot.getString("court_name"),snapshot.get("court_price").toString(),snapshot.get("court_number").toString());
                    Log.d("newestTag", "onSuccess: " + (snapshot.getString("court_name")+snapshot.get("court_price").toString()+snapshot.get("court_number").toString()));
                    sportArrayList.add(sport);
                }
                manageSportsAdapter.notifyDataSetChanged();
            }
        });

        manageRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        manageSportsAdapter = new ManageSportsAdapter(getContext(), sportArrayList);
        manageRecycler.setAdapter(manageSportsAdapter);

        return root;
    }
}