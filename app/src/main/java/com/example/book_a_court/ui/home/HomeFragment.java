package com.example.book_a_court.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
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
    ArrayList<Users> complexes ;
    EditText search;
    ImageButton searchbtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        complexes = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();

        search = root.findViewById(R.id.Search);
        searchbtn = root.findViewById(R.id.searchbtn);

        fStore.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                    if(document.getString("IsAdmin") != null) {
                                        Log.d("Mytag", document.getId() + " => " + document.getId() + document.getString("fName") + document.getString("email") + document.getString("phone"));
                                        Users user = new Users(document.getId(), document.getString("fName"),
                                                document.getString("email"), document.getString("phone"),document.getString("url"));
                                        complexes.add(user);
//                                      Log.d("user", "onComplete: "+user.getName());
                                    }
                                    complexListAdapter.notifyDataSetChanged();
                            }

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

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
                                            Log.d("Mytag", document.getId() + " => " + document.getId() + document.getString("fName") + document.getString("email") + document.getString("phone"));
                                            Users user = new Users(document.getId(), document.getString("fName"),
                                                    document.getString("email"), document.getString("phone"),document.getString("url"));
                                            complexes.add(user);
//                                      Log.d("user", "onComplete: "+user.getName());
                                        }
                                        complexListAdapter.notifyDataSetChanged();
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