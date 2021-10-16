package com.example.book_a_court.ui.chat;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_a_court.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ChatFragment extends Fragment {
    RecyclerView recyclerView;
    ChatUserAdapter chatUserAdapter;
    FirebaseDatabase database;
    FirebaseFirestore fStore;
    FirebaseAuth auth;
    ArrayList<Users> userNames ;
    ArrayList<String> dups,dupr;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_chat, container, false);
        userNames = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();

        DatabaseReference chatReference = database.getReference().child("chats");
        dups = new ArrayList<>();
        dupr = new ArrayList<>();
        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
//                    Log.d("newTag", "onDataChange: new datasnapshot");
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
//                        Log.d("newTag" , "onDataChange: new datasnapshot1");
                        for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                            Messages messages = dataSnapshot2.getValue(Messages.class);
//                            Log.d("newTag", "onDataChange: " + messages.getSenderId());
//                            Log.d("newTag", "onSuccess: "+ (dups.contains(messages.getReceiverId())));
                            if((messages.getSenderId().equals(auth.getUid())) && !dups.contains(messages.getReceiverId())) {
                                DocumentReference df = fStore.collection("users").document(messages.getReceiverId());
                                dups.add(messages.getReceiverId());
                                df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot document) {
                                        Users user = new Users(document.getId(), document.getString("fName"),
                                                document.getString("email"), document.getString("phone"),document.getString("url"));
//                                        Log.d("newTag", document.getId() + " => " + document.getId() + document.getString("fName") + document.getString("email") + document.getString("phone"));
                                        userNames.add(user);
                                        chatUserAdapter.notifyDataSetChanged();
                                    }
                                });
                            }

                            if(messages.getReceiverId().equals(auth.getUid()) && !dupr.contains(messages.getReceiverId())){
                                DocumentReference df = fStore.collection("users").document(messages.getSenderId());
                                dupr.add(messages.getReceiverId());
                                df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot document) {
                                        Users user = new Users(document.getId(), document.getString("fName"),
                                                document.getString("email"), document.getString("phone"),document.getString("url"));
//                                        Log.d("newTag", document.getId() + " => " + document.getId() + document.getString("fName") + document.getString("email") + document.getString("phone"));
                                        userNames.add(user);
                                        chatUserAdapter.notifyDataSetChanged();
                                    }
                                });
                            }

                            break;
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        fStore.collection("users")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("Mytag", document.getId() + " => " + document.getId() + document.getString("fName") + document.getString("email") + document.getString("phone"));
////                                Toast.makeText(getContext(), "Document collected", Toast.LENGTH_SHORT).show();
//                                    Users user = new Users(document.getId(),document.getString("fName"),
//                                            document.getString("email"),document.getString("phone"));
//                                    userNames.add(user);
////                                Log.d("user", "onComplete: "+user.getName());
//                            }
//                            chatUserAdapter.notifyDataSetChanged();
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
        dups.clear();
        dupr.clear();
        recyclerView = root.findViewById(R.id.chatRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatUserAdapter = new ChatUserAdapter(getContext(),userNames);
        recyclerView.setAdapter(chatUserAdapter);

        return root;
    }

}