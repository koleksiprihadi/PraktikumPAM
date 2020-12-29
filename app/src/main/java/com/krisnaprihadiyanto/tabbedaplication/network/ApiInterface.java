package com.krisnaprihadiyanto.tabbedaplication.network;

import com.krisnaprihadiyanto.tabbedaplication.model.Movie;
import com.krisnaprihadiyanto.tabbedaplication.model.MovieDetail;
import com.krisnaprihadiyanto.tabbedaplication.model.Review;
import com.krisnaprihadiyanto.tabbedaplication.model.Trailer;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET(Constant.MOVIE_PATH + "/popular")
    Call<Movie> popularMovies(
            @Query("page") int page);

    @GET(Constant.MOVIE_PATH + "/{movie_id}")
    Call<MovieDetail> movieDetail(
            @Path("movie_id") int movieId);

    @GET(Constant.MOVIE_PATH + "/{movie_id}/" + Constant.VIDEOS)
    Call<Trailer> trailers(
            @Path("movie_id") int movieId);

    @GET(Constant.MOVIE_PATH + "/{movie_id}/" + Constant.REVIEWS)
    Call<Review> reviews(
            @Path("movie_id") int movieId);
}
