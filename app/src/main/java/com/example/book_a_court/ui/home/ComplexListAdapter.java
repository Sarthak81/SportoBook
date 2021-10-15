package com.example.book_a_court.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
//        holder.user_name.setText(user.getName());

    }

    @Override
    public int getItemCount() {
        return complexList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
//        CircleImageView circleImageView;
//        TextView user_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            circleImageView = itemView.findViewById(R.id.chatpage_image);
//            user_name = itemView.findViewById(R.id.chat_name);
        }
    }
}