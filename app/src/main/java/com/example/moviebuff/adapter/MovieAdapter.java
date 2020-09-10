package com.example.moviebuff.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviebuff.DetailsActivity;
import com.example.moviebuff.R;
import com.example.moviebuff.model.Movie;

import java.text.BreakIterator;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {
    private Context mContext;
    private List<Movie> movieList;

    public MovieAdapter(Context mContext, List<Movie> movieList) {
        this.mContext = mContext;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MyViewHolder holder, int position) {
        holder.title.setText(movieList.get(position).getOriginalTitle());
        String vote=Double.toString(movieList.get(position).getVoteAverage());
        holder.userrating.setText(vote);

        Glide.with(mContext)
                .load(movieList.get(position).getPosterPath())
                .placeholder(R.drawable.loading)
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{
        public TextView title;
        public TextView userrating;
        private ImageView thumbnail;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.title);
            userrating=(TextView)itemView.findViewById(R.id.userrating);
            thumbnail=(ImageView)itemView.findViewById(R.id.thumbnail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=getAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION){
                        Movie clickedDataItem = movieList.get(pos);
                        Intent intent = new Intent(mContext, DetailsActivity.class);
                        intent.putExtra("original_title",movieList.get(pos).getOriginalTitle());
                        intent.putExtra("poster_path",movieList.get(pos).getPosterPath());
                        intent.putExtra("overview",movieList.get(pos).getOverview());
                        intent.putExtra("id",movieList.get(pos).getId());
                        intent.putExtra("vote_average",Double.toString(movieList.get(pos).getVoteAverage()));
                        intent.putExtra("release_date",movieList.get(pos).getReleaseDate());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        Toast.makeText(v.getContext(),"You Clicked "+clickedDataItem.getOriginalTitle(),Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }
}
