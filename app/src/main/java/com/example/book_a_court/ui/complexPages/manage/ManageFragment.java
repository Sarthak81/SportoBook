package com.example.book_a_court.ui.complexPages.manage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_a_court.R;
import com.example.book_a_court.ui.complexPages.GalleryFragment;

import java.util.ArrayList;

public class ManageFragment extends Fragment {
    Sport sport;
    ManageSportsAdapter manageSportsAdapter;
    ArrayList<Sport> sportArrayList;
    Button addNew;
    RecyclerView manageRecycler;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_manage, container, false);
        sportArrayList = new ArrayList<>();
        addNew = root.findViewById(R.id.addNew);
        manageRecycler = root.findViewById(R.id.manageRecycler);

        //addnew button fn below
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(),addSport.class );
                startActivity(intent);
            }
        });



        // fetching data from database below





//        manageRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
//        manageSportsAdapter = new ManageSportsAdapter(getContext(),sportArrayList);
//        manageRecycler.setAdapter(manageSportsAdapter);

        return root;
    }
}