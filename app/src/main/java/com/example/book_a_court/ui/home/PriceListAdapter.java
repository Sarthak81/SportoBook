package com.example.book_a_court.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_a_court.R;
import com.example.book_a_court.ui.chat.Users;
import com.example.book_a_court.ui.complexPages.gallery_images.ImageUploadInfo;
import com.example.book_a_court.ui.complexPages.gallery_images.RecyclerViewAdapter;
import com.example.book_a_court.ui.complexPages.manage.Sport;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PriceListAdapter extends RecyclerView.Adapter<PriceListAdapter.ViewHolder> {
    Context contextHere;
    ArrayList<Sport> sportList;

    public PriceListAdapter(Context contextHere, ArrayList<Sport> sportList) {
        this.contextHere = contextHere;
        this.sportList = sportList;
    }

    @NonNull
    @Override
    public PriceListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contextHere).inflate(R.layout.item_price_list,parent,false);
        return new PriceListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PriceListAdapter.ViewHolder holder, int position) {
        Sport sport = sportList.get(position);
        holder.sport_name.setText(sport.getName());
        holder.price.setText(sport.getPrice()+"Rs.");
    }

    @Override
    public int getItemCount() {
        return sportList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView price;
        TextView sport_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.price_list_price);
            sport_name = itemView.findViewById(R.id.price_list_sportName);
        }
    }
}
