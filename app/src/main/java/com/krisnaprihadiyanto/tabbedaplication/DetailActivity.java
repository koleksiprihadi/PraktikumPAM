package com.krisnaprihadiyanto.tabbedaplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.krisnaprihadiyanto.tabbedaplication.fragment.DetailFragment;
import com.krisnaprihadiyanto.tabbedaplication.model.MovieData;

public class DetailActivity extends AppCompatActivity {

    public static void start(Context context, MovieData movieData) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(DetailActivity.class.getSimpleName(), movieData);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        MovieData movieData = getIntent().getParcelableExtra(DetailActivity.class.getSimpleName());

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, DetailFragment.newInstance(movieData))
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
