package com.example.book_a_court.ui.complexPages.manage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_a_court.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class ManageFragment extends Fragment {
    Sport sport;
    ManageSportsAdapter manageSportsAdapter;
    ArrayList<Sport> sportArrayList;
    Button addNew;
    RecyclerView manageRecycler;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    String currentUserId;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_manage, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUserId = user.getUid();
        sportArrayList = new ArrayList<>();
        addNew = root.findViewById(R.id.addNew);
        manageRecycler = root.findViewById(R.id.manageRecycler);
 String sport_name;


        //addnew button fn below
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(),addSport.class );
                startActivity(intent);
            }
        });








//        manageRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
//        manageSportsAdapter = new ManageSportsAdapter(getContext(),sportArrayList);
//        manageRecycler.setAdapter(manageSportsAdapter);

        return root;
    }


    public void  get_data(String sport_name ,ArrayList< Sport > sportArrayList){
        documentReference = db.collection("sports").document(currentUserId).collection("court").document("court_"+sport_name);
        documentReference.addSnapshotListener(new EventListener< DocumentSnapshot >() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {

                assert snapshot != null;
                sportArrayList.set(0,  (Sport)snapshot.get("name"));
                sportArrayList.set(1, (Sport) snapshot.get("price"));
                sportArrayList.set(2, (Sport) snapshot.get("no_of_courts"));

            }

        });


    }
}