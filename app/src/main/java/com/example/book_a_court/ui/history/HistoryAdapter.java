package com.example.book_a_court.ui.history;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_a_court.R;
import com.example.book_a_court.ui.complexPages.manage.Sport;
import com.example.book_a_court.ui.home.BookingData;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    Context contextHere;
    ArrayList<BookingData> data;

    public HistoryAdapter(Context context, ArrayList<BookingData> data) {
        this.contextHere=context;
        this.data = data;
//        Toast.makeText(contextHere, "contructor called ", Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contextHere).inflate(R.layout.item_userhistory,parent,false);
//        Toast.makeText(contextHere, "oncreate called", Toast.LENGTH_SHORT).show();
        return new HistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookingData bookingData = data.get(position);
////        Log.d("chatTag", "onBindViewHolder: "+user.getName());
        holder.sportName.setText(bookingData.getSportName());
        holder.complexName.setText(bookingData.getName().toUpperCase());
        holder.date.setText(bookingData.getDate() +" at "+bookingData.getTime());
        holder.amount.setText("\u20B9"+bookingData.getFare());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView complexName,sportName,amount,date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sportName = itemView.findViewById(R.id.history_sport_name);
            complexName = itemView.findViewById(R.id.complex_name);
            date = itemView.findViewById(R.id.history_date);
            amount = itemView.findViewById(R.id.history_amount);
        }
    }
}

