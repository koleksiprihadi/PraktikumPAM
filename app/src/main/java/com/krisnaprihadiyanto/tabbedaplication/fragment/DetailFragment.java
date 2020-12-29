package com.krisnaprihadiyanto.tabbedaplication.fragment;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.krisnaprihadiyanto.tabbedaplication.model.Genre;
import com.krisnaprihadiyanto.tabbedaplication.model.MovieData;
import com.krisnaprihadiyanto.tabbedaplication.model.MovieDetail;
import com.krisnaprihadiyanto.tabbedaplication.model.Review;
import com.krisnaprihadiyanto.tabbedaplication.model.ReviewData;
import com.krisnaprihadiyanto.tabbedaplication.model.Trailer;
import com.krisnaprihadiyanto.tabbedaplication.model.TrailerData;
import com.krisnaprihadiyanto.tabbedaplication.network.ApiService;
import com.krisnaprihadiyanto.tabbedaplication.R;
import com.krisnaprihadiyanto.tabbedaplication.network.Constant;
import com.krisnaprihadiyanto.tabbedaplication.util.ConnectionUtil;
import com.krisnaprihadiyanto.tabbedaplication.databse.DatabaseHandler;
import com.squareup.picasso.Picasso;

import java.net.SocketTimeoutException;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailFragment extends Fragment {

    DatabaseHandler dbhelper;
    public static String favoriteTitle;
    private Toolbar toolbar;
    private ImageView imgPoster;
    private TextView tvMovieTitle;
    private TextView tvMovieDate;
    private TextView tvMovieDuration;
    private TextView tvMovieGenre;
    private TextView tvMovieHomepage;
    private TextView tvMovieOverview;
    private LinearLayout viewTrailers;
    private LinearLayout viewReviews;

    private ProgressBar pgTrailers;
    private ProgressBar pgReviews;

    private MovieData movieData;

    private ApiService apiService;



    public static DetailFragment newInstance(MovieData movieData) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(DetailFragment.class.getSimpleName(), movieData);
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(bundle);
        return detailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        imgPoster = (ImageView) view.findViewById(R.id.img_poster);
        tvMovieTitle = (TextView) view.findViewById(R.id.movie_title);
        tvMovieDate = (TextView) view.findViewById(R.id.movie_date);
        tvMovieDuration = (TextView) view.findViewById(R.id.movie_duration);
        tvMovieGenre = (TextView) view.findViewById(R.id.movie_genre);
        tvMovieHomepage = (TextView) view.findViewById(R.id.movie_homepage);
        tvMovieOverview = (TextView) view.findViewById(R.id.movie_overview);
        viewTrailers = (LinearLayout) view.findViewById(R.id.view_trailers);
        viewReviews = (LinearLayout) view.findViewById(R.id.view_reviews);

        pgTrailers = (ProgressBar) view.findViewById(R.id.pg_trailers);
        pgReviews = (ProgressBar) view.findViewById(R.id.pg_reviews);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_fav);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFav();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.app_name);
            actionBar.setSubtitle(R.string.movie_detail);
        }

        movieData = getArguments().getParcelable(DetailFragment.class.getSimpleName());

        apiService = new ApiService();

        if(ConnectionUtil.isConnected(getContext())) {
            if(movieData != null) {
                loadMovieDetail(movieData.getId());
                loadTrailer(movieData.getId());
                loadReviews(movieData.getId());
            }
        }else{
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

    }

    private void loadMovieDetail(int id) {

                apiService.getMovieDetail(id, new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                MovieDetail movieDetail = (MovieDetail) response.body();

                if(movieDetail != null) {
                    Picasso.with(getContext())
                            .load(Constant.IMG_URL + movieDetail.getPosterPath())
                            .into(imgPoster);


                    tvMovieTitle.setText(movieDetail.getOriginalTitle());
                    tvMovieDate.setText(movieDetail.getReleaseDate());
                    tvMovieDuration.setText(movieDetail.getRuntime() + " Minutes");
                    favoriteTitle = tvMovieTitle.toString();

                    for (int i = 0; i < movieDetail.getGenres().size(); i++) {
                        Genre genre = movieDetail.getGenres().get(i);

                        if(i < movieDetail.getGenres().size() - 1) {
                            tvMovieGenre.append(genre.getName() + ",");
                        }else{
                            tvMovieGenre.append(genre.getName());
                        }
                    }

                    tvMovieHomepage.setText(movieDetail.getHomepage());
                    tvMovieOverview.setText(movieDetail.getOverview());
                }else{
                    Toast.makeText(getContext(), "No Data!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                if(t instanceof SocketTimeoutException) {
                    Toast.makeText(getContext(), "Request Timeout. Please try again!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getContext(), "Connection Error!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void loadTrailer(int id) {
        pgTrailers.setVisibility(View.VISIBLE);

        apiService.getTrailers(id, new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Trailer trailer = (Trailer) response.body();

                if(trailer != null) {
                    showTrailers(trailer.getResults());
                }else{
                    Toast.makeText(getContext(), "No Data!", Toast.LENGTH_LONG).show();
                }

                pgTrailers.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                if(t instanceof SocketTimeoutException) {
                    Toast.makeText(getContext(), "Request Timeout. Please try again!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getContext(), "Connection Error!", Toast.LENGTH_LONG).show();
                }

                pgTrailers.setVisibility(View.GONE);
            }
        });
    }

    private void loadReviews(int id) {
        pgReviews.setVisibility(View.VISIBLE);

        apiService.getReviews(id, new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Review review = (Review) response.body();

                if(review != null) {
                    showReviews(review.getResults());
                }else{
                    Toast.makeText(getContext(), "No Data!", Toast.LENGTH_LONG).show();
                }

                pgReviews.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                if(t instanceof SocketTimeoutException) {
                    Toast.makeText(getContext(), "Request Timeout. Please try again!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getContext(), "Connection Error!", Toast.LENGTH_LONG).show();
                }

                pgReviews.setVisibility(View.GONE);
            }
        });
    }

    private void showTrailers(List<TrailerData> trailerDatas) {
        viewTrailers.removeAllViews();

        for (int i = 0; i < trailerDatas.size(); i++) {

            final TrailerData trailerData = trailerDatas.get(i);
            View view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_trailer, viewTrailers, false);

            ImageView trailerThumb = (ImageView) view.findViewById(R.id.trailer_thumb);
            TextView trailerName = (TextView) view.findViewById(R.id.trailer_name);

            if(trailerData.getSite().equalsIgnoreCase("youtube")) {
                Picasso.with(getContext())
                        .load("http://img.youtube.com/vi/" + trailerData.getKey() + "/default.jpg")
                        .into(trailerThumb);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    watchYoutubeVideo(trailerData.getKey());
                }
            });


            trailerName.setText(trailerData.getName());
            viewTrailers.addView(view);
        }
    }

    public void watchYoutubeVideo(String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    private void showReviews(List<ReviewData> reviewDatas) {
        viewReviews.removeAllViews();

        for (int i = 0; i < reviewDatas.size(); i++) {

            ReviewData reviewData = reviewDatas.get(i);
            View view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_review, viewReviews, false);

            TextView reviewers = (TextView) view.findViewById(R.id.reviewers);
            TextView content = (TextView) view.findViewById(R.id.content);

            reviewers.setText(reviewData.getAuthor());
            content.setText(reviewData.getContent().length() > 100 ?
                    reviewData.getContent().substring(0, 100) + "..." : reviewData.getContent());

            viewReviews.addView(view);
        }
    }


    public void addFav(){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.execSQL("insert into fav(no, nama, tgl, jk, alamat) values('" +
                "NULL" + "','" +
                favoriteTitle + "','" +"')");
        Toast.makeText(getContext(), "Ditambahkan Favorite", Toast.LENGTH_LONG).show();
        db.close();
    }

}