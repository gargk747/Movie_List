package com.example.moviebuff.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewsResponse {

    @SerializedName("id")
    private int id_reviews;
    @SerializedName("results")
    private List<Reviews> results;

    public int getIdReviews() {
        return id_reviews;
    }

    public void setIdReviews(int id_reviews) {
        this.id_reviews = id_reviews;
    }

    public List<Reviews> getResults() {
        return results;
    }

    public void setResults(List<Reviews> results) {
        this.results = results;
    }
}
