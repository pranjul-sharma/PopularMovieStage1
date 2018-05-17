package com.udacity.nanodegree.popularmoviestage1.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.udacity.nanodegree.popularmoviestage1.R;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    public static final int VIEWTYPE_FIRST_ITEM = 0;
    public static final int VIEWTYPE_REST_ITEMS = 1;
    private final Context context;

    public MovieAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId;
        switch (viewType) {
            case VIEWTYPE_FIRST_ITEM:
                layoutId = R.layout.item_recycler_at_first;
                break;
            case VIEWTYPE_REST_ITEMS:
                layoutId = R.layout.item_recycler_rest;
                break;
            default:
                throw new IllegalArgumentException("Invalid value for view type, found : " + viewType);
        }

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new MovieViewHolder(view);

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return VIEWTYPE_FIRST_ITEM;
        else return VIEWTYPE_REST_ITEMS;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if (position != 0) {
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            manager.getDefaultDisplay().getMetrics(metrics);
            int height = metrics.heightPixels;
            holder.ivMovieThumbnail.setMinimumHeight((int) (height / 2.75));
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            int marginCenters = (int) context.getResources().getDimension(R.dimen.cardview_margin);
            int marginEnd = (int) context.getResources().getDimension(R.dimen.cardview_marginEnd);

            if (position % 2 != 0) {
                params.setMargins(marginEnd, marginCenters, marginCenters, marginCenters);
                Log.v("MARRGINS at ODD", params.leftMargin+" " +params.topMargin+ " "+ params.rightMargin+" "+params.bottomMargin);
            } else {
                params.setMargins(marginCenters, marginCenters, marginEnd, marginCenters);
                Log.v("MARRGINS at EVEN", params.leftMargin+" " +params.topMargin+ " "+ params.rightMargin+" "+params.bottomMargin);
            }

            holder.cardViewMovie.setLayoutParams(params);
        }

    }

    @Override
    public int getItemCount() {
        return 8;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView ivMovieThumbnail;
        TextView tvSortOrder;
        CardView cardViewMovie;

        MovieViewHolder(View itemView) {
            super(itemView);
            ivMovieThumbnail = itemView.findViewById(R.id.iv_movie_thumb);
            tvSortOrder = itemView.findViewById(R.id.tv_sort_order);
            cardViewMovie = itemView.findViewById(R.id.card_movies);
        }
    }
}
