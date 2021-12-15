package com.example.book_a_court.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_a_court.R;
import com.example.book_a_court.ui.chat.ChatUserAdapter;
import com.example.book_a_court.ui.chat.Messages;
import com.example.book_a_court.ui.chat.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView complexList;
    ComplexListAdapter complexListAdapter;
    FirebaseDatabase database;
    FirebaseFirestore fStore;
    FirebaseAuth auth;
    ArrayList<complexUsers> complexes ;
    EditText search;
    ImageButton searchbtn;
    Spinner ratingDropdown;
    String[] sortArr;
    ArrayAdapter<String> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        complexes = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();

        search = root.findViewById(R.id.Search);
        searchbtn = root.findViewById(R.id.searchbtn);
        ratingDropdown = root.findViewById(R.id.spinnerRating);
        sortArr = new String[]{"--Select--", "Rating", "Distance"};
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,sortArr);
        ratingDropdown.setAdapter(adapter);
        ratingDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    fStore.collection("users")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            if(document.getString("IsAdmin") != null) {
                                                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                                DatabaseReference mRatingBarCh = rootRef.child("ratings").child(document.getId());

                                                mRatingBarCh.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        float total=0,num=0;
                                                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                                            total = total+Float.parseFloat(postSnapshot.getValue().toString());
                                                            num++;
                                                        }
                                                        float avgrating= total/num;
                                                        complexUsers user = new complexUsers(document.getId(), document.getString("fName"),
                                                                document.getString("email"), document.getString("phone"),document.getString("url"),avgrating);
                                                        complexes.add(user);
                                                        complexListAdapter.notifyDataSetChanged();
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                                Log.d("Mytag", document.getId() + " => " + document.getId() + document.getString("fName") + document.getString("email") + document.getString("phone"));

//                                      Log.d("user", "onComplete: "+user.getName());
                                            }
                                        }

                                    } else {
                                        Log.d("TAG", "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                }
                if(position==1){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        fStore.collection("users")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                    if(document.getString("IsAdmin") != null) {
//                                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
//                                        DatabaseReference mRatingBarCh = rootRef.child("ratings").child(document.getId());
//
//                                        mRatingBarCh.addValueEventListener(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                float total=0,num=0;
//                                                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//                                                    total = total+Float.parseFloat(postSnapshot.getValue().toString());
//                                                    num++;
//                                                }
//                                                float avgrating= total/num;
//                                                complexUsers user = new complexUsers(document.getId(), document.getString("fName"),
//                                                        document.getString("email"), document.getString("phone"),document.getString("url"),avgrating);
//                                                complexes.add(user);
//                                                complexListAdapter.notifyDataSetChanged();
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError error) {
//
//                                            }
//                                        });
//                                        Log.d("Mytag", document.getId() + " => " + document.getId() + document.getString("fName") + document.getString("email") + document.getString("phone"));
//
////                                      Log.d("user", "onComplete: "+user.getName());
//                                    }
//                            }
//
//                        } else {
//                            Log.d("TAG", "Error getting documents: ", task.getException());
//                        }
//                    }
//                });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = search.getText().toString();
                complexes.clear();
                fStore.collection("users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if(document.getString("IsAdmin") != null && document.getString("fName").equals(s)) {
                                            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                            DatabaseReference mRatingBarCh = rootRef.child("ratings").child(document.getId());

                                            mRatingBarCh.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    float total=0,num=0;
                                                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                                        total = total+Float.parseFloat(postSnapshot.getValue().toString());
                                                        num++;
                                                    }
                                                    float avgrating= total/num;
                                                    complexUsers user = new complexUsers(document.getId(), document.getString("fName"),
                                                            document.getString("email"), document.getString("phone"),document.getString("url"),avgrating);
                                                    complexes.add(user);
                                                    complexListAdapter.notifyDataSetChanged();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                            Log.d("Mytag", document.getId() + " => " + document.getId() + document.getString("fName") + document.getString("email") + document.getString("phone"));

//                                      Log.d("user", "onComplete: "+user.getName());
                                        }
                                    }

                                } else {
                                    Log.d("TAG", "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });

        complexList = root.findViewById(R.id.complexList);
        complexList.setLayoutManager(new LinearLayoutManager(getContext()));
        complexListAdapter = new ComplexListAdapter(getContext(),complexes);
        complexList.setAdapter(complexListAdapter);

        return root;
    }
}