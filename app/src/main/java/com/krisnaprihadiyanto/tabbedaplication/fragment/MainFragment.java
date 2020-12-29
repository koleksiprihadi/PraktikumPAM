package com.krisnaprihadiyanto.tabbedaplication.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.krisnaprihadiyanto.tabbedaplication.DetailActivity;
import com.krisnaprihadiyanto.tabbedaplication.R;
import com.krisnaprihadiyanto.tabbedaplication.adapter.MovieListAdapter;
import com.krisnaprihadiyanto.tabbedaplication.model.Movie;
import com.krisnaprihadiyanto.tabbedaplication.network.ApiService;
import com.krisnaprihadiyanto.tabbedaplication.util.EndlessRecyclerOnScrollListener;
import com.krisnaprihadiyanto.tabbedaplication.util.GridMarginDecoration;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.net.SocketTimeoutException;

public class MainFragment extends Fragment implements MovieListAdapter.OnMovieItemSelectedListener {

    private Toolbar toolbar;
    private RecyclerView rvMovies;
    private GridLayoutManager gridLayoutManager;
    private MovieListAdapter movieListAdapter;
    private SwipeRefreshLayout refreshLayout;

    private ActionBar actionBar;
    private int page = 1;
    private int limit = 20;

    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;

    private ApiService apiService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        rvMovies = (RecyclerView) view.findViewById(R.id.rv_movies);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(savedInstanceState == null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

            actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(R.string.app_name);
                actionBar.setSubtitle(R.string.most_popular);
            }

            movieListAdapter = new MovieListAdapter(getContext());
            movieListAdapter.setOnMovieItemSelectedListener(this);

            gridLayoutManager = new GridLayoutManager(getContext(), 2);
            rvMovies.setLayoutManager(gridLayoutManager);

            rvMovies.addItemDecoration(new GridMarginDecoration(getContext(), 1, 1, 1, 1));
            rvMovies.setHasFixedSize(true);
            rvMovies.setAdapter(movieListAdapter);

            addScroll();

            refreshLayout.setColorSchemeResources(R.color.colorPrimary);
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshData();
                }
            });

            loadData();
        }
    }

    private void addScroll() {
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(gridLayoutManager, page, limit) {
            @Override
            public void onLoadMore(int next) {
                page = next;
                loadData();
            }
        };

        rvMovies.addOnScrollListener(endlessRecyclerOnScrollListener);
    }

    private void removeScroll() {
        rvMovies.removeOnScrollListener(endlessRecyclerOnScrollListener);
    }

    private void loadData(){
        if (refreshLayout != null) {
            refreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.setRefreshing(true);
                }
            });
        }

        apiService = new ApiService();
        apiService.getPopularMovies(page, new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Movie movie = (Movie) response.body();

                if(movie != null) {
                    if(movieListAdapter != null) {
                        movieListAdapter.addAll(movie.getResults());
                    }
                }else{
                    Toast.makeText(getContext(), "No Data!", Toast.LENGTH_LONG).show();
                }

                if (refreshLayout != null)
                    refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                if(t instanceof SocketTimeoutException) {
                    Toast.makeText(getContext(), "Request Timeout. Please try again!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getContext(), "Connection Error!", Toast.LENGTH_LONG).show();
                }

                if (refreshLayout != null)
                    refreshLayout.setRefreshing(false);
            }
        });
    }

    private void refreshData() {
        if(movieListAdapter != null) {
            movieListAdapter.clear();
        }
        page = 1;

        limit = 20;

        removeScroll();
        addScroll();

        loadData();
    }

    @Override
    public void onItemClick(View v, int position) {
        DetailActivity.start(getContext(), movieListAdapter.getItem(position));
    }

}
