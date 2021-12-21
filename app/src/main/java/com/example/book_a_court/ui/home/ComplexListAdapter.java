package com.example.book_a_court.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_a_court.R;
import com.example.book_a_court.ui.chat.ChatUserAdapter;
import com.example.book_a_court.ui.chat.Users;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class ComplexListAdapter extends RecyclerView.Adapter<ComplexListAdapter.ViewHolder> implements Filterable{
    Context contextHere;
    ArrayList<complexUsers> complexList;
    private ArrayList<complexUsers> exampleList;
    private ArrayList<complexUsers> filteredNameList;
    private ArrayList<complexUsers> fullList;


    public Filter mFilter;



    public ComplexListAdapter(Context contextHere, ArrayList<complexUsers> complexList) {
        this.contextHere = contextHere;
        this.complexList = complexList;
       this.filteredNameList= complexList;
       this.fullList=complexList;
        //filteredNameList = new ArrayList<>(complexList);
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
        complexUsers user = complexList.get(position);
//        Log.d("chatTag", "onBindViewHolder: "+user.getName());
        holder.ratingBar.setRating(user.getRating());
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



    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                complexList=fullList;
                //filteredNameList = new ArrayList<>();
                ArrayList<complexUsers> filteredList = new ArrayList<>();
                String charSequenceString = constraint.toString();
                if (charSequenceString.isEmpty()) {
                    filteredList.addAll(complexList);
                } else {

                 //   ArrayList<complexUsers> filteredList = new ArrayList<>();
                    for (complexUsers complexUsers : complexList) {
                        if (complexUsers.getName().toLowerCase().contains(charSequenceString.toLowerCase())) {
                            Toast.makeText(contextHere, ""+complexUsers.getEmail(), Toast.LENGTH_SHORT).show();
                            filteredList.add(complexUsers);
                            //Toast.makeText(contextHere, "ncfsndfn"+complexList.get(0).getName(), Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                //complexList.clear();
               // filteredNameList.clear();
               // exampleList.addAll((List<complexUsers >)results.values)
                //complexList=((ArrayList) results.values);
                complexList = (ArrayList<complexUsers>) results.values;
                //Toast.makeText(contextHere, "hioo"+exampleList.get(0).getName(), Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        };
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
