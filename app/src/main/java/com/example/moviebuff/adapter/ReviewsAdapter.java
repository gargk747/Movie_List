package com.example.moviebuff.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebuff.R;
import com.example.moviebuff.model.Reviews;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MyViewHolder>{

    private Context context;
    private List<Reviews> reviewsList;

    public ReviewsAdapter(Context context, List<Reviews> reviewsList) {
        this.context = context;
        this.reviewsList = reviewsList;
    }

    @NonNull
    @Override
    public ReviewsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_card,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapter.MyViewHolder holder, int position) {
        holder.author.setText(reviewsList.get(position).getAuthor());
        holder.content.setText(reviewsList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView author,content;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            author=(TextView)itemView.findViewById(R.id.author);
            content=(TextView)itemView.findViewById(R.id.content);


        }
    }
}
