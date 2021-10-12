package com.example.book_a_court.ui.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_a_court.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatUserAdapter extends RecyclerView.Adapter<ChatUserAdapter.ViewHolder> {
    Context contextHere;
    ArrayList<String> userNames;
    public ChatUserAdapter(Context context, ArrayList<String> userNames) {
        this.contextHere=context;
        this.userNames = userNames;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contextHere).inflate(R.layout.item_chat,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String names = userNames.get(position);
        holder.user_name.setText(names);
    }

    @Override
    public int getItemCount() {
        return userNames.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView user_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.chat_image);
            user_name = itemView.findViewById(R.id.chat_name);
        }
    }
}
