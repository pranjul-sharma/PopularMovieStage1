package com.udacity.nanodegree.popularmoviestage1.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/*
 * Class for fetching movie data from json string returned by network call to tmdb server.
 */
public class MoviesDataJsonUtils {

    // method for getting list of movies from the data string.
    public static List<Movie> getMoviesFromJson(String jsonString) {
        List<Movie> movies = new ArrayList<>();

        // Various keys to fetch from JSONObject for constructing a movie object.
        final String title = "original_title";
        final String posterPath = "poster_path";
        final String voteAverage = "vote_average";
        final String originalLang = "original_language";
        final String coverPath = "backdrop_path";
        final String overview = "overview";
        final String releaseDate = "release_date";
        final String results = "results";

        // fetching the keys from JSONObject and creating a movie object.
        try {
            JSONObject baseObject = new JSONObject(jsonString);
            JSONArray moviesArray = baseObject.getJSONArray(results);
            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject movieObj = moviesArray.getJSONObject(i);
                Movie movie = new Movie(
                        movieObj.getString(title),
                        movieObj.getString(posterPath),
                        movieObj.getDouble(voteAverage),
                        movieObj.getString(originalLang),
                        movieObj.getString(coverPath),
                        movieObj.getString(overview),
                        movieObj.getString(releaseDate)
                );

                // Adding newly created movie object to list of movies.
                movies.add(i, movie);
            }
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }

        // returning the list of movies.
        return movies;
    }
}
