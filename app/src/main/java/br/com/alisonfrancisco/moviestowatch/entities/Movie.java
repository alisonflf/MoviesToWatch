package br.com.alisonfrancisco.moviestowatch.entities;


import com.google.gson.annotations.SerializedName;


public class Movie {

    public String title;

    public String id;

    @SerializedName("poster_path")
    public String posterPath;

    @SerializedName("vote_average")
    public String avgVote;

    public String popularity;

    @SerializedName("release_date")
    public String releaseDate;

    public String overview;

}
