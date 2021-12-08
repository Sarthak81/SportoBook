package com.example.book_a_court.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_a_court.R;
import com.example.book_a_court.ui.chat.ChatUserAdapter;
import com.example.book_a_court.ui.chat.Users;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ComplexListAdapter extends RecyclerView.Adapter<ComplexListAdapter.ViewHolder> {
    Context contextHere;
    ArrayList<Users> complexList;

    public ComplexListAdapter(Context contextHere, ArrayList<Users> complexList) {
        this.contextHere = contextHere;
        this.complexList = complexList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contextHere).inflate(R.layout.item_complex_list,parent,false);
//        Toast.makeText(contextHere, "oncreate called", Toast.LENGTH_SHORT).show();
        return new ComplexListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users user = complexList.get(position);
//        Log.d("chatTag", "onBindViewHolder: "+user.getName());
        holder.complex_name.setText(user.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(contextHere,ComplexDesc.class);
                intent.putExtra("cName",user.getName());
                intent.putExtra("uid",user.getUid());
                contextHere.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return complexList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CardView tile;
        RatingBar ratingBar;
        TextView complex_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            tile = itemView.findViewById(R.id.complexTile);
            complex_name = itemView.findViewById(R.id.complex_name);
        }
    }
}
