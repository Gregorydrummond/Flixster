package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

public class MovieDetailsActivity extends AppCompatActivity {

    //Movie to display
    Movie movie;

    //View Objects
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    ImageView ivBackdrop;
    ImageView ivPoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        tvTitle = findViewById(R.id.tvDetailTitle);
        tvOverview = findViewById(R.id.tvDetailOverview);
        rbVoteAverage = findViewById(R.id.rbVoteAverage);
        ivBackdrop = findViewById(R.id.ivDetailBackdrop);
        ivPoster = findViewById(R.id.ivDetailPoster);

        //Unwrap the movie passed in via intent, using its simple name as a key
        movie = Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailActivity", "Showing details for " + movie.getTitle());

        //vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage / 2.0f);

        //Set Title text
        tvTitle.setText(movie.getTitle());

        //Set overview
        tvOverview.setText(movie.getOverview());

        //Set backdrop image
        Glide.with(this)
                .load(movie.getBackdropPath())
                .override(1080)
                .into(ivBackdrop);

        //Set poster image
        Glide.with(this)
                .load(movie.getPosterPath())
                .transform(new CircleCrop())
                .override(300)
                .into(ivPoster);
    }
}