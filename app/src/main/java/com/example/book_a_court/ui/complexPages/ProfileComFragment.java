package com.example.book_a_court.ui.complexPages;

import android.content.Intent;
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

import com.example.book_a_court.Profile;
import com.example.book_a_court.R;

public class ProfileComFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_profile, container, false);
        Intent intent = new Intent(getContext(), Profile.class);
        startActivity(intent);
        return root;
    }
}