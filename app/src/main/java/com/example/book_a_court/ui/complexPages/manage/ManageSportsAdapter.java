package com.example.book_a_court.ui.complexPages.manage;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_a_court.R;
import com.example.book_a_court.ui.chat.Users;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ManageSportsAdapter extends RecyclerView.Adapter<ManageSportsAdapter.ViewHolder> {
    Context contextHere;
    ArrayList<Sport> sports;
    public ManageSportsAdapter(Context context, ArrayList<Sport> sports) {
        this.contextHere=context;
        this.sports = sports;
//        Toast.makeText(contextHere, "contructor called ", Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contextHere).inflate(R.layout.item_manage_sport,parent,false);
//        Toast.makeText(contextHere, "oncreate called", Toast.LENGTH_SHORT).show();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sport sport = sports.get(position);
////        Log.d("chatTag", "onBindViewHolder: "+user.getName());
        holder.sportName.setText("Sport Name : "+ sport.getName());
        holder.price.setText("Hourly Price : " + sport.getPrice());
        holder.numOfCourts.setText("Number of Courts : " + sport.getNum_of_courts());
    }

    @Override
    public int getItemCount() {
        return sports.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView sportName,numOfCourts,price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sportName = itemView.findViewById(R.id.sport_name);
            numOfCourts = itemView.findViewById(R.id.numOfCourts);
            price = itemView.findViewById(R.id.sportPrice);
        }
    }
}
