package com.example.book_a_court.ui.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.book_a_court.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatUserAdapter extends RecyclerView.Adapter<ChatUserAdapter.ViewHolder> {
    Context contextHere;
    ArrayList<Users> userNames;
    public ChatUserAdapter(Context context, ArrayList<Users> userNames) {
        this.contextHere=context;
        this.userNames = userNames;
//        Toast.makeText(contextHere, "contructor called ", Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contextHere).inflate(R.layout.item_chat,parent,false);
//        Toast.makeText(contextHere, "oncreate called", Toast.LENGTH_SHORT).show();
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users user = userNames.get(position);
//        Log.d("chatTag", "onBindViewHolder: "+user.getName());
        holder.user_name.setText(user.getName());
        int nightModeFlags = contextHere.getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                holder.user_name.setTextColor(R.color.black);
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                holder.user_name.setTextColor(R.color.white);
                break;
        }
        if(user.uri!=null){
            Picasso.get().load(user.getUri()).into(holder.circleImageView);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(contextHere,ChatActivity.class);
                intent.putExtra("name",user.name);
                intent.putExtra("uid",user.getUid());
                intent.putExtra("ReceiverImage", user.getUri());
                contextHere.startActivity(intent);
            }
        });
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
            circleImageView = itemView.findViewById(R.id.chatpage_image);
            user_name = itemView.findViewById(R.id.chat_name);
        }
    }
}
