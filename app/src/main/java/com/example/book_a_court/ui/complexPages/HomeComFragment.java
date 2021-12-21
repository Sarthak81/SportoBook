package com.example.book_a_court.ui.complexPages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_a_court.R;
import com.example.book_a_court.ui.history.HistoryAdapter;
import com.example.book_a_court.ui.home.BookingData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class HomeComFragment extends Fragment {
    RecyclerView historyRecycler;
    HistoryAdapter historyAdapter;
    FirebaseDatabase db;
    DatabaseReference personReference;
    FirebaseAuth fAuth;
    ArrayList<BookingData> list;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home_com, container, false);
        historyRecycler = root.findViewById(R.id.complex_history_recycler);

        db = FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();
        personReference = db.getReference().child("ComplexBookings").child(Objects.requireNonNull(fAuth.getUid()));
        list = new ArrayList<>();

        personReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BookingData bookingData = snapshot.getValue(BookingData.class);
                list.add(bookingData);
                historyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        historyRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        historyAdapter = new HistoryAdapter(getContext(), list);
        historyRecycler.setAdapter(historyAdapter);
        return root;
    }
}