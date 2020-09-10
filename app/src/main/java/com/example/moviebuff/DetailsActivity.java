package com.example.moviebuff;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviebuff.adapter.ReviewsAdapter;
import com.example.moviebuff.adapter.TrailerAdapter;
import com.example.moviebuff.api.Client;
import com.example.moviebuff.api.Service;
import com.example.moviebuff.model.Reviews;
import com.example.moviebuff.model.ReviewsResponse;
import com.example.moviebuff.model.Trailer;
import com.example.moviebuff.model.TrailerResponse;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {
    TextView nameofMovie,plotSynopsis,userrating,releaseDate;
    ImageView imageView;
    private RecyclerView trailerRecyclerView,reviewsRecyclerView;
    private TrailerAdapter trailerAdapter;
    private ReviewsAdapter reviewsAdapter;
    private List<Trailer> trailerList;
    private List<Reviews> reviewsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initCollapsingToolbar();

        imageView= (ImageView)findViewById(R.id.thumbnail_image_header);
        nameofMovie=(TextView)findViewById(R.id.title);
        plotSynopsis=(TextView)findViewById(R.id.plotsynopsis);
        userrating=(TextView)findViewById(R.id.userrating);
        releaseDate=(TextView)findViewById(R.id.releasedate);

        Intent intetget=getIntent();
        if(intetget.hasExtra("original_title")){
            String  thumbnail=getIntent().getExtras().getString("poster_path");
            String  movieName=getIntent().getExtras().getString("original_title");
            String  synopsis=getIntent().getExtras().getString("overview");
            String  rating=getIntent().getExtras().getString("vote_average");
            String  dateofRelease=getIntent().getExtras().getString("release_date");

            Glide.with(this)
                    .load(thumbnail)
                    .placeholder(R.drawable.loading)
                    .into(imageView);
            nameofMovie.setText(movieName);
            plotSynopsis.setText(synopsis);
            userrating.setText(rating);
            releaseDate.setText(dateofRelease);
        }else{
            Toast.makeText(this,"No API DATA",Toast.LENGTH_SHORT).show();
        }

        initView();

    }

    private void initCollapsingToolbar(){
        final CollapsingToolbarLayout collapsingToolbarLayout=
                (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");
        AppBarLayout appBarLayout=(AppBarLayout)findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow=false;
            int scrollRange=-1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(scrollRange==-1){
                    scrollRange=appBarLayout.getTotalScrollRange();
                }
                if(scrollRange+verticalOffset==0){
                    collapsingToolbarLayout.setTitle(getString(R.string.MovieDetails));
                    isShow=true;
                }else if(isShow){
                    collapsingToolbarLayout.setTitle(" ");
                    isShow=false;
                }
            }
        });
    }

    private void initView(){

        trailerList= new ArrayList<>();
        trailerAdapter=new TrailerAdapter(this,trailerList);
        reviewsList= new ArrayList<>();
        reviewsAdapter=new ReviewsAdapter(this,reviewsList);

        trailerRecyclerView=(RecyclerView)findViewById(R.id.recyclerViewTrailer);
        reviewsRecyclerView=(RecyclerView)findViewById(R.id.recyclerViewReviews);

        RecyclerView.LayoutManager trailerlayoutManager=new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        trailerRecyclerView.setLayoutManager(trailerlayoutManager);
        trailerRecyclerView.setAdapter(trailerAdapter);
        trailerAdapter.notifyDataSetChanged();

        RecyclerView.LayoutManager reviewslayoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        reviewsRecyclerView.setLayoutManager(reviewslayoutManager);
        reviewsRecyclerView.setAdapter(reviewsAdapter);
        reviewsAdapter.notifyDataSetChanged();

        loadJSON();
    }

    private void loadJSON() {
        int movie_id=getIntent().getExtras().getInt("id");
        try{
            Client client= new Client();
            Service apiService= Client.getClient().create(Service.class);
            Call<TrailerResponse> callTrailer=apiService.getMovieTrailer(movie_id,"eea7a331c01a1e5cfc8354a8d02b4e57");
            callTrailer.enqueue(new Callback<TrailerResponse>() {
                @Override
                public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                    List<Trailer> trailers= response.body().getResults();
                    trailerRecyclerView.setAdapter(new TrailerAdapter(getApplicationContext(),trailers));
                }

                @Override
                public void onFailure(Call<TrailerResponse> call, Throwable t) {
                    Log.d("Error",t.getMessage());
                    Toast.makeText(DetailsActivity.this,"Error fetching data",Toast.LENGTH_SHORT).show();
                }
            });
            Client client1= new Client();
            Service apiService1= Client.getClient().create(Service.class);
            Call<ReviewsResponse> callReviews=apiService1.getMovieReview(movie_id,"eea7a331c01a1e5cfc8354a8d02b4e57");
            callReviews.enqueue(new Callback<ReviewsResponse>() {
                @Override
                public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                    List<Reviews> reviews= response.body().getResults();
                    reviewsRecyclerView.setAdapter(new ReviewsAdapter(getApplicationContext(),reviews));
                }

                @Override
                public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                    Log.d("Error",t.getMessage());
                    Toast.makeText(DetailsActivity.this,"Error fetching data",Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception e){
            Log.d("Error",e.getMessage());
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

}
