package com.udacity.nanodegree.popularmoviestage1.Utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Class for encapsulating data fetched from server into an object form
 * so that data can be accessed efficiently and will be easy to handle.
 */
public class Movie implements Parcelable {

    // Parcelable creator for creating parcels for movie class object such that
    // it will be efficiently sent from one activity to another activity.
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };

    private String title;
    private String posterPath;
    private double voteAverage;
    private String originalLang;
    private String coverPath;
    private String overview;
    private Date releaseDate;

    //Constructor for creating and initializing movie class object from fata fetched from server.
    public Movie(String title, String posterPath, double voteAverage, String originalLang, String coverPath, String overview, String releaseDate) throws ParseException {
        this.title = title;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
        this.originalLang = originalLang;
        this.coverPath = coverPath;
        this.overview = overview;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        this.releaseDate = sdf.parse(releaseDate);
    }

    //Constructor for creating and initializing movie class object from data which is put into parcel.
    private Movie(Parcel parcel) {
        title = parcel.readString();
        posterPath = parcel.readString();
        voteAverage = parcel.readDouble();
        originalLang = parcel.readString();
        coverPath = parcel.readString();
        overview = parcel.readString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        try {
            releaseDate = sdf.parse(sdf.format(parcel.readLong()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    /*
     * Various getter methods to get properties of the movie class object.
     */
    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getOriginalLang() {
        return originalLang;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public String getOverview() {
        return overview;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("title: ").append(title).append("\n")
                .append("Poster path: ").append(posterPath).append("\n")
                .append("Vote average: ").append(voteAverage).append("\n")
                .append("Original Lang: ").append(originalLang).append("\n")
                .append("Cover path: ").append(coverPath).append("\n")
                .append("Overview: ").append(overview).append("\n")
                .append("Release Date: ").append(releaseDate);
        return builder.toString();
    }

    // Overridden methods from Parcelable interface.
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(posterPath);
        parcel.writeDouble(voteAverage);
        parcel.writeString(originalLang);
        parcel.writeString(coverPath);
        parcel.writeString(overview);
        parcel.writeLong(releaseDate.getTime());
    }
}
