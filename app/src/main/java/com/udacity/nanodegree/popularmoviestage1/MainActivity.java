package com.udacity.nanodegree.popularmoviestage1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.udacity.nanodegree.popularmoviestage1.adapters.MovieAdapter;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerViewMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewMovies = (RecyclerView) findViewById(R.id.recycler_view_movies);
        final MovieAdapter adapter = new MovieAdapter(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)) {
                    case MovieAdapter.VIEWTYPE_FIRST_ITEM:
                        return 2;
                    case MovieAdapter.VIEWTYPE_REST_ITEMS:
                        return 1;
                    default:
                        return -1;
                }
            }
        });
        recyclerViewMovies.setLayoutManager(gridLayoutManager);
        recyclerViewMovies.setAdapter(adapter);
    }

}
