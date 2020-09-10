package com.example.moviebuff.api;

import com.example.moviebuff.model.MoviesResponse;
import com.example.moviebuff.model.ReviewsResponse;
import com.example.moviebuff.model.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {

    String apikeyy="eea7a331c01a1e5cfc8354a8d02b4e57";
    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apikey);

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apikey);

    @GET("movie/{movie_id}/videos")
    Call<TrailerResponse> getMovieTrailer(@Path("movie_id") int id, @Query("api_key") String apikey);

    @GET("movie/{movie_id}/reviews")
    Call<ReviewsResponse> getMovieReview(@Path("movie_id") int id, @Query("api_key") String apikey);
}
