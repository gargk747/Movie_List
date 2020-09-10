package com.example.moviebuff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.moviebuff.adapter.MovieAdapter;
import com.example.moviebuff.api.Client;
import com.example.moviebuff.api.Service;
import com.example.moviebuff.model.Movie;
import com.example.moviebuff.model.MoviesResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private List<Movie> movieList;
    ProgressDialog pd;
    public static final String LOG_TAG= MovieAdapter.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    public Activity getActivity(){
        Context context=this;
        while(context instanceof ContextWrapper){
            if(context instanceof Activity){
                return (Activity)context;
            }
            context= ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }

    private void initView() {
        pd=new ProgressDialog(this);
        pd.setMessage("Fetching Movies...");
        pd.setCancelable(false);
        pd.show();

        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);

        movieList= new ArrayList<>();
        adapter= new MovieAdapter(this,movieList);

        if(getActivity().getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        }else{
            recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        loadJSON();
    }

    private void loadJSON() {

        try {
            Client client= new Client();
            Service apiService= Client.getClient().create(Service.class);
            Call<MoviesResponse> call= apiService.getPopularMovies("eea7a331c01a1e5cfc8354a8d02b4e57");
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    List<Movie> movies=response.body().getResults();
                    recyclerView.setAdapter(new MovieAdapter(getApplicationContext(),movies));
                    recyclerView.smoothScrollToPosition(0);
                    pd.dismiss();
                }
                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {
                    Log.d("Error",t.getMessage());
                    Toast.makeText(MainActivity.this,"Error Fetching Data!",Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }catch (Exception e){
            Log.d("Error",e.getMessage());
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
        }
    }




}