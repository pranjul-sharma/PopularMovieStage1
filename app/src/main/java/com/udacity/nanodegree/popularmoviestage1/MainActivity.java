package com.udacity.nanodegree.popularmoviestage1;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.nanodegree.popularmoviestage1.Utils.Movie;
import com.udacity.nanodegree.popularmoviestage1.Utils.MoviesDataJsonUtils;
import com.udacity.nanodegree.popularmoviestage1.Utils.NetworkUtils;
import com.udacity.nanodegree.popularmoviestage1.adapters.MovieAdapter;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/*
 * Main activity launched on start up of the application and
 * displays the movies in list form.
 */
public class MainActivity extends AppCompatActivity {

    public static final String PARCELABLE_KEY = "movie_parcelable";
    RecyclerView recyclerViewMovies;
    ProgressBar progressBar;
    MovieAdapter adapter;
    RadioButton activeButton;
    FetchMoviesTask fetchMoviesTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewMovies = findViewById(R.id.recycler_view_movies);
        progressBar = findViewById(R.id.progress_bar_main);

        adapter = new MovieAdapter(this);

        // Spanning the first two columns in one so that UI will look more general and user friendly.
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

        loadMovies();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_sort) {
            // for handling the user action if user wants to change preference
            // on which the list of movies is shown.
            showSortOptions();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // method for handling user preference change.
    private void showSortOptions() {
        final SharedPreferences menuPrefs = getSharedPreferences(getResources().getString(R.string.sp_name), MODE_PRIVATE);
        final SharedPreferences.Editor editor = menuPrefs.edit();

        // Building a dialog box through which user can change his preference.
        final Dialog menuDialog = new Dialog(this);
        menuDialog.setContentView(R.layout.movie_preference);
        menuDialog.setCancelable(true);
        RadioGroup menuGroup = menuDialog.findViewById(R.id.movie_order_selector);
        activeButton = menuGroup.findViewById(R.id.movie_by_popularity);

        // setting currently selected movie type from preferences.
        String checkedBtn = menuPrefs.getString(getResources().getString(R.string.sp_key_movie_current_type),
                getResources().getString(R.string.movie_by_popularity_key));
        if (checkedBtn.equals(getResources().getString(R.string.movie_by_popularity_key)))
            activeButton.setChecked(true);
        else {
            activeButton = menuGroup.findViewById(R.id.movie_by_rating);
            activeButton.setChecked(true);
        }

        // updating preferences when user changes it.
        menuGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                activeButton = (RadioButton) radioGroup.findViewById(i);
                activeButton.setChecked(true);
                editor.putString(getResources().getString(R.string.sp_key_movie_order), activeButton.getTag().toString());
                editor.putString(getResources().getString(R.string.sp_key_movie_current_type), activeButton.getText().toString());
                editor.apply();
                menuDialog.cancel();
                // loading movies depending upon the preferences user just changed to.
                loadMovies();
            }
        });

        TextView cancelTv = menuDialog.findViewById(R.id.action_cancel_dialog);
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuDialog.cancel();
            }
        });

        menuDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void loadMovies() {

        // reading the type of movies to be shown from preferences.
        SharedPreferences movieOrderPrefs = getSharedPreferences(getResources().getString(R.string.sp_name), MODE_PRIVATE);
        String searchKey = movieOrderPrefs.getString(getResources().getString(R.string.sp_key_movie_order),
                getResources().getString(R.string.movie_by_popularity_value));

        // check if internet is available.
        if (isNetworkAvailable()) {

            // if internet is available, cancel any other network call that has been made and
            // still in progress.
            if (fetchMoviesTask != null)
                fetchMoviesTask.cancel(true);

            // create new network task of fetching movies and call it.
            fetchMoviesTask = new FetchMoviesTask();
            fetchMoviesTask.execute(searchKey);
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection.", Toast.LENGTH_LONG).show();
        }
    }

    // method to check if internet is available on user device.
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(MainActivity.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // class for handling background task of fetching movies.
    class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // displaying loading indicator in case of slow internet connection,
            // it will act to show user that some process is going on and UI is not
            // blocked and has some task to process.
            if (!progressBar.isShown())
                progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected List<Movie> doInBackground(String... args) {

            // fetching the actual movie data from tmdb server in background.
            String sortOrder = getResources().getString(R.string.movie_by_popularity_value);
            if (args.length != 0)
                sortOrder = args[0];
            URL url = NetworkUtils.buildMovieUrl(sortOrder, getResources().getString(R.string.api_key),
                    getResources().getString(R.string.moviedb_api_key));
            try {
                String jsonData = NetworkUtils.getJsonStringFromUrl(url);
                Log.v("JSON_DATA", jsonData);
                List<Movie> movies = null;
                if (!jsonData.isEmpty())
                    movies = MoviesDataJsonUtils.getMoviesFromJson(jsonData);
                return movies;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);

            // Once the movie data is fetched from server and
            // list of movies is fetched from this data,
            // disable indicator and show actual data.
            if (progressBar.isShown())
                progressBar.setVisibility(View.INVISIBLE);

            // Update the adapter's content to newly fetched movies.
            adapter.updateMovies(movies);
        }
    }
}
