package com.udacity.nanodegree.popularmoviestage1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.nanodegree.popularmoviestage1.Utils.Movie;
import com.udacity.nanodegree.popularmoviestage1.Utils.NetworkUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/*
 * Activity for displaying the detailed information about a movie
 * available currently.
 */
public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageView coverPhoto = findViewById(R.id.iv_cover_photo);
        ImageView posterPhoto = findViewById(R.id.iv_poster_photo);
        TextView movieTitle = findViewById(R.id.tv_movie_title);
        TextView movieRating = findViewById(R.id.tv_rating_movie);
        RatingBar movieRatingBar = findViewById(R.id.rating_movie);
        TextView releaseDate = findViewById(R.id.release_date);
        TextView originalLang = findViewById(R.id.original_lang);
        TextView overview = findViewById(R.id.overview);

        // getting data from intent to populate the UI components.
        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra(MainActivity.PARCELABLE_KEY);

        //populating UI components
        // fetching images for cover photo and poster photo from tmdb server.
        Picasso.with(this).load(NetworkUtils.buildImgUrl(movie.getCoverPath()).toString()).into(coverPhoto);
        Picasso.with(this).load(NetworkUtils.buildImgUrl(movie.getPosterPath()).toString()).into(posterPhoto);

        // setting the title of the movie.
        movieTitle.setText(movie.getTitle());
        String temp = String.valueOf(movie.getVoteAverage()) + "/10";

        // displaying rating of movie in numerical form.
        movieRating.setText(temp);

        // displaying rating of movie in graphical form.
        movieRatingBar.setRating((float) (movie.getVoteAverage() / 2));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd", Locale.US);

        // displaying other information available about the movie.
        releaseDate.setText(sdf.format(movie.getReleaseDate()));
        originalLang.setText(movie.getOriginalLang());
        overview.setText(movie.getOverview());

    }
}
